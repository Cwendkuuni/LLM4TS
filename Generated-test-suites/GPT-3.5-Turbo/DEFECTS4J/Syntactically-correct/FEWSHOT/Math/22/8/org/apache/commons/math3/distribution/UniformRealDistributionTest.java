package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

import static org.junit.Assert.*;

public class UniformRealDistributionTest {
    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(1.0, dist.density(0.5), 0.0);
        assertEquals(0.0, dist.density(1.5), 0.0);
        assertEquals(0.0, dist.density(-0.5), 0.0);

        UniformRealDistribution dist2 = new UniformRealDistribution(2, 4);
        assertEquals(0.5, dist2.density(3), 0.0);
        assertEquals(0.0, dist2.density(1), 0.0);
        assertEquals(0.0, dist2.density(5), 0.0);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(0.5, dist.cumulativeProbability(0.5), 0.0);
        assertEquals(1.0, dist.cumulativeProbability(1), 0.0);
        assertEquals(0.0, dist.cumulativeProbability(-0.5), 0.0);
        assertEquals(0.0, dist.cumulativeProbability(1.5), 0.0);

        UniformRealDistribution dist2 = new UniformRealDistribution(2, 4);
        assertEquals(0.0, dist2.cumulativeProbability(1), 0.0);
        assertEquals(0.5, dist2.cumulativeProbability(3), 0.0);
        assertEquals(1.0, dist2.cumulativeProbability(4), 0.0);
        assertEquals(0.0, dist2.cumulativeProbability(5), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(0.5, dist.getNumericalMean(), 0.0);

        UniformRealDistribution dist2 = new UniformRealDistribution(2, 4);
        assertEquals(3.0, dist2.getNumericalMean(), 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(1.0/12, dist.getNumericalVariance(), 0.0);

        UniformRealDistribution dist2 = new UniformRealDistribution(2, 4);
        assertEquals(1.0/3, dist2.getNumericalVariance(), 0.0);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);

        UniformRealDistribution dist2 = new UniformRealDistribution(2, 4);
        assertEquals(2.0, dist2.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);

        UniformRealDistribution dist2 = new UniformRealDistribution(2, 4);
        assertEquals(4.0, dist2.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertTrue(dist.isSupportLowerBoundInclusive());

        UniformRealDistribution dist2 = new UniformRealDistribution(2, 4);
        assertTrue(dist2.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertFalse(dist.isSupportUpperBoundInclusive());

        UniformRealDistribution dist2 = new UniformRealDistribution(2, 4);
        assertFalse(dist2.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertTrue(dist.isSupportConnected());

        UniformRealDistribution dist2 = new UniformRealDistribution(2, 4);
        assertTrue(dist2.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 0.0 && sample <= 1.0);
        }

        UniformRealDistribution dist2 = new UniformRealDistribution(2, 4);
        for (int i = 0; i < 1000; i++) {
            double sample = dist2.sample();
            assertTrue(sample >= 2.0 && sample <= 4.0);
        }
    }
}