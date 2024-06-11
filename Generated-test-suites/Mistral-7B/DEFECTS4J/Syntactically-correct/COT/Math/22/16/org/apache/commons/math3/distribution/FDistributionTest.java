package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the FDistribution class.
 */
public class FDistributionTest {

    private FDistribution dist;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        dist = new FDistribution(2.0, 3.0);
    }

    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        FDistribution fDist1 = new FDistribution(1.0, 1.0);
        FDistribution fDist2 = new FDistribution(3.0, 2.0);
        FDistribution fDist3 = new FDistribution(4.0, 4.0);
        FDistribution fDist4 = new FDistribution(1.5, 1.5);

        assertEquals(1.0, fDist1.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(1.0, fDist1.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(1.0, fDist1.getSolverAbsoluteAccuracy(), 0.0);

        assertEquals(3.0, fDist2.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(2.0, fDist2.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(1.0e-9, fDist2.getSolverAbsoluteAccuracy(), 1e-10);

        assertEquals(4.0, fDist3.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, fDist3.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(1.0e-9, fDist3.getSolverAbsoluteAccuracy(), 1e-10);

        assertEquals(1.5, fDist4.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(1.5, fDist4.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(1.0e-9, fDist4.getSolverAbsoluteAccuracy(), 1e-10);
    }

    @Test
    public void testDensity() {
        final double accuracy = 1e-6;
        double x;

        x = 0.5;
        double density = dist.density(x);
        double exact = Beta.density(getNumeratorDegreesOfFreedom() / 2,
                getDenominatorDegreesOfFreedom() / 2, x);
        assertTrue(Precision.isClose(density, exact, accuracy));

        x = getSupportUpperBound();
        density = dist.density(x);
        assertEquals(0.0, density, accuracy);

        x = FastMath.nextUp(getSupportUpperBound());
        density = dist.density(x);
        assertEquals(Double.NEGATIVE_INFINITY, density, accuracy);

        x = -0.5;
        density = dist.density(x);
        assertEquals(0.0, density, accuracy);

        x = FastMath.nextDown(0.0);
        density = dist.density(x);
        assertEquals(Double.NEGATIVE_INFINITY, density, accuracy);
    }

    @Test
    public void testCumulativeProbability() {
        final double accuracy = 1e-6;
        double x;

        x = 0.0;
        double cumulativeProbability = dist.cumulativeProbability(x);
        double exact = 0.0;
        assertTrue(Precision.isClose(cumulativeProbability, exact, accuracy));

        x = 2.5;
        cumulativeProbability = dist.cumulativeProbability(x);
        exact = 1.0;
        assertTrue(Precision.isClose(cumulativeProbability, exact, accuracy));

        x = FastMath.nextUp(getSupportUpperBound());
        cumulativeProbability = dist.cumulativeProbability(x);
        assertEquals(1.0, cumulativeProbability, accuracy);

        x = FastMath.nextDown(getSupportLowerBound());
        cumulativeProbability = dist.cumulativeProbability(x);
        assertEquals(0.0, cumulativeProbability, accuracy);

        x = -1.0;
        cumulativeProbability = dist.cumulativeProbability(x);
        assertEquals(0.0, cumulativeProbability, accuracy);
    }

    @Test
    public void testNumericalVariance() {
        final double accuracy = 1e-6;

        double variance = dist.getNumericalVariance();
        assertEquals(1.5, variance, accuracy);

        dist = new FDistribution(2.0, 4.0);
        variance = dist.getNumericalVariance();
        assertEquals(2.15, variance, accuracy);

        dist = new FDistribution(5.0, 10.0);
        variance = dist.getNumericalVariance();
        assertEquals(14.583333333333333, variance, accuracy);

        dist = new FDistribution(-1.0, 2.0);
        try {
            dist.getNumericalVariance();
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        dist = new FDistribution(1.0, -2.0);
        try {
            dist.getNumericalVariance();
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        dist = new FDistribution(0.0, 2.0);
        try {
            dist.getNumericalVariance();
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testGetSupportBounds() {
        assertTrue(isSupportLowerBoundInclusive());
        assertTrue(isSupportUpperBoundInclusive());

        double lowerBound = getSupportLowerBound();
        assertEquals(0.0, lowerBound, 1e-10);

        double upperBound = getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 1e-10);

        dist = new FDistribution(2.0, 3.0);
        lowerBound = dist.getSupportLowerBound();
        assertEquals(0.0, lowerBound, 1e-10);

        upperBound = dist.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 1e-10);

        dist = new FDistribution(1.0, 1.0);
        lowerBound = dist.getSupportLowerBound();
        assertEquals(0.0, lowerBound, 1e-10);

        upperBound = dist.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 1e-10);
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(isSupportConnected());
    }
}