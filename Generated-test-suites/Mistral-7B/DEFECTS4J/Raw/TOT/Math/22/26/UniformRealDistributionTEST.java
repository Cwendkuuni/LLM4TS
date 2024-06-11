package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistribution() {
        // Test default constructor
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getNumericalMean(), 1e-9);
        assertEquals(0.0, dist.getNumericalVariance(), 1e-9);
        assertEquals(0.0, dist.sample(), 1e-9);

        // Test constructor with valid bounds
        dist = new UniformRealDistribution(0.1, 0.2);
        assertEquals(0.15, dist.getNumericalMean(), 1e-9);
        assertEquals(0.00136, dist.getNumericalVariance(), 1e-9);
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 0.1 && sample < 0.2);
        }

        // Test constructor with invalid bounds
        try {
            new UniformRealDistribution(0.2, 0.1);
            // this line should not be reached
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        // test valid points
        for (double x = 0.1; x <= 0.3; x += 0.01) {
            double density = dist.density(x);
            assertEquals(0.01 / (0.3 - 0.1), density, 1e-6);
        }
        // test lower and upper bounds
        double densityLower = dist.density(0.1);
        assertEquals(1 / (0.3 - 0.1), densityLower, 1e-6);
        double densityUpper = dist.density(0.3);
        assertEquals(1 / (0.3 - 0.1), densityUpper, 1e-6);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        // test valid points
        for (double x = 0.1; x <= 0.3; x += 0.01) {
            double cdf = dist.cumulativeProbability(x);
            assertEquals(x - 0.1, cdf, 1e-6);
        }
        // test lower and upper bounds
        double cdfLower = dist.cumulativeProbability(0.1);
        assertEquals(0.0, cdfLower, 1e-6);
        double cdfUpper = dist.cumulativeProbability(0.3);
        assertEquals(1.0, cdfUpper, 1e-6);
    }

    @Test
    public void testSolverAbsoluteAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        double inverseCumProbability = 0.2;
        double expected = 0.1 + 0.1 * Math.pow(2.0, Math.log(inverseCumProbability) / Math.log(2.0));
        double solverResult = dist.inverseCumulativeProbability(inverseCumProbability);
        assertEquals(expected, solverResult, 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        assertEquals(0.15, dist.getNumericalMean(), 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        assertEquals(0.00136, dist.getNumericalVariance(), 1e-9);
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 0.1 && sample < 0.3);
        }
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        assertTrue(dist.isSupportConnected());
    }
}