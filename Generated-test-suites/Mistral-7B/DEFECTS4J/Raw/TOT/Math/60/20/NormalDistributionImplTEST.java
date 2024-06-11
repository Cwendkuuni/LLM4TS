package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test suite for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        final double mean = 0.0;
        final double standardDeviation = 1.0;
        final double inverseCumAccuracy = 1e-9;
        NormalDistributionImpl distribution = new NormalDistributionImpl(mean, standardDeviation, inverseCumAccuracy);
        assertEquals(mean, distribution.getMean(), 0.0);
        assertEquals(standardDeviation, distribution.getStandardDeviation(), 0.0);
        assertEquals(inverseCumAccuracy, distribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test method {@link NormalDistributionImpl#density(double)}.
     */
    @Test
    public void testDensity() {
        final NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

        // typical use case
        final double x = 0.5;
        final double expected = 0.12732058904109536;
        assertEquals(expected, distribution.density(x), 1e-6);

        // edge case: minimum value
        final double xMin = Double.MIN_VALUE;
        final double expectedMin = Precision.isClose(1.0 / (Math.sqrt(2.0) * Math.PI), distribution.density(xMin), 1e-8);
        assertTrue(expectedMin);

        // edge case: maximum value
        final double xMax = Double.MAX_VALUE;
        final double expectedMax = Precision.isClose(1.0 / (Math.sqrt(2.0) * Math.PI), distribution.density(xMax), 1e-8);
        assertTrue(expectedMax);

        // error case: negative argument
        final double xNegative = -1.0;
        try {
            distribution.density(xNegative);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test method {@link NormalDistributionImpl#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        final NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

        // typical use case
        final double x = 0.5;
        final double expected = 0.5;
        assertEquals(expected, distribution.cumulativeProbability(x), 1e-6);

        // edge case: minimum value
        final double xMin = Double.MIN_VALUE;
        final double expectedMin = 0.0;
        assertEquals(expectedMin, distribution.cumulativeProbability(xMin), 1e-8);

        // edge case: maximum value
        final double xMax = Double.MAX_VALUE;
        final double expectedMax = 1.0;
        assertEquals(expectedMax, distribution.cumulativeProbability(xMax), 1e-8);

        // error case: negative argument
        final double xNegative = -1.0;
        try {
            distribution.cumulativeProbability(xNegative);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }

        // error case: argument out of range
        final double xOutOfRange = 3.0;
        try {
            distribution.cumulativeProbability(xOutOfRange);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test method {@link NormalDistributionImpl#inverseCumulativeProbability(double)}.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException, OutOfRangeException {
        final NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

        // typical use case
        final double p = 0.5;
        final double expected = 0.0;
        assertEquals(expected, distribution.inverseCumulativeProbability(p), 1e-9);

        // edge case: minimum value
        final double pMin = 0.0;
        final double expectedMin = Double.NEGATIVE_INFINITY;
        assertEquals(expectedMin, distribution.inverseCumulativeProbability(pMin));

        // edge case: maximum value
        final double pMax = 1.0;
        final double expectedMax = Double.POSITIVE_INFINITY;
        assertEquals(expectedMax, distribution.inverseCumulativeProbability(pMax));

        // error case: argument out of range
        final double pOutOfRange = 1.1;
        try {
            distribution.inverseCumulativeProbability(pOutOfRange);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }

        // error case: argument does not represent a probability
        final double pNotProbability = -1.0;
        try {
            distribution.inverseCumulativeProbability(pNotProbability);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test method {@link NormalDistributionImpl#sample()}.
     */
    @Test
    public void testSample() throws MathException {
        final NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

        // typical use case
        final double value = distribution.sample();
        // We cannot know the expected value in this case, but we can check that it is a valid random number
        assertTrue(value > -10000 && value < 10000);

        // edge case: minimum value
        try {
            distribution.sample();
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }

        // edge case: maximum value
        try {
            distribution.sample();
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }
    }

}