package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testFDistribution() throws NotStrictlyPositiveException {
        FDistribution fDist = new FDistribution(2.5, 3.0);
        double density = fDist.density(1.5);
        assertEquals(0.2185532157142858, density, 0.0001);

        double cumulativeProbability = fDist.cumulativeProbability(1.5);
        assertEquals(0.9121873430717336, cumulativeProbability, 0.0001);

        double lowerBound = fDist.getSupportLowerBound();
        assertEquals(0.0, lowerBound, 0.0);

        double upperBound = fDist.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 0.0);

        boolean isSupportLowerBoundInclusive = fDist.isSupportLowerBoundInclusive();
        assertTrue(isSupportLowerBoundInclusive);

        boolean isSupportUpperBoundInclusive = fDist.isSupportUpperBoundInclusive();
        assertFalse(isSupportUpperBoundInclusive);

        boolean isSupportConnected = fDist.isSupportConnected();
        assertTrue(isSupportConnected);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(2.5, 3.0);
        double numeratorDegreesOfFreedom = fDist.getNumeratorDegreesOfFreedom();
        assertEquals(2.5, numeratorDegreesOfFreedom, 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(2.5, 3.0);
        double denominatorDegreesOfFreedom = fDist.getDenominatorDegreesOfFreedom();
        assertEquals(3.0, denominatorDegreesOfFreedom, 0.0);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution fDist = new FDistribution(2.5, 3.0, 0.001);
        double solverAbsoluteAccuracy = fDist.getSolverAbsoluteAccuracy();
        assertEquals(0.001, solverAbsoluteAccuracy, 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDist = new FDistribution(2.5, 3.0);
        double numericalMean = fDist.getNumericalMean();
        assertEquals(1.5, numericalMean, 0.1);

        fDist = new FDistribution(1.5, 3.0);
        numericalMean = fDist.getNumericalMean();
        assertNull(numericalMean);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDist = new FDistribution(2.5, 3.0);
        double numericalVariance = fDist.getNumericalVariance();
        assertEquals(1.0472152349533515, numericalVariance, 0.01);

        fDist = new FDistribution(1.5, 3.0);
        numericalVariance = fDist.getNumericalVariance();
        assertNull(numericalVariance);
    }
}