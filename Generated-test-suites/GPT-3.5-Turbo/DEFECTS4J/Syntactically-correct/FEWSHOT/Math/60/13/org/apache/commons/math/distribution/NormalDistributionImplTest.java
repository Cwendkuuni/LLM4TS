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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    private static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1e-9;

    private NormalDistributionImpl distribution;

    @Before
    public void setUp() {
        distribution = new NormalDistributionImpl(0, 1);
    }

    @Test
    public void testGetMean() {
        assertEquals(0, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        assertEquals(0.3989422804014327, distribution.density(0), 1e-9);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, distribution.cumulativeProbability(0), 1e-9);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0, distribution.inverseCumulativeProbability(0.5), 1e-9);
    }

    @Test
    public void testSample() throws MathException {
        double sample = distribution.sample();
        assertNotNull(sample);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        assertEquals(DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 0);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.5), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(-1, distribution.getInitialDomain(0.49), 0);
        assertEquals(1, distribution.getInitialDomain(0.51), 0);
        assertEquals(0, distribution.getInitialDomain(0.5), 0);
    }
}