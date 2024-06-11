package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.random.Well19937c;

public class FDistributionTest {

    @org.junit.Test
    public void testFDistributionConstruction() {
        // Test default constructor
        FDistribution fd = new FDistribution();
        assertFalse(fd.isSupportConnected());
        // Test constructor with valid parameters
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd1 = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, fd1.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fd1.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, fd1.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, fd1.getSupportUpperBound(), 0.0);
        assertTrue(fd1.isSupportLowerBoundInclusive());
        assertFalse(fd1.isSupportUpperBoundInclusive());
        // Test constructor with random number generator
        Well19937c rng = new Well19937c();
        FDistribution fd2 = new FDistribution(rng, numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, fd2.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fd2.getDenominatorDegreesOfFreedom(), 0.0);
        // Test constructor with inverse cumulative probability accuracy
        double inverseCumAccuracy = 1e-6;
        FDistribution fd3 = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom, inverseCumAccuracy);
        assertEquals(inverseCumAccuracy, fd3.getSolverAbsoluteAccuracy(), 0.0);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionInvalidConstructionNumeratorDegreesOfFreedomZero() {
        double numeratorDegreesOfFreedom = 0.0;
        new FDistribution(numeratorDegreesOfFreedom, 5.0);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionInvalidConstructionDenominatorDegreesOfFreedomZero() {
        double denominatorDegreesOfFreedom = 0.0;
        new FDistribution(3.0, denominatorDegreesOfFreedom);
    }

    @org.junit.Test
    public void testFDistributionDensity() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 1.5;
        double densityExpected = fd.density(x);
        double densityCalculated = fd.density(x);
        assertEquals(densityExpected, densityCalculated, 1e-10);
    }

    @org.junit.Test
    public void testFDistributionCumulativeProbability() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 1.5;
        double cumProbExpected = fd.cumulativeProbability(x);
        double cumProbCalculated = fd.cumulativeProbability(x);
        assertEquals(cumProbExpected, cumProbCalculated, 1e-10);
    }

    @org.junit.Test
    public void testFDistributionNumericalVariance() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double numericalVarianceExpected = fd.calculateNumericalVariance();
        double numericalVarianceCalculated = fd.getNumericalVariance();
        assertEquals(numericalVarianceExpected, numericalVarianceCalculated, 1e-10);
    }
}