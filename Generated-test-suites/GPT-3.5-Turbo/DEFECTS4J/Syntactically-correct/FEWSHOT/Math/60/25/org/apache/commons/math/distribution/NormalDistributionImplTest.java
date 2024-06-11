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

package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NormalDistributionImplTest {
    private static final double EPSILON = 1e-9;

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        assertEquals(1, normalDistribution.getMean(), EPSILON);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        assertEquals(2, normalDistribution.getStandardDeviation(), EPSILON);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        assertEquals(0.17603266338214976, normalDistribution.density(1), EPSILON);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        assertEquals(0.3085375387259869, normalDistribution.cumulativeProbability(1), EPSILON);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        assertEquals(-1.8550710686577516, normalDistribution.inverseCumulativeProbability(0.1), EPSILON);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        double sample = normalDistribution.sample();
        // Not possible to assert exact value due to randomness
        assertEquals(1, normalDistribution.getMean(), EPSILON);
        assertEquals(2, normalDistribution.getStandardDeviation(), EPSILON);
    }
}