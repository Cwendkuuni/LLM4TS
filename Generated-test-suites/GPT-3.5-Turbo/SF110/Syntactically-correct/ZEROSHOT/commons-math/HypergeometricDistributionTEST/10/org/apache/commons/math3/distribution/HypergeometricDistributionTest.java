package org.apache.commons.math3.distribution;

import org.junit.Test;
import org.junit.Assert;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(populationSize, hypergeometricDistribution.getPopulationSize());
        Assert.assertEquals(numberOfSuccesses, hypergeometricDistribution.getNumberOfSuccesses());
        Assert.assertEquals(sampleSize, hypergeometricDistribution.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_PopulationSizeNotPositive() {
        int populationSize = -10;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_NumberOfSuccessesNotPositive() {
        int populationSize = 100;
        int numberOfSuccesses = -50;
        int sampleSize = 10;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_SampleSizeNotPositive() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = -10;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_NumberOfSuccessesLargerThanPopulationSize() {
        int populationSize = 100;
        int numberOfSuccesses = 200;
        int sampleSize = 10;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_SampleSizeLargerThanPopulationSize() {
        int populationSize = 100;
        int numberOfSuccesses = 10;
        int sampleSize = 200;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test
    public void testCumulativeProbability() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(0.0, hypergeometricDistribution.cumulativeProbability(0), 0.0001);
        Assert.assertEquals(1.0, hypergeometricDistribution.cumulativeProbability(100), 0.0001);
        Assert.assertEquals(0.5437, hypergeometricDistribution.cumulativeProbability(5), 0.0001);
    }

    @Test
    public void testGetDomain() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = hypergeometricDistribution.getDomain(populationSize, numberOfSuccesses, sampleSize);
        Assert.assertArrayEquals(new int[]{40, 50}, domain);
    }

    @Test
    public void testGetLowerDomain() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int lowerDomain = hypergeometricDistribution.getLowerDomain(populationSize, numberOfSuccesses, sampleSize);
        Assert.assertEquals(40, lowerDomain);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(numberOfSuccesses, hypergeometricDistribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(populationSize, hypergeometricDistribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        Assert.assertEquals(sampleSize, hypergeometricDistribution.getSampleSize());
    }

    @Test
    public void testGetUpperDomain() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int upperDomain = hypergeometricDistribution.getUpperDomain(numberOfSuccesses, sampleSize);
        Assert.assertEquals(sampleSize, upperDomain);
    }

    @Test
    public void testProbability() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(0.0077, hypergeometricDistribution.probability(5), 0.0001);
        Assert.assertEquals(0.0556, hypergeometricDistribution.probability(6), 0.0001);
        Assert.assertEquals(0.1509, hypergeometricDistribution.probability(7), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(1.0, hypergeometricDistribution.upperCumulativeProbability(0), 0.0001);
        Assert.assertEquals(0.0, hypergeometricDistribution.upperCumulativeProbability(100), 0.0001);
        Assert.assertEquals(0.4563, hypergeometricDistribution.upperCumulativeProbability(5), 0.0001);
    }

    @Test
    public void testInnerCumulativeProbability() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double innerCumulativeProbability = hypergeometricDistribution.innerCumulativeProbability(40, 50, 1);
        Assert.assertEquals(0.4545, innerCumulativeProbability, 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(5.0, hypergeometricDistribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(0.1111, hypergeometricDistribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testCalculateNumericalVariance() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double calculateNumericalVariance = hypergeometricDistribution.calculateNumericalVariance();
        Assert.assertEquals(0.1111, calculateNumericalVariance, 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(0, hypergeometricDistribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(10, hypergeometricDistribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        
        Assert.assertTrue(hypergeometricDistribution.isSupportConnected());
    }
}