package org.apache.commons.cli.test;

import static org.apache.commons.math3.util.Precision.abs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testFDistCreation() {
        // Test with default constructor
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        assertNotNull(fDistribution);

        // Test with specific inverse cumulative probability accuracy
        FDistribution fDistributionAccuracy = new FDistribution(2.0, 3.0, 0.0001);
        assertNotNull(fDistributionAccuracy);

        // Test with RandomGenerator
        RandomGenerator rng = new Well19937c();
        FDistribution fDistributionWithRng = new FDistribution(rng, 2.0, 3.0);
        assertNotNull(fDistributionWithRng);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(-1.0, 3.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(2.0, 0.0);
    }

    @Test
    public void testGetters() {
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        assertEquals(2.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(0.0, fDistribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testDensity() {
        // Test density with valid input
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        double densityValue = fDistribution.density(1.5);
        assertEquals(0.2005728133238029, densityValue, 0.00001);

        // Test density with negative input
        try {
            fDistribution.density(-1.0);
        } catch (IllegalArgumentException e) {
            assertEquals("Input must be non-negative", e.getMessage());
        }

        // Test density with infinite input
        double densityAtInfinity = fDistribution.density(Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, densityAtInfinity, 0.0);
    }

    @Test
    public void testCumulativeProbability() {
        // Test cumulativeProbability with valid input
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        double cumulativeProbabilityValue = fDistribution.cumulativeProbability(1.5);
        assertEquals(0.9210433412954254, cumulativeProbabilityValue, 0.00001);

        // Test cumulativeProbability with zero input
        double cumulativeProbabilityAtZero = fDistribution.cumulativeProbability(0.0);
        assertEquals(0.0, cumulativeProbabilityAtZero, 0.0);

        // Test cumulativeProbability with negative input
        try {
            fDistribution.cumulativeProbability(-1.0);
        } catch (IllegalArgumentException e) {
            assertEquals("Input must be non-negative", e.getMessage());
        }
    }

    @Test
    public void testNumericalMean() {
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        double numericalMean = fDistribution.getNumericalMean();
        assertEquals(1.5, numericalMean, 0.0);
    }

    @Test
    public void testNumericalVariance() {
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        double numericalVariance = fDistribution.getNumericalVariance();
        assertEquals(1.2411741131854806, numericalVariance, 0.00001);
    }

    @Test
    public void testSupportBounds() {
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        assertEquals(0.0, fDistribution.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0.0);
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
        assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testSupportConnected() {
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        assertTrue(fDistribution.isSupportConnected());
    }
}