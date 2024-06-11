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

    @Test
    public void testGetMean() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 1.5);
        assertEquals(2.5, dist.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 1.5);
        assertEquals(1.5, dist.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 1.5);
        assertEquals(0.1467626, dist.density(1.0), 0.0001);
        assertEquals(0.1257944, dist.density(2.0), 0.0001);
        assertEquals(0.1064827, dist.density(3.0), 0.0001);
        assertEquals(0.0904261, dist.density(4.0), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 1.5);
        assertEquals(0.0668072, dist.cumulativeProbability(1.0), 0.0001);
        assertEquals(0.1586553, dist.cumulativeProbability(2.0), 0.0001);
        assertEquals(0.3085375, dist.cumulativeProbability(3.0), 0.0001);
        assertEquals(0.6305587, dist.cumulativeProbability(4.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 1.5);
        assertEquals(1.0, dist.inverseCumulativeProbability(0.0668072), 0.0001);
        assertEquals(2.0, dist.inverseCumulativeProbability(0.1586553), 0.0001);
        assertEquals(3.0, dist.inverseCumulativeProbability(0.3085375), 0.0001);
        assertEquals(4.0, dist.inverseCumulativeProbability(0.6305587), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 1.5);
        double sample = dist.sample();
        assertEquals(true, sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }

}