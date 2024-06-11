package org.apache.commons.math.distribution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    @Test
    public void testNormalDistributionImpl_constructor() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 0.5);
        assertEquals(2.5, distribution.getMean(), 0.001);
        assertEquals(0.5, distribution.getStandardDeviation(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalDistributionImpl_negativeStandardDeviation() {
        new NormalDistributionImpl(0.0, -1.0);
    }

    @Test
    public void testNormalDistributionImpl_zeroStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 0.0);
        assertEquals(0.0, distribution.getMean(), 0.001);
        assertEquals(0.0, distribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testNormalDistributionImpl_cumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        double p;

        for (p = 0.0; p <= 1.0; p += 0.01) {
            assertEquals(p, distribution.cumulativeProbability(distribution.inverseCumulativeProbability(p)), 0.001);
        }
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_cumulativeProbability_outsideRange() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.cumulativeProbability(Double.NEGATIVE_INFINITY);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_inverseCumulativeProbability_outsideRange() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.inverseCumulativeProbability(1.1);
    }

    @Test
    public void testNormalDistributionImpl_inverseCumulativeProbability_edgeCases() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0);
    }

    @Test
    public void testNormalDistributionImpl_inverseCumulativeProbability_zeroMeanOneStandardDeviation() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double x = distribution.inverseCumulativeProbability(p);
            double y = Erf.erfinv(p / 2.0);
            assertEquals(y, x, 0.001);
        }
    }

    @Test
    public void testNormalDistributionImpl_inverseCumulativeProbability_nonZeroMeanStandardDeviation() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.0);
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double x = distribution.inverseCumulativeProbability(p);
            double mean = distribution.getMean();
            double y = Erf.erfinv((p + 0.5) / 2.0);
            assertEquals(mean + y * distribution.getStandardDeviation(), x, 0.01);
        }
    }
}