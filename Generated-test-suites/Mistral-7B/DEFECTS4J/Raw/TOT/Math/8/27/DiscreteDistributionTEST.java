package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link DiscreteDistribution} class.
 */
public class DiscreteDistributionTest {

    /**
     * Test for constructor with valid input.
     */
    @Test
    public void testValidInput() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, 0.2),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, 0.5));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Basic tests
        assertEquals(3, distribution.singletons.size());
        assertEquals(1.0, distribution.probabilities.length, 0.0);
        for (int i = 0; i < distribution.probabilities.length; i++) {
            assertEquals(distribution.singletons.get(i), distribution.singletons.get(i));
            assertTrue(distribution.probabilities[i] >= 0);
        }
        for (double p : distribution.probabilities) {
            assertTrue(p <= 1.0);
        }
        assertEquals(1.0, Arrays.stream(distribution.probabilities).sum(), 0.0);

        // Probability test
        double probability = distribution.probability(1);
        assertEquals(0.2, probability, 0.001);
        probability = distribution.probability(2);
        assertEquals(0.3, probability, 0.001);
        probability = distribution.probability(3);
        assertEquals(0.5, probability, 0.001);
        probability = distribution.probability(4);
        assertEquals(0.0, probability, 0.001);

        // Sampling test
        int[] samplesArray = new int[1000];
        for (int i = 0; i < samplesArray.length; i++) {
            samplesArray[i] = distribution.sample();
        }

        int count = 0;
        for (int value : samplesArray) {
            if (value == 1) {
                count++;
            }
        }
        assertEquals(Math.round(200.0 * 0.2), count, 20);
        count = 0;
        for (int value : samplesArray) {
            if (value == 2) {
                count++;
            }
        }
        assertEquals(Math.round(200.0 * 0.3), count, 20);
        count = 0;
        for (int value : samplesArray) {
            if (value == 3) {
                count++;
            }
        }
        assertEquals(Math.round(200.0 * 0.5), count, 20);

        // getSamples test
        List<Pair<Integer, Double>> samplesList = distribution.getSamples();
        assertEquals(distribution.singletons.size(), samplesList.size());
        for (int i = 0; i < samplesList.size(); i++) {
            assertEquals(distribution.singletons.get(i), samplesList.get(i).getKey());
            assertEquals(distribution.probabilities[i], samplesList.get(i).getValue(), 0.001);
        }
    }

    /**
     * Test for constructor with invalid input: negative probabilities.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, -0.2),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, 0.5));

        new DiscreteDistribution<Integer>(new Well19937c(), samples);
    }

    /**
     * Test for constructor with invalid input: infinite probabilities.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInfiniteProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, Double.POSITIVE_INFINITY),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, 0.5));

        new DiscreteDistribution<Integer>(new Well19937c(), samples);
    }

    /**
     * Test for constructor with invalid input: sum of probabilities is zero.
     */
    @Test(expected = MathArithmeticException.class)
    public void testZeroProbabilities() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, 0.1),
                new Pair<Integer, Double>(2, -0.1),
                new Pair<Integer, Double>(3, 0.5));

        new DiscreteDistribution<Integer>(new Well19937c(), samples);
    }

    /**
     * Test for constructor with invalid input: null values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullValues() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, 0.2),
                new Pair<Integer, Double>(null, 0.3),
                new Pair<Integer, Double>(3, 0.5));

        new DiscreteDistribution<Integer>(new Well19937c(), samples);
    }

    /**
     * Test for constructor with invalid input: empty samples list.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptySamplesList() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();

        new DiscreteDistribution<Integer>(new Well19937c(), samples);
    }

    /**
     * Test for constructor with invalid input: array size mismatch.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testArraySizeMismatch() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, 0.2),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, 0.5),
                new Pair<Integer, Double>(4, 0.1));

        new DiscreteDistribution<Integer>(new Well19937c(), samples);
    }

    /**
     * Test for sample method with invalid input: negative sample size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSampleSize() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), Arrays.asList(
                new Pair<Integer, Double>(1, 0.2),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, 0.5)));

        distribution.sample(-1);
    }

    /**
     * Test for sample method with valid input.
     */
    @Test
    public void testSampleMethod() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), Arrays.asList(
                new Pair<Integer, Double>(1, 0.2),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, 0.5)));

        List<Integer> samples = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            samples.add(distribution.sample());
        }

        int count = 0;
        for (int value : samples) {
            if (value == 1) {
                count++;
            }
        }
        assertEquals(Math.round(20.0 * 0.2), count, 20);
        count = 0;
        for (int value : samples) {
            if (value == 2) {
                count++;
            }
        }
        assertEquals(Math.round(20.0 * 0.3), count, 20);
        count = 0;
        for (int value : samples) {
            if (value == 3) {
                count++;
            }
        }
        assertEquals(Math.round(60.0 * 0.5), count, 30);
    }
}