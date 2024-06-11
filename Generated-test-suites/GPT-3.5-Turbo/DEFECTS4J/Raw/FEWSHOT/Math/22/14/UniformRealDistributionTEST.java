package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;

public class UniformRealDistributionTest {
    private UniformRealDistribution distribution;

    @Before
    public void setUp() {
        distribution = new UniformRealDistribution();
    }

    @Test
    public void testConstructor() throws NumberIsTooLargeException {
        assertEquals(0, distribution.getSupportLowerBound(), 0);
        assertEquals(1, distribution.getSupportUpperBound(), 0);
    }

    @Test
    public void testDensity() {
        assertEquals(0, distribution.density(-1), 0);
        assertEquals(0, distribution.density(2), 0);
        assertEquals(1, distribution.density(0.5), 0);
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
        assertEquals(1/12, distribution.getNumericalVariance(), 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, distribution.getSupportLowerBound(), 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(1, distribution.getSupportUpperBound(), 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        RandomGenerator rng = new Well19937c();
        double sample = distribution.sample();
        assertTrue(sample >= 0 && sample < 1);
    }
}