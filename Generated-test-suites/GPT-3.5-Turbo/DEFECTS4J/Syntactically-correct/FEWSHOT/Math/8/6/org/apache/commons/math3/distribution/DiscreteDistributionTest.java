package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    private Well19937c rng;

    @Before
    public void setup() {
        rng = new Well19937c();
    }

    @Test
    public void testProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);

        assertEquals(0.2, distribution.probability(1), 0.0001);
        assertEquals(0.3, distribution.probability(2), 0.0001);
        assertEquals(0.5, distribution.probability(3), 0.0001);
        assertEquals(0.0, distribution.probability(4), 0.0001);
        assertEquals(0.0, distribution.probability(null), 0.0001);
    }

    @Test
    public void testGetSamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("a", 0.3));
        samples.add(new Pair<>("b", 0.7));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(rng, samples);

        List<Pair<String, Double>> expected = new ArrayList<>();
        expected.add(new Pair<>("a", 0.3));
        expected.add(new Pair<>("b", 0.7));

        assertEquals(expected, distribution.getSamples());
    }

    @Test
    public void testSample() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            samples.add(new Pair<>(i, 1.0));
        }

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);

        for (int i = 0; i < 100; i++) {
            int sample = distribution.sample();

            assertEquals(i, sample);
        }
    }

    @Test
    public void testSampleWithSampleSize() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            samples.add(new Pair<>(i, 1.0));
        }

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);

        int sampleSize = 10;
        Integer[] sample = distribution.sample(sampleSize);

        assertEquals(sampleSize, sample.length);

        for (int i = 0; i < sampleSize; i++) {
            assertEquals(i, (int)sample[i]);
        }
    }

}