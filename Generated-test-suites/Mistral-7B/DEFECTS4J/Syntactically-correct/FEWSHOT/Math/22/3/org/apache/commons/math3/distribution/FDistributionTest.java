package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class FDistributionTest {
    @Test
    public void testFDistribution() throws NotStrictlyPositiveException {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeNumeratorDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(-2.0, 3.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeDenominatorDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(2.0, -3.0);
    }

    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double density = fd.density(1.5);
        assertEquals(0.0305950223761536, density, 0.00001);
    }

    @Test
    public void testDensityNegativeX() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double density = fd.density(-1.5);
        assertEquals(0.0, density, 0.0);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double cdf = fd.cumulativeProbability(1.5);
        assertEquals(0.916262992068426, cdf, 0.00001);
    }

    @Test
    public void testCumulativeProbabilityZero() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double cdf = fd.cumulativeProbability(0);
        assertEquals(0.0, cdf, 0.0);
    }

    @Test
    public void testCumulativeProbabilityMaxValue() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double cdf = fd.cumulativeProbability(Double.POSITIVE_INFINITY);
        assertEquals(1.0, cdf, 0.0);
    }

    @Test
    public void testMean() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double mean = fd.getNumericalMean();
        assertEquals(1.5, mean, 0.0);
    }

    @Test
    public void testMeanInvalidNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(0.0, 3.0);
        double mean = fd.getNumericalMean();
        assertNotNull(mean);
        assertEquals(Double.NaN, mean);
    }

    @Test
    public void testMeanInvalidDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2.0, 0.0);
        double mean = fd.getNumericalMean();
        assertNotNull(mean);
        assertEquals(Double.NaN, mean);
    }

    @Test
    public void testVariance() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double variance = fd.getNumericalVariance();
        assertEquals(0.350811667461628, variance, 0.0001);
    }

    @Test
    public void testVarianceInvalidDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2.0, 2.0);
        double variance = fd.getNumericalVariance();
        assertNotNull(variance);
        assertEquals(Double.NaN, variance);
    }
}