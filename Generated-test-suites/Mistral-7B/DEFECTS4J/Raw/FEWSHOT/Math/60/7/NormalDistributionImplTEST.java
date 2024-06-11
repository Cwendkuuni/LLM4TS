package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {
    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 0);
        assertEquals(1, dist.getStandardDeviation(), 0);

        double density = dist.density(0.5);
        assertEquals(0.15430801797214715, density, 1e-6);

        double cumulativeProbability = dist.cumulativeProbability(0.5);
        assertEquals(0.6826894621360141, cumulativeProbability, 1e-6);

        double inverseCumulativeProbability = dist.inverseCumulativeProbability(0.5);
        assertEquals(0.5, inverseCumulativeProbability, 1e-6);

        double sample = dist.sample();
        assertEquals(0, sample, 1e-6);

        NormalDistributionImpl dist2 = new NormalDistributionImpl(1, 2);
        assertEquals(1, dist2.getMean(), 0);
        assertEquals(2, dist2.getStandardDeviation(), 0);

        assertFalse(dist2.isSupportPoint(Integer.MIN_VALUE));
        assertFalse(dist2.isSupportPoint(Integer.MAX_VALUE));

        assertArrayEquals(new double[]{Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY},
                dist2.getDomainBounds(0.5), 0);

        assertNull(dist2.density(Double.NaN));
        assertNull(dist2.cumulativeProbability(Double.NaN));
        assertNull(dist2.inverseCumulativeProbability(Double.NaN));
        assertNull(dist2.sample());

        NormalDistribution dist3 = new NormalDistributionImpl(3, 3);
        assertEquals(3, dist3.getMean(), 0);
        assertEquals(3, dist3.getStandardDeviation(), 0);

        double density2 = dist3.density(6);
        assertEquals(0.0, density2, 1e-6);

        double cumulativeProbability2 = dist3.cumulativeProbability(6);
        assertEquals(0.0, cumulativeProbability2, 1e-6);

        double inverseCumulativeProbability2 = dist3.inverseCumulativeProbability(0);
        assertEquals(Double.NEGATIVE_INFINITY, inverseCumulativeProbability2, 0);

        double inverseCumulativeProbability3 = dist3.inverseCumulativeProbability(1);
        assertEquals(-1.8313348835759216, inverseCumulativeProbability3, 1e-6);

        double inverseCumulativeProbability4 = dist3.inverseCumulativeProbability(0.5);
        assertEquals(1.2886618055734523, inverseCumulativeProbability4, 1e-6);

        double inverseCumulativeProbability5 = dist3.inverseCumulativeProbability(0.999);
        assertEquals(5.243782848420325, inverseCumulativeProbability5, 1e-6);

        double inverseCumulativeProbability6 = dist3.inverseCumulativeProbability(1);
        assertEquals(Double.POSITIVE_INFINITY, inverseCumulativeProbability6, 0);

        double inverseCumulativeProbability7 = dist3.inverseCumulativeProbability(1.1);
        assertEquals(6.629436528169296, inverseCumulativeProbability7, 1e-6);

        assertEquals(0.5, dist3.cumulativeProbability(inverseCumulativeProbability4), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplNegativeStandardDeviation() {
        new NormalDistributionImpl(-1, 1);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplMaxIterationsExceededException() {
        new NormalDistributionImpl(0, Double.MAX_VALUE);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplMathException() {
        new NormalDistributionImpl(Double.NaN, 1);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplMathException2() {
        new NormalDistributionImpl(1, Double.NaN);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplMathException3() {
        new NormalDistributionImpl(1, 0);
    }

    @Test
    public void testErf() {
        assertEquals(0.0, Erf.erf(0.0), 1e-6);
        assertEquals(0.7568025216, Erf.erf(0.5), 1e-6);
        assertEquals(0.8427010655, Erf.erf(1.0), 1e-6);
        assertEquals(-0.8427010655, Erf.erf(-1.0), 1e-6);
    }

    @Test
    public void testInverseCumulativeProbabilityWithHighProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1, 1);
        double inverseCumulativeProbability = dist.inverseCumulativeProbability(1 - 1e-12);
        assertEquals(Double.POSITIVE_INFINITY, inverseCumulativeProbability, 0);
    }

    @Test
    public void testInverseCumulativeProbabilityWithLowProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1, 1);
        double inverseCumulativeProbability = dist.inverseCumulativeProbability(1e-12);
        assertEquals(Double.NEGATIVE_INFINITY, inverseCumulativeProbability, 0);
    }

    @Test
    public void testInverseCumulativeProbabilityWithZeroProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1, 1);
        double inverseCumulativeProbability = dist.inverseCumulativeProbability(0);
        assertEquals(Double.NEGATIVE_INFINITY, inverseCumulativeProbability, 0);
    }

    @Test
    public void testInverseCumulativeProbabilityWithOneProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(1, 1);
        double inverseCumulativeProbability = dist.inverseCumulativeProbability(1);
        assertEquals(Double.POSITIVE_INFINITY, inverseCumulativeProbability, 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistribution dist = new NormalDistributionImpl(1, 1);
        double domainUpperBound = dist.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, domainUpperBound, 0);
    }

    @Test
    public void testGetDomainUpperBoundWithZeroProbability() {
        NormalDistribution dist = new NormalDistributionImpl(1, 1);
        double domainUpperBound = dist.getDomainUpperBound(0);
        assertEquals(Double.MAX_VALUE, domainUpperBound, 0);
    }

    @Test
    public void testGetDomainUpperBoundWithOneProbability() {
        NormalDistribution dist = new NormalDistributionImpl(1, 1);
        double domainUpperBound = dist.getDomainUpperBound(1);
        assertEquals(Double.MAX_VALUE, domainUpperBound, 0);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistribution dist = new NormalDistributionImpl(1, 1);
        double domainLowerBound = dist.getDomainLowerBound(0.5);
        assertEquals(-Double.MAX_VALUE, domainLowerBound, 0);
    }

    @Test
    public void testGetDomainLowerBoundWithOneProbability() {
        NormalDistribution dist = new NormalDistributionImpl(1, 1);
        double domainLowerBound = dist.getDomainLowerBound(1);
        assertEquals(-Double.MAX_VALUE, domainLowerBound, 0);
    }

    @Test
    public void testGetDomainLowerBoundWithZeroProbability() {
        NormalDistribution dist = new NormalDistributionImpl(1, 1);
        double domainLowerBound = dist.getDomainLowerBound(0);
        assertEquals(-Double.MAX_VALUE, domainLowerBound, 0);
    }
}