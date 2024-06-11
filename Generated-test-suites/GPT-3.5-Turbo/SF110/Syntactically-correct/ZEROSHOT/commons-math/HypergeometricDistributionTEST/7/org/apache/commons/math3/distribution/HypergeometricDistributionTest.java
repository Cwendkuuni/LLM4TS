package org.apache.commons.math3.distribution;

import org.junit.*;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_InvalidPopulationSize() {
        int populationSize = -10;
        int numberOfSuccesses = 50;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_InvalidNumberOfSuccesses() {
        int populationSize = 100;
        int numberOfSuccesses = -10;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_InvalidSampleSize() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = -10;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_NumberOfSuccessesGreaterThanPopulationSize() {
        int populationSize = 100;
        int numberOfSuccesses = 150;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_SampleSizeGreaterThanPopulationSize() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 200;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }
    
    @Test
    public void testCumulativeProbability() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        double result = dist.cumulativeProbability(10);
        assertEquals(0.093, result, 0.001);
        
        result = dist.cumulativeProbability(30);
        assertEquals(0.81, result, 0.001);
        
        result = dist.cumulativeProbability(60);
        assertEquals(0.997, result, 0.001);
    }
    
    @Test
    public void testGetDomain() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        int[] result = dist.getDomain(populationSize, numberOfSuccesses, sampleSize);
        
        assertEquals(25, result[0]);
        assertEquals(50, result[1]);
    }
    
    @Test
    public void testGetLowerDomain() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        int result = dist.getLowerDomain(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(25, result);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        int result = dist.getNumberOfSuccesses();
        assertEquals(50, result);
    }
    
    @Test
    public void testGetPopulationSize() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        int result = dist.getPopulationSize();
        assertEquals(100, result);
    }
    
    @Test
    public void testGetSampleSize() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        int result = dist.getSampleSize();
        assertEquals(25, result);
    }
    
    @Test
    public void testGetUpperDomain() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        int result = dist.getUpperDomain(numberOfSuccesses, sampleSize);
        assertEquals(25, result);
    }
    
    @Test
    public void testProbability() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        double result = dist.probability(10);
        assertEquals(0.101, result, 0.001);
        
        result = dist.probability(30);
        assertEquals(0.362, result, 0.001);
        
        result = dist.probability(60);
        assertEquals(0.001, result, 0.001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        double result = dist.upperCumulativeProbability(10);
        assertEquals(0.998, result, 0.001);
        
        result = dist.upperCumulativeProbability(30);
        assertEquals(0.188, result, 0.001);
        
        result = dist.upperCumulativeProbability(60);
        assertEquals(0.0, result, 0.001);
    }
    
    @Test
    public void testInnerCumulativeProbability() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        double result = dist.innerCumulativeProbability(25, 50, 1);
        assertEquals(0.003, result, 0.001);
        
        result = dist.innerCumulativeProbability(50, 25, -1);
        assertEquals(0.997, result, 0.001);
    }
    
    @Test
    public void testGetNumericalMean() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        double result = dist.getNumericalMean();
        assertEquals(12.5, result, 0.001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        double result = dist.getNumericalVariance();
        assertEquals(57.716, result, 0.001);
    }
    
    @Test
    public void testCalculateNumericalVariance() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        double result = dist.calculateNumericalVariance();
        assertEquals(57.716, result, 0.001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        int result = dist.getSupportLowerBound();
        assertEquals(75, result);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        int result = dist.getSupportUpperBound();
        assertEquals(25, result);
    }
    
    @Test
    public void testIsSupportConnected() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 25;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        boolean result = dist.isSupportConnected();
        assertTrue(result);
    }
}