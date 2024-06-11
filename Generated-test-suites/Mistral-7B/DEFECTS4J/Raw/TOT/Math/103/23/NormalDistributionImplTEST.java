package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.probability.DistributionFunction;
import org.apache.commons.math3.stat.probability.Statistics;
import org.junit.Test;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Test for {@link NormalDistributionImpl} class.
 */
public class NormalDistributionImplTest {

    /**
     * Test the creation of a normal distribution using given mean and standard
     * deviation.
     */
    @Test
    public void testNormalDistributionCreation() {
        NormalDistribution distribution = new NormalDistributionImpl(3.14, 2.718);
        assertTrue(distribution instanceof NormalDistributionImpl);
        assertEquals(3.14, distribution.getMean(), 0.0);
        assertEquals(2.718, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test the creation of a normal distribution with default values.
     */
    @Test
    public void testNormalDistributionDefaultCreation() {
        NormalDistribution distribution = new NormalDistributionImpl();
        assertTrue(distribution instanceof NormalDistributionImpl);
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test the cumulative probability method with valid inputs.
     */
    @Test
    public void testCumulativeProbabilityValidInput() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

        for (int i = -12; i <= 12; i++) {
            double x = i / 2.0;
            double p = distribution.cumulativeProbability(x);
            double expected = 0.5 * (1 + Erf.erf(x / Math.sqrt(2)));
            assertEquals(expected, p, 1e-7);
        }
    }

    /**
     * Test the inverse cumulative probability method with valid inputs.
     */
    @Test
    public void testInverseCumulativeProbabilityValidInput() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

        for (int i = 0; i <= 100; i += 10) {
            double p = i / 100.0;
            double x = distribution.inverseCumulativeProbability(p);
            double expected = Statistics.percentile(new NormalDistribution(), p);
            assertEquals(expected, x, 1e-7);
        }
    }

    /**
     * Test the cumulative probability method with out-of-range input.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRangeInput() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        double x = Double.POSITIVE_INFINITY;
        distribution.cumulativeProbability(x);
    }

    /**
     * Test the inverse cumulative probability method with out-of-range input.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRangeInput() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        double p = -1.0;
        distribution.inverseCumulativeProbability(p);
    }

    /**
     * Test the setMean method with negative input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetMeanNegativeInput() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setMean(-1.0);
    }

    /**
     * Test the setStandardDeviation method with negative input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegativeInput() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(-1.0);
    }

    /**
     * Test the method to generate random samples from the distribution.
     */
    @Test
    public void testGenerateRandomSamples() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        int samplesCount = 10000;
        double[] samples = new double[samplesCount];
        for (int i = 0; i < samplesCount; i++) {
            samples[i] = distribution.inverseCumulativeProbability(new Double(Math.random()).doubleValue());
        }
        Mean mean = new Mean();
        StandardDeviation stdDev = new StandardDeviation();
        Mean sampleMean = new Mean();
        StandardDeviation sampleStdDev = new StandardDeviation();
        mean.evaluate(distribution);
        stdDev.evaluate(distribution);
        sampleMean.evaluate(samples);
        sampleStdDev.evaluate(samples);
        assertEquals(mean.getresult(), sampleMean.getresult(), 1e-5);
        assertEquals(stdDev.getresult(), sampleStdDev.getresult(), 1e-5);
    }

}