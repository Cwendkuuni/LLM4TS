package org.apache.commons.math.distribution.test;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.distribution.NormalDistributionImpl;

@Category(org.apache.commons.math3.FunctionTest.class)
public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructionWithMeanAndStdDev() {
        NormalDistribution normalDist = new NormalDistributionImpl(3.5, 2.5);
        assertEquals(3.5, normalDist.getMean(), 0.001);
        assertEquals(2.5, normalDist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testNormalDistributionImplConstructionWithDefaultValues() {
        NormalDistribution normalDist = new NormalDistributionImpl();
        assertEquals(0.0, normalDist.getMean(), 0.001);
        assertEquals(1.0, normalDist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testGetMean() {
        NormalDistribution normalDist = new NormalDistributionImpl(3.5, 2.5);
        assertEquals(3.5, normalDist.getMean(), 0.001);
    }

    @Test
    public void testSetMean() {
        NormalDistribution normalDist = new NormalDistributionImpl(3.5, 2.5);
        normalDist.setMean(4.5);
        assertEquals(4.5, normalDist.getMean(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMeanNegativeValue() {
        NormalDistribution normalDist = new NormalDistributionImpl(3.5, 2.5);
        normalDist.setMean(-1.5);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistribution normalDist = new NormalDistributionImpl(3.5, 2.5);
        assertEquals(2.5, normalDist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistribution normalDist = new NormalDistributionImpl(3.5, 2.5);
        normalDist.setStandardDeviation(3.5);
        assertEquals(3.5, normalDist.getStandardDeviation(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationZero() {
        NormalDistribution normalDist = new NormalDistributionImpl(3.5, 2.5);
        normalDist.setStandardDeviation(0.0);
    }

    @Test
    public void testCumulativeProbabilityNormalValue() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(3.5, 2.5);
        double x = 4.0;
        double expected = 0.341345346142842;
        double actual = normalDist.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityValueCloseToMean() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(3.5, 2.5);
        double x = 3.5;
        double expected = 0.682689462101637;
        double actual = normalDist.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityValueFarFromMean() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(3.5, 2.5);
        double x = 5.5;
        double expected = 0.000330153569885;
        double actual = normalDist.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityValueOutsideRange() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(3.5, 2.5);
        double x = -5.0;
        // The method should return 0.5 for values outside the range
        double expected = 0.5;
        double actual = normalDist.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityValuesOutsideRangeWithMaxIterations() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(3.5, 2.5);
        double x = -20.0;
    }

    @Test
    public void testInverseCumulativeProbabilityZero() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(3.5, 2.5);
        double p = 0.0;
        double expected = Double.NEGATIVE_INFINITY;
        double actual = normalDist.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testInverseCumulativeProbabilityOne() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(3.5, 2.5);
        double p = 1.0;
        double expected = Double.POSITIVE_INFINITY;
        double actual = normalDist.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testInverseCumulativeProbabilityValue() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(3.5, 2.5);
        double p = 0.5;
        double expected = 3.5;
        double actual = normalDist.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testInverseCumulativeProbabilityValueOutsideRange() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(3.5, 2.5);
        double p = 0.999;
        // The method should return a value close to the mean for values outside the range
        double expected = 3.5 + 2.5;
        double actual = normalDist.inverseCumulativeProbability(p);
        assertTrue(actual > expected - 0.5 && actual < expected + 0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityNegativeProbability() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(3.5, 2.5);
        double p = -0.5;
        normalDist.inverseCumulativeProbability(p);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutsideRangeWithMaxIterations() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(3.5, 2.5);
        double p = 0.01;
    }
}