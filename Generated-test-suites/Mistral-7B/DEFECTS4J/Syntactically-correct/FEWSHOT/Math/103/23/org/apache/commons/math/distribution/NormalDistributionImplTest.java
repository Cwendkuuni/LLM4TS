package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, dist.getMean(), 0);
        assertEquals(2.0, dist.getStandardDeviation(), 0);

        NormalDistributionImpl distZeroMeanOneSD = new NormalDistributionImpl();
        assertEquals(0.0, distZeroMeanOneSD.getMean(), 0);
        assertEquals(1.0, distZeroMeanOneSD.getStandardDeviation(), 0);
    }

    @Test
    public void testMeanGetSet() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, dist.getMean(), 0);

        dist.setMean(3.0);
        assertEquals(3.0, dist.getMean(), 0);
    }

    @Test
    public void testStandardDeviationGetSet() throws MathIllegalArgumentException {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(2.0, dist.getStandardDeviation(), 0);

        dist.setStandardDeviation(0.5);
        assertEquals(0.5, dist.getStandardDeviation(), 0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeStandardDeviationSet() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        dist.setStandardDeviation(-1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);

        assertEquals(0.15434, dist.cumulativeProbability(0.5), 0.001);
        assertEquals(0.84134, dist.cumulativeProbability(1.5), 0.001);
        assertEquals(0.5, dist.cumulativeProbability(1.0), 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        dist.cumulativeProbability(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);

        assertEquals(0.5, dist.inverseCumulativeProbability(0.15434), 0.001);
        assertEquals(1.5, dist.inverseCumulativeProbability(0.84134), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testInverseCumulativeProbabilityInvalidProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        dist.inverseCumulativeProbability(-1);
    }

    @Test
    public void testInverseCumulativeProbabilityConvergence() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        double p = 0.1;
        assertEquals(1.28155, dist.inverseCumulativeProbability(p), 0.001);

        p = 0.99999;
        assertEquals(2.30622, dist.inverseCumulativeProbability(p), 0.001);
    }

    @Test
    public void testInverseCumulativeProbabilityEdgeCases() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(Double.NEGATIVE_INFINITY, dist.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(1.0), 0.0);
    }
}