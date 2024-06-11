
package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        new UniformRealDistribution(2, 1);
    }

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution urd = new UniformRealDistribution();
        assertEquals(0, urd.getSupportLowerBound(), 0);
        assertEquals(1, urd.getSupportUpperBound(), 0);
        assertTrue(urd.isSupportLowerBoundInclusive());
        assertTrue(!urd.isSupportUpperBoundInclusive());
        assertTrue(urd.isSupportConnected());
    }

    @Test
    public void testConstructorWithBounds() {
        UniformRealDistribution urd = new UniformRealDistribution(1, 5);
        assertEquals(1, urd.getSupportLowerBound(), 0);
        assertEquals(5, urd.getSupportUpperBound(), 0);
        assertTrue(urd.isSupportLowerBoundInclusive());
        assertTrue(!urd.isSupportUpperBoundInclusive());
        assertTrue(urd.isSupportConnected());
    }

    @Test
    public void testConstructorWithBoundsAndAccuracy() {
        UniformRealDistribution urd = new UniformRealDistribution(1, 5, 0.001);
        assertEquals(1, urd.getSupportLowerBound(), 0);
        assertEquals(5, urd.getSupportUpperBound(), 0);
        assertTrue(urd.isSupportLowerBoundInclusive());
        assertTrue(!urd.isSupportUpperBoundInclusive());
        assertTrue(urd.isSupportConnected());
    }

    @Test
    public void testDensity() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 10);
        assertEquals(0.1, urd.density(1), 0);
        assertEquals(0.1, urd.density(5), 0);
        assertEquals(0.1, urd.density(10), 0);
        assertEquals(0, urd.density(-1), 0);
        assertEquals(0, urd.density(11), 0);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 10);
        assertEquals(0, urd.cumulativeProbability(-1), 0);
        assertEquals(0, urd.cumulativeProbability(0), 0);
        assertEquals(0.5, urd.cumulativeProbability(5), 0);
        assertEquals(1, urd.cumulativeProbability(10), 0);
        assertEquals(1, urd.cumulativeProbability(11), 0);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 10);
        assertEquals(5, urd.getNumericalMean(), 0);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 10);
        assertEquals(8.3333, urd.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testSample() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 10);
        for (int i = 0; i < 1000; i++) {
            double sample = urd.sample();
            assertTrue(sample >= 0 && sample <= 10);
        }
    }
}
