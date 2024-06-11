package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

/**
 * Test cases for FDistribution class.
 */
public class FDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidParameters() {
        new FDistribution(-1.0, 2.0);
    }

    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double result = distribution.density(1.5);
        assertEquals(0.115433881685915, result, 0.00001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double result = distribution.cumulativeProbability(0.5);
        assertEquals(0.374156396785493, result, 0.00001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double result = distribution.getNumeratorDegreesOfFreedom();
        assertEquals(3.0, result, 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double result = distribution.getDenominatorDegreesOfFreedom();
        assertEquals(5.0, result, 0.0);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double result = distribution.getSolverAbsoluteAccuracy();
        assertEquals(1e-9, result, 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double result = distribution.getNumericalMean();
        assertEquals(3.2, result, 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double result = distribution.getNumericalVariance();
        assertEquals(1.21857997514022, result, 0.0);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double result = distribution.getSupportLowerBound();
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double result = distribution.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        boolean result = distribution.isSupportLowerBoundInclusive();
        assertTrue(result);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        boolean result = distribution.isSupportUpperBoundInclusive();
        assertFalse(result);
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        boolean result = distribution.isSupportConnected();
        assertTrue(result);
    }

    @Test
    public void testGenerateRandomSample() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        RandomGenerator rng = new Well19937c();

        double sample = distribution.inverseCumulativeProbability(rng.nextDouble());
        assertEquals(1.1833133057153, sample, 0.01);
    }
}