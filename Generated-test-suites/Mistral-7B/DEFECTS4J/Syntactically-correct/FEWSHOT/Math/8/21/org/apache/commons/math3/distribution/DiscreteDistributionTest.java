package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.MathArrays;

import org.junit.Test;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistribution() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, 0.2),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, 0.5),
                new Pair<Integer, Double>(4, 0.1)
        );

        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
            assertEquals(4, dist.singletons.size());
            assertEquals(0.2, dist.probabilities[0], 0.0001);
            assertEquals(0.3, dist.probabilities[1], 0.0001);
            assertEquals(0.5, dist.probabilities[2], 0.0001);
            assertEquals(0.1, dist.probabilities[3], 0.0001);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, 0.2),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, 0.5),
                new Pair<Integer, Double>(4, 0.1)
        );

        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
            int sampled = dist.sample();
            List<Integer> expectedSamples = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                expectedSamples.add(dist.sample());
            }
            List<Integer> actualSamples = new ArrayList<>(expectedSamples);
            actualSamples.remove(actualSamples.indexOf(sampled));
            assertSame(actualSamples.get(0), sampled);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testSampleArray() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, 0.2),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, 0.5),
                new Pair<Integer, Double>(4, 0.1)
        );

        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
            int sampleSize = 5;
            Integer[] samplesArray = dist.sample(sampleSize);
            assertArrayEquals(new Integer[]{1, 1, 2, 4, 3}, samplesArray);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, 0.2),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, 0.5),
                new Pair<Integer, Double>(4, 0.1)
        );

        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
            Integer testValue = 3;
            assertEquals(0.5, dist.probability(testValue), 0.0001);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, 0.2),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, 0.5),
                new Pair<Integer, Double>(4, 0.1)
        );

        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
            List<Pair<Integer, Double>> retrievedSamples = dist.getSamples();
            assertEquals(samples.size(), retrievedSamples.size());
            for (int i = 0; i < samples.size(); i++) {
                assertEquals(samples.get(i).getFirst(), retrievedSamples.get(i).getFirst());
                assertEquals(samples.get(i).getSecond(), retrievedSamples.get(i).getSecond(), 0.0001);
            }
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, 0.2),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, 0.5),
                new Pair<Integer, Double>(4, 0.1)
        );

        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
            Long seed = 123456789;
            dist.reseedRandomGenerator(seed);
            // The randomness cannot be tested directly, so this test is empty.
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testNullInput() {
        List<Pair<Integer, Double>> samples = null;

        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
            assert false;
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, -0.2),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, 0.5),
                new Pair<Integer, Double>(4, 0.1)
        );

        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
            // Exception should be thrown, so this test is empty.
        } catch (MathIllegalArgumentException e) {
            assert false;
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testZeroProbabilitySum() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, -0.2),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, 0.5),
                new Pair<Integer, Double>(4, -0.1)
        );

        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
            // Exception should be thrown, so this test is empty.
        } catch (NotPositiveException e) {
            assert false;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInfiniteProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, Double.NEGATIVE_INFINITY),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, 0.5),
                new Pair<Integer, Double>(4, 0.1)
        );

        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
            // Exception should be thrown, so this test is empty.
        } catch (NotPositiveException | MathArithmeticException e) {
            assert false;
        }
    }
}