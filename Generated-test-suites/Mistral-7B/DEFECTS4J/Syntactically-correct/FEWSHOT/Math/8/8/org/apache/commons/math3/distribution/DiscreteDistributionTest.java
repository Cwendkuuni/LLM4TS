package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.DiscreteUniformDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testConstructorWithInvalidProbabilities() {
        List<Pair<Number, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0, -1.1));
        try {
            new DiscreteDistribution<Number>(samples);
            assert false;
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithZeroProbabilities() {
        List<Pair<Number, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0, 0.0));
        try {
            new DiscreteDistribution<Number>(samples);
            assert false;
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithProbabilitiesSumToZero() {
        List<Pair<Number, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0, 0.5));
        samples.add(new Pair<>(2.0, 0.5));
        try {
            new DiscreteDistribution<Number>(samples);
            assert false;
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithValidProbabilities() {
        List<Pair<Number, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0, 0.3));
        samples.add(new Pair<>(2.0, 0.7));
        DiscreteDistribution<Number> distribution = new DiscreteDistribution<Number>(samples);
        assertNotNull(distribution);
    }

    @Test
    public void testConstructorWithRandomGenerator() {
        List<Pair<Number, Double>> samples = Arrays.asList(
                new Pair<>(1.0, 0.3),
                new Pair<>(2.0, 0.7)
        );
        RandomGenerator rng = new Well19937c();
        DiscreteDistribution<Number> distribution = new DiscreteDistribution<Number>(rng, samples);
        assertNotNull(distribution);
    }

    @Test
    public void testProbability() {
        List<Pair<Number, Double>> samples = Arrays.asList(
                new Pair<>(1.0, 0.3),
                new Pair<>(2.0, 0.7)
        );
        DiscreteDistribution<Number> distribution = new DiscreteDistribution<Number>(samples);
        assertEquals(0.3, distribution.probability(1.0), 0.001);
        assertEquals(0.7, distribution.probability(2.0), 0.001);
        assertNull(distribution.probability(3.0));
    }

    @Test
    public void testGetSamples() {
        List<Pair<Number, Double>> samples = Arrays.asList(
                new Pair<>(1.0, 0.3),
                new Pair<>(2.0, 0.7)
        );
        DiscreteDistribution<Number> distribution = new DiscreteDistribution<Number>(samples);
        List<Pair<Number, Double>> actualSamples = distribution.getSamples();
        List<Pair<Number, Double>> expectedSamples = samples;
        assertArrayEquals(expectedSamples.toArray(), actualSamples.toArray());
    }

    @Test
    public void testSample() {
        List<Pair<Number, Double>> samples = Arrays.asList(
                new Pair<>(1.0, 0.3),
                new Pair<>(2.0, 0.7)
        );
        DiscreteDistribution<Number> distribution = new DiscreteDistribution<Number>(samples);
        Number actualSample = distribution.sample();
        Number expectedSample = samples.get(0).getKey();
        assertEquals(expectedSample, actualSample);
    }

    @Test
    public void testSampleWithNegativeProbability() {
        List<Pair<Number, Double>> samples = Arrays.asList(
                new Pair<>(1.0, -1.0),
                new Pair<>(2.0, 0.7)
        );
        DiscreteDistribution<Number> distribution = null;
        try {
            distribution = new DiscreteDistribution<Number>(samples);
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
        assertNull(distribution);
    }

    @Test
    public void testSampleWithInfProbability() {
        List<Pair<Number, Double>> samples = Arrays.asList(
                new Pair<>(1.0, Double.POSITIVE_INFINITY),
                new Pair<>(2.0, 0.7)
        );
        DiscreteDistribution<Number> distribution = null;
        try {
            distribution = new DiscreteDistribution<Number>(samples);
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
        assertNull(distribution);
    }

    @Test
    public void testSampleWithZeroSize() {
        List<Pair<Number, Double>> samples = new ArrayList<>();
        DiscreteDistribution<Number> distribution = new DiscreteDistribution<Number>(samples);
        Number actualSample = distribution.sample();
        assertNull(actualSample);
    }

    @Test
    public void testSampleWithSingleValue() {
        List<Pair<Number, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0, 1.0));
        DiscreteDistribution<Number> distribution = new DiscreteDistribution<Number>(samples);
        Number actualSample = distribution.sample();
        Number expectedSample = samples.get(0).getKey();
        assertEquals(expectedSample, actualSample);
    }

    @Test
    public void testSampleWithSingleValueAndInfProbability() {
        List<Pair<Number, Double>> samples = Arrays.asList(
                new Pair<>(1.0, Double.POSITIVE_INFINITY)
        );
        DiscreteDistribution<Number> distribution = null;
        try {
            distribution = new DiscreteDistribution<Number>(samples);
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
        assertNull(distribution);
    }

    @Test
    public void testSampleWithSingleProbabilityZero() {
        List<Pair<Number, Double>> samples = Arrays.asList(
                new Pair<>(1.0, 0.0)
        );
        DiscreteDistribution<Number> distribution = new DiscreteDistribution<Number>(samples);
        Number actualSample = distribution.sample();
        assertNull(actualSample);
    }

    @Test
    public void testSampleWithMultipleValues() {
        List<Pair<Number, Double>> samples = Arrays.asList(
                new Pair<>(1.0, 0.3),
                new Pair<>(2.0, 0.7)
        );
        DiscreteDistribution<Number> distribution = new DiscreteDistribution<Number>(samples);
        Number actualSample = distribution.sample();
        Number expectedSample = samples.get(0).getKey();
        assertEquals(expectedSample, actualSample);
    }

    @Test
    public void testSampleWithMultipleValuesAndSameProbability() {
        List<Pair<Number, Double>> samples = Arrays.asList(
                new Pair<>(1.0, 0.5),
                new Pair<>(2.0, 0.5)
        );
        DiscreteDistribution<Number> distribution = new DiscreteDistribution<Number>(samples);
        Number actualSample = distribution.sample();
        Number expectedSample = samples.get(0).getKey();
        Number alternativeSample = samples.get(1).getKey();
        List<Number> samplesList = new ArrayList<>();
        samplesList.add(expectedSample);
        samplesList.add(alternativeSample);
        List<Number> actualSamplesList = new ArrayList<>();
        actualSamplesList.add(actualSample);
        assertArrayEquals(samplesList.toArray(), actualSamplesList.toArray());
    }

    @Test
    public void testSampleWithMultipleValuesAndSameProbabilityInfProbability() {
        List<Pair<Number, Double>> samples = Arrays.asList(
                new Pair<>(1.0, Double.POSITIVE_INFINITY),
                new Pair<>(2.0, Double.POSITIVE_INFINITY)
        );
        DiscreteDistribution<Number> distribution = null;
        try {
            distribution = new DiscreteDistribution<Number>(samples);
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
        assertNull(distribution);
    }

    @Test
    public void testSampleWithMultipleValuesAndZeroProbability() {
        List<Pair<Number, Double>> samples = Arrays.asList(
                new Pair<>(1.0, 0.0),
                new Pair<>(2.0, 0.7)
        );
        DiscreteDistribution<Number> distribution = new DiscreteDistribution<Number>(samples);
        Number actualSample = distribution.sample();
        Number expectedSample = samples.get(1).getKey();
        assertEquals(expectedSample, actualSample);
    }

    @Test
    public void testSampleAndGetDistributionProperties() {
        List<Pair<Number, Double>> samples = Arrays.asList(
                new Pair<>(1.0, 0.3),
                new Pair<>(2.0, 0.7)
        );
        DiscreteDistribution<Number> distribution = new DiscreteDistribution<Number>(samples);
        Number actualSample = distribution.sample();
        Number expectedSample = samples.get(0).getKey();
        assertEquals(expectedSample, actualSample);
        List<Pair<Number, Double>> actualSamples = distribution.getSamples();
        List<Pair<Number, Double>> expectedSamples = samples;
        assertArrayEquals(expectedSamples.toArray(), actualSamples.toArray());
    }

    @Test
    public void testSampleArrayWithValidSize() {
        List<Pair<Number, Double>> samples = Arrays.asList(
                new Pair<>(1.0, 0.3),
                new Pair<>(2.0, 0.7)
        );
        DiscreteDistribution<Number> distribution = new DiscreteDistribution<Number>(samples);
        T[] actualSampleArray = distribution.sample(5);
        T[] expectedSampleArray = (T[]) new Number[5][];
        for (int i = 0; i < 5; i++) {
            expectedSampleArray[i] = samples.get(i % 2).getKey().toArray(new T[0]);
        }
        assertArrayEquals(expectedSampleArray, actualSampleArray);
    }

    @Test
    public void testSampleArrayWithInvalidSize() {
        List<Pair<Number, Double>> samples = Arrays.asList(
                new Pair<>(1.0, 0.3),
                new Pair<>(2.0, 0.7)
        );
        DiscreteDistribution<Number> distribution = new DiscreteDistribution<Number>(samples);
        try {
            distribution.sample(-1);
            assert false;
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }
}