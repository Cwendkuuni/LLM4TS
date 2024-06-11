package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertAlmostEquals;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;

import java.util.Random;

public class FDistributionTest {

    @Test
    public void testFDistributionCreation() throws NotStrictlyPositiveException {
        FDistribution fDist = new FDistribution(2.5, 3.5);
        assertTrue(fDist instanceof FDistribution);
        assertEquals(2.5, fDist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.5, fDist.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(1.0e-9, fDist.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testFDistributionCreationWithRandomGenerator() throws NotStrictlyPositiveException {
        RandomGenerator rng = new Random();
        FDistribution fDist = new FDistribution(rng, 2.5, 3.5);
        assertTrue(fDist instanceof FDistribution);
        assertEquals(2.5, fDist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.5, fDist.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(1.0e-9, fDist.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(-2.5, 3.5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDenominatorDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(2.5, -3.5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeInverseCumulativeProbabilityAccuracy() {
        new FDistribution(2.5, 3.5, -1.0e-9);
    }

    @Test
    public void testDensity() {
        double x;
        FDistribution fDist = new FDistribution(2.5, 3.5);

        x = 1.0;
        double density = fDist.density(x);
        assertTrue(Double.isFinite(density));
        assertAlmostEquals(1.1216263494824678, density, 5);

        x = 2.0;
        density = fDist.density(x);
        assertTrue(Double.isFinite(density));
        assertAlmostEquals(0.880506393543823, density, 5);

        x = 3.0;
        density = fDist.density(x);
        assertTrue(Double.isFinite(density));
        assertAlmostEquals(0.4983583106838441, density, 5);

        x = 4.0;
        density = fDist.density(x);
        assertTrue(Double.isFinite(density));
        assertAlmostEquals(0.2400144243476477, density, 5);

        x = 5.0;
        density = fDist.density(x);
        assertTrue(Double.isFinite(density));
        assertAlmostEquals(0.1113266583156259, density, 5);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDist = new FDistribution(2.5, 3.5);

        double x;
        double cumulativeProbability;

        x = 0.0;
        cumulativeProbability = fDist.cumulativeProbability(x);
        assertEquals(0.0, cumulativeProbability, 1e-12);

        x = 1.0;
        cumulativeProbability = fDist.cumulativeProbability(x);
        assertEquals(0.04438623544053972, cumulativeProbability, 1e-12);

        x = 2.0;
        cumulativeProbability = fDist.cumulativeProbability(x);
        assertEquals(0.1936058506538661, cumulativeProbability, 1e-12);

        x = 3.0;
        cumulativeProbability = fDist.cumulativeProbability(x);
        assertEquals(0.3662581019901208, cumulativeProbability, 1e-12);

        x = 4.0;
        cumulativeProbability = fDist.cumulativeProbability(x);
        assertEquals(0.531783365978035, cumulativeProbability, 1e-12);

        x = 5.0;
        cumulativeProbability = fDist.cumulativeProbability(x);
        assertEquals(0.6874794825829637, cumulativeProbability, 1e-12);

        x = 10.0;
        cumulativeProbability = fDist.cumulativeProbability(x);
        assertEquals(0.9999999999174482, cumulativeProbability, 1e-12);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(2.5, 3.5);
        assertEquals(2.5, fDist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(2.5, 3.5);
        assertEquals(3.5, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution fDist = new FDistribution(2.5, 3.5);
        assertEquals(1.0e-9, fDist.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDist = new FDistribution(2.5, 3.5);
        assertEquals(3.5 / (3.5 - 2.0), fDist.getNumericalMean(), 1e-12);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDist = new FDistribution(2.5, 3.5);
        double numericalVariance = fDist.getNumericalVariance();

        assertTrue(Double.isFinite(numericalVariance));
        assertEquals(1.4511760060984072, numericalVariance, 1e-12);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDist = new FDistribution(2.5, 3.5);
        assertEquals(0.0, fDist.getSupportLowerBound(), 1e-12);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDist = new FDistribution(2.5, 3.5);
        assertEquals(Double.POSITIVE_INFINITY, fDist.getSupportUpperBound(), 1e-12);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDist = new FDistribution(2.5, 3.5);
        assertTrue(fDist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDist = new FDistribution(2.5, 3.5);
        assertFalse(fDist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDist = new FDistribution(2.5, 3.5);
        assertTrue(fDist.isSupportConnected());
    }
}