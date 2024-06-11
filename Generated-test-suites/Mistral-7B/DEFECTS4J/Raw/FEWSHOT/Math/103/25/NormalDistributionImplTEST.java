package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplWithMeanAndStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.5, 2.5);
        assertEquals(1.5, distribution.getMean(), 0.001);
        assertEquals(2.5, distribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testNormalDistributionImplWithMeanAndStandardDeviationZero() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.001);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.001);
        assertEquals(0.84134475, distribution.cumulativeProbability(1.0), 0.001);
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(10.0));
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), 0.001);
        assertEquals(0.84134475, distribution.inverseCumulativeProbability(0.84134475), 0.001);
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(-0.1));
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(1.1));
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setMean(2.0);
        assertEquals(2.0, distribution.getMean(), 0.001);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setStandardDeviation(2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setStandardDeviation(-1.0);
    }

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, distribution.getMean(), 0.001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
    }
}