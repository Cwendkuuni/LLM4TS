package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

public class HypergeometricDistributionTest {

    @org.junit.Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        Well19937c rng = new Well19937c();
        int populationSize = 10;
        int numberOfSuccesses = -2;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        // the exception should be thrown in the constructor
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroPopulationSize() {
        Well19937c rng = new Well19937c();
        int populationSize = 0;
        int numberOfSuccesses = 2;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        // the exception should be thrown in the constructor
    }

    @org.junit.Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeSampleSize() {
        Well19937c rng = new Well19937c();
        int populationSize = 10;
        int numberOfSuccesses = 2;
        int sampleSize = -3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        // the exception should be thrown in the constructor
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
        Well19937c rng = new Well19937c();
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        // the exception should be thrown in the constructor
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
        Well19937c rng = new Well19937c();
        int populationSize = 10;
        int numberOfSuccesses = 2;
        int sampleSize = 11;
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        // the exception should be thrown in the constructor
    }

    @org.junit.Test
    public void testCumulativeProbability() {
        Well19937c rng = new Well19937c();
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
        int x;

        x = 0;
        assertEquals(0.0, distribution.cumulativeProbability(x), 1e-14);

        x = 5;
        assertEquals(0.5, distribution.cumulativeProbability(x), 1e-14);

        x = 6;
        assertEquals(0.6878969731056268, distribution.cumulativeProbability(x), 1e-14);

        x = 10;
        assertEquals(1.0, distribution.cumulativeProbability(x), 1e-14);
    }

    @org.junit.Test
    public void testProbability() {
        Well19937c rng = new Well19937c();
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
        int x;

        x = 0;
        assertEquals(0.038461568627451, distribution.probability(x), 1e-14);

        x = 5;
        assertEquals(0.078723652816883, distribution.probability(x), 1e-14);

        x = 6;
        assertEquals(0.101351362317144, distribution.probability(x), 1e-14);

        x = 10;
        assertEquals(0.0, distribution.probability(x), 1e-14);
    }

    @org.junit.Test
    public void testUpperCumulativeProbability() {
        Well19937c rng = new Well19937c();
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
        int x;

        x = 0;
        assertEquals(1.0, distribution.upperCumulativeProbability(x), 1e-14);

        x = 5;
        assertEquals(0.5, distribution.upperCumulativeProbability(x), 1e-14);

        x = 6;
        assertEquals(0.4121030268943352, distribution.upperCumulativeProbability(x), 1e-14);

        x = 10;
        assertEquals(0.0, distribution.upperCumulativeProbability(x), 1e-14);
    }

    @org.junit.Test
    public void testGetNumberOfSuccesses() {
        Well19937c rng = new Well19937c();
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
        assertEquals(5, distribution.getNumberOfSuccesses());
    }

    @org.junit.Test
    public void testGetPopulationSize() {
        Well19937c rng = new Well19937c();
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
        assertEquals(10, distribution.getPopulationSize());
    }

    @org.junit.Test
    public void testGetSampleSize() {
        Well19937c rng = new Well19937c();
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
        assertEquals(5, distribution.getSampleSize());
    }

    @org.junit.Test
    public void testNumericalMean() {
        Well19937c rng = new Well19937c();
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
        assertEquals(5.0, distribution.getNumericalMean(), 1e-14);
    }

    @org.junit.Test
    public void testNumericalVariance() {
        Well19937c rng = new Well19937c();
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
        assertEquals(2.916666666666667, distribution.getNumericalVariance(), 1e-14);
    }

    @org.junit.Test
    public void testSupportLowerBound() {
        Well19937c rng = new Well19937c();
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
        assertEquals(5, distribution.getSupportLowerBound());
    }

    @org.junit.Test
    public void testSupportUpperBound() {
        Well19937c rng = new Well19937c();
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
        assertEquals(5, distribution.getSupportUpperBound());
    }

    @org.junit.Test
    public void testIsSupportConnected() {
        Well19937c rng = new Well19937c();
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
        assertTrue(distribution.isSupportConnected());
    }
}