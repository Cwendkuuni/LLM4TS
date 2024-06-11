package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.distribution.UniformRealDistribution;

import java.util.Random;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0, distribution.getSupportLowerBound(), 0.001);
        assertEquals(1, distribution.getSupportUpperBound(), 0.001);
        assertEquals(0.5, distribution.getNumericalMean(), 0.001);
        assertEquals(0.25, distribution.getNumericalVariance(), 0.001);
        assertFalse(distribution.isSupportUpperBoundInclusive());
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testUniformRealDistributionCustomConstructor() {
        double lower = 1.0;
        double upper = 3.0;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        assertEquals(lower, distribution.getSupportLowerBound(), 0.001);
        assertEquals(upper, distribution.getSupportUpperBound(), 0.001);
        assertEquals(2.0, distribution.getNumericalMean(), 0.001);
        assertEquals(0.225, distribution.getNumericalVariance(), 0.001);
        assertFalse(distribution.isSupportUpperBoundInclusive());
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testUniformRealDistributionCustomConstructorWithNegativeLowerBound() {
        double lower = -1.0;
        double upper = 1.0;
        try {
            UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
            fail("NumberIsTooLargeException expected");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testUniformRealDistributionCustomConstructorWithNegativeUpperBound() {
        double lower = 1.0;
        double upper = -1.0;
        try {
            UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
            fail("NumberIsTooLargeException expected");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testUniformRealDistributionCustomConstructorWithZeroLowerBound() {
        double lower = 0.0;
        double upper = 1.0;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        assertEquals(lower, distribution.getSupportLowerBound(), 0.001);
        assertEquals(upper, distribution.getSupportUpperBound(), 0.001);
        assertEquals(0.5, distribution.getNumericalMean(), 0.001);
        assertEquals(0.25, distribution.getNumericalVariance(), 0.001);
        assertTrue(distribution.isSupportUpperBoundInclusive());
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testUniformRealDistributionCustomConstructorWithZeroUpperBound() {
        double lower = 1.0;
        double upper = 0.0;
        try {
            UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
            fail("NumberIsTooLargeException expected");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testUniformRealDistributionCustomConstructorWithSameLowerAndUpperBound() {
        double lower = 1.0;
        double upper = 1.0;
        try {
            UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
            fail("NumberIsTooLargeException expected");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testUniformRealDistributionDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        for (double x = 0; x <= 1; x += 0.01) {
            double density = distribution.density(x);
            assertEquals(1.0 / (1.0 - 0.0), density, 0.01);
        }
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        for (double x = 0; x <= 1; x += 0.01) {
            double cumProb = distribution.cumulativeProbability(x);
            assertEquals(x, cumProb, 0.01);
        }
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= 0);
            assertTrue(sample < 1);
        }
    }

    @Test
    public void testUniformRealDistributionSampleUsingDefaultRandomGenerator() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= 0);
            assertTrue(sample < 1);
        }
    }

    @Test
    public void testUniformRealDistributionSampleUsingCustomRandomGenerator() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution distribution = new UniformRealDistribution(rng, 0, 1);
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= 0);
            assertTrue(sample < 1);
        }
    }

    @Test
    public void testUniformRealDistributionSolverAbsoluteAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 0.001);
    }

    @Test
    public void testUniformRealDistributionGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.getNumericalMean(), 0.001);
    }

    @Test
    public void testUniformRealDistributionGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.25, distribution.getNumericalVariance(), 0.001);
    }

    @Test
    public void testUniformRealDistributionGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0, distribution.getSupportLowerBound(), 0.001);
    }

    @Test
    public void testUniformRealDistributionGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1, distribution.getSupportUpperBound(), 0.001);
    }

    @Test
    public void testUniformRealDistributionIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertTrue(distribution.isSupportConnected());
    }
}