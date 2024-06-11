package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathException;

import org.junit.Test;

/**
 * Test class for NormalDistributionImpl.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor NormalDistributionImpl().
     */
    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor NormalDistributionImpl(double mean, double sd).
     */
    @Test
    public void testNormalDistributionImplWithMeanAndSD() {
        NormalDistribution dist = new NormalDistributionImpl(1.5, 2.5);
        assertEquals(1.5, dist.getMean(), 0.0);
        assertEquals(2.5, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test method getMean().
     */
    @Test
    public void testGetMean() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setMean(3.5);
        assertEquals(3.5, dist.getMean(), 0.0);
    }

    /**
     * Test method setMean(double mean).
     */
    @Test
    public void testSetMean() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setMean(-2.5);
        assertEquals(-2.5, dist.getMean(), 0.0);
    }

    /**
     * Test method getStandardDeviation().
     */
    @Test
    public void testGetStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setStandardDeviation(5.5);
        assertEquals(5.5, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test method setStandardDeviation(double sd).
     */
    @Test
    public void testSetStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setStandardDeviation(0.5);
        assertEquals(0.5, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test method cumulativeProbability(double x).
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double result = dist.cumulativeProbability(0.5);
        assertEquals(0.682689462137, result, 0.00000000001);

        dist.setMean(1.5);
        dist.setStandardDeviation(2.5);
        result = dist.cumulativeProbability(3.5);
        assertEquals(0.997371108857, result, 0.00000000001);
    }

    /**
     * Test method inverseCumulativeProbability(double p).
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.3;
        double result = dist.inverseCumulativeProbability(p);
        assertEquals(-0.408248290464, result, 0.00000000001);

        dist.setMean(1.5);
        dist.setStandardDeviation(2.5);
        p = 0.95;
        result = dist.inverseCumulativeProbability(p);
        assertEquals(3.04385133343, result, 0.00000000001);
    }

    /**
     * Test method getDomainLowerBound(double p).
     */
    @Test
    public void testGetDomainLowerBound() {
        NormalDistribution dist = new NormalDistributionImpl();
        double p = 0.1;
        double result = dist.getDomainLowerBound(p);
        assertEquals(-Double.MAX_VALUE, result, 0.0);

        dist.setMean(1.5);
        dist.setStandardDeviation(2.5);
        p = 0.6;
        result = dist.getDomainLowerBound(p);
        assertEquals(-1.86986342818, result, 0.0);
    }

    /**
     * Test method getDomainUpperBound(double p).
     */
    @Test
    public void testGetDomainUpperBound() {
        NormalDistribution dist = new NormalDistributionImpl();
        double p = 0.9;
        double result = dist.getDomainUpperBound(p);
        assertEquals(Double.MAX_VALUE, result, 0.0);

        dist.setMean(1.5);
        dist.setStandardDeviation(2.5);
        p = 0.8;
        result = dist.getDomainUpperBound(p);
        assertEquals(4.35295265796, result, 0.0);
    }

    /**
     * Test method getInitialDomain(double p).
     */
    @Test
    public void testGetInitialDomain() {
        NormalDistribution dist = new NormalDistributionImpl();
        double p = 0.5;
        double result = dist.getInitialDomain(p);
        assertEquals(0.0, result, 0.0);

        dist.setMean(1.5);
        dist.setStandardDeviation(2.5);
        p = 0.6;
        result = dist.getInitialDomain(p);
        assertEquals(0.5, result, 0.0);
    }

    /**
     * Test method cumulativeProbability(double x) with negative standard
     * deviation.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegativeStandardDeviation()
            throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, -1);
        dist.cumulativeProbability(0.5);
    }

    /**
     * Test method cumulativeProbability(double x) with x far from the mean.
     */
    @Test
    public void testCumulativeProbabilityXFarFromMean() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double result = dist.cumulativeProbability(10.0);
        assertEquals(0.0, result, 0.0);

        dist.setMean(1.5);
        dist.setStandardDeviation(2.5);
        result = dist.cumulativeProbability(5.0);
        assertEquals(0.002536898356, result, 0.00000000001);
    }

    /**
     * Test method inverseCumulativeProbability(double p) with p = 0.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityPZero() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.inverseCumulativeProbability(0.0);
    }

    /**
     * Test method inverseCumulativeProbability(double p) with p > 1.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityPGreaterThanOne()
            throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.inverseCumulativeProbability(1.1);
    }

    /**
     * Test method inverseCumulativeProbability(double p) with p = 1.
     */
    @Test
    public void testInverseCumulativeProbabilityPEqualToOne() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();
        double result = dist.inverseCumulativeProbability(1.0);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
    }

    /**
     * Test method inverseCumulativeProbability(double p) with p < 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityPNegative() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.inverseCumulativeProbability(-0.1);
    }

    /**
     * Test method setStandardDeviation(double sd) with sd <= 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationSDLessOrEqualZero() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setStandardDeviation(0.0);
    }

    /**
     * Test method cumulativeProbability(double x) with max iterations
     * exceeded.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testCumulativeProbabilityMaxIterationsExceeded()
            throws MathException, MaxIterationsExceededException {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.cumulativeProbability(Double.POSITIVE_INFINITY);
    }
}