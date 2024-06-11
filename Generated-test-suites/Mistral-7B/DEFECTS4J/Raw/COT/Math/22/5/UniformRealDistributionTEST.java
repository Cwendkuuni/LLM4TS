package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for UniformRealDistribution
 */
public class UniformRealDistributionTest {

    private UniformRealDistribution dist;
    private RandomGenerator rng;

    @Before
    public void setUp() {
        rng = new Well19937c();
        dist = new UniformRealDistribution(rng, 0, 1);
    }

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution defaultDist = new UniformRealDistribution();
        assertEquals(0, defaultDist.getSupportLowerBound(), 0);
        assertEquals(1, defaultDist.getSupportUpperBound(), 0);
    }

    @Test
    public void testConstructorWithLowerAndUpperBound() {
        double lower = 2.5;
        double upper = 4.5;
        assertThrows(NumberIsTooLargeException.class, () -> new UniformRealDistribution(rng, lower, upper));
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0, 1);
        assertEquals(0, dist.getSupportLowerBound(), 0);
        assertEquals(1, dist.getSupportUpperBound(), 0);
    }

    @Test
    public void testConstructorWithLowerBoundAndInverseCumulativeProbabilityAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution(rng, 1, 1e-6);
        assertEquals(1, dist.getSupportLowerBound(), 0);
        assertEquals(1, dist.getSolverAbsoluteAccuracy(), 1e-6);
    }

    @Test
    public void testConstructorWithRandomGeneratorLowerBoundUpperBoundAndInverseCumulativeProbabilityAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0, 1, 1e-6);
        assertEquals(0, dist.getSupportLowerBound(), 0);
        assertEquals(1, dist.getSupportUpperBound(), 0);
        assertEquals(1e-6, dist.getSolverAbsoluteAccuracy(), 1e-6);
    }

    @Test
    public void testDensity() {
        double testValue = 0.5;
        double expectedValue = 1 / (dist.getSupportUpperBound() - dist.getSupportLowerBound());
        assertEquals(expectedValue, dist.density(testValue), 1e-6);
        double testValueBelowLowerBound = 0.2;
        assertEquals(0, dist.density(testValueBelowLowerBound), 1e-6);
        double testValueAboveUpperBound = 1.5;
        assertEquals(0, dist.density(testValueAboveUpperBound), 1e-6);
    }

    @Test
    public void testCumulativeProbability() {
        double testValue = 0.5;
        double expectedValue = 0.5;
        assertEquals(expectedValue, dist.cumulativeProbability(testValue), 1e-6);
        double testValueBelowLowerBound = 0.2;
        assertEquals(0, dist.cumulativeProbability(testValueBelowLowerBound), 1e-6);
        double testValueAboveUpperBound = 1.5;
        assertEquals(1, dist.cumulativeProbability(testValueAboveUpperBound), 1e-6);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        assertEquals(1e-9, dist.getSolverAbsoluteAccuracy(), 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        double expectedValue = 0.5 * (dist.getSupportLowerBound() + dist.getSupportUpperBound());
        assertEquals(expectedValue, dist.getNumericalMean(), 1e-6);
    }

    @Test
    public void testGetNumericalVariance() {
        double expectedValue = Math.pow(dist.getSupportUpperBound() - dist.getSupportLowerBound(), 2) / 12;
        assertEquals(expectedValue, dist.getNumericalVariance(), 1e-6);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, dist.getSupportLowerBound(), 0);
    }

    @Test
    public void testGetSupportUpperBound() {
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

    @Test
    public void testSample() {
        double testSample = dist.sample();
        assertTrue(testSample >= dist.getSupportLowerBound() && testSample < dist.getSupportUpperBound());
    }
}