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

import org.junit.Before;
import org.junit.Test;

public class FDistributionImplTest {

    private FDistributionImpl fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistributionImpl(5, 10);
    }

    @Test
    public void testCumulativeProbability() {
        double result = 0;

        // Test x <= 0
        result = fDistribution.cumulativeProbability(-1);
        assertEquals(0.0, result, 0.00001);

        // Test x > 0
        result = fDistribution.cumulativeProbability(2);
        assertEquals(0.7765767676, result, 0.00001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        double result = 0;

        // Test p = 0
        result = fDistribution.inverseCumulativeProbability(0);
        assertEquals(0.0, result, 0.00001);
        
        // Test p = 1
        result = fDistribution.inverseCumulativeProbability(1);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.00001);

        // Test 0 < p < 1
        result = fDistribution.inverseCumulativeProbability(0.5);
        assertEquals(0.9894171459, result, 0.00001);
    }

    @Test
    public void testGetDomainLowerBound() {
        double result = fDistribution.getDomainLowerBound(0.1);
        assertEquals(0.0, result, 0.00001);
    }

    @Test
    public void testGetDomainUpperBound() {
        double result = fDistribution.getDomainUpperBound(0.1);
        assertEquals(Double.MAX_VALUE, result, 0.00001);
    }

    @Test
    public void testGetInitialDomain() {
        double result = fDistribution.getInitialDomain(0.1);
        assertEquals(1.25, result, 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom_NegativeValue() {
        fDistribution.setNumeratorDegreesOfFreedom(-1);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double result = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(5.0, result, 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom_NegativeValue() {
        fDistribution.setDenominatorDegreesOfFreedom(-1);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double result = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(10.0, result, 0.00001);
    }
}