package org.apache.commons.math3.distribution;

import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {
    @Test
    public void testConstructor() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        try {
            HypergeometricDistribution hyperDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            assertEquals(populationSize, hyperDist.getPopulationSize());
            assertEquals(numberOfSuccesses, hyperDist.getNumberOfSuccesses());
            assertEquals(sampleSize, hyperDist.getSampleSize());
        } catch (Exception e) {
            fail();
        }
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativePopulationSize() {
        int populationSize = -100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        int populationSize = 100;
        int numberOfSuccesses = -50;
        int sampleSize = 10;
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeSampleSize() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = -10;
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
        int populationSize = 50;
        int numberOfSuccesses = 100;
        int sampleSize = 10;
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeLargerThanPopulationSize() {
        int populationSize = 1000;
        int numberOfSuccesses = 500;
        int sampleSize = 1001;
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }
    
    @Test
    public void testCumulativeProbability() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        assertEquals(0.0, hyperDist.cumulativeProbability(-1), 0.000001);
        assertEquals(0.0, hyperDist.cumulativeProbability(0), 0.000001);
        assertEquals(0.111189, hyperDist.cumulativeProbability(1), 0.000001);
        assertEquals(0.867817, hyperDist.cumulativeProbability(6), 0.000001);
    }
    
    @Test
    public void testGetDomain() {
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(100, 50, 10);
        int[] domain = hyperDist.getDomain(100, 50, 10);
        assertEquals(0, domain[0]);
        assertEquals(10, domain[1]);
    }
    
    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(100, 50, 10);
        int lowerDomain = hyperDist.getLowerDomain(100, 50, 10);
        assertEquals(40, lowerDomain);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(numberOfSuccesses, hyperDist.getNumberOfSuccesses());
    }
    
    @Test
    public void testGetPopulationSize() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, hyperDist.getPopulationSize());
    }
    
    @Test
    public void testGetSampleSize() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(sampleSize, hyperDist.getSampleSize());
    }
    
    @Test
    public void testGetUpperDomain() {
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(100, 50, 10);
        int upperDomain = hyperDist.getUpperDomain(50, 10);
        assertEquals(10, upperDomain);
    }
    
    @Test
    public void testProbability() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        assertEquals(0.0, hyperDist.probability(-1), 0.000001);
        assertEquals(0.2440408, hyperDist.probability(5), 0.000001);
        assertEquals(0.0, hyperDist.probability(11), 0.000001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        assertEquals(1.0, hyperDist.upperCumulativeProbability(-1), 0.000001);
        assertEquals(1.0, hyperDist.upperCumulativeProbability(0), 0.000001);
        assertEquals(0.9930923, hyperDist.upperCumulativeProbability(1), 0.000001);
        assertEquals(0.0200817, hyperDist.upperCumulativeProbability(6), 0.000001);
    }
    
    @Test
    public void testInnerCumulativeProbability() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        assertEquals(0.38984008, hyperDist.innerCumulativeProbability(0, 5, 1), 0.000001);
        assertEquals(0.86781738, hyperDist.innerCumulativeProbability(6, 10, 1), 0.000001);
    }
    
    @Test
    public void testGetNumericalMean() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        double mean = sampleSize * numberOfSuccesses / (double)populationSize;
        assertEquals(mean, hyperDist.getNumericalMean(), 0.000001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        double variance = sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses) / (populationSize * populationSize * (populationSize - 1.0));
        assertEquals(variance, hyperDist.getNumericalVariance(), 0.000001);
    }
    
    @Test
    public void testCalculateNumericalVariance() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        double variance = sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses) / (populationSize * populationSize * (populationSize - 1.0));
        assertEquals(variance, hyperDist.calculateNumericalVariance(), 0.000001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        int lowerBound = sampleSize + numberOfSuccesses - populationSize;
        assertEquals(lowerBound, hyperDist.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        int upperBound = Math.min(numberOfSuccesses, sampleSize);
        assertEquals(upperBound, hyperDist.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        assertTrue(hyperDist.isSupportConnected());
    }
}