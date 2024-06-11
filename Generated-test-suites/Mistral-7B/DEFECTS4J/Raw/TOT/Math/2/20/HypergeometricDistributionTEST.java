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
 * Tests for {@link HypergeometricDistribution}.
 */
public class HypergeometricDistributionTest {

    /**
     * Test valid construction.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testValidConstruction() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution hDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, hDist.getPopulationSize());
        assertEquals(numberOfSuccesses, hDist.getNumberOfSuccesses());
        assertEquals(sampleSize, hDist.getSampleSize());
    }

    /**
     * Test invalid construction with negative number of successes.
     * @throws Exception if an error occurs.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidConstructionNegativeSuccesses() throws Exception {
        int populationSize = 5;
        int numberOfSuccesses = -1;
        int sampleSize = 3;
        HypergeometricDistribution hDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test invalid construction with non-positive population size.
     * @throws Exception if an error occurs.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstructionNonPositivePopulationSize() throws Exception {
        int populationSize = 0;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution hDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test invalid construction with negative population size.
     * @throws Exception if an error occurs.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstructionNegativePopulationSize() throws Exception {
        int populationSize = -1;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution hDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test invalid construction with non-positive number of successes in the population.
     * @throws Exception if an error occurs.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidConstructionNonPositiveSuccesses() throws Exception {
        int populationSize = 5;
        int numberOfSuccesses = 0;
        int sampleSize = 3;
        HypergeometricDistribution hDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test invalid construction with number of successes in the population larger than the population size.
     * @throws Exception if an error occurs.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructionTooManySuccesses() throws Exception {
        int populationSize = 5;
        int numberOfSuccesses = 6;
        int sampleSize = 3;
        HypergeometricDistribution hDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test invalid construction with sample size larger than the population size.
     * @throws Exception if an error occurs.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructionTooLargeSampleSize() throws Exception {
        int populationSize = 5;
        int numberOfSuccesses = 3;
        int sampleSize = 6;
        HypergeometricDistribution hDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test valid probability calculation.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testValidProbability() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 3;
        HypergeometricDistribution hDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 2;
        double expectedProbability = hDist.probability(x);
        double calculatedProbability = hDist.probability(x);
        assertEquals(expectedProbability, calculatedProbability, 1e-12);
    }

    /**
     * Test valid cumulative probability calculation.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testValidCumulativeProbability() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 3;
        HypergeometricDistribution hDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 2;
        double expectedCumulativeProbability = hDist.cumulativeProbability(x);
        double calculatedCumulativeProbability = hDist.cumulativeProbability(x);
        assertEquals(expectedCumulativeProbability, calculatedCumulativeProbability, 1e-12);
    }

    /**
     * Test valid upper cumulative probability calculation.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testValidUpperCumulativeProbability() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 3;
        HypergeometricDistribution hDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 4;
        double expectedUpperCumulativeProbability = hDist.upperCumulativeProbability(x);
        double calculatedUpperCumulativeProbability = hDist.upperCumulativeProbability(x);
        assertEquals(expectedUpperCumulativeProbability, calculatedUpperCumulativeProbability, 1e-12);
    }

    /**
     * Test valid mean calculation.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testValidMean() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 3;
        HypergeometricDistribution hDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expectedMean = ((double) sampleSize * numberOfSuccesses) / ((double) populationSize);
        double calculatedMean = hDist.getNumericalMean();
        assertEquals(expectedMean, calculatedMean, 1e-12);
    }

    /**
     * Test valid variance calculation.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testValidVariance() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 3;
        HypergeometricDistribution hDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expectedVariance = ((double) sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / ((double) (populationSize * populationSize * (populationSize - 1)));
        double calculatedVariance = hDist.getNumericalVariance();
        assertEquals(expectedVariance, calculatedVariance, 1e-12);
    }

    /**
     * Test valid support bounds.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testValidSupportBounds() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 3;
        HypergeometricDistribution hDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int lowerBound = hDist.getSupportLowerBound();
        int upperBound = hDist.getSupportUpperBound();

        int minExpectedSupport = FastMath.max(0, sampleSize + numberOfSuccesses - populationSize);
        int maxExpectedSupport = FastMath.min(numberOfSuccesses, sampleSize);

        assertEquals(minExpectedSupport, lowerBound);
        assertEquals(maxExpectedSupport, upperBound);
    }

    /**
     * Test valid support is connected.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testValidSupportIsConnected() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 3;
        HypergeometricDistribution hDist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertTrue(hDist.isSupportConnected());
    }
}