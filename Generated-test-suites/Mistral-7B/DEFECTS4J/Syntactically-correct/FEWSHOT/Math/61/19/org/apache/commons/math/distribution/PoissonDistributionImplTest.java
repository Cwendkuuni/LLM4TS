package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.stat.descript.moment.StandardDeviation;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomDataGenerator;

import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorNegativeMean() {
        new PoissonDistributionImpl(-1);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        assertEquals(0.0, dist.probability(0), 1e-15);
    }

    @Test
    public void testProbabilityNonZero() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        int x = 1;
        double p = dist.probability(x);
        assertEquals(0.36787944115, p, 1e-15);
    }

    @Test
    public void testProbabilityMaxValue() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        int x = Integer.MAX_VALUE;
        double p = dist.probability(x);
        assertEquals(0.0, p, 1e-15);
    }

    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        double p = dist.cumulativeProbability(0);
        assertEquals(0.0, p, 1e-15);
    }

    @Test
    public void testCumulativeProbabilitySmall() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        double p = dist.cumulativeProbability(1);
        assertEquals(0.36787944115, p, 1e-15);
    }

    @Test
    public void testCumulativeProbabilityLarge() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(100.0);
        double p = dist.cumulativeProbability(100);
        assertEquals(1.0, p, 1e-15);
    }

    @Test
    public void testNormalApproximateProbabilityZero() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        double p = dist.normalApproximateProbability(0);
        assertEquals(0.0, p, 1e-15);
    }

    @Test
    public void testNormalApproximateProbabilitySmall() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        int x = 1;
        double p = dist.normalApproximateProbability(x);
        assertEquals(0.36787944115, p, 1e-15);
    }

    @Test
    public void testNormalApproximateProbabilityLarge() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(100.0);
        int x = 100;
        double p = dist.normalApproximateProbability(x);
        assertEquals(1.0, p, 1e-15);
    }

    @Test(expected = MathRuntimeException.class)
    public void testSampleNegative() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        dist.sample();
        throw new AssertionError(); // should not reach this point
    }

    @Test
    public void testSampleZero() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        int sample = dist.sample();
        assertEquals(0, sample);
    }

    @Test
    public void testSampleOne() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        int sample = dist.sample();
        assertEquals(1, sample);
    }

    @Test
    public void testSampleTen() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(10.0);
        int sample = dist.sample();
        assertTrue(sample >= 0 && sample <= 20);
    }

    @Test
    public void testMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        assertEquals(1.0, dist.getMean(), 1e-15);
    }

    @Test
    public void testVariance() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        assertEquals(1.0, dist.getVariance(), 1e-15);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        double p = 0.8;
        int result = dist.inverseCumulativeProbability(p);
        assertEquals(2, result);
    }

    @Test
    public void testSampleAccuracy() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(10.0);
        RandomDataGenerator rnd = new RandomDataGenerator();
        int sampleSum = 0;
        int sampleCount = 100000;
        for (int i = 0; i < sampleCount; i++) {
            sampleSum += dist.sample();
        }
        double mean = (double) sampleSum / sampleCount;
        assertEquals(10.0, mean, 1e-3);
    }

    @Test
    public void testSampleAccuracySmallMean() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        RandomDataGenerator rnd = new RandomDataGenerator();
        int sampleSum = 0;
        int sampleCount = 100000;
        for (int i = 0; i < sampleCount; i++) {
            sampleSum += dist.sample();
        }
        double mean = (double) sampleSum / sampleCount;
        assertEquals(0.5, mean, 1e-3);
    }

    @Test
    public void testSampleAccuracyLargeMean() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(100.0);
        RandomDataGenerator rnd = new RandomDataGenerator();
        int sampleSum = 0;
        int sampleCount = 100000;
        for (int i = 0; i < sampleCount; i++) {
            sampleSum += dist.sample();
        }
        double mean = (double) sampleSum / sampleCount;
        assertEquals(100.0, mean, 1e-2);
    }

    @Test
    public void testSampleAndCumulativeProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = 0.0;
        int sample = dist.sample();
        while (sample < 5) {
            cumulativeProbability += dist.probability(sample++);
        }
        assertEquals(0.15494956533, cumulativeProbability, 1e-15);
    }
}