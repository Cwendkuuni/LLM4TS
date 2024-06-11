package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

public class UniformRealDistributionTest {

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.01);
        assertEquals(1.0, dist.getUpperBound(), 0.01);
    }

    @Test
    public void testConstructorWithArguments() {
        double lower = 2.5;
        double upper = 4.5;
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
        assertEquals(lower, dist.getLowerBound(), 0.01);
        assertEquals(upper, dist.getUpperBound(), 0.01);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidArguments() {
        double lower = 4.5;
        double upper = 2.5;
        new UniformRealDistribution(lower, upper);
    }

    @Test
    public void testConstructorWithRandomGenerator() {
        RandomGenerator rng = new Well19937c();
        double lower = 2.5;
        double upper = 4.5;
        UniformRealDistribution dist = new UniformRealDistribution(rng, lower, upper);
        assertEquals(lower, dist.getLowerBound(), 0.01);
        assertEquals(upper, dist.getUpperBound(), 0.01);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);

        double x = 0.3;
        double expected = 1.0 / (1.0 - 0.0);
        assertEquals(expected, dist.density(x), 0.01);

        x = 0.7;
        expected = 1.0 / (1.0 - 0.0);
        assertEquals(expected, dist.density(x), 0.01);

        x = 0.5;
        expected = 1.0 / (1.0 - 0.0);
        assertEquals(expected * 2.0, dist.density(x), 0.02);

        x = 2.0;
        assertEquals(0.0, dist.density(x), 0.01);

        x = 3.0;
        assertEquals(0.0, dist.density(x), 0.01);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);

        double x = 0.1;
        double expected = 0.1;
        assertEquals(expected, dist.cumulativeProbability(x), 0.01);

        x = 0.6;
        expected = 0.6;
        assertEquals(expected, dist.cumulativeProbability(x), 0.01);

        x = 0.5;
        expected = 0.5;
        assertEquals(expected, dist.cumulativeProbability(x), 0.01);

        x = 1.0;
        expected = 1.0;
        assertEquals(expected, dist.cumulativeProbability(x), 0.01);

        x = 2.0;
        assertEquals(0.0, dist.cumulativeProbability(x), 0.01);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        double expected = UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY;
        assertEquals(expected, dist.getSolverAbsoluteAccuracy(), 0.01);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        double expected = 0.5;
        assertEquals(expected, dist.getNumericalMean(), 0.01);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        double expected = 0.000888889;
        assertEquals(expected, dist.getNumericalVariance(), 0.01);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        double expected = 0.0;
        assertEquals(expected, dist.getSupportLowerBound(), 0.01);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        double expected = 1.0;
        assertEquals(expected, dist.getSupportUpperBound(), 0.01);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        RandomGenerator rng = new Well19937c();

        double expectedMin = 0.0;
        double expectedMax = 1.0;
        double sample = dist.sample(rng);

        assertTrue(sample >= expectedMin);
        assertTrue(sample < expectedMax);
    }
}