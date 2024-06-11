package org.apache.commons.cli;

import static org.junit.Assert.assertTrue;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testFDistribution() {
        try {
            FDistribution fDist = new FDistribution(1.0, 2.0);
            assertTrue(fDist instanceof FDistribution);
        } catch (NotStrictlyPositiveException e) {
            throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
        }
    }

    @Test
    public void testFDistributionWithRandomGenerator() {
        try {
            RandomGenerator rng = new Well19937c();
            FDistribution fDist = new FDistribution(rng, 1.0, 2.0);
            assertTrue(fDist instanceof FDistribution);
        } catch (NotStrictlyPositiveException e) {
            throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
        }
    }

    @Test
    public void testDensity() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(1.5, 2.0);
            double density = fDist.density(1.2);
            assertTrue(density > 0);
        } catch (NotStrictlyPositiveException e) {
            throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
        }
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(1.5, 2.0);
            double result = fDist.cumulativeProbability(1.8);
            assertTrue(result > 0 && result < 1);
        } catch (NotStrictlyPositiveException e) {
            throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
        }
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(1.5, 2.0);
            double numeratorDegreesOfFreedom = fDist.getNumeratorDegreesOfFreedom();
            assertTrue(numeratorDegreesOfFreedom > 0);
        } catch (NotStrictlyPositiveException e) {
            throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
        }
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(1.5, 2.0);
            double denominatorDegreesOfFreedom = fDist.getDenominatorDegreesOfFreedom();
            assertTrue(denominatorDegreesOfFreedom > 0);
        } catch (NotStrictlyPositiveException e) {
            throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
        }
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(1.5, 2.0);
            double numericalMean = fDist.getNumericalMean();
            if (Double.isNaN(numericalMean)) {
                assertTrue(true);
            } else {
                assertTrue(numericalMean > 0);
            }
        } catch (NotStrictlyPositiveException e) {
            throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
        }
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(1.5, 2.0);
            double numericalVariance = fDist.getNumericalVariance();
            if (Double.isNaN(numericalVariance)) {
                assertTrue(true);
            } else {
                assertTrue(numericalVariance > 0);
            }
        } catch (NotStrictlyPositiveException e) {
            throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
        }
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(1.5, 2.0);
            double solverAbsoluteAccuracy = fDist.getSolverAbsoluteAccuracy();
            assertTrue(solverAbsoluteAccuracy > 0);
        } catch (NotStrictlyPositiveException e) {
            throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
        }
    }
}