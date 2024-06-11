package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoSuchElementException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_MeanAndSD_ValidArgs() {
        final double mean = 4.5;
        final double sd = 2.5;
        final NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);

        assertEquals(mean, dist.getMean(), 0.0);
        assertEquals(sd, dist.getStandardDeviation(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImpl_MeanAndSD_InvalidArgs() {
        new NormalDistributionImpl(-1.5, 2.5);
    }

    @Test
    public void testNormalDistributionImpl_MeanAndSD_SampleValues() {
        final int numSamples = 10;
        final double mean = 4.5;
        final double sd = 2.5;
        final NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);
        final RandomDataGenerator rng = new RandomDataGenerator();

        for (int i = 0; i < numSamples; i++) {
            assertEquals("Sample value " + i, mean + sd * rng.nextGaussian(), dist.sample(), 0.1);
        }
    }

    @Test
    public void testNormalDistributionImpl_MeanAndSD_CumulativeProbability() {
        final int numTestValues = 10;
        final double mean = 4.5;
        final double sd = 2.5;
        final double inverseCumulativeAccuracy = 1e-6;
        final NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);

        for (double testValue = -5.0; testValue <= 5.0; testValue += 0.1) {
            assertEquals(String.format("Cumulative probability for test value %f", testValue),
                         dist.cumulativeProbability(testValue),
                         Math.max(0.0, Math.min(1.0, Erf.erf((testValue - mean) / (sd * Math.sqrt(2.0)))) * 0.5),
                         inverseCumulativeAccuracy);
        }
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_MeanAndSD_CumulativeProbability_OutOfRange() {
        final double mean = 4.5;
        final double sd = 2.5;
        final NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);

        dist.cumulativeProbability(Double.NEGATIVE_INFINITY);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_MeanAndSD_CumulativeProbability_OutOfRange_Max() {
        final double mean = 4.5;
        final double sd = 2.5;
        final NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);

        dist.cumulativeProbability(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testNormalDistributionImpl_MeanAndSD_InverseCumulativeProbability() throws MathException {
        final int numTestProbabilities = 10;
        final double mean = 4.5;
        final double sd = 2.5;
        final double inverseCumulativeAccuracy = 1e-6;
        final NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);

        for (double testProbability = 0.0; testProbability <= 1.0; testProbability += 0.1) {
            double inverseCumulativeProbability = dist.inverseCumulativeProbability(testProbability);
            assertEquals(String.format("Inverse cumulative probability for probability %f", testProbability),
                         mean + sd * FastMath.sqrt(FastMath.LOG2E) * Math.erfInverse(testProbability / 2.0),
                         inverseCumulativeProbability,
                         inverseCumulativeAccuracy);
        }
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_MeanAndSD_InverseCumulativeProbability_InvalidProbability() {
        final double mean = 4.5;
        final double sd = 2.5;
        final NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);

        dist.inverseCumulativeProbability(-1.0);
    }

    @Test
    public void testNormalDistributionImpl_MeanAndSD_InverseCumulativeProbability_Zero() throws MathException {
        final double mean = 4.5;
        final double sd = 2.5;
        final NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);

        assertEquals(mean, dist.inverseCumulativeProbability(0.0), 0.0);
    }

    @Test
    public void testNormalDistributionImpl_MeanAndSD_InverseCumulativeProbability_One() throws MathException {
        final double mean = 4.5;
        final double sd = 2.5;
        final NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);

        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(1.0), 0.0);
    }

    @Test(expected = NoSuchElementException.class)
    public void testNormalDistributionImpl_MeanAndSD_InverseCumulativeProbability_InvalidProbability_NotSupported() {
        final double mean = 4.5;
        final double sd = 2.5;
        final NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);

        dist.inverseCumulativeProbability(0.51);
    }

    @Test
    public void testNormalDistributionImpl_ZeroMeanAndOneSD() {
        final NormalDistributionImpl dist = new NormalDistributionImpl();

        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);

        assertEquals(0.5, dist.density(0.0), 0.0001);
        assertEquals(0.341344746, dist.density(0.5), 0.0001);
        assertEquals(0.154304301, dist.density(1.0), 0.0001);

        assertEquals(0.5, dist.cumulativeProbability(0.0), 0.01);
        assertEquals(0.841344746, dist.cumulativeProbability(0.5), 0.01);
        assertEquals(0.997300299, dist.cumulativeProbability(1.0), 0.01);

        assertEquals(0.0, dist.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(0.5, dist.inverseCumulativeProbability(0.5), 0.0);
        assertEquals(1.0, dist.inverseCumulativeProbability(1.0), 0.0);
    }
}