package org.apache.commons.math3.distribution;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    /**
     * Tests constructor with default bounds.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
    }

    /**
     * Tests constructor with valid bounds.
     */
    @Test
    public void testConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution(0.5, 1.5);
        assertEquals(0.5, dist.getSupportLowerBound(), 0.0);
        assertEquals(1.5, dist.getSupportUpperBound(), 0.0);
    }

    /**
     * Tests constructor with invalid bounds.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructor() throws NumberIsTooLargeException {
        new UniformRealDistribution(1.0, 0.5);
    }

    /**
     * Tests constructor with same bounds.
     */
    @Test
    public void testSameBoundsConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution(0.5, 0.5);
        assertEquals(0.5, dist.getSupportLowerBound(), 0.0);
        assertEquals(0.5, dist.getSupportUpperBound(), 0.0);
    }

    /**
     * Tests density method.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = dist.density(x);
            assertEquals(1.0 / (1.0 - 0.0), density, 1e-5);
        }
        for (double x = 1.01; x <= 1.1; x += 0.01) {
            double density = dist.density(x);
            assertEquals(0.0, density, 1e-5);
        }
    }

    /**
     * Tests cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double p = dist.cumulativeProbability(x);
            double expected = x;
            if (x <= 0.5) {
                expected *= 0.5;
            } else {
                expected *= 0.5 + (1.0 - x) * 0.5;
            }
            assertEquals(expected, p, 1e-5);
        }
    }

    /**
     * Tests getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.5, dist.getNumericalMean(), 1e-5);
        UniformRealDistribution dist_ = new UniformRealDistribution(1.0, 2.0);
        assertEquals(1.5, dist_.getNumericalMean(), 1e-5);
    }

    /**
     * Tests getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.0625, dist.getNumericalVariance(), 1e-5);
        UniformRealDistribution dist_ = new UniformRealDistribution(1.0, 2.0);
        assertEquals(0.25, dist_.getNumericalVariance(), 1e-5);
    }

    /**
     * Tests sample method.
     */
    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        for (int i = 0; i < 1000; i++) {
            double x = dist.sample();
            assertTrue(x >= 0.0 && x <= 1.0);
        }
    }

    /**
     * Tests sample method with large number of samples.
     */
    @Test
    public void testSampleLarge() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        double[] samples = new double[1000000];
        for (int i = 0; i < samples.length; i++) {
            samples[i] = dist.sample();
        }
        double sum = 0.0;
        for (double sample : samples) {
            sum += sample;
        }
        double mean = sum / samples.length;
        assertEquals(0.5, mean, 1e-5);
    }
}