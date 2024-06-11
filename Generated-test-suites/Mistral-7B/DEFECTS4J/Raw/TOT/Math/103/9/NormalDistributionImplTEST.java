package org.apache.commons.math.distribution;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, normalDist.getMean(), 0.001);
        assertEquals(1.0, normalDist.getStandardDeviation(), 0.001);

        UniformRealDistribution uniformDist = new UniformRealDistribution(0, 1);
        NormalDistribution normalDistFromUniform = new NormalDistribution(uniformDist);
        assertEquals(0.0, normalDistFromUniform.getMean(), 0.001);
        assertEquals(1.0, normalDistFromUniform.getStandardDeviation(), 0.001);
    }

    @Test
    public void testMeanAndStddevSetter() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setMean(1.0);
        dist.setStandardDeviation(2.0);
        assertEquals(1.0, dist.getMean(), 0.001);
        assertEquals(2.0, dist.getStandardDeviation(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeStddevSetter() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setStandardDeviation(-1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        double result = normalDist.cumulativeProbability(0.5);
        assertEquals(0.6827, result, 0.001);
        result = normalDist.cumulativeProbability(2.5);
        assertEquals(0.9973, result, 0.001);
        result = normalDist.cumulativeProbability(-2.5);
        assertEquals(0.0027, result, 0.001);

        NormalDistribution normalDistWithMean2 = new NormalDistributionImpl(2.0, 1.0);
        result = normalDistWithMean2.cumulativeProbability(0.5);
        assertEquals(0.6821, result, 0.001);

        NormalDistribution normalDistWithMean3 = new NormalDistributionImpl(3.0, 1.0);
        result = normalDistWithMean3.cumulativeProbability(0.5);
        assertEquals(0.6821, result, 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        normalDist.cumulativeProbability(-10.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        double result = normalDist.inverseCumulativeProbability(0.1);
        assertEquals(0.5841, result, 0.001);
        result = normalDist.inverseCumulativeProbability(0.5);
        assertEquals(0.0, result, 0.001);
        result = normalDist.inverseCumulativeProbability(0.9);
        assertEquals(1.2816, result, 0.001);

        NormalDistribution normalDistWithMean2 = new NormalDistributionImpl(2.0, 1.0);
        result = normalDistWithMean2.inverseCumulativeProbability(0.1);
        assertEquals(2.1116, result, 0.001);
        result = normalDistWithMean2.inverseCumulativeProbability(0.5);
        assertEquals(2.0, result, 0.001);
        result = normalDistWithMean2.inverseCumulativeProbability(0.9);
        assertEquals(1.8884, result, 0.001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        normalDist.inverseCumulativeProbability(1.5);
    }
}