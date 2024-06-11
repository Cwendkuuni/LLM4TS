package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.DiscreteDistribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Pair;

public class DiscreteDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroSampleSize() {
        new DiscreteDistribution(new ArrayList<>());
    }

    @Test
    public void testConstructorWithValidSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        RandomGenerator rng = new Well19937c();
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);

        assertEquals(3, distribution.probabilities.length);
        assertEquals(3, distribution.singletons.size());

        assertEquals(0.3, distribution.probability(1), 0.001);
        assertEquals(0.4, distribution.probability(2), 0.001);
        assertEquals(0.3, distribution.probability(3), 0.001);

        assertEquals(samples, distribution.getSamples());

        assertArrayEquals(new Integer[]{1, 1, 1, 1, 1}, distribution.sample(5).asIterable().toArray());
    }

    @Test
    public void testConstructorWithNegativeProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        assertThrows(NotPositiveException.class, () -> new DiscreteDistribution<>(new ArrayList<>(), samples));
    }

    @Test
    public void testConstructorWithInfProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        assertThrows(MathIllegalArgumentException.class, () -> new DiscreteDistribution<>(new ArrayList<>(), samples));
    }

    @Test
    public void testConstructorWithZeroProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.0));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        assertThrows(MathArithmeticException.class, () -> new DiscreteDistribution<>(new ArrayList<>(), samples));
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        RandomGenerator rng = new Well19937c();
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);

        long seed = 123456789;
        distribution.reseedRandomGenerator(seed);

        RandomGenerator reseededRng = new Well19937c(seed);
        DiscreteDistribution<Integer> reseededDistribution = new DiscreteDistribution<>(reseededRng, samples);

        assertEquals(distribution.probabilities, reseededDistribution.probabilities);
        assertEquals(distribution.singletons, reseededDistribution.singletons);
    }

    @Test
    public void testProbabilityNullSingleton() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(null, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        assertThrows(MathIllegalArgumentException.class, () -> new DiscreteDistribution<>(new ArrayList<>(), samples));
    }

    @Test
    public void testProbabilityNullProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, null));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        assertThrows(MathIllegalArgumentException.class, () -> new DiscreteDistribution<>(new ArrayList<>(), samples));
    }

    @Test
    public void testProbabilityValidSingleton() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new ArrayList<>(), samples);

        assertEquals(0.3, distribution.probability(1), 0.001);
    }

    @Test
    public void testSampleNullSingleton() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(null, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        assertThrows(MathIllegalArgumentException.class, () -> new DiscreteDistribution<>(new ArrayList<>(), samples));
    }

    @Test
    public void testSampleValidSingleton() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new ArrayList<>(), samples);

        assertEquals(1, (int) distribution.sample());
    }

    @Test
    public void testSampleMultipleTimes() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new ArrayList<>(), samples);

        List<Integer> results = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            results.add((int) distribution.sample());
        }

        List<Integer> expected = new ArrayList<>();
        for (int value : samples.stream().map(pair -> pair.getKey()).toList()) {
            for (int i = 0; i < 33; i++) {
                expected.add(value);
            }
        }

        assertEquals(expected, results);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new ArrayList<>(), samples);

        distribution.sample(-1);
    }
}