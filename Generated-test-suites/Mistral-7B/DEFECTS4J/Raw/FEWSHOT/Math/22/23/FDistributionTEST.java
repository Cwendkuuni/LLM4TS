package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testFDistribution() {
        try {
            double numeratorDegreesOfFreedom = 3.0;
            double denominatorDegreesOfFreedom = 5.0;
            FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
            assertEquals(numeratorDegreesOfFreedom, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
            assertEquals(denominatorDegreesOfFreedom, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
        } catch (NotStrictlyPositiveException e) {
            fail("FDistribution should be instantiated without throwing an exception.");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeNumeratorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = -3.0;
        double denominatorDegreesOfFreedom = 5.0;
        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDenominatorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = -3.0;
        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    @Test
    public void testDensity() {
        try {
            double numeratorDegreesOfFreedom = 3.0;
            double denominatorDegreesOfFreedom = 5.0;
            FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
            double x = 1.5;
            double expectedDensity = 0.272239534221717;
            double actualDensity = fDistribution.density(x);
            assertEquals(expectedDensity, actualDensity, 1e-6);
        } catch (NotStrictlyPositiveException e) {
            fail("FDistribution should be instantiated without throwing an exception.");
        }
    }

    @Test
    public void testCumulativeProbability() {
        try {
            double numeratorDegreesOfFreedom = 3.0;
            double denominatorDegreesOfFreedom = 5.0;
            FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
            double x = 1.5;
            double expectedCumulativeProbability = 0.921564145983654;
            double actualCumulativeProbability = fDistribution.cumulativeProbability(x);
            assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 1e-6);
        } catch (NotStrictlyPositiveException e) {
            fail("FDistribution should be instantiated without throwing an exception.");
        }
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        try {
            double numeratorDegreesOfFreedom = 3.0;
            double denominatorDegreesOfFreedom = 5.0;
            FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
            double expectedNumeratorDegreesOfFreedom = 3.0;
            double actualNumeratorDegreesOfFreedom = fDistribution.getNumeratorDegreesOfFreedom();
            assertEquals(expectedNumeratorDegreesOfFreedom, actualNumeratorDegreesOfFreedom, 0.0);
        } catch (NotStrictlyPositiveException e) {
            fail("FDistribution should be instantiated without throwing an exception.");
        }
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        try {
            double numeratorDegreesOfFreedom = 3.0;
            double denominatorDegreesOfFreedom = 5.0;
            FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
            double expectedDenominatorDegreesOfFreedom = 5.0;
            double actualDenominatorDegreesOfFreedom = fDistribution.getDenominatorDegreesOfFreedom();
            assertEquals(expectedDenominatorDegreesOfFreedom, actualDenominatorDegreesOfFreedom, 0.0);
        } catch (NotStrictlyPositiveException e) {
            fail("FDistribution should be instantiated without throwing an exception.");
        }
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        try {
            double numeratorDegreesOfFreedom = 3.0;
            double denominatorDegreesOfFreedom = 5.0;
            FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
            double expectedSolverAbsoluteAccuracy = Double.NaN;
            double actualSolverAbsoluteAccuracy = fDistribution.getSolverAbsoluteAccuracy();
            assertEquals(expectedSolverAbsoluteAccuracy, actualSolverAbsoluteAccuracy, 0.0);
        } catch (NotStrictlyPositiveException e) {
            fail("FDistribution should be instantiated without throwing an exception.");
        }
    }

    @Test
    public void testGetNumericalMean() {
        try {
            double numeratorDegreesOfFreedom = 3.0;
            double denominatorDegreesOfFreedom = 5.0;
            FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
            double expectedNumericalMean = 1.5;
            double actualNumericalMean = fDistribution.getNumericalMean();
            assertEquals(expectedNumericalMean, actualNumericalMean, 0.0);
        } catch (NotStrictlyPositiveException e) {
            fail("FDistribution should be instantiated without throwing an exception.");
        }
    }

    @Test
    public void testGetNumericalVariance() {
        try {
            double numeratorDegreesOfFreedom = 3.0;
            double denominatorDegreesOfFreedom = 5.0;
            FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
            double expectedNumericalVariance = 1.2095103564664;
            double actualNumericalVariance = fDistribution.getNumericalVariance();
            assertEquals(expectedNumericalVariance, actualNumericalVariance, 1e-6);
        } catch (NotStrictlyPositiveException e) {
            fail("FDistribution should be instantiated without throwing an exception.");
        }
    }

    @Test
    public void testGetSupportLowerBound() {
        try {
            double numeratorDegreesOfFreedom = 3.0;
            double denominatorDegreesOfFreedom = 5.0;
            FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
            double expectedSupportLowerBound = 0.0;
            double actualSupportLowerBound = fDistribution.getSupportLowerBound();
            assertEquals(expectedSupportLowerBound, actualSupportLowerBound, 0.0);
        } catch (NotStrictlyPositiveException e) {
            fail("FDistribution should be instantiated without throwing an exception.");
        }
    }

    @Test
    public void testGetSupportUpperBound() {
        try {
            double numeratorDegreesOfFreedom = 3.0;
            double denominatorDegreesOfFreedom = 5.0;
            FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
            double expectedSupportUpperBound = Double.POSITIVE_INFINITY;
            double actualSupportUpperBound = fDistribution.getSupportUpperBound();
            assertEquals(expectedSupportUpperBound, actualSupportUpperBound, 0.0);
        } catch (NotStrictlyPositiveException e) {
            fail("FDistribution should be instantiated without throwing an exception.");
        }
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        try {
            double numeratorDegreesOfFreedom = 3.0;
            double denominatorDegreesOfFreedom = 5.0;
            FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
            boolean expectedIsSupportLowerBoundInclusive = true;
            boolean actualIsSupportLowerBoundInclusive = fDistribution.isSupportLowerBoundInclusive();
            assertEquals(expectedIsSupportLowerBoundInclusive, actualIsSupportLowerBoundInclusive);
        } catch (NotStrictlyPositiveException e) {
            fail("FDistribution should be instantiated without throwing an exception.");
        }
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        try {
            double numeratorDegreesOfFreedom = 3.0;
            double denominatorDegreesOfFreedom = 5.0;
            FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
            boolean expectedIsSupportUpperBoundInclusive = false;
            boolean actualIsSupportUpperBoundInclusive = fDistribution.isSupportUpperBoundInclusive();
            assertEquals(expectedIsSupportUpperBoundInclusive, actualIsSupportUpperBoundInclusive);
        } catch (NotStrictlyPositiveException e) {
            fail("FDistribution should be instantiated without throwing an exception.");
        }
    }

    @Test
    public void testIsSupportConnected() {
        try {
            double numeratorDegreesOfFreedom = 3.0;
            double denominatorDegreesOfFreedom = 5.0;
            FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
            boolean expectedIsSupportConnected = true;
            boolean actualIsSupportConnected = fDistribution.isSupportConnected();
            assertEquals(expectedIsSupportConnected, actualIsSupportConnected);
        } catch (NotStrictlyPositiveException e) {
            fail("FDistribution should be instantiated without throwing an exception.");
        }
    }
}