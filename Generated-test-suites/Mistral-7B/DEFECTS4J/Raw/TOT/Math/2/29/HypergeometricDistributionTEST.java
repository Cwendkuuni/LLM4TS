package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

/**
 * Test cases for {@link HypergeometricDistribution}.
 */
public class HypergeometricDistributionTest {

    /**
     * Test construction with valid arguments.
     */
    @org.junit.Test
    public void testConstructionValidArguments() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertNotNull(dist);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
        int[] domain = dist.getDomain();
        assertEquals(2, domain.length);
        assertEquals(FastMath.max(0, numberOfSuccesses - (populationSize - sampleSize)), domain[0]);
        assertEquals(FastMath.min(numberOfSuccesses, sampleSize), domain[1]);
    }

    /**
     * Test construction with invalid arguments.
     */
    @org.junit.Test
    public void testConstructionInvalidArguments() {
        int populationSize = 0;
        int numberOfSuccesses = -1;
        int sampleSize = 11;

        // test populationSize <= 0
        assertThrows(NotStrictlyPositiveException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));

        // test numberOfSuccesses < 0
        populationSize = 5;
        assertThrows(NotPositiveException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));

        // test sampleSize <= 0
        assertThrows(NotPositiveException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));

        // test numberOfSuccesses > populationSize
        numberOfSuccesses = 6;
        assertThrows(NumberIsTooLargeException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));

        // test sampleSize > populationSize
        sampleSize = 7;
        assertThrows(NumberIsTooLargeException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));
    }

    /**
     * Test cumulativeProbability() with different values.
     */
    @org.junit.Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double p = dist.cumulativeProbability(x);
            int expected;
            if (x == domain[0]) {
                expected = 0;
            } else if (x == domain[1]) {
                expected = 1;
            } else {
                double cdf = 0;
                for (int k = domain[0]; k <= x; k++) {
                    cdf += dist.probability(k);
                }
                expected = (int) Math.round(cdf);
            }
            assertEquals(expected, (int) p);
        }
    }

    /**
     * Test probability() with different values.
     */
    @org.junit.Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double p = dist.probability(x);
            double expected = 0;
            if (x == domain[0]) {
                expected = FastMath.binomialProbability(sampleSize, populationSize, numberOfSuccesses, 0.0, 1.0);
            } else if (x == domain[1]) {
                expected = FastMath.binomialProbability(sampleSize, populationSize, numberOfSuccesses + 1, 1.0, 1.0);
            } else {
                expected = FastMath.binomialProbability(sampleSize, populationSize, numberOfSuccesses, 1.0, 1.0)
                        - FastMath.binomialProbability(sampleSize, populationSize, numberOfSuccesses, x + 1, 1.0);
            }
            assertEquals(expected, p, 1e-12);
        }
    }

    /**
     * Test upperCumulativeProbability() with different values.
     */
    @org.junit.Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = dist.getDomain();
        for (int x = domain[1]; x >= domain[0]; x--) {
            double p = dist.upperCumulativeProbability(x);
            int expected = 1;
            if (x == domain[1]) {
                expected = 1;
            } else {
                expected = (int) Math.round(1.0 - dist.cumulativeProbability(x));
            }
            assertEquals(expected, (int) p);
        }
    }

    /**
     * Test getNumericalMean() and getNumericalVariance().
     */
    @org.junit.Test
    public void testNumericalMeanAndVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double mean = dist.getNumericalMean();
        assertEquals(5.0 * 3.0 / 10.0, mean, 1e-12);

        double variance = dist.getNumericalVariance();
        double expectedVariance = (5.0 * 3.0 / 10.0) * (10.0 - 5.0) * (10.0 - 3.0) / (100.0 * (100.0 - 1.0));
        assertEquals(expectedVariance, variance, 1e-12);
    }

    /**
     * Test getSupportLowerBound() and getSupportUpperBound().
     */
    @org.junit.Test
    public void testSupportBounds() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int lowerBound = dist.getSupportLowerBound();
        int upperBound = dist.getSupportUpperBound();
        int[] domain = dist.getDomain();

        assertEquals(lowerBound, domain[0]);
        assertEquals(upperBound, domain[1]);
    }

    /**
     * Test isSupportConnected().
     */
    @org.junit.Test
    public void testIsSupportConnected() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertTrue(dist.isSupportConnected());
    }

    /**
     * Test generating random numbers.
     */
    @org.junit.Test
    public void testRandomNumbers() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 10000;
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        int[] observedFrequencies = new int[sampleSize + 1];
        for (int i = 0; i < sampleSize; i++) {
            int x = dist.nextInt();
            observedFrequencies[x + 1]++;
        }

        int[] expectedFrequencies = new int[sampleSize + 1];
        int totalCount = sampleSize;
        for (int x = 0; x <= sampleSize; x++) {
            double p = dist.probability(x);
            expectedFrequencies[x + 1] = (int) Math.round(totalCount * p);
            totalCount -= expectedFrequencies[x + 1];
        }

        for (int x = 0; x <= sampleSize; x++) {
            assertEquals(expectedFrequencies[x + 1], observedFrequencies[x + 1]);
        }
    }
}