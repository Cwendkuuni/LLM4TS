package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

import java.util.Arrays;

public class FDistributionTest {

    @Test
    public void testFDistribution() {
        try {
            FDistribution fd = new FDistribution(2.0, 3.0);
            assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.001);
            assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.001);
            assertNotNull(fd.getNumericalVariance());
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            FDistribution fd = new FDistribution(0.0, 3.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            FDistribution fd = new FDistribution(2.0, 0.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double[] values = new double[]{-1.0, 0.0, 1.0, 2.0, 3.0, 5.0, 10.0};
        for (double x : values) {
            double density = fd.density(x);
            if (Double.isNaN(density)) {
                fail("density is NaN for x = " + x);
            }
            System.out.println("density(" + x + ") = " + density);
        }
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double[] values = new double[]{0.0, 1.0, 2.5, 3.0, 4.0, 5.0, 10.0};
        for (double x : values) {
            double p = fd.cumulativeProbability(x);
            System.out.println("cumulativeProbability(" + x + ") = " + p);
            assertEquals(0.0, p, 0.001, "cumulativeProbability(" + x + ") != " + p);
        }
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double mean = fd.getNumericalMean();
        assertEquals(3.0, mean, 0.001, "mean != " + mean);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double variance = fd.getNumericalVariance();
        assertEquals(3.3333333333333335, variance, 0.0001, "variance != " + variance);

        double[] values = new double[]{0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 10.0};
        double sum = 0.0;
        for (double x : values) {
            sum += Math.pow(fd.density(x) - variance / 1000.0, 2);
        }
        double calculatedVariance = sum / 7.0;
        assertEquals(calculatedVariance, variance, 0.0001, "calculatedVariance != " + calculatedVariance);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertEquals(0.0, fd.getSupportLowerBound(), 0.001);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 0.001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertTrue(fd.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertFalse(fd.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertTrue(fd.isSupportConnected());
    }
}