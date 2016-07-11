/*
 *  Copyright 2016 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.google.android.apps.forscience.whistlepunk.audiogen.voices;

import android.util.Log;

import com.google.android.apps.forscience.whistlepunk.audiogen.JsynUnitVoiceAdapterInterface;
import com.google.common.primitives.Doubles;
import com.jsyn.Synthesizer;
import com.softsynth.math.AudioMath;
import com.softsynth.shared.time.TimeStamp;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Base class to adapt the SimpleJsynUnitVoice to the SimpleJsynAudioGenerator using a scale.
 * <p>
 * Adapt the SimpleJsynUnitVoice to the SimpleJsynAudioGenerator.
 * This implementation does nothing.
 * </p>
 */
public class DataToScalePitchEnvelopeSimpleJsynUnitVoiceAdapter implements
        JsynUnitVoiceAdapterInterface {
    private final SimpleJsynUnitVoiceBase mVoice;
    private final int[] mPitches;
    private static final double AMP_VALUE = 1.0; // default value for amplitude

    public DataToScalePitchEnvelopeSimpleJsynUnitVoiceAdapter(Synthesizer synth, int[] scale,
                                                              int pitchMin, int pitchMax) {
        mPitches = PitchGenerator.generatePitches(scale, pitchMin, pitchMax);
        mVoice = new SineEnvelope();
        synth.add(mVoice);
    }

    public void noteOn(double value, double min, double max, TimeStamp timeStamp) {
        // Default implementation (or any implementation with no pitches) does nothing.
        if (mPitches.length == 0) return;
        // Range checking, in case min or max is higher or lower than value (respectively).
        if (value < min) value = min;
        if (value > max) value = max;

        int index = (int) Math.floor((value - min) / (max - min) * (mPitches.length-1));
        double freq = AudioMath.pitchToFrequency(mPitches[index]);
        mVoice.noteOn(freq, AMP_VALUE, timeStamp);
    }

    public SimpleJsynUnitVoiceBase getVoice() {
        return mVoice;
    }
}