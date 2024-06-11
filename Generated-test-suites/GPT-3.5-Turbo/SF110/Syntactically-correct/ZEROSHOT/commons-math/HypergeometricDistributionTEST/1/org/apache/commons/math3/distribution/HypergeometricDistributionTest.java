package org.apache.commons.math3.distribution;

import org.apache.commons.math3.random.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {
    
    @Test
    public void testConstructorWithValidArguments() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(3, distribution.getSampleSize());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(-10, 5, 3);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, -5, 3);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, -3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 15, 3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeLargerThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 20);
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.0, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.054545454545, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.490909090909, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(0.927272727272, distribution.cumulativeProbability(4), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(6), 0.0001);
    }
    
    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(0, distribution.getLowerDomain(10, 5, 3));
        assertEquals(1, distribution.getLowerDomain(10, 5, 4));
    }
    
    @Test
    public void testGetUpperDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(3, distribution.getUpperDomain(5, 3));
        assertEquals(5, distribution.getUpperDomain(10, 5));
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(0.0, distribution.probability(-1), 0.0001);
        assertEquals(0.0, distribution.probability(0), 0.0001);
        assertEquals(0.0, distribution.probability(1), 0.0001);
        assertEquals(0.0363636363636, distribution.probability(2), 0.0001);
        assertEquals(0.327272727273, distribution.probability(3), 0.0001);
        assertEquals(0.654545454545, distribution.probability(4), 0.0001);
        assertEquals(0.654545454545, distribution.probability(5), 0.0001);
        assertEquals(0.327272727273, distribution.probability(6), 0.0001);
        assertEquals(0.0363636363636, distribution.probability(7), 0.0001);
        assertEquals(0.0, distribution.probability(8), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.945454545454, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.509090909091, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.0909090909091, distribution.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(4), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(5), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(6), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(7), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(8), 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(0.818181818181, distribution.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(0, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(3, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertTrue(distribution.isSupportConnected());
    }
}