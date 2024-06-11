package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.DiscreteDistribution.Pair;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.PairUtils;

public class DiscreteDistributionTest {

    private static final RandomGenerator rng = new Well19937c();

    @Test
    public void testDiscreteDistribution() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.1),
                new Pair<>(Integer.valueOf(2), 0.3),
                new Pair<>(Integer.valueOf(3), 0.2),
                new Pair<>(Integer.valueOf(4), 0.4),
                new Pair<>(Integer.valueOf(5), 0.1));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(rng, samples);

        List<Pair<Integer, Double>> expectedSamples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.1),
                new Pair<>(Integer.valueOf(2), 0.3),
                new Pair<>(Integer.valueOf(3), 0.2),
                new Pair<>(Integer.valueOf(4), 0.4),
                new Pair<>(Integer.valueOf(5), 0.1));

        List<Pair<Integer, Double>> samplesList = distribution.getSamples();
        assertEquals(expectedSamples.size(), samplesList.size());
        assertTrue(samplesList.equals(expectedSamples));

        // test probabilities
        double totalProbability = 0.0;
        for (Pair<Integer, Double> pair : samplesList) {
            totalProbability += pair.getValue();
        }
        assertEquals(1.0, totalProbability, 1e-6);

        // test probability method
        Integer sample = Integer.valueOf(3);
        double probability = distribution.probability(sample);
        assertEquals(0.2, probability, 1e-6);

        // test sample method
        Integer distributedSample = distribution.sample();
        List<Integer> samplesList2 = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            samplesList2.add(distribution.sample());
        }
        assertArrayEquals(samples.stream().mapToInt(i -> i.getKey().intValue()).toArray(),
                samplesList2.stream().mapToInt(i -> i.intValue()).toArray());
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), -0.1),
                new Pair<>(Integer.valueOf(2), 0.3),
                new Pair<>(Integer.valueOf(3), 0.2),
                new Pair<>(Integer.valueOf(4), 0.4),
                new Pair<>(Integer.valueOf(5), 0.1));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(rng, samples);
        } catch (Exception e) {
            // expected
        }
        assertNull(distribution);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInfiniteProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), Double.POSITIVE_INFINITY),
                new Pair<>(Integer.valueOf(2), 0.3),
                new Pair<>(Integer.valueOf(3), 0.2),
                new Pair<>(Integer.valueOf(4), 0.4),
                new Pair<>(Integer.valueOf(5), 0.1));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(rng, samples);
        } catch (Exception e) {
            // expected
        }
        assertNull(distribution);
    }

    @Test(expected = MathArithmeticException.class)
    public void testZeroProbability() {
        List<Pair<Integer, Double>> samples = Collections.singletonList(
                new Pair<>(Integer.valueOf(1), 0.0));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(rng, samples);
        } catch (Exception e) {
            // expected
        }
        assertNull(distribution);
    }

    @Test
    public void testSampleSameValueTwice() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.1),
                new Pair<>(Integer.valueOf(1), 0.3),
                new Pair<>(Integer.valueOf(2), 0.2),
                new Pair<>(Integer.valueOf(3), 0.1));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(rng, samples);

        List<Integer> samplesList = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            samplesList.add(distribution.sample());
        }

        int currentValue = distribution.sample().intValue();
        int count = 0;
        for (int value : samplesList) {
            if (value.intValue() == currentValue) {
                count++;
            }
            currentValue = value.intValue();
        }

        double expectedProbability = distribution.probability(currentValue);
        double relativeError = Math.abs(expectedProbability - 1.0 / samplesList.size()) / expectedProbability;
        assertTrue(relativeError < 0.01);
    }

    @Test
    public void testSampleDifferentValues() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.1),
                new Pair<>(Integer.valueOf(2), 0.3),
                new Pair<>(Integer.valueOf(3), 0.2),
                new Pair<>(Integer.valueOf(4), 0.4));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(rng, samples);

        List<Integer> samplesList = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            samplesList.add(distribution.sample());
        }

        List<Integer> uniqueValues = new ArrayList<>();
        for (int value : samplesList) {
            if (!uniqueValues.contains(value)) {
                uniqueValues.add(value);
            }
        }

        assertEquals(samples.size(), uniqueValues.size());
    }

    @Test
    public void testSampleSameSizeAsInput() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.1),
                new Pair<>(Integer.valueOf(2), 0.3),
                new Pair<>(Integer.valueOf(3), 0.2),
                new Pair<>(Integer.valueOf(4), 0.4));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(rng, samples);

        T[] output = distribution.sample(samples.size());

        // check that all values are present in the output
        for (Pair<Integer, Double> sample : samples) {
            boolean found = false;
            for (int i = 0; i < samples.size(); i++) {
                if (output[i].equals(sample.getKey())) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSampleSize() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.1),
                new Pair<>(Integer.valueOf(2), 0.3),
                new Pair<>(Integer.valueOf(3), 0.2),
                new Pair<>(Integer.valueOf(4), 0.4));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(rng, samples);

        distribution.sample(-1);
    }
}