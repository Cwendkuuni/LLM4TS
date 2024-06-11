package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;

        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertNotNull(hypergeometricDistribution);
        assertEquals(populationSize, hypergeometricDistribution.getPopulationSize());
        assertEquals(numberOfSuccesses, hypergeometricDistribution.getNumberOfSuccesses());
        assertEquals(sampleSize, hypergeometricDistribution.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -1;
        int sampleSize = 4;

        HypergeometricDistribution hypergeometricDistribution = null;
        try {
            hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } finally {
            assertNull(hypergeometricDistribution);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 4;

        HypergeometricDistribution hypergeometricDistribution = null;
        try {
            hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } finally {
            assertNull(hypergeometricDistribution);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 0;
        int sampleSize = 4;

        HypergeometricDistribution hypergeometricDistribution = null;
        try {
            hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } finally {
            assertNull(hypergeometricDistribution);
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionNumberOfSuccessesGreaterThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 4;

        HypergeometricDistribution hypergeometricDistribution = null;
        try {
            hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } finally {
            assertNull(hypergeometricDistribution);
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionSampleSizeGreaterThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 11;

        HypergeometricDistribution hypergeometricDistribution = null;
        try {
            hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } finally {
            assertNull(hypergeometricDistribution);
        }
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;

        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = hypergeometricDistribution.getDomain();
        int x = domain[0];
        double expectedCumulativeProbability = 0.0;
        double actualCumulativeProbability = hypergeometricDistribution.cumulativeProbability(x);
        assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.0);

        x++;
        expectedCumulativeProbability = 0.00781187;
        actualCumulativeProbability = hypergeometricDistribution.cumulativeProbability(x);
        assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.00001);

        x = domain[1];
        expectedCumulativeProbability = 1.0;
        actualCumulativeProbability = hypergeometricDistribution.cumulativeProbability(x);
        assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.0);
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;

        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 2;
        double expectedProbability = 0.127931;
        double actualProbability = hypergeometricDistribution.probability(x);
        assertEquals(expectedProbability, actualProbability, 0.00001);
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;

        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 0;
        double expectedUpperCumulativeProbability = 1.0;
        double actualUpperCumulativeProbability = hypergeometricDistribution.upperCumulativeProbability(x);
        assertEquals(expectedUpperCumulativeProbability, actualUpperCumulativeProbability, 0.0);

        x++;
        expectedUpperCumulativeProbability = 0.0;
        actualUpperCumulativeProbability = hypergeometricDistribution.upperCumulativeProbability(x);
        assertEquals(expectedUpperCumulativeProbability, actualUpperCumulativeProbability, 0.0);

        x = populationSize;
        expectedUpperCumulativeProbability = 1.0;
        actualUpperCumulativeProbability = hypergeometricDistribution.upperCumulativeProbability(x);
        assertEquals(expectedUpperCumulativeProbability, actualUpperCumulativeProbability, 0.0);
    }

    @Test
    public void testHypergeometricDistributionSupportBounds() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;

        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int lowerBound = hypergeometricDistribution.getSupportLowerBound();
        int upperBound = hypergeometricDistribution.getSupportUpperBound();

        assertTrue(lowerBound <= populationSize && lowerBound >= 0);
        assertTrue(upperBound <= populationSize && upperBound >= 0);
        assertTrue(lowerBound <= upperBound);
    }

    @Test
    public void testHypergeometricDistributionIsSupportConnected() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;

        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertTrue(hypergeometricDistribution.isSupportConnected());
    }

    @Test
    public void testHypergeometricDistributionRandomSample() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        int[] sample = new int[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            sample[i] = hypergeometricDistribution.nextInt();
        }

        int[] domain = hypergeometricDistribution.getDomain();
        for (int i = 0; i < sampleSize; i++) {
            assertTrue(domain[0] <= sample[i] && sample[i] <= domain[1]);
        }
    }
}