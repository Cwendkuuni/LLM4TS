package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;

public class UniformRealDistributionTest {
    private UniformRealDistribution dist;

    @Before
    public void setUp() throws NumberIsTooLargeException {
        Well19937c rng = new Well19937c();
        dist = new UniformRealDistribution(rng, 0, 1);
    }

    @Test
    public void testConstructors() {
        UniformRealDistribution defaultDist = new UniformRealDistribution();
        assertEquals(0, defaultDist.getSupportLowerBound(), 0);
        assertEquals(1, defaultDist.getSupportUpperBound(), 0);

        UniformRealDistribution customDist = new UniformRealDistribution(0.5, 1.5);
        assertEquals(0.5, customDist.getSupportLowerBound(), 0);
        assertEquals(1.5, customDist.getSupportUpperBound(), 0);

        try {
            new UniformRealDistribution(-1, 0);
            fail();
        } catch (NumberIsTooLargeException e) {
            // expected
        }

        try {
            new UniformRealDistribution(1, -1);
            fail();
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testDensity() {
        double[] xValues = new double[] { 0, 0.1, 0.5, 0.9, 1 };
        double[] expectedDensities = new double[] { 1.0 / 1.0, 1.0 / 1.2, 1.0 / 0.5, 1.0 / 1.2, 1.0 / 1.0 };
        for (int i = 0; i < xValues.length; i++) {
            assertEquals(expectedDensities[i], dist.density(xValues[i]), 0.0001);
        }
    }

    @Test
    public void testCumulativeProbability() {
        double[] xValues = new double[] { 0, 0.1, 0.5, 0.9, 1 };
        double[] expectedCumProbs = new double[] { 0, 0.01111111111111111, 0.5, 0.9888888888888888, 1 };
        for (int i = 0; i < xValues.length; i++) {
            assertEquals(expectedCumProbs[i], dist.cumulativeProbability(xValues[i]), 0.0001);
        }
    }

    @Test
    public void testSample() {
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getSupportLowerBound());
            assertTrue(sample < dist.getSupportUpperBound());
        }
    }

    @Test
    public void testGetMean() {
        assertEquals(0.5, dist.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetVariance() {
        assertEquals(0.0625, dist.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportBounds() {
        assertEquals(0, dist.getSupportLowerBound(), 0);
        assertEquals(1, dist.getSupportUpperBound(), 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(dist.isSupportConnected());
    }
}