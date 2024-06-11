package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;

import java.util.Random;

public class FDistributionTest {

    @Test
    public void testFDistributionCreation() {
        try {
            FDistribution fd = new FDistribution(2.5, 3.5);
            assertEquals(2.5, fd.getNumeratorDegreesOfFreedom(), 0.001);
            assertEquals(3.5, fd.getDenominatorDegreesOfFreedom(), 0.001);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            FDistribution fd = new FDistribution(1, 3.5);
            fail("Expected NotStrictlyPositiveException but no exception was thrown.");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            FDistribution fd = new FDistribution(2.5, 0);
            fail("Expected NotStrictlyPositiveException but no exception was thrown.");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fd = null;
        try {
            fd = new FDistribution(5, 7);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        double[] xValues = {0.1, 0.5, 1.0, 2.0, 3.0, 5.0};
        double[] expectedValues = {0.000236251455715, 0.0201888109034, 0.1047197551126,
                0.3125142259225, 0.5451521613464, 0.9162796770989};

        for (int i = 0; i < xValues.length; i++) {
            double actualValue = fd.density(xValues[i]);
            assertEquals(expectedValues[i], actualValue, 0.00001);
        }
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fd = null;
        try {
            fd = new FDistribution(5, 7);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        double[] xValues = {0.0, 0.1, 0.5, 1.0, 1.5, 2.0, 3.0, 4.0, 5.0};
        double[] expectedValues = {0.0, 0.0411428391419, 0.1791723480314, 0.3989422804018,
                0.6127353181233, 0.776465529182, 0.9253731641248, 0.9772652605283, 1.0};

        for (int i = 0; i < xValues.length; i++) {
            double actualValue = fd.cumulativeProbability(xValues[i]);
            assertEquals(expectedValues[i], actualValue, 0.00001);
        }
    }

    @Test
    public void testFDistributionMean() {
        FDistribution fd = null;
        try {
            fd = new FDistribution(5, 7);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        double mean = fd.getNumericalMean();
        assertNotEquals(Double.NaN, mean);
        assertEquals(7.0 / 4.0, mean, 0.00001);
    }

    @Test
    public void testFDistributionVariance() {
        FDistribution fd = null;
        try {
            fd = new FDistribution(5, 7);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        double variance = fd.getNumericalVariance();
        assertNotNull(variance);
        double expectedVariance = 7.0 / (12.0 * (5.0 - 2.0) * (7.0 - 4.0));
        assertEquals(expectedVariance, variance, 0.00001);
    }

    @Test
    public void testFDistributionSupport() {
        FDistribution fd = null;
        try {
            fd = new FDistribution(5, 7);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 0.00001);
        assertEquals(0.0, fd.getSupportLowerBound(), 0.00001);
        assertEquals(true, fd.isSupportLowerBoundInclusive());
        assertEquals(false, fd.isSupportUpperBoundInclusive());
        assertEquals(true, fd.isSupportConnected());
    }
}