package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {
    @Test
    public void testNormalDistributionImpl() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, dist.getMean(), 0.001);
        assertEquals(2.0, dist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testNormalDistributionImplMeanZeroSDOne() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.001);
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.5, dist.cumulativeProbability(0.0), 0.001);
        assertEquals(0.841345, dist.cumulativeProbability(1.0), 0.001);
        assertEquals(0.158655, dist.cumulativeProbability(-1.0), 0.001);
        assertThrows(MathException.class, () -> dist.cumulativeProbability(10.0));
        assertThrows(MathException.class, () -> dist.cumulativeProbability(-10.0));
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-0.841345, dist.inverseCumulativeProbability(0.158655), 0.001);
        assertEquals(0.841345, dist.inverseCumulativeProbability(0.841345), 0.001);
        assertEquals(0.0, dist.inverseCumulativeProbability(0.5), 0.001);
        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(1.2));
        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(-1.2));
        assertThrows(IllegalArgumentException.class, () -> dist.inverseCumulativeProbability(1.5));
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        dist.setMean(2.0);
        assertEquals(2.0, dist.getMean(), 0.001);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        dist.setStandardDeviation(3.0);
        assertEquals(3.0, dist.getStandardDeviation(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        dist.setStandardDeviation(-1.0);
    }
}