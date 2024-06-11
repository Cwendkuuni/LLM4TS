package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

/**
 * Tests for the {@link HypergeometricDistribution} class.
 */
public class HypergeometricDistributionTest {

    /**
     * Test construction with valid parameters.
     *
     * @throws Exception if an exception is thrown during test
     */
    @org.junit.Test
    public void testConstruction()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 20;
        final int numberOfSuccesses = 8;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses,
                sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    /**
     * Test construction with invalid population size.
     *
     * @throws Exception if an exception is thrown during test
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionWithNegativePopulationSize()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = -1;
        final int numberOfSuccesses = 8;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid number of successes.
     *
     * @throws Exception if an exception is thrown during test
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testConstructionWithNegativeNumberOfSuccesses()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 20;
        final int numberOfSuccesses = -1;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid sample size.
     *
     * @throws Exception if an exception is thrown during test
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructionWithInvalidSampleSize()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 20;
        final int numberOfSuccesses = 8;
        final int sampleSize = 21;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test method {@link HypergeometricDistribution#cumulativeProbability(int)}.
     *
     * @throws Exception if an exception is thrown during test
     */
    @org.junit.Test
    public void testCumulativeProbability()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 20;
        final int numberOfSuccesses = 8;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses,
                sampleSize);

        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            assertEquals(dist.cumulativeProbability(x), dist.cumulativeProbability(x), 1e-15);
        }
    }

    /**
     * Test method {@link HypergeometricDistribution#getDomain()}.
     *
     * @throws Exception if an exception is thrown during test
     */
    @org.junit.Test
    public void testGetDomain()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 20;
        final int numberOfSuccesses = 8;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses,
                sampleSize);
        final int[] expectedDomain = new int[] { 5, 9 };
        assertArrayEquals(expectedDomain, dist.getDomain());
    }

    /**
     * Test method {@link HypergeometricDistribution#getLowerDomain(int, int, int)}.
     *
     * @throws Exception if an exception is thrown during test
     */
    @org.junit.Test
    public void testGetLowerDomain()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 20;
        final int numberOfSuccesses = 8;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses,
                sampleSize);
        assertEquals(3, dist.getLowerDomain(populationSize, numberOfSuccesses, sampleSize));
    }

    /**
     * Test method {@link HypergeometricDistribution#getUpperDomain(int, int)}.
     *
     * @throws Exception if an exception is thrown during test
     */
    @org.junit.Test
    public void testGetUpperDomain()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 20;
        final int numberOfSuccesses = 8;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses,
                sampleSize);
        assertEquals(8, dist.getUpperDomain(numberOfSuccesses, sampleSize));
    }

    /**
     * Test method {@link HypergeometricDistribution#probability(int)}.
     *
     * @throws Exception if an exception is thrown during test
     */
    @org.junit.Test
    public void testProbability()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 20;
        final int numberOfSuccesses = 8;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses,
                sampleSize);

        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            assertEquals(dist.probability(x), dist.probability(x), 1e-15);
        }
    }

    /**
     * Test method {@link HypergeometricDistribution#upperCumulativeProbability(int)}.
     *
     * @throws Exception if an exception is thrown during test
     */
    @org.junit.Test
    public void testUpperCumulativeProbability()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 20;
        final int numberOfSuccesses = 8;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses,
                sampleSize);

        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            assertEquals(1.0 - dist.cumulativeProbability(domain[1]), dist.upperCumulativeProbability(x),
                        1e-15);
        }
    }

    /**
     * Test method {@link HypergeometricDistribution#getNumericalMean()}.
     *
     * @throws Exception if an exception is thrown during test
     */
    @org.junit.Test
    public void testNumericalMean()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 20;
        final int numberOfSuccesses = 8;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses,
                sampleSize);
        assertEquals(populationSize * numberOfSuccesses / populationSize, dist.getNumericalMean(), 1e-15);
    }

    /**
     * Test method {@link HypergeometricDistribution#getNumericalVariance()}.
     *
     * @throws Exception if an exception is thrown during test
     */
    @org.junit.Test
    public void testNumericalVariance()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 20;
        final int numberOfSuccesses = 8;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses,
                sampleSize);
        final double numericalVariance = dist.getNumericalVariance();
        final double expectedVariance = (populationSize * numberOfSuccesses * (populationSize - numberOfSuccesses)
                * (populationSize - sampleSize) * (populationSize - sampleSize)) / (populationSize * populationSize * (populationSize
                - 1));
        assertEquals(expectedVariance, numericalVariance, 1e-15);
    }

    /**
     * Test method {@link HypergeometricDistribution#isSupportConnected()}.
     *
     * @throws Exception if an exception is thrown during test
     */
    @org.junit.Test
    public void testIsSupportConnected()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 20;
        final int numberOfSuccesses = 8;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses,
                sampleSize);
        assertTrue(dist.isSupportConnected());
    }

    /**
     * Test method {@link HypergeometricDistribution#sample()}.
     *
     * @throws Exception if an exception is thrown during test
     */
    @org.junit.Test
    public void testSample()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        final int populationSize = 20;
        final int numberOfSuccesses = 8;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses,
                sampleSize);
        final int[] sample = new int[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            sample[i] = dist.sample();
        }

        int[] domain = dist.getDomain();
        for (int x : sample) {
            assertTrue(x >= domain[0] && x <= domain[1]);
        }
    }
}