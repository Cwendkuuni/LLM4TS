package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;

/**
 * Test cases for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {
    @Test
    public void testConstructorZeroToOne() {
        UniformRealDistribution dist = new UniformRealDistribution();

        assertEquals(0, dist.getSupportLowerBound(), 0);
        assertEquals(1, dist.getSupportUpperBound(), 0);
    }

    @Test
    public void testConstructorWithBounds() {
        double lower = 1.5;
        double upper = 5.0;

        UniformRealDistribution dist = null;
        try {
            dist = new UniformRealDistribution(lower, upper);
        } catch (NumberIsTooLargeException e) {
            // lower >= upper
        }

        assertEquals(lower, dist.getSupportLowerBound(), 0);
        assertEquals(upper, dist.getSupportUpperBound(), 0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithLowerGeqUpper() {
        new UniformRealDistribution(3.0, 2.0);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);

        double x = 0.1;
        double density = dist.density(x);

        assertEquals(1.0 / (1.0 - 0.0), density, 1e-14);
    }

    @Test
    public void testCumulativeProbabilityBelowLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);

        double x = -1.0;
        double cumProb = dist.cumulativeProbability(x);

        assertEquals(0.0, cumProb, 1e-14);
    }

    @Test
    public void testCumulativeProbabilityAboveUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);

        double x = 2.0;
        double cumProb = dist.cumulativeProbability(x);

        assertEquals(1.0, cumProb, 1e-14);
    }

    @Test
    public void testCumulativeProbabilityInsideRange() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);

        double x = 0.5;
        double cumProb = dist.cumulativeProbability(x);

        assertEquals(0.5, cumProb, 1e-14);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);

        double solverAbsoluteAccuracy = dist.getSolverAbsoluteAccuracy();

        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, solverAbsoluteAccuracy, 1e-15);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);

        double numericalMean = dist.getNumericalMean();

        assertEquals(0.5, numericalMean, 1e-15);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);

        double numericalVariance = dist.getNumericalVariance();

        assertEquals(0.0002777777777777778, numericalVariance, 1e-15);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);

        double supportLowerBound = dist.getSupportLowerBound();

        assertEquals(0.0, supportLowerBound, 1e-15);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);

        double supportUpperBound = dist.getSupportUpperBound();

        assertEquals(1.0, supportUpperBound, 1e-15);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);

        boolean isSupportLowerBoundInclusive = dist.isSupportLowerBoundInclusive();

        assertTrue(isSupportLowerBoundInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);

        boolean isSupportUpperBoundInclusive = dist.isSupportUpperBoundInclusive();

        assertFalse(isSupportUpperBoundInclusive);
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);

        boolean isSupportConnected = dist.isSupportConnected();

        assertTrue(isSupportConnected);
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);

        double sample = dist.sample();

        assertTrue(sample >= 0.0 && sample < 1.0);
    }

    @Test
    public void testSampleManyTimes() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        int n = 10000;

        for (int i = 0; i < n; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 0.0 && sample < 1.0);
        }
    }

    @Test
    public void testSampleWithCustomRng() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0.0, 1.0);

        double sample = dist.sample();

        assertTrue(sample >= 0.0 && sample < 1.0);
    }
}