package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructor() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    @Test
    public void testNormalDistributionImplConstructorMeanNegative() {
        assertThrows(NotStrictlyPositiveException.class, () -> new NormalDistributionImpl(-1, 1));
    }

    @Test
    public void testNormalDistributionImplConstructorStdDevZero() {
        assertThrows(NotStrictlyPositiveException.class, () -> new NormalDistributionImpl(0, 0));
    }

    @Test
    public void testNormalDistributionImplConstructorAccuracy() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1, 0.001);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
        assertEquals(0.001, dist.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testNormalDistributionImplDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double x = 1.0;
        double density = dist.density(x);
        assertEquals(0.39894228040143271, density, 0.0000001);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double x = 0.5;
        double p = dist.cumulativeProbability(x);
        assertEquals(0.68268949213732353, p, 0.0000001);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbabilityLessThanMinus20Sd() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double x = -50;
        double p = dist.cumulativeProbability(x);
        assertEquals(0.0, p, 0.0);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbabilityGreaterThan20Sd() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double x = 50;
        double p = dist.cumulativeProbability(x);
        assertEquals(1.0, p, 0.0);
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbabilityZero() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.0;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(Double.NEGATIVE_INFINITY, x);
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbabilityOne() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 1.0;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, x);
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbabilityAccuracy() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1, 0.001);
        double p = 0.5;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(0.0, Precision.abs(x - 0.0, 6), 0.0);
    }

    @Test
    public void testNormalDistributionImplSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        RandomData rand = new RandomData();
        double x = dist.sample(rand);
        double mean = dist.getMean();
        double stdDev = dist.getStandardDeviation();
        assertEquals(mean, x, stdDev);
    }
}