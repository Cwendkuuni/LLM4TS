package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;

public class DiscreteDistributionTest {

    @Test
    public void testCreateWithValidArguments() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        assertNotNull(distribution);
        assertEquals(3, distribution.probabilities.length);
        assertTrue(Arrays.equals(new Double[]{0.3, 0.4, 0.3}, distribution.probabilities, 0.001));
        assertEquals(3, distribution.singletons.size());
        assertEquals(1, distribution.singletons.get(0));
        assertEquals(2, distribution.singletons.get(1));
        assertEquals(3, distribution.singletons.get(2));
    }

    @Test(expected = NotPositiveException.class)
    public void testCreateWithNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCreateWithZeroProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0));
        samples.add(new Pair<>(3, 0.4));

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testCreateWithInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        assertEquals(0.3, distribution.probability(1), 0.001);
        assertEquals(0.4, distribution.probability(2), 0.001);
        assertEquals(0.3, distribution.probability(3), 0.001);
        assertEquals(0, distribution.probability(4), 0.001);
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        int[] expectedSamples = {1, 2, 2, 3, 3, 3};

        for (int i = 0; i < 6; i++) {
            assertEquals(expectedSamples[i], distribution.sample());
        }
    }

    @Test
    public void testSampleArray() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        int sampleSize = 5;
        Integer[] expectedSamples = new Integer[sampleSize];

        for (int i = 0; i < sampleSize; i++) {
            expectedSamples[i] = distribution.sample();
        }

        assertArrayEquals(expectedSamples, distribution.sample(sampleSize));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSize() {
        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(new Pair<>(1, 0.3)));

        distribution.sample(-1);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        List<Pair<Integer, Double>> expectedSamples = Arrays.asList(new Pair<>(1, 0.3), new Pair<>(2, 0.4), new Pair<>(3, 0.3));

        List<Pair<Integer, Double>> actualSamples = distribution.getSamples();

        assertArrayEquals(expectedSamples.toArray(), actualSamples.toArray());
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        long seed = 12345;
        distribution.reseedRandomGenerator(seed);

        int sample = distribution.sample().intValue();

        int sampleFromOriginalRNG = new Well19937c().nextInt();

        assertEquals(sampleFromOriginalRNG, sample);
    }
}