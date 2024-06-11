package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link HypergeometricDistribution}.
 */
public class HypergeometricDistributionTest {

    /**
     * Test construction with valid parameters.
     */
    @Test
    public void testConstruction() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    /**
     * Test construction with invalid population size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionInvalidPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(-1, 5, 3);
    }

    /**
     * Test construction with invalid number of successes.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructionInvalidNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, -1, 3);
    }

    /**
     * Test construction with invalid sample size.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructionInvalidSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, -1);
    }

    /**
     * Test construction with invalid population size, number of successes, and sample size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructionInvalidParameters() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 11, 3);
    }

    /**
     * Test {@link HypergeometricDistribution#getNumberOfSuccesses()}.
     */
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(5, dist.getNumberOfSuccesses());
    }

    /**
     * Test {@link HypergeometricDistribution#getPopulationSize()}.
     */
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, dist.getPopulationSize());
    }

    /**
     * Test {@link HypergeometricDistribution#getSampleSize()}.
     */
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, dist.getSampleSize());
    }

    /**
     * Test {@link HypergeometricDistribution#cumulativeProbability(int)}.
     */
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int[] domain = dist.getDomain();
        int xMin = domain[0];
        int xMax = domain[1];
        for (int x = xMin; x <= xMax; x++) {
            double p = dist.cumulativeProbability(x);
            int xPrime = x;
            if (x < xMax) {
                xPrime++;
            }
            double pPrime = dist.cumulativeProbability(xPrime);
            assertTrue(p <= pPrime);
        }
    }

    /**
     * Test {@link HypergeometricDistribution#probability(int)}.
     */
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double p = dist.probability(x);
            double q = dist.upperCumulativeProbability(x);
            assertEquals(q, p, 0.0001);
        }
    }

    /**
     * Test {@link HypergeometricDistribution#upperCumulativeProbability(int)}.
     */
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int[] domain = dist.getDomain();
        int xMin = domain[0];
        int xMax = domain[1];
        for (int x = xMin; x <= xMax; x++) {
            double p = dist.upperCumulativeProbability(x);
            int xPrime = x;
            if (x < xMax) {
                xPrime++;
            }
            double pPrime = dist.upperCumulativeProbability(xPrime);
            assertFalse(p <= pPrime);
        }
    }

    /**
     * Test {@link HypergeometricDistribution#getDomain()}.
     */
    @Test
    public void testGetDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int[] expectedDomain = dist.getDomain();
        int populationSize = dist.getPopulationSize();
        int numberOfSuccesses = dist.getNumberOfSuccesses();
        int sampleSize = dist.getSampleSize();
        assertEquals(expectedDomain[0], FastMath.max(0, numberOfSuccesses + sampleSize - populationSize));
        assertEquals(expectedDomain[1], FastMath.min(numberOfSuccesses, sampleSize));
    }

    /**
     * Test {@link HypergeometricDistribution#getLowerDomain(int, int, int)}.
     */
    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int populationSize = dist.getPopulationSize();
        int numberOfSuccesses = dist.getNumberOfSuccesses();
        int sampleSize = dist.getSampleSize();
        int x = FastMath.max(0, numberOfSuccesses + sampleSize - populationSize);
        int expectedLowerDomain = dist.getLowerDomain(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(x, expectedLowerDomain);
    }

    /**
     * Test {@link HypergeometricDistribution#getUpperDomain(int, int)}.
     */
    @Test
    public void testGetUpperDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int populationSize = dist.getPopulationSize();
        int numberOfSuccesses = dist.getNumberOfSuccesses();
        int sampleSize = dist.getSampleSize();
        int x = FastMath.min(numberOfSuccesses, sampleSize);
        int expectedUpperDomain = dist.getUpperDomain(numberOfSuccesses, sampleSize);
        assertEquals(x, expectedUpperDomain);
    }

    /**
     * Test {@link HypergeometricDistribution#getNumericalMean()}.
     */
    @Test
    public void testNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double numericalMean = dist.getNumericalMean();
        assertEquals(3.0, numericalMean, 0.0001);
    }

    /**
     * Test {@link HypergeometricDistribution#getNumericalVariance()}.
     */
    @Test
    public void testNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double numericalVariance = dist.getNumericalVariance();
        double expectedVariance = 0.010468728643172162;
        assertEquals(expectedVariance, numericalVariance, 0.0001);
    }

    /**
     * Test {@link HypergeometricDistribution#getSupportLowerBound()}.
     */
    @Test
    public void testSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int supportLowerBound = dist.getSupportLowerBound();
        int populationSize = dist.getPopulationSize();
        int numberOfSuccesses = dist.getNumberOfSuccesses();
        int sampleSize = dist.getSampleSize();
        int expectedSupportLowerBound = FastMath.max(0, numberOfSuccesses + sampleSize - populationSize);
        assertEquals(expectedSupportLowerBound, supportLowerBound);
    }

    /**
     * Test {@link HypergeometricDistribution#getSupportUpperBound()}.
     */
    @Test
    public void testSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int supportUpperBound = dist.getSupportUpperBound();
        int populationSize = dist.getPopulationSize();
        int numberOfSuccesses = dist.getNumberOfSuccesses();
        int sampleSize = dist.getSampleSize();
        int expectedSupportUpperBound = FastMath.min(numberOfSuccesses, sampleSize);
        assertEquals(expectedSupportUpperBound, supportUpperBound);
    }

    /**
     * Test {@link HypergeometricDistribution#isSupportConnected()}.
     */
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertTrue(dist.isSupportConnected());
    }

    /**
     * Test {@link HypergeometricDistribution#sample()}.
     */
    @Test
    public void testSample() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 3);
        int x = dist.sample();
        int xPrime = dist.sample();
        assertTrue(x >= 0);
        assertTrue(x <= 5);
        assertTrue(xPrime >= 0);
        assertTrue(xPrime <= 5);
        assertFalse(x == xPrime);
    }

    /**
     * Test {@link HypergeometricDistribution#sample(int, int)}.
     */
    @Test
    public void testSampleCount() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 3);
        int count = 10000;
        int[] results = new int[count];
        for (int i = 0; i < count; i++) {
            results[i] = dist.sample();
        }
        int[] expectedDomain = dist.getDomain();
        int populationSize = dist.getPopulationSize();
        int numberOfSuccesses = dist.getNumberOfSuccesses();
        int sampleSize = dist.getSampleSize();
        int xMin = expectedDomain[0];
        int xMax = expectedDomain[1];
        int totalSuccesses = 0;
        int totalSamples = 0;
        for (int x : results) {
            totalSamples++;
            if (x >= xMin && x <= xMax) {
                totalSuccesses++;
            }
        }
        double expectedProbability = 1.0;
        if (xMax > xMin) {
            expectedProbability = (double) (numberOfSuccesses * sampleSize) / ((double) populationSize * ((double) sampleSize - 1));
        }
        double observedProbability = (double) totalSuccesses / totalSamples;
        assertEquals(expectedProbability, observedProbability, 0.0001);
    }
}