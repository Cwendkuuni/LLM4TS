
package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class UniformRealDistributionTest {

    private final double lower = 2;
    private final double upper = 7;
    private final double accuracy = 1e-9;

    @Test
    public void testConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0, dist.getSupportLowerBound(), accuracy);
        assertEquals(1, dist.getSupportUpperBound(), accuracy);
        assertFalse(dist.isSupportUpperBoundInclusive());

        dist = new UniformRealDistribution(lower, upper);
        assertEquals(lower, dist.getSupportLowerBound(), accuracy);
        assertEquals(upper, dist.getSupportUpperBound(), accuracy);
        assertFalse(dist.isSupportUpperBoundInclusive());

        dist = new UniformRealDistribution(lower, upper, accuracy);
        assertEquals(lower, dist.getSupportLowerBound(), accuracy);
        assertEquals(upper, dist.getSupportUpperBound(), accuracy);
        assertFalse(dist.isSupportUpperBoundInclusive());

        RandomGenerator rng = new Well19937c();
        dist = new UniformRealDistribution(rng, lower, upper, accuracy);
        assertEquals(lower, dist.getSupportLowerBound(), accuracy);
        assertEquals(upper, dist.getSupportUpperBound(), accuracy);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorThrowsException() {
        UniformRealDistribution dist = new UniformRealDistribution(upper, lower);
        fail("Expected NumberIsTooLargeException");
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
        assertEquals(0, dist.density(1), accuracy);
        assertEquals(1 / (upper - lower), dist.density(3), accuracy);
        assertEquals(1 / (upper - lower), dist.density(5), accuracy);
        assertEquals(0, dist.density(10), accuracy);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
        assertEquals(0, dist.cumulativeProbability(1), accuracy);
        assertEquals((3 - lower) / (upper - lower), dist.cumulativeProbability(3), accuracy);
        assertEquals((5 - lower) / (upper - lower), dist.cumulativeProbability(5), accuracy);
        assertEquals(1, dist.cumulativeProbability(10), accuracy);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
        double mean = (lower + upper) / 2;
        assertEquals(mean, dist.getNumericalMean(), accuracy);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
        double variance = Math.pow(upper - lower, 2) / 12;
        assertEquals(variance, dist.getNumericalVariance(), accuracy);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
        assertEquals(lower, dist.getSupportLowerBound(), accuracy);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
        assertEquals(upper, dist.getSupportUpperBound(), accuracy);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
        double sample = dist.sample();
        assertTrue(sample >= lower && sample < upper);
    }
}
