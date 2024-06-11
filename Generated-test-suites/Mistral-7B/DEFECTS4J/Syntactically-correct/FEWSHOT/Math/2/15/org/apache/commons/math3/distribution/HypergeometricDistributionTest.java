package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionImplTest;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

public class HypergeometricDistributionTest {
    @org.junit.Test
    public void testConstructors() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
        }

        RandomGenerator rng = new Well19937c();
        try {
            new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
        }
    }

    @org.junit.Test(expected = NotPositiveException.class)
    public void testNumberOfSuccessesNegative() {
        int populationSize = 10;
        int numberOfSuccesses = -1;
        int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testPopulationSizeZero() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testSampleSizeZero() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 0;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesGreaterThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeGreaterThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 11;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double cumulativeProbability = distribution.cumulativeProbability(x);
            HypergeometricDistributionImplTest.assertEquals(x, cumulativeProbability, distribution);
        }
    }

    @org.junit.Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int x = 0; x <= distribution.getSupportUpperBound(); x++) {
            double probability = distribution.probability(x);
            HypergeometricDistributionImplTest.assertEquals(x, probability, distribution);
        }
    }

    @org.junit.Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double upperCumulativeProbability = distribution.upperCumulativeProbability(x);
            HypergeometricDistributionImplTest.assertEquals(1.0 - distribution.cumulativeProbability(x), upperCumulativeProbability, distribution);
        }
    }

    @org.junit.Test
    public void testMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double mean = distribution.getNumericalMean();
        assertEquals(mean, (double) sampleSize * numberOfSuccesses / (double) populationSize, 0.0);
    }

    @org.junit.Test
    public void testVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double variance = distribution.getNumericalVariance();
        double expectedVariance = ((double) sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / ((double) populationSize * populationSize * (populationSize - 1));
        assertEquals(variance, expectedVariance, 0.0);
    }

    @org.junit.Test
    public void testGetNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
    }

    @org.junit.Test
    public void testGetPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, distribution.getPopulationSize());
    }

    @org.junit.Test
    public void testGetSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(sampleSize, distribution.getSampleSize());
    }

    @org.junit.Test
    public void testGetLowerDomain() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int lowerDomain = distribution.getSupportLowerBound();
        int expectedLowerDomain = FastMath.max(0, numberOfSuccesses + sampleSize - populationSize);
        assertEquals(lowerDomain, expectedLowerDomain);
    }

    @org.junit.Test
    public void testGetUpperDomain() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int upperDomain = distribution.getSupportUpperBound();
        int expectedUpperDomain = FastMath.min(numberOfSuccesses, sampleSize);
        assertEquals(upperDomain, expectedUpperDomain);
    }

    @org.junit.Test
    public void testIsSupportConnected() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertTrue(distribution.isSupportConnected());
    }
}

class HypergeometricDistributionImplTest {
    static void assertEquals(int x, double p, HypergeometricDistribution distribution) {
        double prob = distribution.probability(x);
        assertEquals(x, prob, 0.00001);
    }
}