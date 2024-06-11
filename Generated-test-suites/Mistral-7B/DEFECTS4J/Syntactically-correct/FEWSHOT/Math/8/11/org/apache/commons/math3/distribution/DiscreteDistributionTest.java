package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.DiscreteUniformDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link DiscreteDistribution}.
 */
public class DiscreteDistributionTest {
    private static final double DELTA = 1e-10;

    private RandomGenerator rng;
    private DiscreteDistribution<Integer> dist;

    @Before
    public void setUp() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(0, 0.1));
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.2));
        samples.add(new Pair<>(3, 0.4));

        rng = new Well19937c();
        try {
            dist = new DiscreteDistribution<>(rng, samples);
        } catch (MathIllegalArgumentException | MathArithmeticException | NotPositiveException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testProbability() {
        assertEquals(0.1, dist.probability(0), DELTA);
        assertEquals(0.3, dist.probability(1), DELTA);
        assertEquals(0.2, dist.probability(2), DELTA);
        assertEquals(0.4, dist.probability(3), DELTA);
        assertNull(dist.probability(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityNullValue() {
        dist.probability(null);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = dist.getSamples();
        List<Pair<Integer, Double>> expected = Collections.unmodifiableList(samples);
        assertEquals(4, samples.size());
        assertEquals(0.1, samples.get(0).getValue(), DELTA);
        assertEquals(0.3, samples.get(1).getValue(), DELTA);
        assertEquals(0.2, samples.get(2).getValue(), DELTA);
        assertEquals(0.4, samples.get(3).getValue(), DELTA);
        assertArrayEquals(singleton(-1), samples.toArray(new Pair[0]));
    }

    @Test
    public void testSample() {
        final int expected = 1;
        assertEquals(expected, dist.sample().intValue());
        assertEquals(expected, dist.sample().intValue());
        assertEquals(expected, dist.sample().intValue());
    }

    @Test
    public void testSampleNullValue() {
        assertNull(dist.sample());
    }

    @Test
    public void testSampleList() {
        final int sampleSize = 10;
        final T[] expected = new T[sampleSize];

        for (int i = 0; i < sampleSize; i++) {
            expected[i] = dist.sample();
        }

        final T[] result = dist.sample(sampleSize);
        assertNotSame(expected, result);
        assertArrayEquals(expected, result);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSize() {
        dist.sample(-1);
    }

    @Test
    public void testReseedGenerator() {
        final long seed = 1234L;
        dist.reseedRandomGenerator(seed);

        final List<Pair<Integer, Double>> samples = dist.getSamples();
        final T sample1 = dist.sample();
        final T sample2 = dist.sample();

        // Generate new samples after reseeding
        dist.reseedRandomGenerator(seed + 1);
        final List<Pair<Integer, Double>> newSamples = dist.getSamples();

        // Check that the distribution has not changed
        assertEquals(samples.size(), newSamples.size());
        for (int i = 0; i < samples.size(); i++) {
            final Pair<Integer, Double> sample = samples.get(i);
            final Pair<Integer, Double> newSample = newSamples.get(i);
            assertEquals(sample.getKey(), newSample.getKey());
            assertEquals(Precision.isClose(sample.getValue(), newSample.getValue(), DELTA), true);
        }
        // Check that new samples are different from previous ones
        assertNotSame(sample1, sample2);
        assertNotSame(sample1, dist.sample());
    }

    @Test
    public void testDiscreteUniformDistribution() {
        final int minValue = 0;
        final int maxValue = 5;
        final int sampleSize = 10;

        final RandomGenerator rng = new Well19937c();
        final DiscreteDistribution<Integer> dist = new DiscreteUniformDistribution<>(rng, minValue,
                maxValue + 1);

        final T[] result = dist.sample(sampleSize);
        final int[] expected = new int[sampleSize];

        for (int i = 0; i < sampleSize; i++) {
            expected[i] = minValue + (int) (Math.random() * ((maxValue - minValue) + 1));
            assertTrue(minValue <= expected[i] && expected[i] <= maxValue);
        }

        assertArrayEquals(expected, result);
    }

    private static Pair<Integer, Integer> singleton(int value) {
        return new Pair<Integer, Integer>(value, 1);
    }
}