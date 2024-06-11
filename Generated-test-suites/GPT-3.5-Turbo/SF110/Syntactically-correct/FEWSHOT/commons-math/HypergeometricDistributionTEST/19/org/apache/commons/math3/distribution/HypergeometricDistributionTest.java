package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class HypergeometricDistributionTest {
    
    @Test
    public void testConstructor() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);  
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(3, distribution.getSampleSize());
        
        distribution = new HypergeometricDistribution(new Well19937c(), 20, 10, 7);
        assertEquals(20, distribution.getPopulationSize());
        assertEquals(10, distribution.getNumberOfSuccesses());
        assertEquals(7, distribution.getSampleSize());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorPopulationSizeNegative() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(-10, 5, 3);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNumberOfSuccessesZero() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, 0, 3);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorSampleSizeZero() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, 5, 0);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesLargerThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, 15, 3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeLargerThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, 5, 15);
    }
    
    @Test
    public void testGetDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);  
        int[] domain = distribution.getDomain(10, 5, 3);
        assertNotNull(domain);
        assertEquals(3, domain.length);
        assertEquals(2, domain[0]);
        assertEquals(5, domain[1]);
        
        domain = distribution.getDomain(20, 10, 7);
        assertNotNull(domain);
        assertEquals(3, domain.length);
        assertEquals(3, domain[0]);
        assertEquals(7, domain[1]);
    }
    
    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);  
        int lowerDomain = distribution.getLowerDomain(10, 5, 3);
        assertEquals(2, lowerDomain);
        
        lowerDomain = distribution.getLowerDomain(20, 10, 7);
        assertEquals(3, lowerDomain);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);  
        assertEquals(5, distribution.getNumberOfSuccesses());
    }
    
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);  
        assertEquals(10, distribution.getPopulationSize());
    }
    
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);  
        assertEquals(3, distribution.getSampleSize());
    }
    
    @Test
    public void testGetUpperDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);  
        int upperDomain = distribution.getUpperDomain(5, 3);
        assertEquals(3, upperDomain);
        
        upperDomain = distribution.getUpperDomain(10, 7);
        assertEquals(7, upperDomain);
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);  
        double probability = distribution.cumulativeProbability(2);
        assertEquals(0.1111111111111111, probability, 0.000001);
        
        probability = distribution.cumulativeProbability(5);
        assertEquals(1.0, probability, 0.000001);
        
        probability = distribution.cumulativeProbability(6);
        assertEquals(1.0, probability, 0.000001);
        
        probability = distribution.cumulativeProbability(8);
        assertEquals(1.0, probability, 0.000001);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);  
        double probability = distribution.probability(2);
        assertEquals(0.3333333333333333, probability, 0.000001);
        
        probability = distribution.probability(5);
        assertEquals(0.14285714285714285, probability, 0.000001);
        
        probability = distribution.probability(6);
        assertEquals(0.0, probability, 0.000001);
        
        probability = distribution.probability(8);
        assertEquals(0.0, probability, 0.000001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);  
        double probability = distribution.upperCumulativeProbability(2);
        assertEquals(1.0, probability, 0.000001);
        
        probability = distribution.upperCumulativeProbability(5);
        assertEquals(0.8888888888888888, probability, 0.000001);
        
        probability = distribution.upperCumulativeProbability(6);
        assertEquals(0.8888888888888888, probability, 0.000001);
        
        probability = distribution.upperCumulativeProbability(8);
        assertEquals(0.1111111111111111, probability, 0.000001);
    }
    
    @Test
    public void testInnerCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);  
        double probability = distribution.innerCumulativeProbability(2, 5, 1);
        assertEquals(0.8095238095238095, probability, 0.000001);
        
        probability = distribution.innerCumulativeProbability(3, 7, 1);
        assertEquals(0.06060606060606061, probability, 0.000001);
        
        probability = distribution.innerCumulativeProbability(6, 3, -1);
        assertEquals(1.0, probability, 0.000001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);  
        double mean = distribution.getNumericalMean();
        assertEquals(1.5, mean, 0.000001);
        
        distribution = new HypergeometricDistribution(20, 10, 7);  
        mean = distribution.getNumericalMean();
        assertEquals(3.5, mean, 0.000001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);  
        double variance = distribution.getNumericalVariance();
        assertEquals(0.9375, variance, 0.000001);
        
        distribution = new HypergeometricDistribution(20, 10, 7);  
        variance = distribution.getNumericalVariance();
        assertEquals(1.5476190476190477, variance, 0.000001);
    }
    
    @Test
    public void testCalculateNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);  
        double variance = distribution.calculateNumericalVariance();
        assertEquals(0.9375, variance, 0.000001);
        
        distribution = new HypergeometricDistribution(20, 10, 7);  
        variance = distribution.calculateNumericalVariance();
        assertEquals(1.5476190476190477, variance, 0.000001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);  
        int lowerBound = distribution.getSupportLowerBound();
        assertEquals(3, lowerBound);
        
        distribution = new HypergeometricDistribution(20, 10, 7);  
        lowerBound = distribution.getSupportLowerBound();
        assertEquals(10, lowerBound);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);  
        int upperBound = distribution.getSupportUpperBound();
        assertEquals(5, upperBound);
        
        distribution = new HypergeometricDistribution(20, 10, 7);  
        upperBound = distribution.getSupportUpperBound();
        assertEquals(7, upperBound);
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);  
        assertTrue(distribution.isSupportConnected());
        
        distribution = new HypergeometricDistribution(20, 10, 7);  
        assertTrue(distribution.isSupportConnected());
    }
}