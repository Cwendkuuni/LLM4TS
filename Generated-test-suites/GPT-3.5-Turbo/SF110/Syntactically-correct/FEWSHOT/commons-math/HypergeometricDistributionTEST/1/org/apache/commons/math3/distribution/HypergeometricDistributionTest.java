
package org.apache.commons.math3.distribution;

import org.apache.commons.math3.random.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HypergeometricDistributionTest {
    
    @Test
    public void testConstructor() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(3, distribution.getSampleSize());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_InvalidPopulationSize() {
        new HypergeometricDistribution(0, 5, 3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_InvalidNumberOfSuccesses() {
        new HypergeometricDistribution(10, -1, 3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_InvalidSampleSize() {
        new HypergeometricDistribution(10, 5, -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(10, 11, 3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_SampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(10, 5, 11);
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.440, distribution.cumulativeProbability(2), 0.001);
        assertEquals(0.840, distribution.cumulativeProbability(3), 0.001);
        assertEquals(1.000, distribution.cumulativeProbability(5), 0.001);
        assertEquals(1.000, distribution.cumulativeProbability(10), 0.001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, distribution.getNumericalMean(), 0.001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.642, distribution.getNumericalVariance(), 0.001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(5, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertTrue(distribution.isSupportConnected());
    }
}
