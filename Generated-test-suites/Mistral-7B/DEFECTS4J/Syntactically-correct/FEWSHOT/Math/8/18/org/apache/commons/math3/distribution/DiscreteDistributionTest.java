package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testProbabilityZeroNegativeAndInfiniteValues() {
        List<Pair<Object, Double>> samples = Arrays.asList(
                new Pair<>(null, -1.0),
                new Pair<>(1, Double.POSITIVE_INFINITY),
                new Pair<>(2.0, 0.0)
        );

        assertThrows(NotPositiveException.class, () -> new DiscreteDistribution<Object>(samples));
    }

    @Test
    public void testProbabilitiesSumToZero() {
        List<Pair<Object, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.1),
                new Pair<>(3, -0.1)
        );

        assertThrows(MathArithmeticException.class, () -> new DiscreteDistribution<Object>(samples));
    }

    @Test
    public void testProbabilitiesSumToOne() {
        List<Pair<Object, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5)
        );

        DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(new Well19937c(), samples);

        double sum = 0;
        for (double probability : distribution.probabilities) {
            sum += probability;
        }

        assertEquals(1.0, sum, 1e-12);
    }

    @Test
    public void testProbabilityOfNullValue() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(null, 0.1),
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.1),
                new Pair<>(3, 0.1)
        );

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);

        assertNull(distribution.probability(null));
    }

    @Test
    public void testProbabilityOfSingleValue() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 1.0)
        );

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);

        assertEquals(1.0, distribution.probability(1), 1e-12);
    }

    @Test
    public void testProbabilityOfMultipleValues() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5)
        );

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);

        double probabilityOfOne = distribution.probability(1);
        double probabilityOfTwo = distribution.probability(2);
        double probabilityOfThree = distribution.probability(3);

        assertEquals(0.2, probabilityOfOne, 1e-12);
        assertEquals(0.3, probabilityOfTwo, 1e-12);
        assertEquals(0.5, probabilityOfThree, 1e-12);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5)
        );

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);

        List<Pair<Integer, Double>> getSamples = distribution.getSamples();

        assertEquals(samples.size(), getSamples.size());

        for (int i = 0; i < samples.size(); i++) {
            Pair<Integer, Double> sample = samples.get(i);
            Pair<Integer, Double> sampleFromGetSamples = getSamples.get(i);

            assertEquals(sample.getKey(), sampleFromGetSamples.getKey());
            assertEquals(sample.getValue(), sampleFromGetSamples.getValue(), 1e-12);
        }
    }

    @Test
    public void testSampleSingleValue() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 1.0)
        );

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);

        Integer sample = distribution.sample();

        assertEquals(1, sample.intValue());
    }

    @Test
    public void testSampleMultipleValues() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5)
        );

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);

        Integer[] samplesArray = distribution.sample(10);

        int countOfOne = 0;
        int countOfTwo = 0;
        int countOfThree = 0;

        for (Integer sample : samplesArray) {
            if (sample.intValue() == 1) {
                countOfOne++;
            } else if (sample.intValue() == 2) {
                countOfTwo++;
            } else if (sample.intValue() == 3) {
                countOfThree++;
            }
        }

        assertEquals(2, countOfOne);
        assertEquals(3, countOfTwo);
        assertEquals(5, countOfThree);
    }

    @Test
    public void testReSeedRandomGenerator() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 1.0),
                new Pair<>(2, 0.5),
                new Pair<>(3, 0.3)
        );

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);

        long seed = 123456L;
        distribution.reseedRandomGenerator(seed);

        Integer sample = distribution.sample();

        Integer sampleBeforeReSeed = distribution.sample();
        Integer sampleAfterReSeed = distribution.sample();

        assertNotSame(sampleBeforeReSeed, sample);
        assertNotSame(sampleAfterReSeed, sampleBeforeReSeed);
        assertNotSame(sample, sampleBeforeReSeed);
    }
}