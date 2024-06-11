package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testFDistributionDefaultConstructor() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.001);
        assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDegreesOfFreedom() {
        new FDistribution(-1.0, 3.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroDegreesOfFreedom() {
        new FDistribution(0.0, 3.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeNumeratorDegreesOfFreedom() {
        new FDistribution(1.0, -3.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroNumeratorDegreesOfFreedom() {
        new FDistribution(0.0, 3.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroDenominatorDegreesOfFreedom() {
        new FDistribution(2.0, 0.0);
    }

    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double denominatorDF = fd.getDenominatorDegreesOfFreedom();
        double result = 0.0;
        for (double x = 0.0; x <= 5.0; x += 0.1) {
            result += fd.density(x);
        }
        double expected = (2.0 * FastMath.log(2.0) + 2.0 * Math.log(3.0) - 2.0 * Math.log(2.0 + 3.0 * 5.0)) / (2.0 * 5.0 * 3.0 * (3.0 - 2.0) * (3.0 - 4.0)) - Beta.logBeta(1.0, 1.0);
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double x = 0.0;
        double result = 0.0;
        for (; x <= 5.0; x += 0.1) {
            result += fd.cumulativeProbability(x);
        }
        double expected = 1.0;
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testNumericalMean() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double result = fd.getNumericalMean();
        assertEquals(1.5, result, 1e-9);
    }

    @Test
    public void testNumericalVariance() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double result = fd.getNumericalVariance();
        assertEquals(1.2071472824762136, result, 1e-9);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertEquals(1e-9, fd.getSolverAbsoluteAccuracy(), 1e-9);
    }
}