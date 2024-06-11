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

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import static org.junit.Assert.*;
import org.junit.Test;

public class UniformRealDistributionTest {
    
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());
        assertTrue(distribution.isSupportConnected());
        assertEquals(0.5, distribution.getNumericalMean(), 0.0);
        assertEquals(1.0/12.0, distribution.getNumericalVariance(), 0.0);
    }
    
    @Test
    public void testConstructorWithValidBounds() {
        double lower = -1.5;
        double upper = 2.5;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        assertEquals(lower, distribution.getSupportLowerBound(), 0.0);
        assertEquals(upper, distribution.getSupportUpperBound(), 0.0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());
        assertTrue(distribution.isSupportConnected());
        assertEquals((lower + upper) / 2.0, distribution.getNumericalMean(), 0.0);
        assertEquals((upper - lower)*(upper - lower) / 12.0, distribution.getNumericalVariance(), 0.0);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        double lower = 2.5;
        double upper = -1.5;
        new UniformRealDistribution(lower, upper);
    }
    
    @Test
    public void testPDF() {
        double lower = -1.5;
        double upper = 2.5;
        double step = 0.1;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        for (double x = lower; x < upper; x += step) {
            double expectedPDF = 1 / (upper - lower);
            assertEquals(expectedPDF, distribution.density(x), 0.0);
        }
    }
    
    @Test
    public void testCDF() {
        double lower = -1.5;
        double upper = 2.5;
        double step = 0.1;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        for (double x = lower; x < upper; x += step) {
            double expectedCDF = (x - lower) / (upper - lower);
            assertEquals(expectedCDF, distribution.cumulativeProbability(x), 0.0);
        }
        assertEquals(0.0, distribution.cumulativeProbability(lower - step), 0.0);
        assertEquals(1.0, distribution.cumulativeProbability(upper + step), 0.0);
    }
    
    @Test
    public void testMean() {
        double lower = -1.5;
        double upper = 2.5;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        double expectedMean = (lower + upper) / 2.0;
        assertEquals(expectedMean, distribution.getNumericalMean(), 0.0);
    }
    
    @Test
    public void testVariance() {
        double lower = -1.5;
        double upper = 2.5;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        double expectedVariance = (upper - lower)*(upper - lower) / 12.0;
        assertEquals(expectedVariance, distribution.getNumericalVariance(), 0.0);
    }
    
    @Test
    public void testSupport() {
        double lower = -1.5;
        double upper = 2.5;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        assertEquals(lower, distribution.getSupportLowerBound(), 0.0);
        assertEquals(upper, distribution.getSupportUpperBound(), 0.0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testSampling() {
        double lower = -1.5;
        double upper = 2.5;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        int numSamples = 10000;
        double sum = 0.0;
        for (int i = 0; i < numSamples; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= lower);
            assertTrue(sample < upper);
            sum += sample;
        }
        double mean = sum / numSamples;
        double expectedMean = (lower + upper) / 2.0;
        assertEquals(expectedMean, mean, 0.1);
    }
}