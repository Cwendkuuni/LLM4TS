package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getLowerBound(), 0.0);
        assertEquals(1.0, distribution.getUpperBound(), 0.0);
        assertEquals(0.5, distribution.getNumericalMean(), 0.0);
        assertEquals(0.25, distribution.getNumericalVariance(), 0.0);
    }

    @Test
    public void testUniformRealDistributionConstructorWithBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 3.0);
        assertEquals(1.0, distribution.getLowerBound(), 0.0);
        assertEquals(3.0, distribution.getUpperBound(), 0.0);
        assertEquals(2.0, distribution.getNumericalMean(), 0.0);
        assertEquals(1.5874999999999999, distribution.getNumericalVariance(), 0.0);
    }

    @Test
    public void testUniformRealDistributionConstructorWithBoundsAndAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 3.0, 1e-6);
        assertEquals(1.0, distribution.getLowerBound(), 1e-6);
        assertEquals(3.0, distribution.getUpperBound(), 1e-6);
        assertEquals(2.0, distribution.getNumericalMean(), 1e-6);
        assertEquals(1.5874999999999999, distribution.getNumericalVariance(), 1e-6);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionConstructorWithInvalidBounds() {
        new UniformRealDistribution(3.0, 1.0);
    }

    @Test
    public void testUniformRealDistributionDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double expectedDensity = 1.0 / (1.0 - 0.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double actualDensity = distribution.density(x);
            assertEquals(expectedDensity, actualDensity, 1e-6);
        }
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double actualProbability = distribution.cumulativeProbability(x);
            double expectedProbability = x;
            assertEquals(expectedProbability, actualProbability, 1e-6);
        }
    }

    @Test
    public void testUniformRealDistributionGetSolverAbsoluteAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 1e-6);
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= 0.0 && sample <= 1.0);
        }
    }

    @Test
    public void testUniformRealDistributionSampleMultipleThreads() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        int numThreads = 10;
        int numSamplesPerThread = 100;
        Thread[] threads = new Thread[numThreads];
        double[] samples = new double[numThreads * numSamplesPerThread];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < numSamplesPerThread; j++) {
                    samples[i * numSamplesPerThread + j] = distribution.sample();
                }
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        for (int i = 0; i < numThreads * numSamplesPerThread; i++) {
            assertTrue(samples[i] >= 0.0 && samples[i] <= 1.0);
        }
    }

    @Test
    public void testUniformRealDistributionGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.5, distribution.getNumericalMean(), 1e-6);
    }

    @Test
    public void testUniformRealDistributionGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.25, distribution.getNumericalVariance(), 1e-6);
    }

    @Test
    public void testUniformRealDistributionGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.0, distribution.getSupportLowerBound(), 1e-6);
    }

    @Test
    public void testUniformRealDistributionGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(1.0, distribution.getSupportUpperBound(), 1e-6);
    }

    @Test
    public void testUniformRealDistributionIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertTrue(distribution.isSupportConnected());
    }
}