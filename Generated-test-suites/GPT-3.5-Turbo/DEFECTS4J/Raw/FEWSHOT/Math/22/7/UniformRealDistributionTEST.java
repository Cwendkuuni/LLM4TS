package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;

public class UniformRealDistributionTest {

    private static final double EPSILON = 1e-9;
    private UniformRealDistribution distribution;

    @Before
    public void setUp() {
        distribution = new UniformRealDistribution();
    }

    @Test
    public void testConstructorWithoutParameters() {
        assertEquals(0, distribution.getSupportLowerBound(), EPSILON);
        assertEquals(1, distribution.getSupportUpperBound(), EPSILON);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testConstructorWithParameters() {
        double lower = -1.0;
        double upper = 1.0;
        distribution = new UniformRealDistribution(lower, upper);

        assertEquals(lower, distribution.getSupportLowerBound(), EPSILON);
        assertEquals(upper, distribution.getSupportUpperBound(), EPSILON);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testConstructorWithInvalidParameters() {
        try {
            double lower = 1.0;
            double upper = -1.0;
            distribution = new UniformRealDistribution(lower, upper);
            fail("Did not throw expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testConstructorWithRandomGenerator() {
        RandomGenerator rng = new Well19937c();
        double lower = -1.0;
        double upper = 1.0;
        distribution = new UniformRealDistribution(rng, lower, upper);

        assertEquals(lower, distribution.getSupportLowerBound(), EPSILON);
        assertEquals(upper, distribution.getSupportUpperBound(), EPSILON);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testConstructorWithRandomGeneratorAndInvalidParameters() {
        try {
            RandomGenerator rng = new Well19937c();
            double lower = 1.0;
            double upper = -1.0;
            distribution = new UniformRealDistribution(rng, lower, upper);
            fail("Did not throw expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testDensity() {
        assertEquals(1.0, distribution.density(0.5), EPSILON);
    }

    @Test
    public void testDensityBelowLowerBound() {
        assertEquals(0.0, distribution.density(-1.0), EPSILON);
    }

    @Test
    public void testDensityAboveUpperBound() {
        assertEquals(0.0, distribution.density(1.0), EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.5, distribution.cumulativeProbability(0.5), EPSILON);
    }

    @Test
    public void testCumulativeProbabilityBelowLowerBound() {
        assertEquals(0.0, distribution.cumulativeProbability(-1.0), EPSILON);
    }

    @Test
    public void testCumulativeProbabilityAboveUpperBound() {
        assertEquals(1.0, distribution.cumulativeProbability(1.0), EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        double lower = 0.0;
        double upper = 1.0;
        distribution = new UniformRealDistribution(lower, upper);
        assertEquals(0.5, distribution.getNumericalMean(), EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        double lower = -1.0;
        double upper = 1.0;
        distribution = new UniformRealDistribution(lower, upper);
        assertEquals(1.0/3, distribution.getNumericalVariance(), EPSILON);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0.0, distribution.getSupportLowerBound(), EPSILON);
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(1.0, distribution.getSupportUpperBound(), EPSILON);
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
        RandomDataGenerator generator = new RandomDataGenerator();
        double lower = 0.0;
        double upper = 1.0;
        distribution = new UniformRealDistribution(generator.getRandomGenerator(),
                                                  lower, upper);
        double sample = distribution.sample();
        assertTrue(sample >= lower && sample < upper);
    }
}