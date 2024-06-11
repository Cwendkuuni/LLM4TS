package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;

    @Before
    public void setUp() {
        try {
            distribution = new UniformRealDistribution(new Well19937c(), 0, 1);
        } catch (NumberIsTooLargeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstruction() {
        assertEquals(0, distribution.getSupportLowerBound(), 0);
        assertEquals(1, distribution.getSupportUpperBound(), 0);
        assertEquals(1, distribution.getNumericalMean(), 0.5);
        assertEquals(1, distribution.getNumericalVariance(), 0.25);
        assertEquals(1, distribution.isSupportLowerBoundInclusive(), true);
        assertEquals(1, distribution.isSupportUpperBoundInclusive(), false);
        assertEquals(1, distribution.isSupportConnected(), true);
    }

    @Test
    public void testDensity() {
        for (double x = 0; x <= 1; x += 0.01) {
            double density = distribution.density(x);
            double expectedDensity = 1 / (1 - 0);
            double tolerance = 0.001;
            assertEquals(expectedDensity, density, tolerance);
        }
    }

    @Test
    public void testCumulativeProbability() {
        for (double x = 0; x <= 1; x += 0.01) {
            double cumProb = distribution.cumulativeProbability(x);
            double expectedCumProb = x;
            double tolerance = 0.001;
            assertEquals(expectedCumProb, cumProb, tolerance);
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNegativeLowerBound() {
        new UniformRealDistribution(new Well19937c(), -1, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUpperBoundLessThanLowerBound() {
        new UniformRealDistribution(new Well19937c(), 2, 1);
    }
}