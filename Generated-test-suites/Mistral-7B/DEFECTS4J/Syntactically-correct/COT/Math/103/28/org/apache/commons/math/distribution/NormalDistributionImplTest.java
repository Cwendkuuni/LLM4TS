package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Tests for the NormalDistributionImpl class.
 */
public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructionWithMeanAndStdDev() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, distribution.getMean(), 0.001);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testNormalDistributionImplConstructionWithMeanAndStdDevZero() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.001);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testSettingMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(3.0);
        assertEquals(3.0, distribution.getMean(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNegativeMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(-1.0);
    }

    @Test
    public void testSettingStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(5.0);
        assertEquals(5.0, distribution.getStandardDeviation(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNegativeStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(-1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double x = 0.5;
        double expectedResult = 0.5;
        double result = distribution.cumulativeProbability(x);
        assertEquals(expectedResult, result, 0.001);
    }

    @Test
    public void testCumulativeProbabilityNegativeX() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double x = -5.0;
        double expectedResult = 0.0;
        double result = distribution.cumulativeProbability(x);
        assertEquals(expectedResult, result, 0.001);
    }

    @Test
    public void testCumulativeProbabilityPositiveXMoreThan20StdDevFromMean() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double x = 50.0 * distribution.getStandardDeviation();
        double expectedResult = 0.5;
        double result = distribution.cumulativeProbability(x);
        assertEquals(expectedResult, result, 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithNegativeXMoreThan20StdDevFromMean() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double x = -50.0 * distribution.getStandardDeviation();
        distribution.cumulativeProbability(x);
    }

    @Test
    public void testInverseCumulativeProbabilityZero() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double p = 0.0;
        double expectedResult = Double.NEGATIVE_INFINITY;
        double result = distribution.inverseCumulativeProbability(p);
        assertEquals(expectedResult, result, 0.0);
    }

    @Test
    public void testInverseCumulativeProbabilityOne() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double p = 1.0;
        double expectedResult = Double.POSITIVE_INFINITY;
        double result = distribution.inverseCumulativeProbability(p);
        assertEquals(expectedResult, result, 0.0);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityNegativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double p = -0.1;
        distribution.inverseCumulativeProbability(p);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutsideOfDomain() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double p = 1.1;
        distribution.inverseCumulativeProbability(p);
    }
}