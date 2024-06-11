package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class FDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeDegreesOfFreedom() {
        new FDistribution(new Well19937c(), -1, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeNumeratorDegreesOfFreedom() {
        new FDistribution(new Well19937c(), 3, -1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeDenominatorDegreesOfFreedom() {
        new FDistribution(new Well19937c(), 3, 3, -1);
    }

    @Test
    public void testDensityZero() {
        FDistribution dist = new FDistribution(new Well19937c(), 1, 1);
        assertEquals(0, dist.density(0), 1e-11);
    }

    @Test
    public void testDensityPositive() {
        FDistribution dist = new FDistribution(new Well19937c(), 1, 1);
        double x = 1.5;
        double density = dist.density(x);
        assertEquals(density, 0.110666666666667, 1e-11);
    }

    @Test
    public void testDensityNegative() {
        FDistribution dist = new FDistribution(new Well19937c(), 1, 1);
        double x = -1.5;
        double density = dist.density(x);
        assertEquals(density, 0, 1e-11);
    }

    @Test
    public void testCumulativeProbabilityZero() {
        FDistribution dist = new FDistribution(new Well19937c(), 1, 1);
        double x = 0;
        double prob = dist.cumulativeProbability(x);
        assertEquals(prob, 0, 1e-11);
    }

    @Test
    public void testCumulativeProbabilityPositive() {
        FDistribution dist = new FDistribution(new Well19937c(), 1, 1);
        double x = 1.5;
        double prob = dist.cumulativeProbability(x);
        assertEquals(prob, 0.880797014252284, 1e-11);
    }

    @Test
    public void testCumulativeProbabilityNegative() {
        FDistribution dist = new FDistribution(new Well19937c(), 1, 1);
        double x = -1.5;
        double prob = dist.cumulativeProbability(x);
        assertEquals(prob, 0, 1e-11);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(new Well19937c(), 5, 7);
        assertEquals(dist.getNumeratorDegreesOfFreedom(), 5, 1e-11);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(new Well19937c(), 5, 7);
        assertEquals(dist.getDenominatorDegreesOfFreedom(), 7, 1e-11);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution dist = new FDistribution(new Well19937c(), 5, 7);
        assertEquals(dist.getSolverAbsoluteAccuracy(), 1e-9, 1e-11);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution dist = new FDistribution(new Well19937c(), 3, 4);
        assertEquals(dist.getNumericalMean(), 4.0 / 3.0, 1e-11);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution dist = new FDistribution(new Well19937c(), 3, 4);
        double numericalVariance = dist.getNumericalVariance();
        assertEquals(numericalVariance, 2.0 / 15.0, 1e-11);

        dist = new FDistribution(new Well19937c(), 3, 3);
        numericalVariance = dist.getNumericalVariance();
        assertTrue(Double.isNaN(numericalVariance));
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution dist = new FDistribution(new Well19937c(), 1, 1);
        assertEquals(dist.getSupportLowerBound(), 0, 1e-11);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution dist = new FDistribution(new Well19937c(), 1, 1);
        assertEquals(dist.getSupportUpperBound(), Double.POSITIVE_INFINITY, 1e-11);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution dist = new FDistribution(new Well19937c(), 1, 1);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution dist = new FDistribution(new Well19937c(), 1, 1);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution dist = new FDistribution(new Well19937c(), 1, 1);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testGenerateValues() {
        FDistribution dist = new FDistribution(new Well19937c(), 5, 7);

        double[] values = new double[1000];
        for (int i = 0; i < values.length; i++) {
            values[i] = dist.inverseCumulativeProbability(FastMath.nextUniform(0, 1));
        }

        RealDistribution reference = new FDistribution(5, 7);
        assertArrayEquals(values, reference.sample(values.length), 0.001);
    }
}