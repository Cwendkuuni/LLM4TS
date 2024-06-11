package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.DiscreteDistribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.PairFactory;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistribution() throws Exception {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(1), 0.25));
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(2), 0.33));
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(3), 0.42));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        assertEquals(new Well19937c(), distribution.random);
        assertEquals(Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)), distribution.singletons);
        assertArrayEquals(new double[] {0.25, 0.33, 0.42}, distribution.probabilities, 0.0);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeProbability() throws Exception {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(1), -0.25));
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(2), 0.33));
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(3), 0.42));
        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testSumZeroProbability() throws Exception {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(1), 0.25));
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(2), -0.33));
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(3), 0.42));
        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInfiniteProbability() throws Exception {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(1), Double.POSITIVE_INFINITY));
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(2), 0.33));
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(3), 0.42));
        new DiscreteDistribution<>(samples);
    }

    @Test
    public void testReseedRandomGenerator() throws Exception {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(1), 0.25));
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(2), 0.33));
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(3), 0.42));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        final long seed = System.nanoTime();
        distribution.reseedRandomGenerator(seed);
    }

    @Test
    public void testProbability() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(1), 0.25));
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(2), 0.33));
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(3), 0.42));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        assertEquals(0.25, distribution.probability(Integer.valueOf(1)), 0.01);
        assertEquals(0.33, distribution.probability(Integer.valueOf(2)), 0.01);
        assertEquals(0.42, distribution.probability(Integer.valueOf(3)), 0.01);
        assertNull(distribution.probability(Integer.valueOf(4)));
    }

    @Test
    public void testGetSamples() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(1), 0.25));
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(2), 0.33));
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(3), 0.42));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        assertEquals(samples, distribution.getSamples());
    }

    @Test
    public void testSample() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(1), 0.25));
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(2), 0.33));
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(3), 0.42));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        final Integer sample = distribution.sample();
        assertSame(samples.get(0).getKey(), sample);
    }

    @Test
    public void testSampleArray() throws NotStrictlyPositiveException {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(1), 0.25));
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(2), 0.33));
        samples.add(new PairFactory<Integer, Double>().create(Integer.valueOf(3), 0.42));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        final Integer[] sample = distribution.sample(3);
        assertArrayEquals(new Integer[] {samples.get(0).getKey(), samples.get(0).getKey(), samples.get(1).getKey()}, sample, 0);
    }
}