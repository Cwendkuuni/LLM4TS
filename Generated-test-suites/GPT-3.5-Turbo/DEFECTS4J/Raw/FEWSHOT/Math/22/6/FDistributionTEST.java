package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class FDistributionTest {
    private static final double EPSILON = 1e-9;
    private final double numeratorDegreesOfFreedom = 2.0;
    private final double denominatorDegreesOfFreedom = 3.0;
    private final double inverseCumAccuracy = 0.001;

    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        // Test specific values
        assertEquals(0.020780209719014594, distribution.density(0.5), EPSILON);
        assertEquals(0.08838834764831844, distribution.density(1.0), EPSILON);
        assertEquals(0.04382784760012266, distribution.density(2.5), EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        // Test specific values
        assertEquals(0.25, distribution.cumulativeProbability(0.5), EPSILON);
        assertEquals(0.4166666666666667, distribution.cumulativeProbability(1.0), EPSILON);
        assertEquals(0.5693602257378261, distribution.cumulativeProbability(2.5), EPSILON);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        assertEquals(numeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), EPSILON);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        assertEquals(denominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), EPSILON);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom, inverseCumAccuracy);

        assertEquals(inverseCumAccuracy, distribution.getSolverAbsoluteAccuracy(), EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        // Test specific values
        assertEquals(Double.NaN, distribution.getNumericalMean(), EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        // Test specific values
        assertEquals(Double.NaN, distribution.getNumericalVariance(), EPSILON);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        assertEquals(0.0, distribution.getSupportLowerBound(), EPSILON);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), EPSILON);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        assertTrue(distribution.isSupportConnected());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeNumeratorDegreesOfFreedom() {
        new FDistribution(-1.0, denominatorDegreesOfFreedom);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeDenominatorDegreesOfFreedom() {
        new FDistribution(numeratorDegreesOfFreedom, -1.0);
    }
}