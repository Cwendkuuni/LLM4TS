
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

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Pair;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> dist;

    @Before
    public void setUp() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.2));
        samples.add(new Pair<>(3, 0.3));
        samples.add(new Pair<>(4, 0.4));
        try {
            dist = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
    }

    @Test
    public void testProbability() {
        assertEquals(0.1, dist.probability(1), 0);
        assertEquals(0.2, dist.probability(2), 0);
        assertEquals(0.3, dist.probability(3), 0);
        assertEquals(0.4, dist.probability(4), 0);
        assertEquals(0, dist.probability(0), 0);
        assertEquals(0, dist.probability(5), 0);
        assertEquals(0, dist.probability(null), 0);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = dist.getSamples();
        assertNotNull(samples);
        assertEquals(4, samples.size());
        assertEquals(new Pair<>(1, 0.1), samples.get(0));
        assertEquals(new Pair<>(2, 0.2), samples.get(1));
        assertEquals(new Pair<>(3, 0.3), samples.get(2));
        assertEquals(new Pair<>(4, 0.4), samples.get(3));
    }

    @Test
    public void testSample() {
        int numSamples = 100;
        int num1 = 0, num2 = 0, num3 = 0, num4 = 0;

        for (int i = 0; i < numSamples; i++) {
            int value = dist.sample();
            assertTrue(value >= 1 && value <= 4);
            switch (value) {
                case 1:
                    num1++;
                    break;
                case 2:
                    num2++;
                    break;
                case 3:
                    num3++;
                    break;
                case 4:
                    num4++;
                    break;
            }
        }

        assertEquals(10, num1, 10);
        assertEquals(20, num2, 10);
        assertEquals(30, num3, 10);
        assertEquals(40, num4, 10);
    }

    @Test
    public void testSampleSize() {
        int sampleSize = 50;
        Integer[] values = dist.sample(sampleSize);
        assertNotNull(values);
        assertEquals(sampleSize, values.length);
        for (int i = 0; i < values.length; i++) {
            assertTrue(values[i] >= 1 && values[i] <= 4);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleSizeInvalid() throws NotStrictlyPositiveException {
        int sampleSize = 0;
        dist.sample(sampleSize);
    }

    @Test
    public void testGetProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.2));
        samples.add(new Pair<>(3, 0.3));
        samples.add(new Pair<>(4, 0.4));
        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
            double[] probabilities = dist.getProbabilities();
            assertNotNull(probabilities);
            assertEquals(samples.size(), probabilities.length);
            for (int i = 0; i < samples.size(); i++) {
                assertEquals(samples.get(i).getValue(), probabilities[i], 0);
            }
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception thrown during test: " + e.getMessage());
        }
    }

    @Test
    public void testReseedRandomGenerator() {
        long seed1 = 12345;
        long seed2 = 67890;
        double epsilon = 0.01;

        dist.reseedRandomGenerator(seed1);
        int numSamples1 = 100;
        int num1Seed1 = 0, num2Seed1 = 0, num3Seed1 = 0, num4Seed1 = 0;

        for (int i = 0; i < numSamples1; i++) {
            int value = dist.sample();
            assertTrue(value >= 1 && value <= 4);
            switch (value) {
                case 1:
                    num1Seed1++;
                    break;
                case 2:
                    num2Seed1++;
                    break;
                case 3:
                    num3Seed1++;
                    break;
                case 4:
                    num4Seed1++;
                    break;
            }
        }

        dist.reseedRandomGenerator(seed1);
        int numSamples2 = 100;
        int num1Seed2 = 0, num2Seed2 = 0, num3Seed2 = 0, num4Seed2 = 0;

        for (int i = 0; i < numSamples2; i++) {
            int value = dist.sample();
            assertTrue(value >= 1 && value <= 4);
            switch (value) {
                case 1:
                    num1Seed2++;
                    break;
                case 2:
                    num2Seed2++;
                    break;
                case 3:
                    num3Seed2++;
                    break;
                case 4:
                    num4Seed2++;
                    break;
            }
        }

        assertEquals(num1Seed1, num1Seed2, epsilon);
        assertEquals(num2Seed1, num2Seed2, epsilon);
        assertEquals(num3Seed1, num3Seed2, epsilon);
        assertEquals(num4Seed1, num4Seed2, epsilon);

        dist.reseedRandomGenerator(seed2);
        int numSamples3 = 100;
        int num1Seed3 = 0, num2Seed3 = 0, num3Seed3 = 0, num4Seed3 = 0;

        for (int i = 0; i < numSamples3; i++) {
            int value = dist.sample();
            assertTrue(value >= 1 && value <= 4);
            switch (value) {
                case 1:
                    num1Seed3++;
                    break;
                case 2:
                    num2Seed3++;
                    break;
                case 3:
                    num3Seed3++;
                    break;
                case 4:
                    num4Seed3++;
                    break;
            }
        }

        assertTrue(num1Seed1 != num1Seed3 || num2Seed1 != num2Seed3 || num3Seed1 != num3Seed3 || num4Seed1 != num4Seed3);
    }
}
