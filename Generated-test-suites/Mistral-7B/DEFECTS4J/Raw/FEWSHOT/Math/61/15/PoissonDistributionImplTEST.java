package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

public class PoissonDistributionImplTest {
    @Test
    public void testConstructorWithValidMean() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(1.5);
        assertEquals(1.5, pd.getMean(), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityWithValidArgument() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.0);
        double probability = pd.probability(1);
        assertEquals(0.13533528323062834, probability, 0.0000001);
        double probabilityWithMaxValue = pd.probability(Integer.MAX_VALUE);
        assertEquals(0.2704475053443873, probabilityWithMaxValue, 0.0000001);
    }

    @Test
    public void testProbabilityWithInvalidArgument() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.0);
        assertEquals(0.0, pd.probability(-1), 0.0);
        assertEquals(0.0, pd.probability(Integer.MAX_VALUE + 1), 0.0);
    }

    @Test
    public void testCumulativeProbabilityWithValidArguments() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.0);
        double probability = pd.cumulativeProbability(0);
        assertEquals(0.0, probability, 0.0000001);
        probability = pd.cumulativeProbability(1);
        assertEquals(0.13533528323062834, probability, 0.0000001);
        probability = pd.cumulativeProbability(2);
        assertEquals(0.3200721182925226, probability, 0.0000001);
        probability = pd.cumulativeProbability(Integer.MAX_VALUE);
        assertEquals(1.0, probability, 0.0000001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithInvalidArguments() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.0);
        pd.cumulativeProbability(-1);
    }

    @Test
    public void testNormalApproximateProbabilityWithValidArguments() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.0);
        double probability = pd.normalApproximateProbability(0);
        assertEquals(0.1543080288630278, probability, 0.0000001);
        probability = pd.normalApproximateProbability(1);
        assertEquals(0.13533528323062834, probability, 0.0000001);
        probability = pd.normalApproximateProbability(2);
        assertEquals(0.1016453875035605, probability, 0.0000001);
        probability = pd.normalApproximateProbability(Integer.MAX_VALUE);
        assertEquals(0.04516848043938133, probability, 0.0000001);
    }

    @Test(expected = MathException.class)
    public void testNormalApproximateProbabilityWithInvalidArguments() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.0);
        pd.normalApproximateProbability(-1);
    }

    @Test
    public void testSampleFromDistribution() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.0);
        int sample = pd.sample();
        assertEquals(0, sample, 0);
        sample = pd.sample();
        assertEquals(1, sample, 0);
        sample = pd.sample();
        assertEquals(1, sample, 0);
        sample = pd.sample();
        assertEquals(2, sample, 0);
        sample = pd.sample();
        assertEquals(2, sample, 0);
        sample = pd.sample();
        assertEquals(3, sample, 0);
        sample = pd.sample();
        assertEquals(3, sample, 0);
        sample = pd.sample();
        assertEquals(3, sample, 0);
        sample = pd.sample();
        assertEquals(3, sample, 0);
    }

    @Test
    public void testInverseCumulativeProbabilityWithValidArgument() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.0);
        double probability = 0.25;
        int expectedValue = 1;
        int actualValue = pd.inverseCumulativeProbability(probability);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testInverseCumulativeProbabilityWithInvalidArgument() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.0);
        assertFalse(pd.inverseCumulativeProbability(1.5) > Integer.MAX_VALUE);
    }

    @Test
    public void testStatisticalProperties() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.0);
        int size = 100000;
        double[] observedValues = new double[size];
        for (int i = 0; i < size; i++) {
            observedValues[i] = pd.sample();
        }
        double mean = pd.getMean();
        double variance = pd.getVariance();
        double minObservedValue = Integer.MAX_VALUE;
        double maxObservedValue = Integer.MIN_VALUE;
        double sumObservedValues = 0.0;
        for (double value : observedValues) {
            sumObservedValues += value;
            minObservedValue = Math.min(minObservedValue, value);
            maxObservedValue = Math.max(maxObservedValue, value);
        }
        double expectedMean = mean;
        double expectedVariance = mean;
        double expectedStandardDeviation = Math.sqrt(mean);
        double observedMean = StatUtils.mean(observedValues);
        double observedVariance = StatUtils.variance(observedValues);
        double observedStandardDeviation = StatUtils.standardDeviation(observedValues);
        assertEquals(expectedMean, observedMean, 0.001);
        assertEquals(expectedVariance, observedVariance, 0.001);
        assertEquals(expectedStandardDeviation, observedStandardDeviation, 0.001);
        assertTrue(minObservedValue >= 0);
        assertTrue(maxObservedValue <= Integer.MAX_VALUE);
    }

    @Test
    public void testNegativeMean() {
        try {
            new PoissonDistributionImpl(-1.5);
            fail();
        } catch (MathIllegalArgumentException e) {
            assertEquals("mean must be non-negative", e.getMessage());
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.0);
        int result = pd.getDomainLowerBound(0.1);
        assertEquals(0, result);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.0);
        int result = pd.getDomainUpperBound(0.1);
        assertEquals(Integer.MAX_VALUE, result);
    }
}