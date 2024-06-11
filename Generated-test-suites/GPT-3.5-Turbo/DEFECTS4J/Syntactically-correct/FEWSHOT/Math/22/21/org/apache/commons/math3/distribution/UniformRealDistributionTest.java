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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;

    @Before
    public void setUp() {
        distribution = new UniformRealDistribution();
    }
    
    @Test
    public void testDensityWithinBounds() {
        Assert.assertEquals(0.5, distribution.density(0.5), 0);
    }
    
    @Test
    public void testDensityBelowLowerBound() {
        Assert.assertEquals(0, distribution.density(-1), 0);
    }
    
    @Test
    public void testDensityAboveUpperBound() {
        Assert.assertEquals(0, distribution.density(2), 0);
    }
    
    @Test
    public void testCumulativeProbabilityWithinBounds() {
        Assert.assertEquals(0.5, distribution.cumulativeProbability(0.5), 0);
    }
    
    @Test
    public void testCumulativeProbabilityBelowLowerBound() {
        Assert.assertEquals(0, distribution.cumulativeProbability(-1), 0);
    }
    
    @Test
    public void testCumulativeProbabilityAboveUpperBound() {
        Assert.assertEquals(1, distribution.cumulativeProbability(2), 0);
    }
    
    @Test
    public void testGetNumericalMean() {
        Assert.assertEquals(0.5, distribution.getNumericalMean(), 0);
    }
    
    @Test
    public void testGetNumericalVariance() {
        Assert.assertEquals(1.0 / 12, distribution.getNumericalVariance(), 0);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        Assert.assertEquals(0, distribution.getSupportLowerBound(), 0);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        Assert.assertEquals(1, distribution.getSupportUpperBound(), 0);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        Assert.assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testSample() {
        double sample = distribution.sample();
        Assert.assertTrue(sample >= 0 &&  sample < 1);
    }
}