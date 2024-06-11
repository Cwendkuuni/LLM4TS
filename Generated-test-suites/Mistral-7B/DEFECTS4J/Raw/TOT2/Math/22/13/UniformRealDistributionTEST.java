package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link UniformRealDistribution} class.
 */
public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;
    private RandomGenerator random;

    @Before
    public void setUp() throws NumberIsTooLargeException {
        random = new Well19937c();
        distribution = new UniformRealDistribution(random, 0, 1);
    }

    /**
     * Test constructor with default bounds.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution uniform = new UniformRealDistribution();
        assertEquals(0, uniform.getSupportLowerBound(), 0.001);
        assertEquals(1, uniform.getSupportUpperBound(), 0.001);
    }

    /**
     * Test constructor with custom bounds.
     */
    @Test
    public void testCustomConstructor() throws NumberIsTooLargeException {
        UniformRealDistribution uniform = new UniformRealDistribution(random, 2, 5);
        assertEquals(2, uniform.getSupportLowerBound(), 0.001);
        assertEquals(5, uniform.getSupportUpperBound(), 0.001);
    }

    /**
     * Test constructor with invalid bounds.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructor() throws NumberIsTooLargeException {
        new UniformRealDistribution(random, 5, 5);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        double x;

        for (x = 0; x < 0.9999; x += 0.01) {
            double density = distribution.density(x);
            assertTrue(density > 0);
            assertTrue(density <= 1.0 / (1 - 0));
        }

        x = 0.9999;
        double density = distribution.density(x);
        assertTrue(density > 0);
        assertTrue(density <= 1.0 / (1 - 0.0001));

        x = 1.0;
        density = distribution.density(x);
        assertEquals(0, density, 1e-15);

        for (x = 1.0001; x < distribution.getSupportUpperBound(); x += 0.01) {
            double density = distribution.density(x);
            assertTrue(density > 0);
            assertTrue(density <= 1.0 / (distribution.getSupportUpperBound() - distribution.getSupportLowerBound()));
        }
    }

    /**
     * Test cumulative probability method with valid values.
     */
    @Test
    public void testCumulativeProbabilityValidValues() {
        double x;

        for (x = 0; x <= 1; x += 0.01) {
            double cumProb = distribution.cumulativeProbability(x);
            assertTrue(cumProb >= 0);
            assertTrue(cumProb <= 1);
            assertEquals(x, distribution.inverseCumulativeProbability(cumProb), 1e-14);
        }
    }

    /**
     * Test cumulative probability method with an invalid value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCumulativeProbabilityInvalidValue() {
        distribution.cumulativeProbability(-1);
    }

    /**
     * Test inverse cumulative probability method with valid values.
     */
    @Test
    public void testInverseCumulativeProbabilityValidValues() {
        double cumProb;

        for (cumProb = 0; cumProb <= 1; cumProb += 0.01) {
            double x = distribution.inverseCumulativeProbability(cumProb);
            assertTrue(x >= distribution.getSupportLowerBound());
            assertTrue(x < distribution.getSupportUpperBound());
            assertEquals(cumProb, distribution.cumulativeProbability(x), 1e-14);
        }
    }

    /**
     * Test inverse cumulative probability method with an invalid value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityInvalidValue() {
        distribution.inverseCumulativeProbability(1.1);
    }

    /**
     * Test getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        assertEquals(0.5, distribution.getNumericalMean(), 1e-15);
    }

    /**
     * Test getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        double variance = distribution.getNumericalVariance();
        assertEquals(0.0002222222222222222, variance, 1e-15);
    }

    /**
     * Test getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, distribution.getSupportLowerBound(), 0.001);
    }

    /**
     * Test getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        assertEquals(1, distribution.getSupportUpperBound(), 0.001);
    }

    /**
     * Test isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    /**
     * Test isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    /**
     * Test isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        for (int i = 0; i < 10000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= distribution.getSupportLowerBound());
            assertTrue(sample < distribution.getSupportUpperBound());
        }
    }

}