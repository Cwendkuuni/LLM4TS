package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link FDistribution} class.
 */
public class FDistributionTest {

    private FDistribution dist;
    private static final double EPS = 1e-10;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        dist = new FDistribution(new Well19937c(), 3.0, 5.0);
    }

    @Test
    public void testDensity() {
        double x = 1.5;
        double density = dist.density(x);
        assertEquals(density, 0.209839731289674, EPS);
    }

    @Test
    public void testCumulativeProbability() {
        double x = 1.5;
        double cdf = dist.cumulativeProbability(x);
        assertEquals(cdf, 0.918280236211534, EPS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCumulativeProbabilityNegativeX() {
        dist.cumulativeProbability(-1.0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        assertEquals(dist.getNumeratorDegreesOfFreedom(), 3.0, EPS);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        assertEquals(dist.getDenominatorDegreesOfFreedom(), 5.0, EPS);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        assertEquals(dist.getSolverAbsoluteAccuracy(), 0.0, EPS);
    }

    @Test
    public void testGetNumericalMean() {
        double mean = dist.getNumericalMean();
        assertEquals(mean, 3.0 / 2.0, EPS);
    }

    @Test
    public void testGetNumericalVariance() {
        double variance = dist.getNumericalVariance();
        assertEquals(variance, 3.0 / (12.0 * 4.0), EPS);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNumericalVarianceTwice() {
        dist.getNumericalVariance();
        dist.getNumericalVariance();
    }

    @Test
    public void testSupportBounds() {
        assertTrue(dist.isSupportLowerBoundInclusive());
        assertFalse(dist.isSupportUpperBoundInclusive());
        assertEquals(dist.getSupportLowerBound(), 0.0, EPS);
        assertEquals(dist.getSupportUpperBound(), Double.POSITIVE_INFINITY, EPS);
    }

    @Test
    public void testSupportConnected() {
        assertTrue(dist.isSupportConnected());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeNumeratorDF() throws NotStrictlyPositiveException {
        dist = new FDistribution(new Well19937c(), -1.0, 5.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeDenominatorDF() throws NotStrictlyPositiveException {
        dist = new FDistribution(new Well19937c(), 3.0, -1.0);
    }
}