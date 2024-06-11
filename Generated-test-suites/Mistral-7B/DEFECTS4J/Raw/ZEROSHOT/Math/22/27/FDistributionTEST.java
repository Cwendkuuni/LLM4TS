package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

/**
 * Test class for {@link FDistribution}.
 */
public class FDistributionTest {

    /**
     * Test constructor.
     * @throws NotStrictlyPositiveException if init parameters are not positive
     */
    @Test
    public void testConstruction() throws NotStrictlyPositiveException {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(5.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);

        distribution = new FDistribution(2.0, 4.0, 0.001);
        assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(0.001, distribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test method density.
     */
    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(3.0, 5.0);

        double value = 1.0;
        double density = distribution.density(value);
        assertTrue(Double.isFinite(density));
        double eps = 1e-6;
        assertEquals(2.14098360679775, density, eps);

        value = 2.0;
        density = distribution.density(value);
        assertTrue(Double.isFinite(density));
        assertEquals(0.88079707265811, density, eps);

        value = 0.5;
        density = distribution.density(value);
        assertTrue(Double.isFinite(density));
        assertEquals(0.16048635857354, density, eps);
    }

    /**
     * Test method cumulativeProbability.
     */
    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(3.0, 5.0);

        double value = 1.0;
        double cdf = distribution.cumulativeProbability(value);
        assertEquals(0.84134474604214, cdf, 1e-6);

        value = 2.0;
        cdf = distribution.cumulativeProbability(value);
        assertEquals(0.95028862987726, cdf, 1e-6);

        value = 0.5;
        cdf = distribution.cumulativeProbability(value);
        assertEquals(0.11223761824767, cdf, 1e-6);
    }

    /**
     * Test method getNumeratorDegreesOfFreedom.
     */
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);

        distribution = new FDistribution(2.0, 4.0, 0.001);
        assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test method getDenominatorDegreesOfFreedom.
     */
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        assertEquals(5.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);

        distribution = new FDistribution(2.0, 4.0, 0.001);
        assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test method getSolverAbsoluteAccuracy.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        assertEquals(0.0, distribution.getSolverAbsoluteAccuracy(), 0.0);

        distribution = new FDistribution(2.0, 4.0, 0.001);
        assertEquals(0.001, distribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test method getNumericalMean.
     */
    @Test
    public void testGetNumericalMean() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        assertEquals(3.25, distribution.getNumericalMean(), 1e-6);

        distribution = new FDistribution(2.0, 4.0);
        assertEquals(Double.NaN, distribution.getNumericalMean(), 0.0);
    }

    /**
     * Test method calculateNumericalVariance.
     */
    @Test
    public void testCalculateNumericalVariance() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double variance = distribution.calculateNumericalVariance();
        assertEquals(1.42263284204747, variance, 1e-6);

        distribution = new FDistribution(2.0, 4.0);
        assertEquals(Double.NaN, distribution.calculateNumericalVariance(), 0.0);
    }

    /**
     * Test method getNumericalVariance.
     */
    @Test
    public void testGetNumericalVarianceWithCalculatedValue() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        distribution.calculateNumericalVariance();
        double variance = distribution.getNumericalVariance();
        assertEquals(1.42263284204747, variance, 1e-6);

        distribution = new FDistribution(2.0, 4.0);
        distribution.calculateNumericalVariance();
        assertEquals(Double.NaN, distribution.getNumericalVariance(), 0.0);
    }

    /**
     * Test method getSupportLowerBound.
     */
    @Test
    public void testGetSupportLowerBound() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
    }

    /**
     * Test method getSupportUpperBound.
     */
    @Test
    public void testGetSupportUpperBound() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), 0.0);
    }

    /**
     * Test method isSupportLowerBoundInclusive.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    /**
     * Test method isSupportUpperBoundInclusive.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    /**
     * Test method isSupportConnected.
     */
    @Test
    public void testIsSupportConnected() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        assertTrue(distribution.isSupportConnected());
    }

    /**
     * Test method sample.
     */
    @Test
    public void testSample() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double sample = distribution.sample();
        assertTrue(sample > 0.0);
    }

    /**
     * Test method sample.
     * @param args unused
     */
    public static void main(String[] args) {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        System.out.println(distribution.sample());
        System.out.println(distribution.sample());
        System.out.println(distribution.sample());
    }
}