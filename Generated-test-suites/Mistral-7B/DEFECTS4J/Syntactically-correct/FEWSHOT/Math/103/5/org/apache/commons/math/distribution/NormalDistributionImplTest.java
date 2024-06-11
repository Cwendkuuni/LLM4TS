package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Erf;

import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplWithMean0AndStandardDeviation1() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.01);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.01);
    }

    @Test
    public void testNormalDistributionImplWithMeanAndStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.5, 2.5);
        assertEquals(1.5, distribution.getMean(), 0.01);
        assertEquals(2.5, distribution.getStandardDeviation(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNegativeStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(-1.5);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double probability = 0.1;
        double expectedResult = Erf.erf((-0.84264) / (Math.sqrt(2.0) * 1.0));
        assertEquals(expectedResult, distribution.cumulativeProbability(0.84264), 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithValueMoreThan20StandardDeviationsFromMean() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double x = 21 * distribution.getStandardDeviation() + distribution.getMean();
        distribution.cumulativeProbability(x);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double probability = 0.45;
        double expectedResult = distribution.getMean() + (Math.sqrt(2.0) * Erf.erfinv(probability));
        assertEquals(expectedResult, distribution.inverseCumulativeProbability(probability), 0.01);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithInvalidProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double probability = 1.5;
        distribution.inverseCumulativeProbability(probability);
    }
}