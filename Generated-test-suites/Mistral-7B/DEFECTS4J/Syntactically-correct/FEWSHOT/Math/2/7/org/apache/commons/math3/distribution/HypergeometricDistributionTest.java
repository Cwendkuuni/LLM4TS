package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() throws Exception {
        // Test valid construction
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution hgd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        // Test illegal arguments
        assertThrows(NotPositiveException.class, () -> new HypergeometricDistribution(populationSize, -1, sampleSize));
        assertThrows(NotStrictlyPositiveException.class, () -> new HypergeometricDistribution(populationSize, 0, sampleSize));
        assertThrows(NotStrictlyPositiveException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, 0));
        assertThrows(NumberIsTooLargeException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, populationSize + 1));
        assertThrows(NumberIsTooLargeException.class, () -> new HypergeometricDistribution(populationSize, populationSize, sampleSize));
        assertThrows(NumberIsTooLargeException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, populationSize - numberOfSuccesses + 1));
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution hgd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int i = 0; i <= sampleSize; i++) {
            double p = hgd.probability(i);
            double c = hgd.cumulativeProbability(i);
            assertEquals("Wrong probability for x = " + i, p, c, 0.00001);
        }
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution hgd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int i = 0; i <= sampleSize; i++) {
            double c = hgd.cumulativeProbability(i);
            double cPrev = i > 0 ? hgd.cumulativeProbability(i - 1) : 0;
            assertEquals("Wrong cumulative probability for x = " + i, c, cPrev + hgd.probability(i), 0.00001);
        }

        int x = populationSize + 1;
        double c = hgd.cumulativeProbability(x);
        assertEquals("Wrong cumulative probability for x = " + x, 1.0, c, 0.00001);
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution hgd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int i = sampleSize + 1; i <= populationSize; i++) {
            double c = hgd.upperCumulativeProbability(i);
            double cPrev = i > sampleSize + 1 ? hgd.upperCumulativeProbability(i - 1) : 0;
            assertEquals("Wrong upper cumulative probability for x = " + i, 1.0 - cPrev, c, 0.00001);
        }

        int x = populationSize + 1;
        double c = hgd.upperCumulativeProbability(x);
        assertEquals("Wrong upper cumulative probability for x = " + x, 0.0, c, 0.00001);
    }

    @Test
    public void testHypergeometricDistributionMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution hgd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double mean = hgd.getNumericalMean();
        assertEquals("Wrong mean", mean, (double) sampleSize * numberOfSuccesses / (double) populationSize, 0.00001);
    }

    @Test
    public void testHypergeometricDistributionVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution hgd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double variance = hgd.getNumericalVariance();
        double expectedVariance = ((double) sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses))
                / ((double) populationSize * populationSize * (populationSize - 1));
        assertEquals("Wrong variance", expectedVariance, variance, 0.00001);
    }

    @Test
    public void testHypergeometricDistributionSupport() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution hgd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int lowerBound = hgd.getSupportLowerBound();
        int upperBound = hgd.getSupportUpperBound();

        for (int i = lowerBound; i <= upperBound; i++) {
            assertTrue("x=" + i + " is not in the support", hgd.isSupportPoint(i));
        }

        int x = lowerBound - 1;
        assertFalse("x=" + x + " is in the support", hgd.isSupportPoint(x));
        x = upperBound + 1;
        assertFalse("x=" + x + " is in the support", hgd.isSupportPoint(x));
    }

    private void assertThrows(Class<?> expectedExceptionClass, Runnable action) {
        try {
            action.run();
            fail("Expected to throw " + expectedExceptionClass.getSimpleName());
        } catch (Throwable e) {
            if (expectedExceptionClass.isInstance(e)) {
                return;
            }
            throw new RuntimeException("Unexpected exception", e);
        }
    }
}