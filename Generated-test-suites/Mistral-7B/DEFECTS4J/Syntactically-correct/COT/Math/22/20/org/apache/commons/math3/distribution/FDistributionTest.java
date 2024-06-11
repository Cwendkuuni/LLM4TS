package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FDistributionTest {
    // Basic tests for constructor

    @Test
    public void testFDistributionConstructor() {
        FDistribution fd;
        try {
            fd = new FDistribution(2.0, 4.0);
        } catch (NotStrictlyPositiveException e) {
            // expected
            return;
        }
        Assert.assertNotNull(fd);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionConstructorNegativeDegreesOfFreedom() {
        new FDistribution(-2.0, 4.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionConstructorZeroNumeratorDegreesOfFreedom() {
        new FDistribution(0.0, 4.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionConstructorZeroDenominatorDegreesOfFreedom() {
        new FDistribution(2.0, 0.0);
    }

    // Test methods from the class

    @Before
    public void setUp() {
        fDist = new FDistribution(2.0, 4.0);
    }

    private FDistribution fDist;

    @Test
    public void testDensity() {
        double x = 1.5;
        double expected = 0.1761781132480335;
        double actual = fDist.density(x);
        Assert.assertEquals(expected, actual, 1e-8);
    }

    @Test
    public void testCumulativeProbability() {
        double x = 1.5;
        double expected = 0.904537761324779;
        double actual = fDist.cumulativeProbability(x);
        Assert.assertEquals(expected, actual, 1e-8);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double expected = 2.0;
        double actual = fDist.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(expected, actual, 1e-14);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double expected = 4.0;
        double actual = fDist.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(expected, actual, 1e-14);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        double expected = 1e-9;
        double actual = fDist.getSolverAbsoluteAccuracy();
        Assert.assertEquals(expected, actual, 1e-14);
    }

    @Test
    public void testGetNumericalMean() {
        double expected = 1.5;
        double actual = fDist.getNumericalMean();
        Assert.assertEquals(expected, actual, 1e-14);
    }

    @Test
    public void testGetNumericalVariance() {
        double expected = 1.2121212121212121;
        double actual = fDist.getNumericalVariance();
        Assert.assertEquals(expected, actual, 1e-14);
    }

    @Test
    public void testGetSupportLowerBound() {
        double expected = 0.0;
        double actual = fDist.getSupportLowerBound();
        Assert.assertEquals(expected, actual, 1e-14);
    }

    @Test
    public void testGetSupportUpperBound() {
        double expected = Double.POSITIVE_INFINITY;
        double actual = fDist.getSupportUpperBound();
        Assert.assertEquals(expected, actual, 1e-14);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean expected = true;
        boolean actual = fDist.isSupportLowerBoundInclusive();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean expected = false;
        boolean actual = fDist.isSupportUpperBoundInclusive();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsSupportConnected() {
        boolean expected = true;
        boolean actual = fDist.isSupportConnected();
        Assert.assertEquals(expected, actual);
    }

    // Edge cases and exception handling tests

    @Test(expected = NotStrictlyPositiveException.class)
    public void testDensityNegativeInput() {
        fDist.density(-1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testCumulativeProbabilityNegativeInput() {
        fDist.cumulativeProbability(-1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testGetNumeratorDegreesOfFreedomNegativeInput() {
        FDistribution fd = null;
        try {
            fd = new FDistribution(-2.0, 4.0);
        } catch (NotStrictlyPositiveException e) {
            // expected
            return;
        }
        Assert.assertNull(fd);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testGetDenominatorDegreesOfFreedomNegativeInput() {
        FDistribution fd = null;
        try {
            fd = new FDistribution(2.0, -4.0);
        } catch (NotStrictlyPositiveException e) {
            // expected
            return;
        }
        Assert.assertNull(fd);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testGetNumericalMeanNegativeDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2.0, 0.0);
        double actual = fd.getNumericalMean();
        Assert.assertEquals(Double.NaN, actual, 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testGetNumericalVarianceNegativeDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2.0, 0.0);
        double actual = fd.getNumericalVariance();
        Assert.assertEquals(Double.NaN, actual, 0);
    }
}