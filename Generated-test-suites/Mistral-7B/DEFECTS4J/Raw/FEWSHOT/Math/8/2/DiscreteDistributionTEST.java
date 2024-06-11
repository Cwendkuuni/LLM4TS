package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    private static final double DELTA = 1e-12;

    @Test
    public void testProbabilityNullValue() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(null, 0.1),
                new Pair<>(Integer.valueOf(1), 0.2));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        assertNull(dist.probability(null));
    }

    @Test
    public void testProbabilityValidValue() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.2),
                new Pair<>(Integer.valueOf(2), 0.3),
                new Pair<>(Integer.valueOf(3), 0.5));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        assertEquals(0.2, dist.probability(Integer.valueOf(1)), DELTA);
    }

    @Test
    public void testProbabilitySameValueMultipleTimes() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.3),
                new Pair<>(Integer.valueOf(1), 0.5),
                new Pair<>(Integer.valueOf(1), 0.2));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        assertEquals(0.106, dist.probability(Integer.valueOf(1)), DELTA);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), -0.1),
                new Pair<>(Integer.valueOf(2), 0.2));
        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testSumToZeroProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.2),
                new Pair<>(Integer.valueOf(2), -0.2));
        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInfiniteProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), Double.POSITIVE_INFINITY),
                new Pair<>(Integer.valueOf(2), 0.2));
        new DiscreteDistribution<>(samples);
    }

    @Test
    public void testGetSamplesEqualObjects() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.2),
                new Pair<>(Integer.valueOf(1), 0.3));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        List<Pair<Integer, Double>> expectedSamples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.2),
                new Pair<>(Integer.valueOf(1), 0.3));
        List<Pair<Integer, Double>> actualSamples = dist.getSamples();
        assertEquals(expectedSamples.size(), actualSamples.size());
        for (int i = 0; i < expectedSamples.size(); i++) {
            assertEquals(expectedSamples.get(i).getKey(), actualSamples.get(i).getKey());
            assertEquals(expectedSamples.get(i).getValue(), actualSamples.get(i).getValue(), DELTA);
        }
    }

    @Test
    public void testSampleSingleValue() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 1.0));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        Integer actualSample = dist.sample();
        Integer expectedSample = Integer.valueOf(1);
        assertEquals(expectedSample, actualSample);
    }

    @Test
    public void testSampleMultipleValues() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.3),
                new Pair<>(Integer.valueOf(2), 0.4),
                new Pair<>(Integer.valueOf(3), 0.3));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        List<Integer> actualSamples = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            actualSamples.add(dist.sample());
        }
        List<Integer> expectedSamples = Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(2), Integer.valueOf(2), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(3), Integer.valueOf(3), Integer.valueOf(3), Integer.valueOf(3));
        assertArrayEquals(expectedSamples.toArray(), actualSamples.toArray());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSampleSize() {
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(new ArrayList<>());
        dist.sample(-1);
    }

    @Test
    public void testSampleWithNullGenerator() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.3),
                new Pair<>(Integer.valueOf(2), 0.4),
                new Pair<>(Integer.valueOf(3), 0.3));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        assertNull(dist.random);
    }

    @Test
    public void testSampleWithNotNullGenerator() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.3),
                new Pair<>(Integer.valueOf(2), 0.4),
                new Pair<>(Integer.valueOf(3), 0.3));
        RandomGenerator rng = new Well19937c();
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(rng, samples);
        assertNotNull(dist.random);
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.3),
                new Pair<>(Integer.valueOf(2), 0.4),
                new Pair<>(Integer.valueOf(3), 0.3));
        RandomGenerator rng = new Well19937c();
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(rng, samples);
        long seed = rng.nextLong();
        dist.reseedRandomGenerator(seed);
        long newSeed = rng.nextLong();
        assertTrue(newSeed != seed);
    }
}