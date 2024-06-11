package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

/**
 * Tests for NormalDistributionImpl class
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor NormalDistributionImpl with given mean and standard deviation.
     */
    @Test
    public void testNormalDistributionImplWithMeanAndStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(2.5, 1.2);
        assertEquals(2.5, distribution.getMean(), 0.001);
        assertEquals(1.2, distribution.getStandardDeviation(), 0.001);
    }

    /**
     * Test constructor NormalDistributionImpl with default mean and standard deviation.
     */
    @Test
    public void testNormalDistributionImplWithDefaultMeanAndStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.001);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
    }

    /**
     * Test setMean method.
     */
    @Test
    public void testSetMean() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setMean(3.14);
        assertEquals(3.14, distribution.getMean(), 0.001);
    }

    /**
     * Test setStandardDeviation method with positive value.
     */
    @Test
    public void testSetStandardDeviationWithPositiveValue() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(2.71);
        assertEquals(2.71, distribution.getStandardDeviation(), 0.001);
    }

    /**
     * Test setStandardDeviation method with non-positive value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationWithNonPositiveValue() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(-1.2);
    }

    /**
     * Test cumulativeProbability method for different values of x.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double[] x = {-3.0, -1.5, 0.0, 1.5, 3.0};
        double[] expected = {0.00003086, 0.15485963, 0.5, 0.84514037, 0.9973665};
        for (int i = 0; i < x.length; i++) {
            assertEquals(expected[i], distribution.cumulativeProbability(x[i]), 0.001);
        }

        NormalDistribution distribution2 = new NormalDistributionImpl(1.5, 0.5);
        x = new double[]{-5.0, -2.5, 1.5, 2.5, 5.0};
        expected = new double[] {0.000000011, 0.052757725, 0.5, 0.947242275, 0.999999989};
        for (int i = 0; i < x.length; i++) {
            assertEquals(expected[i], distribution2.cumulativeProbability(x[i]), 0.001);
        }
    }

    /**
     * Test inverseCumulativeProbability method for different values of p.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException, IllegalArgumentException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double[] p = {0.0, 0.25, 0.5, 0.75, 1.0};
        double[] expected = {Double.NEGATIVE_INFINITY, -0.6745, 0.0, 0.6745, Double.POSITIVE_INFINITY};
        for (int i = 0; i < p.length; i++) {
            assertEquals(expected[i], distribution.inverseCumulativeProbability(p[i]), 0.001);
        }

        NormalDistribution distribution2 = new NormalDistributionImpl(1.5, 0.5);
        p = new double[]{0.0, 0.2, 0.5, 0.8, 1.0};
        expected = new double[]{-3.75164625, -0.66479617, 0.0, 1.16520385, 3.75164625};
        for (int i = 0; i < p.length; i++) {
            assertEquals(expected[i], distribution2.inverseCumulativeProbability(p[i]), 0.001);
        }
    }

    /**
     * Test getDomainLowerBound, getDomainUpperBound, and getInitialDomain methods.
     */
    @Test
    public void testDomainBoundaries() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double p = 0.2;
        double[] expected = {Double.NEGATIVE_INFINITY, 0.0, -0.6745};
        for (int i = 0; i < 3; i++) {
            double domainBoundary =
                    (i == 0) ? distribution.getDomainLowerBound(p) :
                            (i == 1) ? distribution.getInitialDomain(p) : distribution.getDomainUpperBound(p);
            assertEquals(expected[i], domainBoundary, 0.001);
        }
    }

    /**
     * Test getMean, getStandardDeviation, and setMean methods with extreme values.
     */
    @Test
    public void testExtremeValues() {
        NormalDistribution distribution = new NormalDistributionImpl();
        double extremeMean = Double.POSITIVE_INFINITY;
        distribution.setMean(extremeMean);
        assertEquals(extremeMean, distribution.getMean(), 0.001);

        double expectedStandardDeviation = StatUtils.max(StatUtils.sampleMean(100000, StatUtils.rndn(1, 1)),
                StatUtils.sampleMean(100000, StatUtils.rndn(1, 1)));
        distribution.setStandardDeviation(expectedStandardDeviation);
        assertEquals(expectedStandardDeviation, distribution.getStandardDeviation(), 0.001);

        // Test with negative extreme values
        double extremeNegativeMean = Double.NEGATIVE_INFINITY;
        try {
            distribution.setMean(extremeNegativeMean);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            distribution.setStandardDeviation(-1 * expectedStandardDeviation);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test exception handling for MathException and MaxIterationsExceededException.
     */
    @Test(expected = MathException.class)
    public void testMathException() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl();
        double x = 100.0 * Math.sqrt(2.0) * distribution.getStandardDeviation();
        distribution.cumulativeProbability(x);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl();
        double x = 100.0 * distribution.getStandardDeviation();
        distribution.inverseCumulativeProbability(0.5 + 1.0e-10);
    }
}