package org.apache.commons.math3.distribution;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;

    @Before
    public void setUp() {
        distribution = new UniformRealDistribution(0, 1);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, distribution.getSupportLowerBound(), 0);
        assertEquals(1, distribution.getSupportUpperBound(), 0);
        assertFalse(distribution.isSupportUpperBoundInclusive());
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructor() {
        new UniformRealDistribution(1, 0);
    }

    @Test
    public void testDensity() {
        assertEquals(1, distribution.density(0.5), 0);
        assertEquals(0, distribution.density(-1), 0);
        assertEquals(0, distribution.density(2), 0);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0, distribution.cumulativeProbability(-1), 0);
        assertEquals(1, distribution.cumulativeProbability(2), 0);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(0.5, distribution.getNumericalMean(), 0);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(1.0 / 12, distribution.getNumericalVariance(), 0);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 0);
    }

    @Test
    public void testSample() {
        double sample = distribution.sample();
        assertTrue(sample >= 0 && sample < 1);
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }

}