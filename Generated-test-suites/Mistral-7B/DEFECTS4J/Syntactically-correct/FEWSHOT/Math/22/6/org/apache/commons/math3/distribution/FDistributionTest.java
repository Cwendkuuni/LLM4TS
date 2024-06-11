package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.apache.commons.math3.util.Precision.abs;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class FDistributionTest {

    @Test
    public void testCreateFDistribution() {
        try {
            FDistribution fd = new FDistribution(2.0, 4.0);
            assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
            assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Non positive degrees of freedom should not be accepted.");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeDegreesOfFreedom() {
        new FDistribution(-1.0, 4.0);
    }

    @Test
    public void testCreateFDistributionWithInverseCumulativeProbabilityAccuracy() {
        try {
            FDistribution fd = new FDistribution(2.0, 4.0, 1.0e-9);
            assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
            assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
            assertEquals(1.0e-9, fd.getSolverAbsoluteAccuracy(), 1.0e-12);
        } catch (NotStrictlyPositiveException e) {
            fail("Non positive degrees of freedom should not be accepted.");
        }
    }

    @Test
    public void testCreateFDistributionWithRandomGenerator() {
        try {
            RandomGenerator rng = new Well19937c();
            FDistribution fd = new FDistribution(rng, 2.0, 4.0);
            assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
            assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
            assertNull(fd.getRng());
        } catch (NotStrictlyPositiveException e) {
            fail("Non positive degrees of freedom should not be accepted.");
        }
    }

    @Test
    public void testCreateFDistributionWithRandomGeneratorAndAccuracy() {
        try {
            RandomGenerator rng = new Well19937c();
            FDistribution fd = new FDistribution(rng, 2.0, 4.0, 1.0e-9);
            assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
            assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
            assertEquals(1.0e-9, fd.getSolverAbsoluteAccuracy(), 1.0e-12);
            assertTrue(fd.getRng() instanceof Well19937c);
        } catch (NotStrictlyPositiveException e) {
            fail("Non positive degrees of freedom should not be accepted.");
        }
    }

    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        double x = 1.0;
        double density = fd.density(x);
        double expectedDensity = 0.13211122081263489;
        assertEquals(expectedDensity, density, 1.0e-10);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        double x = 1.0;
        double cdf = fd.cumulativeProbability(x);
        double expectedCDF = 0.6794895457088794;
        assertEquals(expectedCDF, cdf, 1.0e-10);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        double degreesOfFreedom = fd.getNumeratorDegreesOfFreedom();
        assertEquals(2.0, degreesOfFreedom, 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        double degreesOfFreedom = fd.getDenominatorDegreesOfFreedom();
        assertEquals(4.0, degreesOfFraction, 0.0);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        double solverAbsoluteAccuracy = fd.getSolverAbsoluteAccuracy();
        assertEquals(1.0e-9, solverAbsoluteAccuracy, 1.0e-10);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        double numericalMean = fd.getNumericalMean();
        assertEquals(2.5, numericalMean, 1.0e-12);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        double numericalVariance = fd.getNumericalVariance();
        double expectedVariance = 1.5;
        assertEquals(expectedVariance, numericalVariance, 1.0e-12);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        double lowerBound = fd.getSupportLowerBound();
        assertEquals(0.0, lowerBound, 1.0e-12);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        double upperBound = fd.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 1.0e-12);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        boolean inclusive = fd.isSupportLowerBoundInclusive();
        assertTrue(inclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        boolean inclusive = fd.isSupportUpperBoundInclusive();
        assertFalse(inclusive);
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        boolean connected = fd.isSupportConnected();
        assertTrue(connected);
    }
}