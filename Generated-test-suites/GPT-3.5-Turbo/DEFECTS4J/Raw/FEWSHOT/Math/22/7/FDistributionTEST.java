package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FDistributionTest {
    private FDistribution fDistribution;

    @Test
    public void testDensity() {
        // Test for valid input
        fDistribution = new FDistribution(5, 10);
        assertEquals(0.08511605393416043, fDistribution.density(0.5), 1e-9);
        assertEquals(0.08440003133060362, fDistribution.density(2.0), 1e-9);
        assertEquals(0.03662093997608289, fDistribution.density(5.0), 1e-9);

        // Test for invalid input
        boolean notStrictlyPositiveExceptionThrown = false;
        try {
            fDistribution = new FDistribution(0, 10);
        } catch (org.apache.commons.math3.exception.NotStrictlyPositiveException e) {
            notStrictlyPositiveExceptionThrown = true;
        }
        assertTrue(notStrictlyPositiveExceptionThrown);
    }

    @Test
    public void testCumulativeProbability() {
        // Test for valid input
        fDistribution = new FDistribution(5, 10);
        assertEquals(0.038310589664889754, fDistribution.cumulativeProbability(0.5), 1e-9);
        assertEquals(0.1307300468014583, fDistribution.cumulativeProbability(2.0), 1e-9);
        assertEquals(0.36688180664632636, fDistribution.cumulativeProbability(5.0), 1e-9);

        // Test for invalid input
        boolean notStrictlyPositiveExceptionThrown = false;
        try {
            fDistribution = new FDistribution(0, 10);
        } catch (org.apache.commons.math3.exception.NotStrictlyPositiveException e) {
            notStrictlyPositiveExceptionThrown = true;
        }
        assertTrue(notStrictlyPositiveExceptionThrown);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        fDistribution = new FDistribution(5, 10);
        assertEquals(5.0, fDistribution.getNumeratorDegreesOfFreedom(), 1e-9);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        fDistribution = new FDistribution(5, 10);
        assertEquals(10.0, fDistribution.getDenominatorDegreesOfFreedom(), 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        // Test for valid input
        fDistribution = new FDistribution(5, 10);
        assertEquals(Double.NaN, fDistribution.getNumericalMean(), 1e-9);

        fDistribution = new FDistribution(6, 10);
        assertEquals(1.25, fDistribution.getNumericalMean(), 1e-9);

        // Test for invalid input
        boolean notStrictlyPositiveExceptionThrown = false;
        try {
            fDistribution = new FDistribution(0, 10);
        } catch (org.apache.commons.math3.exception.NotStrictlyPositiveException e) {
            notStrictlyPositiveExceptionThrown = true;
        }
        assertTrue(notStrictlyPositiveExceptionThrown);
    }

    @Test
    public void testGetNumericalVariance() {
        // Test for valid input
        fDistribution = new FDistribution(5, 10);
        assertEquals(Double.NaN, fDistribution.getNumericalVariance(), 1e-9);

        fDistribution = new FDistribution(6, 10);
        assertEquals(4.6875, fDistribution.getNumericalVariance(), 1e-9);

        // Test for invalid input
        boolean notStrictlyPositiveExceptionThrown = false;
        try {
            fDistribution = new FDistribution(0, 10);
        } catch (org.apache.commons.math3.exception.NotStrictlyPositiveException e) {
            notStrictlyPositiveExceptionThrown = true;
        }
        assertTrue(notStrictlyPositiveExceptionThrown);
    }

    @Test
    public void testGetSupportLowerBound() {
        fDistribution = new FDistribution(5, 10);
        assertEquals(0, fDistribution.getSupportLowerBound(), 1e-9);
    }

    @Test
    public void testGetSupportUpperBound() {
        fDistribution = new FDistribution(5, 10);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 1e-9);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        fDistribution = new FDistribution(5, 10);
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        fDistribution = new FDistribution(5, 10);
        assertTrue(!fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        fDistribution = new FDistribution(5, 10);
        assertTrue(fDistribution.isSupportConnected());
    }
}