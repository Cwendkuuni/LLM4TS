/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

public class DiscreteDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testConstructor_NegativeProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", -0.5));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
    }

    @Test
    public void testConstructor_SingleOne() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 1.0));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        assertEquals(1, distribution.getSamples().size());
        assertEquals(1.0, distribution.probability("A"), 0.0);
        assertEquals("A", distribution.sample());
    }

    @Test
    public void testConstructor_MultipleEqual() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.5));
        samples.add(new Pair<>("B", 0.5));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        assertEquals(2, distribution.getSamples().size());
        assertEquals(0.5, distribution.probability("A"), 0.0);
        assertEquals(0.5, distribution.probability("B"), 0.0);

        int countA = 0;
        int countB = 0;
        for (int i = 0; i < 100; i++) {
            String sample = distribution.sample();
            if (sample.equals("A")) {
                countA++;
            } else if (sample.equals("B")) {
                countB++;
            }
        }

        assertTrue(countA > 20 && countA < 80);
        assertTrue(countB > 20 && countB < 80);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_ZeroProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.5));
        samples.add(new Pair<>("B", 0.0));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructor_InfiniteProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.5));
        samples.add(new Pair<>("B", Double.POSITIVE_INFINITY));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
    }

    @Test
    public void testSample_SingleOne() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 1.0));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        for (int i = 0; i < 100; i++) {
            String sample = distribution.sample();
            assertEquals("A", sample);
        }
    }

    @Test
    public void testSample_MultipleEqual() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.5));
        samples.add(new Pair<>("B", 0.5));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        int countA = 0;
        int countB = 0;
        for (int i = 0; i < 1000; i++) {
            String sample = distribution.sample();
            if (sample.equals("A")) {
                countA++;
            } else if (sample.equals("B")) {
                countB++;
            }
        }

        assertTrue(countA > 400 && countA < 600);
        assertTrue(countB > 400 && countB < 600);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSample_InvalidSampleSize_Negative() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException, NotStrictlyPositiveException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.5));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        distribution.sample(-1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSample_InvalidSampleSize_Zero() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException, NotStrictlyPositiveException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.5));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        distribution.sample(0);
    }

    @Test
    public void testSample_ValidSampleSize() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException, NotStrictlyPositiveException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.4));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.2));
        samples.add(new Pair<>("D", 0.1));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        String[] results = distribution.sample(100);

        assertEquals(100, results.length);

        int countA = 0;
        int countB = 0;
        int countC = 0;
        int countD = 0;

        for (String result : results) {
            if (result.equals("A")) {
                countA++;
            } else if (result.equals("B")) {
                countB++;
            } else if (result.equals("C")) {
                countC++;
            } else if (result.equals("D")) {
                countD++;
            }
        }

        assertEquals(40, countA);
        assertEquals(30, countB);
        assertEquals(20, countC);
        assertEquals(10, countD);
    }

}