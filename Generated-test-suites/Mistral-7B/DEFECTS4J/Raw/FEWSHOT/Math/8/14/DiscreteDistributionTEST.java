package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.DiscreteDistribution.Pair;
import org.apache.commons.math3.random.Well19937c;

public class DiscreteDistributionTest {
    @SuppressWarnings("unchecked")
    private static <T> DiscreteDistribution<T> createInstance(List<Pair<T, Double>> samples) {
        return new DiscreteDistribution<T>(new Well19937c(), samples);
    }

    @Test
    public void constructor_emptyList_throwsMathArithmeticException() {
        List<Pair<Integer, Double>> emptyList = Collections.emptyList();

        try {
            createInstance(emptyList);
            fail("Expected MathArithmeticException but got none");
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void constructor_singletonList_samplesCorrectly() {
        List<Pair<Integer, Double>> singletonList = Arrays.asList(new Pair<>(1, 1.0));
        DiscreteDistribution<Integer> distribution = createInstance(singletonList);

        assertEquals(1, distribution.sample().intValue());
    }

    @Test
    public void constructor_multipleListItems_samplesCorrectly() {
        List<Pair<Integer, Double>> multipleListItems = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        );

        DiscreteDistribution<Integer> distribution = createInstance(multipleListItems);

        int[] samples = new int[10];

        for (int i = 0; i < 10; i++) {
            samples[i] = distribution.sample().intValue();
        }

        List<Integer> expectedSamples = new ArrayList<>();
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(2);
        expectedSamples.add(2);
        expectedSamples.add(2);
        expectedSamples.add(2);
        expectedSamples.add(3);

        assertArrayEquals(expectedSamples.toArray(), samples);
    }

    @Test
    public void constructor_multipleListItems_probabilitiesSumToOne() {
        List<Pair<Integer, Double>> multipleListItems = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        );

        DiscreteDistribution<Integer> distribution = createInstance(multipleListItems);

        double sum = 0;

        for (int i = 0; i < distribution.probabilities.length; i++) {
            sum += distribution.probabilities[i];
        }

        assertEquals(1.0, sum, 0.001);
    }

    @Test
    public void constructor_multipleListItems_samplesFromZeroProbabilityValue() {
        List<Pair<Integer, Double>> multipleListItems = Arrays.asList(
                new Pair<>(1, 0.5),
                new Pair<>(2, 0.0),
                new Pair<>(3, 0.5)
        );

        DiscreteDistribution<Integer> distribution = createInstance(multipleListItems);

        assertTrue(distribution.sample().equals(1));
        assertTrue(distribution.sample().equals(1));
        assertTrue(distribution.sample().equals(1));
        assertTrue(distribution.sample().equals(3));
        assertTrue(distribution.sample().equals(3));
    }

    @Test
    public void constructor_multipleListItems_samplesFromNullValue() {
        List<Pair<Integer, Double>> multipleListItems = Arrays.asList(
                new Pair<>(null, 0.5),
                new Pair<>(1, 0.5),
                new Pair<>(2, 0.5)
        );

        DiscreteDistribution<Integer> distribution = createInstance(multipleListItems);

        assertTrue(distribution.sample() == null);
        assertTrue(distribution.sample().equals(1));
        assertTrue(distribution.sample().equals(1));
        assertTrue(distribution.sample().equals(1));
        assertTrue(distribution.sample().equals(2));
    }

    @Test
    public void constructor_multipleListItems_samplesFromNullValueAndSingleton() {
        List<Pair<Integer, Double>> multipleListItems = Arrays.asList(
                new Pair<>(null, 0.5),
                new Pair<>(1, 0.5),
                new Pair<>(2, 0.5),
                new Pair<>(3, 0.5)
        );

        DiscreteDistribution<Integer> distribution = createInstance(multipleListItems);

        ArrayList<Integer> samples = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            samples.add(distribution.sample().intValue());
        }

        ArrayList<Integer> expectedSamples = new ArrayList<>();
        expectedSamples.add(null);
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(2);
        expectedSamples.add(2);
        expectedSamples.add(2);
        expectedSamples.add(2);
        expectedSamples.add(3);

        assertEquals(expectedSamples, samples);
    }

    @Test
    public void probability_returnsCorrectProbabilityForSample() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        );

        DiscreteDistribution<Integer> distribution = createInstance(samples);

        int sample = 1;

        assertEquals(0.1, distribution.probability(sample), 0.001);
    }

    @Test
    public void probability_returnsZeroProbabilityForNullValue() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        );

        DiscreteDistribution<Integer> distribution = createInstance(samples);

        assertEquals(0.0, distribution.probability(null), 0.001);
    }

    @Test
    public void probability_returnsZeroProbabilityForNonExistentValue() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        );

        DiscreteDistribution<Integer> distribution = createInstance(samples);

        int sample = 4;

        assertEquals(0.0, distribution.probability(sample), 0.001);
    }

    @Test
    public void getSamples_returnsCorrectSamples() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        );

        DiscreteDistribution<Integer> distribution = createInstance(samples);

        List<Pair<Integer, Double>> expectedSamples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        );

        assertEquals(expectedSamples, distribution.getSamples());
    }

    @Test
    public void sample_samplesFromCorrectDistribution() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        );

        DiscreteDistribution<Integer> distribution = createInstance(samples);

        int expectedCountForSample1 = 1;
        int expectedCountForSample2 = 3;
        int expectedCountForSample3 = 6;

        int[] samplesCount = new int[3];

        for (int i = 0; i < 10; i++) {
            int sample = distribution.sample().intValue();
            samplesCount[sample]++;
        }

        assertEquals(expectedCountForSample1, samplesCount[1]);
        assertEquals(expectedCountForSample2, samplesCount[2]);
        assertEquals(expectedCountForSample3, samplesCount[3]);
    }

    @Test
    public void sample_samplesFromZeroProbabilityValue() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.5),
                new Pair<>(2, 0.0),
                new Pair<>(3, 0.5)
        );

        DiscreteDistribution<Integer> distribution = createInstance(samples);

        ArrayList<Integer> samplesList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            samplesList.add(distribution.sample().intValue());
        }

        ArrayList<Integer> expectedSamples = new ArrayList<>();
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(3);
        expectedSamples.add(3);

        assertEquals(expectedSamples, samplesList);
    }

    @Test
    public void sample_samplesFromNullValue() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        );

        DiscreteDistribution<Integer> distribution = createInstance(samples);

        ArrayList<Integer> samplesList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            samplesList.add(distribution.sample().intValue());
        }

        ArrayList<Integer> expectedSamples = new ArrayList<>();
        expectedSamples.add(null);
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(2);
        expectedSamples.add(2);
        expectedSamples.add(2);
        expectedSamples.add(2);
        expectedSamples.add(3);

        assertEquals(expectedSamples, samplesList);
    }

    @Test
    public void sample_samplesFromNullValueAndSingleton() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(null, 0.5),
                new Pair<>(1, 0.5),
                new Pair<>(2, 0.5),
                new Pair<>(3, 0.5)
        );

        DiscreteDistribution<Integer> distribution = createInstance(samples);

        ArrayList<Integer> samplesList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            samplesList.add(distribution.sample().intValue());
        }

        ArrayList<Integer> expectedSamples = new ArrayList<>();
        expectedSamples.add(null);
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(1);
        expectedSamples.add(2);
        expectedSamples.add(2);
        expectedSamples.add(2);
        expectedSamples.add(3);

        assertEquals(expectedSamples, samplesList);
    }

    @Test
    public void sample_samplesFromListWithInfProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, Double.POSITIVE_INFINITY),
                new Pair<>(3, 0.6)
        );

        try {
            createInstance(samples);
            fail("Expected MathIllegalArgumentException but got none");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }
}