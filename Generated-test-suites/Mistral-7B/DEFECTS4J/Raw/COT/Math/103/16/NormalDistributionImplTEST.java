package org.apache.commons.math.distribution.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Test class for NormalDistributionImpl
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor NormalDistributionImpl()
     */
    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor NormalDistributionImpl(double mean, double sd)
     */
    @Test
    public void testNormalDistributionImpl_mean_sd() {
        NormalDistribution distribution = new NormalDistributionImpl(2.5, 0.5);
        assertEquals(2.5, distribution.getMean(), 0.0);
        assertEquals(0.5, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test method getMean()
     */
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0);
        distribution.setMean(4.0);
        assertEquals(4.0, distribution.getMean(), 0.0);
    }

    /**
     * Test method setMean(double mean)
     */
    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0);
        distribution.setMean(-1.5);
        assertEquals(-1.5, distribution.getMean(), 0.0);
        assertThrows(IllegalArgumentException.class, () -> distribution.setMean(-2.0));
    }

    /**
     * Test method getStandardDeviation()
     */
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
        distribution.setStandardDeviation(2.5);
        assertEquals(2.5, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test method setStandardDeviation(double sd)
     */
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
        distribution.setStandardDeviation(0.25);
        assertEquals(0.25, distribution.getStandardDeviation(), 0.0);
        assertThrows(IllegalArgumentException.class, () -> distribution.setStandardDeviation(-1.0));
    }

    /**
     * Test method cumulativeProbability(double x)
     */
    @Test
    public void testCumulativeProbability() {
        NormalDistribution distribution = new NormalDistributionImpl();
        double[] xValues = StatUtils.generateStandardNormal(10000);
        for (double x : xValues) {
            double cdf = 0.5 * (1.0 + Erf.erf((x - distribution.getMean()) / (distribution.getStandardDeviation() * Math.sqrt(2.0))));
            assertEquals(cdf, distribution.cumulativeProbability(x), 1e-10);
        }
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(Double.NaN));
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(Double.NEGATIVE_INFINITY));
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(Double.POSITIVE_INFINITY));
    }

    /**
     * Test method inverseCumulativeProbability(double p)
     */
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistribution distribution = new NormalDistributionImpl();
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double inverseCdf = distribution.inverseCumulativeProbability(p);
            double x = distribution.cumulativeProbability(inverseCdf);
            assertEquals(p, distribution.cumulativeProbability(x), 1e-10);
        }
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(-0.1));
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(1.1));
    }

    /**
     * Test method getDomainLowerBound(double p)
     */
    @Test
    public void testGetDomainLowerBound() {
        NormalDistribution distribution = new NormalDistributionImpl();
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double domainLowerBound = distribution.getDomainLowerBound(p);
            assertEquals(p < 0.5 ? -Double.MAX_VALUE : distribution.getMean(), domainLowerBound, 0.0);
        }
    }

    /**
     * Test method getDomainUpperBound(double p)
     */
    @Test
    public void testGetDomainUpperBound() {
        NormalDistribution distribution = new NormalDistributionImpl();
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double domainUpperBound = distribution.getDomainUpperBound(p);
            assertEquals(p < 0.5 ? distribution.getMean() : Double.MAX_VALUE, domainUpperBound, 0.0);
        }
    }

    /**
     * Test method getInitialDomain(double p)
     */
    @Test
    public void testGetInitialDomain() {
        NormalDistribution distribution = new NormalDistributionImpl();
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double initialDomain = distribution.getInitialDomain(p);
            double mean = distribution.getMean();
            double sd = distribution.getStandardDeviation();
            if (p < 0.5) {
                assertEquals(mean - sd, initialDomain, 0.0);
            } else if (p > 0.5) {
                assertEquals(mean + sd, initialDomain, 0.0);
            } else {
                assertEquals(mean, initialDomain, 0.0);
            }
        }
    }

    /**
     * Test exception handling
     */
    @Test
    public void testExceptionHandling() {
        NormalDistribution distribution = new NormalDistributionImpl();
        assertThrows(IllegalArgumentException.class, () -> distribution.setMean(-2.0));
        assertThrows(IllegalArgumentException.class, () -> distribution.setStandardDeviation(-1.0));
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(Double.NaN));
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(Double.NEGATIVE_INFINITY));
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(Double.POSITIVE_INFINITY));
        assertThrows(MathIllegalArgumentException.class, () -> distribution.inverseCumulativeProbability(-0.1));
        assertThrows(MathIllegalArgumentException.class, () -> distribution.inverseCumulativeProbability(1.1));
        assertThrows(MaxIterationsExceededException.class, () -> distribution.inverseCumulativeProbability(0.5));
    }
}