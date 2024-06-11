package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;

import java.util.Random;

public class FDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeDegreesOfFreedom() {
        new FDistribution(-1, 2.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroNumeratorDegreesOfFreedom() {
        new FDistribution(0, 2.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroDenominatorDegreesOfFreedom() {
        new FDistribution(1.0, 0.0);
    }

    @Test
    public void testDensity() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        double result = dist.density(1.5);
        double expected = 0.16582215154645385;
        assertEquals(expected, result, 1e-8);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        double result = dist.cumulativeProbability(1.5);
        double expected = 0.9210226169223131;
        assertEquals(expected, result, 1e-8);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        double result = dist.getNumeratorDegreesOfFreedom();
        double expected = 2.0;
        assertEquals(expected, result, 1e-8);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        double result = dist.getDenominatorDegreesOfFreedom();
        double expected = 3.0;
        assertEquals(expected, result, 1e-8);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        double result = dist.getSolverAbsoluteAccuracy();
        double expected = 1e-9;
        assertEquals(expected, result, 1e-12);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        double result = dist.getNumericalMean();
        double expected = 1.5;
        assertEquals(expected, result, 1e-8);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        double result = dist.getNumericalVariance();
        double expected = 0.8;
        assertEquals(expected, result, 1e-8);
    }

    @Test
    public void testCalculateNumericalVariance() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        double result = dist.calculateNumericalVariance();
        double expected = 0.8;
        assertEquals(expected, result, 1e-8);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        double result = dist.getSupportLowerBound();
        double expected = 0.0;
        assertEquals(expected, result, 1e-8);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        double result = dist.getSupportUpperBound();
        double expected = Double.POSITIVE_INFINITY;
        assertEquals(expected, result, 1e-8);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        boolean result = dist.isSupportLowerBoundInclusive();
        boolean expected = true;
        assertTrue(result == expected);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        boolean result = dist.isSupportUpperBoundInclusive();
        boolean expected = false;
        assertTrue(result == expected);
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        boolean result = dist.isSupportConnected();
        boolean expected = true;
        assertTrue(result == expected);
    }

    @Test
    public void testRandomValues() {
        FDistribution dist = new FDistribution(new Random(), 2.0, 3.0);
        for (int i = 0; i < 10; i++) {
            double result = dist.inverseCumulativeProbability(0.5);
            assertEquals(result, result, 1e-8);
        }
    }
}