package org.apache.commons.math3.distribution;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for DiscreteDistribution.
 */
public class DiscreteDistributionTest {

    /**
     * Test the basic behavior of DiscreteDistribution.
     */
    @Test
    public void testBasicBehavior() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, 0.4),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, 0.2),
                new Pair<Integer, Double>(4, 0.1));

        DiscreteDistribution<Integer> dist = null;
        try {
            dist = new DiscreteDistribution<Integer>(samples);
        } catch (Exception e) {
            fail("Failed to create distribution: " + e.getMessage());
        }

        // Probability of each sample
        for (Pair<Integer, Double> sample : samples) {
            double prob = dist.probability(sample.getKey());
            assertEquals(sample.getValue(), prob, 0.001);
        }

        // Sampling
        for (int i = 0; i < 1000; i++) {
            int sample = dist.sample().intValue();
            int expectedCount = 0;
            for (Pair<Integer, Double> sampleDefinition : samples) {
                if (sample == sampleDefinition.getKey().intValue()) {
                    expectedCount += sampleDefinition.getValue().intValue() * 1000;
                }
            }
            assertTrue(expectedCount > 0);
            assertEquals(expectedCount, (int) (sample * 1000.0 * dist.probability(sample)));
        }

        // Sampling large array
        T[] sampleArray = dist.sample(10000);
        int[] expectedCountArray = new int[singletons.size()];
        for (int i = 0; i < singletons.size(); i++) {
            expectedCountArray[i] = Math.round(sampleSize * probabilities[i]);
        }
        for (int i = 0; i < sampleArray.length; i++) {
            int actualIndex = Arrays.asList(singletons.toArray()).indexOf(sampleArray[i]);
            assertEquals(expectedCountArray[actualIndex], (int) sampleArray[i].intValue());
        }
    }

    /**
     * Test exception handling.
     */
    @Test(expected = NotPositiveException.class)
    public void testNegativeProbability() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, -0.4),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, 0.2),
                new Pair<Integer, Double>(4, 0.1));

        new DiscreteDistribution<Integer>(samples);
    }

    /**
     * Test exception handling.
     */
    @Test(expected = MathArithmeticException.class)
    public void testSumToZeroProbability() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, 0.4),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, -0.1), // Negative probability
                new Pair<Integer, Double>(4, 0.1));

        new DiscreteDistribution<Integer>(samples);
    }

    /**
     * Test exception handling.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInfiniteProbability() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, 0.4),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, Double.POSITIVE_INFINITY),
                new Pair<Integer, Double>(4, 0.1));

        new DiscreteDistribution<Integer>(samples);
    }

    /**
     * Test if samples can be retrieved.
     */
    @Test
    public void testGetSamples() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, 0.4),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, 0.2),
                new Pair<Integer, Double>(4, 0.1));

        DiscreteDistribution<Integer> dist = null;
        try {
            dist = new DiscreteDistribution<Integer>(samples);
        } catch (Exception e) {
            fail("Failed to create distribution: " + e.getMessage());
        }

        List<Pair<Integer, Double>> retrievedSamples = dist.getSamples();
        List<Pair<Integer, Double>> expectedSamples = samples;

        assertEquals(expectedSamples.size(), retrievedSamples.size());
        for (int i = 0; i < expectedSamples.size(); i++) {
            assertEquals(expectedSamples.get(i).getKey(), retrievedSamples.get(i).getKey());
            assertEquals(expectedSamples.get(i).getValue(), retrievedSamples.get(i).getValue(), 0.001);
        }
    }

    /**
     * Test distribution reseeding.
     */
    @Test
    public void testReseedRandomGenerator() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<Integer, Double>(1, 0.4),
                new Pair<Integer, Double>(2, 0.3),
                new Pair<Integer, Double>(3, 0.2),
                new Pair<Integer, Double>(4, 0.1));

        DiscreteDistribution<Integer> dist = null;
        try {
            dist = new DiscreteDistribution<Integer>(samples);
        } catch (Exception e) {
            fail("Failed to create distribution: " + e.getMessage());
        }

        long seed = RandomUtils.nextLong();
        dist.reseedRandomGenerator(seed);
        int sample = dist.sample().intValue();
        long newSeed = dist.random.nextLong();
        assertEquals(seed, newSeed);
    }
}