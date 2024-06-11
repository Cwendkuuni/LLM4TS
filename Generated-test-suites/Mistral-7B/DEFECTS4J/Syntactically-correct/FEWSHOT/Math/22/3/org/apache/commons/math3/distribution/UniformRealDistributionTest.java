package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        new UniformRealDistribution(2.0, 1.0);
    }

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
    }

    @Test
    public void testConstructorWithCustomBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 5.0);
        assertEquals(1.0, dist.getLowerBound(), 0.0);
        assertEquals(5.0, dist.getUpperBound(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidInverseCumulativeAccuracy() {
        new UniformRealDistribution(1.0, 5.0, -1.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithLowerBoundGreaterThanUpperBound() {
        new UniformRealDistribution(5.0, 1.0);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 5.0);
        assertEquals(0.2, dist.density(2.5), 0.001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 5.0);
        assertEquals(0.2, dist.cumulativeProbability(2.5), 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 5.0);
        assertEquals(3.0, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 5.0);
        assertEquals(1.6666666666666666, dist.getNumericalVariance(), 0.001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 5.0);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 5.0);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 5.0);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 5.0);
        Well19937c rng = new Well19937c();
        assertEquals(3.137368155382235, dist.sample(rng), 0.001);
    }
}