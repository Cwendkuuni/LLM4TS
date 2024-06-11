package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {
    @Test
    public void testHypergeometricDistribution() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(populationSize, hd.getPopulationSize());
        assertEquals(numberOfSuccesses, hd.getNumberOfSuccesses());
        assertEquals(sampleSize, hd.getSampleSize());

        int[] domain = hd.getDomain(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(FastMath.max(0, numberOfSuccesses - (populationSize - sampleSize)), domain[0]);
        assertEquals(FastMath.min(numberOfSuccesses, sampleSize), domain[1]);

        assertEquals(hd.getSupportLowerBound(), domain[0]);
        assertEquals(hd.getSupportUpperBound(), domain[1]);

        assertEquals(hd.getNumericalMean(), (double) (sampleSize * numberOfSuccesses) / populationSize, 0.001);

        double numericalVariance = hd.getNumericalVariance();
        assertEquals(numericalVariance, hd.calculateNumericalVariance(), 0.001);

        for (int x = domain[0]; x <= domain[1]; x++) {
            assertEquals(hd.cumulativeProbability(x), hd.upperCumulativeProbability(x + 1), 0.001);
            assertEquals(hd.probability(x), hd.cumulativeProbability(x + 1) - hd.cumulativeProbability(x - 1), 0.001);
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -3, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroPopulationSize() {
        new HypergeometricDistribution(0, 3, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroPopulationSizeNumberOfSuccesses() {
        new HypergeometricDistribution(10, 0, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSampleSize() {
        new HypergeometricDistribution(10, 3, -5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(10, 11, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(10, 3, 11);
    }
}