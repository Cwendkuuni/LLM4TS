package org.apache.commons.math.distribution;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.stat.descript.NumberStatistics;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testConstructorDefault() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(1.0, distribution.getMean(), 0.0);
    }

    @Test
    public void testConstructorCustom() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.5, 1.0e-15, 10000000);
        assertEquals(5.5, distribution.getMean(), 0.0);
    }

    @Test
    public void testConstructorCustomConvergenceCriterion() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.5, 1.0e-13, 10000000);
        assertEquals(5.5, distribution.getMean(), 0.0);
    }

    @Test
    public void testConstructorCustomMaxIterations() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.5, 1.0e-15, 1000);
        assertEquals(5.5, distribution.getMean(), 0.0);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorInvalidMean() {
        new PoissonDistributionImpl(0.0);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);

        assertEquals(0.2400985378, distribution.probability(0), 0.00001);
        assertEquals(0.3679243559, distribution.probability(1), 0.00001);
        assertEquals(0.1771156641, distribution.probability(2), 0.00001);
        assertEquals(0.0981976711, distribution.probability(3), 0.00001);
        assertEquals(0.0497979981, distribution.probability(4), 0.00001);
        assertEquals(0.0248014462, distribution.probability(5), 0.00001);
        assertEquals(0.0121646265, distribution.probability(6), 0.00001);
        assertEquals(0.0061272774, distribution.probability(7), 0.00001);
        assertEquals(0.0030776275, distribution.probability(8), 0.00001);
        assertEquals(0.0015403966, distribution.probability(9), 0.00001);
        assertEquals(0.0007715271, distribution.probability(10), 0.00001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testCumulativeProbabilityTooLarge() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        distribution.cumulativeProbability(Integer.MAX_VALUE + 1);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityInvalidX() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        distribution.cumulativeProbability(-1);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);

        assertEquals(0.0, distribution.cumulativeProbability(0), 0.00001);
        assertEquals(0.2400985378, distribution.cumulativeProbability(0
                + Math.ceil(distribution.probability(1) * 1000)
                % 1000), 0.00001);
        assertEquals(0.5881751721, distribution.cumulativeProbability(0
                + Math.ceil(distribution.probability(1) * 1000)
                * 2 % 1000), 0.00001);
        assertEquals(0.9602683768, distribution.cumulativeProbability(0
                + Math.ceil(distribution.probability(1) * 1000)
                * 3 % 1000), 0.00001);
        assertEquals(1.0, distribution.cumulativeProbability(Integer.MAX_VALUE), 0.00001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);

        assertEquals(0.2441963445, distribution.normalApproximateProbability(0), 0.00001);
        assertEquals(0.3657229771, distribution.normalApproximateProbability(1), 0.00001);
        assertEquals(0.3657229771, distribution.normalApproximateProbability(2), 0.00001);
        assertEquals(0.2536235289, distribution.normalApproximateProbability(3), 0.00001);
        assertEquals(0.1799789851, distribution.normalApproximateProbability(4), 0.00001);
        assertEquals(0.1190807931, distribution.normalApproximateProbability(5), 0.00001);
        assertEquals(0.0737799648, distribution.normalApproximateProbability(6), 0.00001);
        assertEquals(0.0469012655, distribution.normalApproximateProbability(7), 0.00001);
        assertEquals(0.0293785150, distribution.normalApproximateProbability(8), 0.00001);
        assertEquals(0.0179848596, distribution.normalApproximateProbability(9), 0.00001);
        assertEquals(0.0107372578, distribution.normalApproximateProbability(10), 0.00001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testCumulativeProbabilityMaxIterationsExceeded() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0, 1.0e-15, 1);
        distribution.cumulativeProbability(1);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityConvergenceCriterionNotMet() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0, 1.0e-15, 10000001);
        distribution.cumulativeProbability(1);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        Random random = new Random();

        for (int i = 0; i < 10000; i++) {
            assertEquals(distribution.sample(), distribution.probability(distribution.sample()), 1.0);
        }
    }

    @Test
    public void testSampleCompareToProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        Random random = new Random();

        for (int i = 0; i < 10000; i++) {
            int sample = distribution.sample();
            double probability = distribution.probability(sample);

            assertTrue(probability > 0.0);
            assertTrue(sample >= 0);
            assertTrue(sample <= Integer.MAX_VALUE);
            assertTrue(Math.abs(sample - distribution.sample()) < 1);
        }
    }

    @Test
    public void testSampleStatistics() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        Random random = new Random();
        int sampleSize = 100000;

        int[] samples = new int[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            samples[i] = distribution.sample();
        }

        NumberStatistics stats = new NumberStatistics();
        stats.addValue(samples);

        assertEquals(1.0, stats.getMean(), 0.001);
        assertEquals(1.0, stats.getStandardDeviation(), 0.001);
    }
}