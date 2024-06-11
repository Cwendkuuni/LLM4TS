package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;

import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, dist.getMean(), 0.001);
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);

        NormalDistribution distWithMeanAndSD = new NormalDistributionImpl(2.5, 0.5);
        assertEquals(2.5, distWithMeanAndSD.getMean(), 0.001);
        assertEquals(0.5, distWithMeanAndSD.getStandardDeviation(), 0.001);
    }

    @Test
    public void testMeanSetter() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setMean(-1.0);
        assertEquals(-1.0, dist.getMean(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMeanSetterNegative() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setMean(-2.0);
    }

    @Test
    public void testStandardDeviationSetter() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setStandardDeviation(2.0);
        assertEquals(2.0, dist.getStandardDeviation(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardDeviationSetterNegative() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setStandardDeviation(-1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.5, dist.cumulativeProbability(0.0), 0.001);
        assertEquals(0.841344746, dist.cumulativeProbability(1.0), 0.001);
        assertThrows(MathException.class, () -> dist.cumulativeProbability(-1.0));
        assertThrows(MathException.class, () -> dist.cumulativeProbability(3.0));
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, dist.inverseCumulativeProbability(0.5), 0.001);
        assertEquals(-0.679436535, dist.inverseCumulativeProbability(0.25), 0.001);
        assertEquals(0.679436535, dist.inverseCumulativeProbability(0.75), 0.001);
        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(1.2));
        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(-0.2));
    }

    @Test
    public void testInverseCumulativeProbabilityEdgeCases() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(Double.NEGATIVE_INFINITY, dist.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(1.0), 0.0);
    }
}