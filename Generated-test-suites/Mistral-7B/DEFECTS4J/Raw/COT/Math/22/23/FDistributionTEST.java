package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;

import java.util.Random;

public class FDistributionTest {

    @ org.junit.Test
    public void testFDistributionConstructor() {
        try {
            new FDistribution(1.0, 1.0);
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        new FDistribution(2.5, 2.5);
    }

    @ org.junit.Test
    public void testDensity() {
        FDistribution fDist = new FDistribution(2.0, 3.0);

        double density = fDist.density(1.5);
        assertEquals("Density calculation is incorrect.", 0.323915234, density, 0.00001);
    }

    @ org.junit.Test
    public void testCumulativeProbability() {
        FDistribution fDist = new FDistribution(2.0, 3.0);

        double cumProb = fDist.cumulativeProbability(0.5);
        assertEquals("Cumulative probability calculation is incorrect.", 0.365832368, cumProb, 0.00001);

        cumProb = fDist.cumulativeProbability(5.0);
        assertEquals("Cumulative probability calculation is incorrect.", 1.0, cumProb, 0.00001);
    }

    @ org.junit.Test
    public void testNumericalMean() {
        FDistribution fDist = new FDistribution(3.0, 4.0);
        double mean = fDist.getNumericalMean();
        assertEquals("Mean calculation is incorrect.", 3.0 / 2, mean, 0.00001);
    }

    @ org.junit.Test
    public void testNumericalVariance() {
        FDistribution fDist = new FDistribution(3.0, 4.0);
        double var = fDist.getNumericalVariance();
        assertEquals("Variance calculation is incorrect.", 9.0 / 12, var, 0.00001);
    }

    @ org.junit.Test
    public void testGetSupportLowerBound() {
        FDistribution fDist = new FDistribution(1.0, 1.0);
        double lowerBound = fDist.getSupportLowerBound();
        assertEquals("Lower bound calculation is incorrect.", 0.0, lowerBound, 0.00001);
    }

    @ org.junit.Test
    public void testGetSupportUpperBound() {
        FDistribution fDist = new FDistribution(1.0, 1.0);
        double upperBound = fDist.getSupportUpperBound();
        assertEquals("Upper bound calculation is incorrect.", Double.POSITIVE_INFINITY, upperBound, 0.00001);
    }

    @ org.junit.Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDist = new FDistribution(1.0, 1.0);
        boolean isInclusive = fDist.isSupportLowerBoundInclusive();
        assertTrue("Support lower bound should be inclusive.", isInclusive);
    }

    @ org.junit.Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDist = new FDistribution(1.0, 1.0);
        boolean isInclusive = fDist.isSupportUpperBoundInclusive();
        assertFalse("Support upper bound should not be inclusive.", isInclusive);
    }

    @ org.junit.Test
    public void testIsSupportConnected() {
        FDistribution fDist = new FDistribution(1.0, 1.0);
        boolean isConnected = fDist.isSupportConnected();
        assertTrue("Support should be connected.", isConnected);
    }

    @ org.junit.Test
    public void testCalculateNumericalVariance() {
        FDistribution fDist = new FDistribution(1.0, 2.0);
        double var = fDist.calculateNumericalVariance();
        assertEquals("Variance calculation is incorrect.", Double.NaN, var, 0.00001);

        fDist = new FDistribution(4.0, 5.0);
        var = fDist.calculateNumericalVariance();
        assertEquals("Variance calculation is incorrect.", 21.0 / 30.0, var, 0.00001);
    }

    @ org.junit.Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution fDist = new FDistribution(1.0, 2.0);
        assertEquals("Solver absolute accuracy should be default.", FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, fDist.getSolverAbsoluteAccuracy(), 0.00001);

        fDist = new FDistribution(1.0, 2.0, 0.001);
        assertEquals("Solver absolute accuracy should be set.", 0.001, fDist.getSolverAbsoluteAccuracy(), 0.00001);
    }
}