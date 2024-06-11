package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.MathException;
import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertNotNull(dist);
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testConstructorWithLowerAndUpperBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertNotNull(dist);
        assertEquals(0.1, dist.getSupportLowerBound(), 0.0);
        assertEquals(0.4, dist.getSupportUpperBound(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        new UniformRealDistribution(2.0, 1.0);
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (int i = 0; i < 10; i++) {
            double value = dist.sample();
            assertTrue(value >= dist.getSupportLowerBound());
            assertTrue(value < dist.getSupportUpperBound());
        }
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        for (double x = 0.1; x <= 0.4; x += 0.01) {
            double density = dist.density(x);
            assertEquals(1.0 / (0.4 - 0.1), density, 1e-6);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        for (double x = 0.1; x <= 0.4; x += 0.01) {
            double probability = dist.cumulativeProbability(x);
            assertEquals(x - 0.1, probability, 1e-6);
        }
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.1, dist.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.4, dist.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testGetMean() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.5, dist.getNumericalMean(), 1e-6);
    }

    @Test
    public void testGetVariance() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.00022222222222222222, dist.getNumericalVariance(), 1e-8);
    }

    @Test(expected = MathException.class)
    public void testGetMeanWithInvalidDistribution() {
        new UniformRealDistribution().getNumericalMean();
    }

    @Test(expected = MathException.class)
    public void testGetVarianceWithInvalidDistribution() {
        new UniformRealDistribution().getNumericalVariance();
    }

    @Test
    public void testSampleWithCustomRandomGenerator() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0.1, 0.4);
        for (int i = 0; i < 10; i++) {
            double value = dist.sample();
            assertTrue(value >= dist.getSupportLowerBound());
            assertTrue(value < dist.getSupportUpperBound());
        }
    }

    @Test
    public void testSampleWithInvalidGenerator() {
        Object obj = new Object();
        UniformRealDistribution dist = new UniformRealDistribution(obj, 0.1, 0.4);
        assertNull(dist.sample());
    }
}