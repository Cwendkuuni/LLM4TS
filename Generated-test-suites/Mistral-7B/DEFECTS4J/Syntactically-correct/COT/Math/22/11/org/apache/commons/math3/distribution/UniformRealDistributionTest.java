package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Test cases for {@link UniformRealDistribution} class.
 */
public class UniformRealDistributionTest {

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
        assertEquals(0.5, dist.getNumericalMean(), 0.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.0);
    }

    @Test
    public void testConstructorWithBounds() {
        UniformRealDistribution dist;
        dist = new UniformRealDistribution(0, 1);
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
        assertEquals(0.5, dist.getNumericalMean(), 0.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.0);

        dist = new UniformRealDistribution(2, 4);
        assertEquals(2.0, dist.getLowerBound(), 0.0);
        assertEquals(4.0, dist.getUpperBound(), 0.0);
        assertEquals(3.0, dist.getNumericalMean(), 0.0);
        assertEquals(0.5, dist.getNumericalVariance(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        new UniformRealDistribution(1, 1); // lower >= upper
    }

    @Test
    public void testSampleFromUniformDistribution() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getLowerBound());
            assertTrue(sample < dist.getUpperBound());
        }
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = dist.getLowerBound(); x <= dist.getUpperBound(); x++) {
            double density = dist.density(x);
            assertEquals(1.0 / (dist.getUpperBound() - dist.getLowerBound()),
                         density, 1e-7);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = dist.getLowerBound(); x <= dist.getUpperBound(); x++) {
            double cdf = dist.cumulativeProbability(x);
            assertEquals(x - dist.getLowerBound() / (dist.getUpperBound() - dist.getLowerBound()),
                         cdf, 1e-7);
        }
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(dist.getLowerBound(), dist.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(dist.getUpperBound(), dist.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testSampleWithCustomRandomGenerator() throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(new Well19937c(), 1, 3);
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getLowerBound());
            assertTrue(sample < dist.getUpperBound());
        }
    }
}