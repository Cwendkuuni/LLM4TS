package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;

import java.util.Random;

public class FDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeDegreesOfFreedom() {
        new FDistribution(-1.0, 2.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroNumeratorDegreesOfFreedom() {
        new FDistribution(0.0, 2.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroDenominatorDegreesOfFreedom() {
        new FDistribution(2.0, 0.0);
    }

    @Test
    public void testDensity() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        double density = dist.density(1.5);
        assertEquals(0.1013383953128255, density, 0.00001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        double cpf = dist.cumulativeProbability(1.5);
        assertEquals(0.9152633631553167, cpf, 0.00001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        assertEquals(3.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        assertEquals(4.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        assertEquals(FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, dist.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        assertEquals(3.0 / 2.0, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        double numericalVariance = dist.getNumericalVariance();
        assertEquals(1.4353192953155134, numericalVariance, 0.001);

        dist = new FDistribution(2.0, 2.0);
        numericalVariance = dist.getNumericalVariance();
        assertEquals(Double.NaN, numericalVariance);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testGetRandomSample() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        Random rng = new Random();
        double sample = dist.inverseCumulativeProbability(rng.nextDouble());
        double density = dist.density(sample);
        assertEquals(density, dist.density(sample), 0.001);
    }
}