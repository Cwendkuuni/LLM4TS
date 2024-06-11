package org.apache.commons.math3.distribution;

import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(populationSize, distribution.getPopulationSize());
        Assert.assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        Assert.assertEquals(sampleSize, distribution.getSampleSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidPopulationSize() {
        int populationSize = -100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidNumberOfSuccesses() {
        int populationSize = 100;
        int numberOfSuccesses = -50;
        int sampleSize = 10;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidSampleSize() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = -10;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorInvalidNumberOfSuccessesGreaterThanPopulationSize() {
        int populationSize = 100;
        int numberOfSuccesses = 150;
        int sampleSize = 10;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorInvalidSampleSizeGreaterThanPopulationSize() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 110;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test
    public void testCumulativeProbability() {
        int populationSize = 30;
        int numberOfSuccesses = 20;
        int sampleSize = 10;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double cumulativeProbability = distribution.cumulativeProbability(5);
        Assert.assertEquals(0.0005731439, cumulativeProbability, 1e-9);

        cumulativeProbability = distribution.cumulativeProbability(12);
        Assert.assertEquals(0.3740355763, cumulativeProbability, 1e-9);

        cumulativeProbability = distribution.cumulativeProbability(20);
        Assert.assertEquals(1.0, cumulativeProbability, 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        int populationSize = 30;
        int numberOfSuccesses = 20;
        int sampleSize = 10;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalMean = distribution.getNumericalMean();
        Assert.assertEquals(3.3333333333, numericalMean, 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        int populationSize = 30;
        int numberOfSuccesses = 20;
        int sampleSize = 10;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalVariance = distribution.getNumericalVariance();
        Assert.assertEquals(2.2222222222, numericalVariance, 1e-9);
    }

    @Test
    public void testProbability() {
        int populationSize = 30;
        int numberOfSuccesses = 20;
        int sampleSize = 10;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double probability = distribution.probability(5);
        Assert.assertEquals(0.0077421255, probability, 1e-9);

        probability = distribution.probability(12);
        Assert.assertEquals(0.3655089857, probability, 1e-9);

        probability = distribution.probability(20);
        Assert.assertEquals(0.0000043716, probability, 1e-9);
    }

    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 30;
        int numberOfSuccesses = 20;
        int sampleSize = 10;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double cumulativeProbability = distribution.upperCumulativeProbability(5);
        Assert.assertEquals(1.0, cumulativeProbability, 1e-9);

        cumulativeProbability = distribution.upperCumulativeProbability(12);
        Assert.assertEquals(0.6259644237, cumulativeProbability, 1e-9);

        cumulativeProbability = distribution.upperCumulativeProbability(20);
        Assert.assertEquals(0.0000043716, cumulativeProbability, 1e-9);
    }

    @Test
    public void testSupportLowerBound() {
        int populationSize = 30;
        int numberOfSuccesses = 20;
        int sampleSize = 10;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int lowerBound = distribution.getSupportLowerBound();
        Assert.assertEquals(0, lowerBound);
    }

    @Test
    public void testSupportUpperBound() {
        int populationSize = 30;
        int numberOfSuccesses = 20;
        int sampleSize = 10;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int upperBound = distribution.getSupportUpperBound();
        Assert.assertEquals(10, upperBound);
    }

    @Test
    public void testIsSupportConnected() {
        int populationSize = 30;
        int numberOfSuccesses = 20;
        int sampleSize = 10;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        boolean isSupportConnected = distribution.isSupportConnected();
        Assert.assertEquals(true, isSupportConnected);
    }
}