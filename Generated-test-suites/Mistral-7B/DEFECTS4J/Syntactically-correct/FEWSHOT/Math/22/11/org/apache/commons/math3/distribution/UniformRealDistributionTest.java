package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

public class UniformRealDistributionTest {

    /**
     * Test constructor with default bounds.
     */
    @Test
    public void testUniformRealDistributionDefault() {
        UniformRealDistribution urd = new UniformRealDistribution();
        assertEquals(0.0, urd.getLowerBound(), 0.0);
        assertEquals(1.0, urd.getUpperBound(), 0.0);
        assertEquals(0.5, urd.getNumericalMean(), 0.0);
        assertEquals(0.25, urd.getNumericalVariance(), 0.0);
        assertEquals(1.0 / (1.0 - 0.0), urd.density(0.5), 0.0);
    }

    /**
     * Test constructor with custom bounds.
     */
    @Test
    public void testUniformRealDistributionCustom() {
        double lower = 2.0;
        double upper = 5.0;
        UniformRealDistribution urd = null;
        try {
            urd = new UniformRealDistribution(lower, upper);
        } catch (NumberIsTooLargeException e) {
            // Expected since lower >= upper
        }
        assertNull(urd);

        lower = 1.0;
        upper = 4.0;
        urd = new UniformRealDistribution(lower, upper);
        assertEquals(lower, urd.getLowerBound(), 0.0);
        assertEquals(upper, urd.getUpperBound(), 0.0);
        assertEquals((lower + upper) / 2.0, urd.getNumericalMean(), 0.0);
        assertEquals((upper - lower) * (upper - lower) / 12.0, urd.getNumericalVariance(), 0.0);
        assertEquals(1.0 / (upper - lower), urd.density(2.5), 0.0);
    }

    /**
     * Test constructor with custom bounds and inverse cumulative probability accuracy.
     */
    @Test
    public void testUniformRealDistributionCustomAccuracy() {
        double lower = 0.0;
        double upper = 1.0;
        double inverseCumAccuracy = 1e-6;
        UniformRealDistribution urd = new UniformRealDistribution(lower, upper, inverseCumAccuracy);
        assertEquals(lower, urd.getLowerBound(), 0.0);
        assertEquals(upper, urd.getUpperBound(), 0.0);
        assertEquals(0.5, urd.getNumericalMean(), 0.0);
        assertEquals(0.25, urd.getNumericalVariance(), 0.0);
        assertEquals(1.0 / (upper - lower), urd.density(0.5), 0.0);
        assertEquals(1.0 / inverseCumAccuracy, urd.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test constructor with custom bounds, inverse cumulative probability accuracy, and random number generator.
     */
    @Test
    public void testUniformRealDistributionCustomAccuracyRng() {
        double lower = 0.0;
        double upper = 1.0;
        double inverseCumAccuracy = 1e-6;
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution urd = new UniformRealDistribution(rng, lower, upper, inverseCumAccuracy);
        assertEquals(lower, urd.getLowerBound(), 0.0);
        assertEquals(upper, urd.getUpperBound(), 0.0);
        assertEquals(0.5, urd.getNumericalMean(), 0.0);
        assertEquals(0.25, urd.getNumericalVariance(), 0.0);
        assertEquals(1.0 / (upper - lower), urd.density(0.5), 0.0);
        assertEquals(1.0 / inverseCumAccuracy, urd.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        UniformRealDistribution urd = new UniformRealDistribution();
        for (int i = 0; i < 100_000; i++) {
            double sample = urd.sample();
            assertTrue(sample >= urd.getLowerBound());
            assertTrue(sample <= urd.getUpperBound());
        }
    }

    /**
     * Test sample method with custom bounds.
     */
    @Test
    public void testSampleCustom() {
        double lower = 2.0;
        double upper = 5.0;
        UniformRealDistribution urd = new UniformRealDistribution(lower, upper);
        for (int i = 0; i < 100_000; i++) {
            double sample = urd.sample();
            assertTrue(sample >= urd.getLowerBound());
            assertTrue(sample <= urd.getUpperBound());
        }
    }

    /**
     * Test inverse cumulative probability method.
     */
    @Test
    public void testInverseCumulativeProbability() {
        UniformRealDistribution urd = new UniformRealDistribution();
        for (double x = urd.getLowerBound(); x < urd.getUpperBound(); x += 0.01) {
            double prob = urd.cumulativeProbability(x);
            double invProb = urd.inverseCumulativeProbability(prob);
            assertEquals(x, invProb, 0.001);
        }
    }

    /**
     * Test inverse cumulative probability method with custom bounds and inverse cumulative probability accuracy.
     */
    @Test
    public void testInverseCumulativeProbabilityCustom() {
        double lower = 0.0;
        double upper = 1.0;
        double inverseCumAccuracy = 1e-6;
        UniformRealDistribution urd = new UniformRealDistribution(lower, upper, inverseCumAccuracy);
        for (double x = lower; x < upper; x += 0.01) {
            double prob = urd.cumulativeProbability(x);
            double invProb = urd.inverseCumulativeProbability(prob);
            assertTrue(Math.abs(x - invProb) <= inverseCumAccuracy);
        }
    }

    /**
     * Test getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution urd = new UniformRealDistribution();
        assertEquals(0.0, urd.getSupportLowerBound(), 0.0);

        urd = new UniformRealDistribution(1.0, 2.0);
        assertEquals(1.0, urd.getSupportLowerBound(), 0.0);
    }

    /**
     * Test getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution urd = new UniformRealDistribution();
        assertEquals(1.0, urd.getSupportUpperBound(), 0.0);

        urd = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.0, urd.getSupportUpperBound(), 0.0);
    }

    /**
     * Test isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution urd = new UniformRealDistribution();
        assertTrue(urd.isSupportLowerBoundInclusive());

        urd = new UniformRealDistribution(1.0, 2.0);
        assertTrue(urd.isSupportLowerBoundInclusive());
    }

    /**
     * Test isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution urd = new UniformRealDistribution();
        assertFalse(urd.isSupportUpperBoundInclusive());

        urd = new UniformRealDistribution(0.0, 1.0);
        assertFalse(urd.isSupportUpperBoundInclusive());
    }

    /**
     * Test isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution urd = new UniformRealDistribution();
        assertTrue(urd.isSupportConnected());
    }
}