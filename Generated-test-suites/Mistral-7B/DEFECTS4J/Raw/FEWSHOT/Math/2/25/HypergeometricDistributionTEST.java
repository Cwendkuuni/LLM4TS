package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructors() {
        try {
            int populationSize = 10;
            int numberOfSuccesses = 5;
            int sampleSize = 5;

            HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            assertEquals(populationSize, distribution.getPopulationSize());
            assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
            assertEquals(sampleSize, distribution.getSampleSize());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            // Expected exceptions
        }

        try {
            RandomGenerator rng = new Well19937c();
            int populationSize = 10;
            int numberOfSuccesses = 5;
            int sampleSize = 5;

            HypergeometricDistribution distribution = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
            assertEquals(populationSize, distribution.getPopulationSize());
            assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
            assertEquals(sampleSize, distribution.getSampleSize());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            // Expected exceptions
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testPopulationSizeNegative() {
        int populationSize = -1;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testPopulationSizeZero() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testNumberOfSuccessesNegative() {
        int populationSize = 10;
        int numberOfSuccesses = -1;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNumberOfSuccessesZero() {
        int populationSize = 10;
        int numberOfSuccesses = 0;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testSampleSizeNegative() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = -1;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesLargerThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeLargerThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 11;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();
        int x;

        // Lower domain value
        x = domain[0];
        assertEquals(0.0, distribution.cumulativeProbability(x), 1e-10);

        // Inside domain
        x = 2;
        assertEquals(0.0029296875, distribution.cumulativeProbability(x), 1e-8);

        // Upper domain value
        x = domain[1];
        assertEquals(0.5, distribution.cumulativeProbability(x), 1e-8);

        // Outside domain (left)
        x = 0;
        assertEquals(0.0, distribution.cumulativeProbability(x), 1e-10);

        // Outside domain (right)
        x = 11;
        assertEquals(0.0, distribution.cumulativeProbability(x), 1e-10);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(5, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(10, distribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(5, distribution.getSampleSize());
    }

    @Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x;
        double p;

        // Lower domain value
        x = 0;
        p = 0.0029296875;
        assertEquals(p, distribution.probability(x), 1e-8);

        // Inside domain
        x = 2;
        p = 0.05;
        assertEquals(p, distribution.probability(x), 1e-8);

        // Upper domain value
        x = 5;
        p = 0.2734375;
        assertEquals(p, distribution.probability(x), 1e-8);

        // Outside domain (left)
        x = -1;
        assertNull(distribution.probability(x));

        // Outside domain (right)
        x = 6;
        assertNull(distribution.probability(x));
    }

    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x;
        double p;

        // Lower domain value
        x = 0;
        assertEquals(1.0, distribution.upperCumulativeProbability(x), 1e-10);

        // Inside domain
        x = 2;
        p = 0.4970588235294118;
        assertEquals(p, distribution.upperCumulativeProbability(x), 1e-8);

        // Upper domain value
        x = 5;
        assertEquals(1.0, distribution.upperCumulativeProbability(x), 1e-10);

        // Outside domain (left)
        x = -1;
        assertEquals(0.0, distribution.upperCumulativeProbability(x), 1e-10);

        // Outside domain (right)
        x = 6;
        assertEquals(0.0, distribution.upperCumulativeProbability(x), 1e-10);
    }

    @Test
    public void testInnerCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x;
        double p;

        // Lower domain value
        x = 0;
        p = 1.0;
        assertEquals(p, distribution.innerCumulativeProbability(x, x + 1, 1), 1e-10);

        // Inside domain
        x = 2;
        p = 0.05;
        assertEquals(p, distribution.innerCumulativeProbability(x, x + 1, 1), 1e-8);

        // Upper domain value
        x = 5;
        p = 0.5;
        assertEquals(p, distribution.innerCumulativeProbability(x, x + 1, 1), 1e-8);

        // Reverse interval
        x = 5;
        p = 0.5;
        assertEquals(p, distribution.innerCumulativeProbability(x, x, -1), 1e-8);

        // Outside domain (left)
        x = -1;
        assertFalse(distribution.innerCumulativeProbability(x, x + 1, 1) > 0);

        // Outside domain (right)
        x = 6;
        assertFalse(distribution.innerCumulativeProbability(x, x + 1, 1) > 0);
    }

    @Test
    public void testGetNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(3.5, distribution.getNumericalMean(), 1e-8);
    }

    @Test
    public void testGetNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double variance = distribution.getNumericalVariance();
        assertTrue(variance > 0.0 && variance < 1.0);
        assertEquals(0.1298557407265193, variance, 1e-8);
    }

    @Test
    public void testGetSupportLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(5, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertTrue(distribution.isSupportConnected());
    }
}