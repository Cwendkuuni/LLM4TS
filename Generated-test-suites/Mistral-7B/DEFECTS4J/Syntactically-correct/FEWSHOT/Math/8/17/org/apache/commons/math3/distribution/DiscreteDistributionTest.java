package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.DiscreteUniformDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

public class DiscreteDistributionTest {

    @SuppressWarnings("unchecked")
    private static <T> DiscreteDistribution<T> createDiscreteDistribution(T[] values) {
        List<Pair<T, Double>> samples = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            samples.add(new Pair<>(values[i], 1. / values.length));
        }
        return new DiscreteDistribution<T>(new Well19937c(), samples);
    }

    @Test
    public void testConstructorWithSamples() {
        final T[] values = {"A", "B", "C"};
        DiscreteDistribution<T> distribution = new DiscreteDistribution<T>(Arrays.asList(
                new Pair<T, Double>("A", 0.33),
                new Pair<T, Double>("B", 0.44),
                new Pair<T, Double>("C", 0.23)
        ));
        assertEquals(values.length, distribution.singletons.size());
        assertEquals(values.length, distribution.probabilities.length);
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], distribution.singletons.get(i));
            assertEquals(distribution.probabilities[i], 1. / values.length, 0.0);
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeProbabilities() {
        List<Pair<T, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(null, -1.));
        new DiscreteDistribution<T>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithZeroProbabilities() {
        List<Pair<T, Double>> samples = new ArrayList<>();
        samples.add(new Pair<T, Double>(null, 0.));
        samples.add(new Pair<T, Double>(null, 0.));
        new DiscreteDistribution<T>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithInfiniteProbabilities() {
        List<Pair<T, Double>> samples = new ArrayList<>();
        samples.add(new Pair<T, Double>(null, Double.POSITIVE_INFINITY));
        new DiscreteDistribution<T>(samples);
    }

    @Test
    public void testProbability() {
        final T[] values = {"A", "B", "C"};
        DiscreteDistribution<T> distribution = createDiscreteDistribution(values);
        for (int i = 0; i < values.length; i++) {
            assertEquals(distribution.probability(values[i]), 1. / values.length, 0.0);
        }
        assertNull(distribution.probability(null));
    }

    @Test
    public void testGetSamples() {
        final T[] values = {"A", "B", "C"};
        DiscreteDistribution<T> distribution = createDiscreteDistribution(values);
        List<Pair<T, Double>> samples = distribution.getSamples();
        assertEquals(values.length, samples.size());
        for (int i = 0; i < values.length; i++) {
            assertEquals(samples.get(i).getKey(), values[i]);
            assertEquals(samples.get(i).getValue(), 1. / values.length, 0.0);
        }
    }

    @Test
    public void testSample() {
        final T[] values = {"A", "B", "C"};
        DiscreteDistribution<T> distribution = createDiscreteDistribution(values);
        for (int i = 0; i < 1000; i++) {
            T sample = distribution.sample();
            int index = Arrays.asList(values).indexOf(sample);
            assertEquals(sample, values[index]);
        }
    }

    @Test
    public void testSampleSizeOne() {
        final T[] values = {"A", "B", "C"};
        DiscreteDistribution<T> distribution = createDiscreteDistribution(values);
        T sample = distribution.sample();
        assertEquals(sample, values[0]);
    }

    @Test
    public void testSampleSizeZero() {
        final T[] values = {"A", "B", "C"};
        DiscreteDistribution<T> distribution = createDiscreteDistribution(values);
        try {
            distribution.sample(0);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testSampleArray() {
        final T[] values = {"A", "B", "C"};
        DiscreteDistribution<T> distribution = createDiscreteDistribution(values);
        T[] sample = distribution.sample(3);
        assertArrayEquals(Arrays.asList(values).subList(0, 3).toArray(), Arrays.copyOfRange(sample, 0, 3));
    }

    @Test
    public void testReseedRandomGenerator() {
        final T[] values = {"A", "B", "C"};
        DiscreteDistribution<T> distribution = createDiscreteDistribution(values);
        long seed = 12345;
        distribution.reseedRandomGenerator(seed);
        // Unpredictable, reseed should change the sequence of generated numbers
    }

    @Test
    public void testSampleSameSequenceWithSeed() {
        final T[] values = {"A", "B", "C"};
        DiscreteDistribution<T> distribution = createDiscreteDistribution(values);
        long seed = 12345;
        T[] sample1 = distribution.sample(3);
        distribution.reseedRandomGenerator(seed);
        T[] sample2 = distribution.sample(3);
        assertArrayEquals(sample1, sample2);
    }

    @Test
    public void testSampleDifferentSequenceWithoutSeed() {
        final T[] values = {"A", "B", "C"};
        DiscreteDistribution<T> distribution = createDiscreteDistribution(values);
        T[] sample1 = distribution.sample(3);
        T[] sample2 = distribution.sample(3);
        assertArrayEquals(sample1, sample2, false);
    }

    @Test
    public void testSampleDifferentSequenceWithSeed() {
        final T[] values = {"A", "B", "C"};
        DiscreteDistribution<T> distribution = createDiscreteDistribution(values);
        long seed = 12345;
        T[] sample1 = distribution.sample(3);
        distribution.reseedRandomGenerator(seed);
        T[] sample2 = distribution.sample(3);
        assertArrayEquals(sample1, sample2, false);
    }

    @Test
    public void testSampleDistribution() {
        final T[] values = {"A", "B", "C"};
        DiscreteDistribution<T> distribution = createDiscreteDistribution(values);
        T[] samples = distribution.sample(1000);
        List<Integer> frequencies = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            frequencies.add(0);
        }
        for (T sample : samples) {
            int index = Arrays.asList(values).indexOf(sample);
            frequencies.set(index, frequencies.get(index) + 1);
        }
        for (int i = 0; i < values.length; i++) {
            double expectedFrequency = 1. / values.length;
            double actualFrequency = (double) frequencies.get(i) / samples.length;
            assertEquals(expectedFrequency, actualFrequency, 0.01);
        }
    }

    @Test
    public void testSampleUniformDistribution() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(
            new ArrayList<Pair<Integer, Double>>(Arrays.asList(
                new Pair<Integer, Double>(0, 1. / 3.),
                new Pair<Integer, Double>(1, 1. / 3.),
                new Pair<Integer, Double>(2, 1. / 3.)
            ))
        );
        Integer[] samples = (Integer[]) distribution.sample(1000);
        int total = 0;
        for (int sample : samples) {
            total += sample;
        }
        int mean = total / samples.length;
        double expectedMean = 1.5;
        assertEquals(expectedMean, mean, 0.1);
    }

    @Test
    public void testSampleDiscreteUniformDistribution() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(
            new ArrayList<Pair<Integer, Double>>(Arrays.asList(
                new Pair<Integer, Double>(0, 1. / 3.),
                new Pair<Integer, Double>(1, 1. / 3.),
                new Pair<Integer, Double>(2, 1. / 3.)
            ))
        );
        Integer[] samples = (Integer[]) distribution.sample(1000);
        int total = 0;
        for (int sample : samples) {
            total += sample;
        }
        int mean = total / samples.length;
        DiscreteUniformDistribution<Integer> uniformDistribution = new DiscreteUniformDistribution<Integer>(0, 2);
        int uniformMean = uniformDistribution.getN(1000);
        assertEquals(mean, uniformMean, 0.1);
    }
}