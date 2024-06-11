package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;

import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplCreation() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, dist.getMean(), 0.001);
        assertEquals(2.0, dist.getStandardDeviation(), 0.001);

        NormalDistributionImpl distZeroMeanOneSD = new NormalDistributionImpl();
        assertEquals(0.0, distZeroMeanOneSD.getMean(), 0.001);
        assertEquals(1.0, distZeroMeanOneSD.getStandardDeviation(), 0.001);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setMean(3.0);
        assertEquals(3.0, dist.getMean(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMeanNegative() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setMean(-1.0);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setStandardDeviation(2.5);
        assertEquals(2.5, dist.getStandardDeviation(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationZero() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setStandardDeviation(0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);

        // Test values within 3 standard deviations from mean
        assertEquals(0.15435, dist.cumulativeProbability(0.5), 0.001);
        assertEquals(0.84134, dist.cumulativeProbability(1.5), 0.001);

        // Test values outside of domain
        assertThrows(MathException.class, () -> dist.cumulativeProbability(-3.0));
        assertThrows(MathException.class, () -> dist.cumulativeProbability(6.0));
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);

        // Test probabilities within domain
        assertEquals(0.5, dist.inverseCumulativeProbability(0.5), 0.001);
        assertEquals(1.5, dist.inverseCumulativeProbability(0.84134), 0.001);

        // Test probabilities outside of domain
        assertEquals(Double.NEGATIVE_INFINITY, dist.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(1.0), 0.0);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        dist.inverseCumulativeProbability(-0.1);
    }
}