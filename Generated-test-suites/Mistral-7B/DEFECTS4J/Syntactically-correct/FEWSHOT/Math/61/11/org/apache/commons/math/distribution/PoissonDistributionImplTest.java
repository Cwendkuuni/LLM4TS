package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.percentile.Percentile;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testCreateWithValidParameters() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(3.0, distribution.getMean(), 0.0);
        assertNotNull(distribution.normal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidZeroMean() {
        new PoissonDistributionImpl(0.0);
    }

    @Test
    public void testProbabilityWithValidValues() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.0697, distribution.probability(0), 0.001);
        assertEquals(0.1112, distribution.probability(1), 0.001);
        assertEquals(0.2241, distribution.probability(2), 0.001);
        assertEquals(0.2162, distribution.probability(3), 0.001);
        assertEquals(0.1737, distribution.probability(4), 0.001);
        assertEquals(0.1361, distribution.probability(5), 0.001);
        assertEquals(0.0993, distribution.probability(6), 0.001);
        assertEquals(0.0715, distribution.probability(7), 0.001);
        assertEquals(0.0477, distribution.probability(8), 0.001);
        assertEquals(0.0315, distribution.probability(9), 0.001);
        assertEquals(0.0193, distribution.probability(10), 0.001);
    }

    @Test
    public void testProbabilityWithNegativeValue() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.0, distribution.probability(-1), 0.0);
    }

    @Test
    public void testProbabilityWithIntegerMaxValue() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.0, distribution.probability(Integer.MAX_VALUE), 0.0);
    }

    @Test
    public void testProbabilityWithNonIntegerValue() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.0, distribution.probability(3.5), 0.0);
    }

    @Test
    public void testCumulativeProbabilityWithValidValues() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.0697, distribution.cumulativeProbability(0), 0.001);
        assertEquals(0.1910, distribution.cumulativeProbability(1), 0.001);
        assertEquals(0.4038, distribution.cumulativeProbability(2), 0.001);
        assertEquals(0.6131, distribution.cumulativeProbability(3), 0.001);
        assertEquals(0.7943, distribution.cumulativeProbability(4), 0.001);
        assertEquals(0.9152, distribution.cumulativeProbability(5), 0.001);
        assertEquals(0.9750, distribution.cumulativeProbability(6), 0.001);
        assertEquals(0.9971, distribution.cumulativeProbability(7), 0.001);
        assertEquals(0.9998, distribution.cumulativeProbability(8), 0.001);
        assertEquals(1.0, distribution.cumulativeProbability(9), 0.001);
        assertEquals(1.0, distribution.cumulativeProbability(10), 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithNegativeValue() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        distribution.cumulativeProbability(-1);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithIntegerMaxValue() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        distribution.cumulativeProbability(Integer.MAX_VALUE);
    }

    @Test
    public void testCumulativeProbabilityWithNonIntegerValue() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.9996, distribution.cumulativeProbability(3.5), 0.001);
    }

    @Test
    public void testNormalApproximateProbabilityWithValidValues() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.0683, distribution.normalApproximateProbability(0), 0.001);
        assertEquals(0.1105, distribution.normalApproximateProbability(1), 0.001);
        assertEquals(0.1904, distribution.normalApproximateProbability(2), 0.001);
        assertEquals(0.2156, distribution.normalApproximateProbability(3), 0.001);
        assertEquals(0.1721, distribution.normalApproximateProbability(4), 0.001);
        assertEquals(0.1338, distribution.normalApproximateProbability(5), 0.001);
        assertEquals(0.0983, distribution.normalApproximateProbability(6), 0.001);
        assertEquals(0.0707, distribution.normalApproximateProbability(7), 0.001);
        assertEquals(0.0484, distribution.normalApproximateProbability(8), 0.001);
        assertEquals(0.0316, distribution.normalApproximateProbability(9), 0.001);
        assertEquals(0.0191, distribution.normalApproximateProbability(10), 0.001);
    }

    @Test
    public void testNormalApproximateProbabilityWithNegativeValue() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.0, distribution.normalApproximateProbability(-1), 0.0);
    }

    @Test
    public void testNormalApproximateProbabilityWithNonIntegerValue() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.0598, distribution.normalApproximateProbability(3.5), 0.001);
    }

    @Test
    public void testSampleWithValidDistribution() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(3, distribution.sample(), 0);
    }

    @Test
    public void testSampleWithSmallMean() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        int result = distribution.sample();
        assertTrue(result >= 0 && result <= 1000);
    }

    @Test
    public void testSampleWithLargeMean() throws MathException {
        Random random = new Random();
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10000.0);
        assertEquals(10000, distribution.sample(), 0);

        for (int i = 0; i < 1000; i++) {
            int result = distribution.sample();
            assertTrue(result >= 0 && result <= 1000 * 10000);
            assertEquals(Math.exp(distribution.logProbability(result)), distribution.probability(result), 0.01);
        }
    }

    @Test
    public void testSampleWithNonIntegerMean() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        int result = distribution.sample();
        assertTrue(result >= 0 && result <= 1000 * (int) distribution.getMean());
        assertEquals(Math.exp(distribution.logProbability(result)), distribution.probability(result), 0.01);
    }

    @Test
    public void testPercentile() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(1, new Percentile(distribution).inverseCumulativeProbability(0.33), 0);
        assertEquals(2, new Percentile(distribution).inverseCumulativeProbability(0.68), 0);
        assertEquals(3, new Percentile(distribution).inverseCumulativeProbability(0.95), 0);
    }

    @Test
    public void testStandardDeviation() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        PoissonDistribution poissonDistribution = new PoissonDistribution();
        assertEquals(Math.sqrt(3.0), new StandardDeviation().evaluate(new PoissonDistribution[]{distribution, poissonDistribution}));
    }
}