package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testProbabilityZero() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(0, 0.5),
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        double probability = distribution.probability(0);
        assertEquals(0.5, probability, 0.001);
    }

    @Test
    public void testProbabilityOne() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(0, 0.5),
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        double probability = distribution.probability(1);
        assertEquals(0.2, probability, 0.001);
    }

    @Test
    public void testProbabilityTwo() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(0, 0.5),
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        double probability = distribution.probability(2);
        assertEquals(0.3, probability, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(0, -0.5),
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testSumToZero() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(0, -0.5),
                new Pair<>(1, 0.2),
                new Pair<>(2, -0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInfProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(0, 1.0),
                new Pair<>(1, 0.2),
                new Pair<>(2, Double.POSITIVE_INFINITY));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeSample() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(0, 0.5),
                new Pair<>(1, 0.2),
                new Pair<>(-1, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(0, 0.5),
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        List<Pair<Integer, Double>> result = distribution.getSamples();
        assertEquals(samples.size(), result.size());
        for (int i = 0; i < samples.size(); i++) {
            assertEquals(samples.get(i).getKey(), result.get(i).getKey());
            assertEquals(samples.get(i).getValue(), result.get(i).getValue(), 0.001);
        }
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(0, 0.5),
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        int sampledValue;
        for (int i = 0; i < 1000; i++) {
            sampledValue = distribution.sample().intValue();
            assertTrue(Arrays.binarySearch(singletonList(0).concat(singletonList(1)
                    .concat(singletonList(2))).toArray(Integer[]
                    .class), sampledValue) >= 0);
        }
    }

    @Test
    public void testSampleArray() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(0, 0.5),
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        int sampleSize = 10;
        Integer[] samplesArray = distribution.sample(sampleSize);

        List<Integer> expected = new ArrayList<>();
        for (int i = 0; i < sampleSize; i++) {
            expected.add(distribution.sample().intValue());
        }

        Arrays.sort(samplesArray);
        for (int i = 0; i < sampleSize; i++) {
            assertEquals(expected.get(i), samplesArray[i]);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSampleSize() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(0, 0.5),
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        distribution.sample(-1);
    }
}