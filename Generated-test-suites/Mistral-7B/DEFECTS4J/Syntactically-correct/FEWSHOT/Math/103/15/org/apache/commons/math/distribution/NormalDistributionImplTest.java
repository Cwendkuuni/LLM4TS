package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, distribution.getMean(), 0.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testNormalDistributionImplZeroMeanOneStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        double value = 2.5;
        double expected = Erf.erf((value - 1.0) / (2.0 * Math.sqrt(2.0))) / 2.0;
        double actual = distribution.cumulativeProbability(value);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testCumulativeProbabilityZeroMeanOneStandardDeviation()
            throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl();
        double value = 0.0;
        double expected = 0.5;
        double actual = distribution.cumulativeProbability(value);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testCumulativeProbabilityNegativeValue() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        double value = -3.0;
        double expected = 0.0;
        double actual = distribution.cumulativeProbability(value);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        double probability = 0.75;
        double expected = 1.5;
        double actual = distribution.inverseCumulativeProbability(probability);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testInverseCumulativeProbabilityZero() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        double probability = 0.0;
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(probability));
        assertNull(distribution.inverseCumulativeProbability(probability));
    }

    @Test
    public void testInverseCumulativeProbabilityOne() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        double probability = 1.0;
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(probability));
        assertNull(distribution.inverseCumulativeProbability(probability));
    }

    @Test
    public void testInverseCumulativeProbabilityOutOfBounds() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        double probability = -0.1;
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(probability));
        double actual = distribution.inverseCumulativeProbability(probability);
        assertEquals(Double.NEGATIVE_INFINITY, actual, 0.0);

        probability = 1.1;
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(probability));
        actual = distribution.inverseCumulativeProbability(probability);
        assertEquals(Double.POSITIVE_INFINITY, actual, 0.0);
    }

    @Test
    public void testSetMean() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        distribution.setMean(3.0);
        assertEquals(3.0, distribution.getMean(), 0.0);
    }

    @Test
    public void testSetMeanNegativeValue() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        assertThrows(IllegalArgumentException.class, () -> distribution.setMean(-1.0));
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        distribution.setStandardDeviation(3.0);
        assertEquals(3.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testSetStandardDeviationZeroOrNegativeValue() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        assertThrows(IllegalArgumentException.class, () -> distribution.setStandardDeviation(0.0));
        assertThrows(IllegalArgumentException.class, () -> distribution.setStandardDeviation(-1.0));
    }

}