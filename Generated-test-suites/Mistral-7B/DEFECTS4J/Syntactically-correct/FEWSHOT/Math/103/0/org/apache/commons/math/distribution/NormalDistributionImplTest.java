package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, dist.getMean(), 0.01);
        assertEquals(1.0, dist.getStandardDeviation(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setStandardDeviation(-1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.1;
        double cdf = dist.cumulativeProbability(p);
        double expectedCdf = 0.5 * (1.0 + Erf.erf(p / (Math.sqrt(2.0) * 1.0)));
        assertEquals(expectedCdf, cdf, 0.01);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double x = 10.0;
        dist.cumulativeProbability(x);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.1;
        double x = dist.inverseCumulativeProbability(p);
        double expectedX = Math.sqrt(2.0) * Erf.erfinv(2 * p - 1.0) + 0.0;
        assertEquals(expectedX, x, 0.01);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidProbability()
            throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = -0.1;
        dist.inverseCumulativeProbability(p);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testInverseCumulativeProbabilityConvergenceException()
            throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 3.0;
        dist.inverseCumulativeProbability(p);
    }

    @Test
    public void testSetMean() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setMean(3.0);
        assertEquals(3.0, dist.getMean(), 0.01);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setStandardDeviation(2.0);
        assertEquals(2.0, dist.getStandardDeviation(), 0.01);
    }

    @Test
    public void testGetMean() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.01);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(1.0, dist.getStandardDeviation(), 0.01);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfMeanRange() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double x = 100.0;
        dist.cumulativeProbability(x);
    }

    @Test
    public void testCumulativeProbabilityWithMeanAndStandardDeviation()
            throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        // Test extreme values
        double p1 = 0.0;
        double x1 = dist.inverseCumulativeProbability(p1);
        double cdf1 = dist.cumulativeProbability(x1);
        assertTrue(cdf1 > p1);

        double p2 = 1.0;
        double x2 = dist.inverseCumulativeProbability(p2);
        double cdf2 = dist.cumulativeProbability(x2);
        assertTrue(cdf2 < p2);

        // Test values close to mean
        double p3 = 0.5;
        double x3 = dist.inverseCumulativeProbability(p3);
        double cdf3 = dist.cumulativeProbability(x3);
        assertEquals(p3, cdf3, 0.01);

        // Test values further from mean
        double p4 = 0.1;
        double x4 = dist.inverseCumulativeProbability(p4);
        double cdf4 = dist.cumulativeProbability(x4);
        assertTrue(cdf4 < p4);

        double p5 = 0.9;
        double x5 = dist.inverseCumulativeProbability(p5);
        double cdf5 = dist.cumulativeProbability(x5);
        assertTrue(cdf5 > p5);

        // Test values far from mean
        double p6 = 0.01;
        double x6 = dist.inverseCumulativeProbability(p6);
        double cdf6 = dist.cumulativeProbability(x6);
        assertTrue(cdf6 < p6);
    }
}