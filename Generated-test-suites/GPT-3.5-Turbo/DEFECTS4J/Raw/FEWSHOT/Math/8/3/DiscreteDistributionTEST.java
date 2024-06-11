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
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DiscreteDistributionTest {
    
    @Mock
    private RandomGenerator mockRandomGenerator;
    
    private DiscreteDistribution<Integer> discreteDistribution;
    
    @Before
    public void setUp() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        MockitoAnnotations.initMocks(this);
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        discreteDistribution = new DiscreteDistribution<Integer>(mockRandomGenerator, samples);
    }
    
    @Test
    public void testProbability() {
        double probability1 = discreteDistribution.probability(1);
        assertEquals(0.2, probability1, 0.0001);
        
        double probability2 = discreteDistribution.probability(2);
        assertEquals(0.3, probability2, 0.0001);
        
        double probability3 = discreteDistribution.probability(3);
        assertEquals(0.5, probability3, 0.0001);
    }
    
    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> expectedSamples = new ArrayList<Pair<Integer, Double>>();
        expectedSamples.add(new Pair<>(1, 0.2));
        expectedSamples.add(new Pair<>(2, 0.3));
        expectedSamples.add(new Pair<>(3, 0.5));
        List<Pair<Integer, Double>> actualSamples = discreteDistribution.getSamples();
        assertEquals(expectedSamples, actualSamples);
    }
    
    @Test
    public void testSample() {
        when(mockRandomGenerator.nextDouble()).thenReturn(0.1);
        assertEquals(new Integer(1), discreteDistribution.sample());
        
        when(mockRandomGenerator.nextDouble()).thenReturn(0.35);
        assertEquals(new Integer(2), discreteDistribution.sample());
        
        when(mockRandomGenerator.nextDouble()).thenReturn(0.65);
        assertEquals(new Integer(3), discreteDistribution.sample());
    }
    
    @Test
    public void testSampleWithSampleSize() throws NotStrictlyPositiveException {
        when(mockRandomGenerator.nextDouble()).thenReturn(0.1).thenReturn(0.35).thenReturn(0.65);
        Integer[] expectedSamples = {1, 2, 3};
        Integer[] actualSamples = discreteDistribution.sample(3);
        assertArrayEquals(expectedSamples, actualSamples);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithInvalidSampleSize() throws NotStrictlyPositiveException {
        discreteDistribution.sample(0);
    }
}