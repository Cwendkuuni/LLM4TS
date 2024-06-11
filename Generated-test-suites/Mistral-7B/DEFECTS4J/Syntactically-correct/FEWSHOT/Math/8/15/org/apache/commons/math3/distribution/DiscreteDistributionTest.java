package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.DiscreteUniformDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;

/**
 * Test cases for DiscreteDistribution class
 */
public class DiscreteDistributionTest {

    /**
     * Test constructor with null input
     */
    @Test(expected = NullPointerException.class)
    public void testNullConstructor() {
        List<Pair<Integer, Double>> samples = null;
        new DiscreteDistribution<>(samples);
    }

    /**
     * Test constructor with empty input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyConstructor() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        new DiscreteDistribution<>(samples);
    }

    /**
     * Test constructor with invalid input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.5), new Pair<>(2, 1.5),
                new Pair<>(3, -1.0));
        new DiscreteDistribution<>(samples);
    }

    /**
     * Test constructor with valid input
     */
    @Test
    public void testValidConstructor() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.25), new Pair<>(2, 0.5),
                new Pair<>(3, 0.25));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        assertEquals(samples.size(), distribution.singletons.size());
        assertEquals(samples, distribution.getSamples());
    }

    /**
     * Test probability method with valid input
     */
    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.25), new Pair<>(2, 0.5),
                new Pair<>(3, 0.25));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        assertEquals(0.25, distribution.probability((Integer) 1, null), 0.001);
        assertEquals(0.5, distribution.probability((Integer) 2, null), 0.001);
        assertEquals(0.25, distribution.probability((Integer) 3, null), 0.001);
        assertEquals(0.0, distribution.probability((Integer) 4, null), 0.001);
    }

    /**
     * Test probability method with null input
     */
    @Test
    public void testProbabilityNullInput() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.25), new Pair<>(2, 0.5),
                new Pair<>(3, 0.25));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        assertNull(distribution.probability(null, null));
    }

    /**
     * Test probability method with non-matching input
     */
    @Test
    public void testProbabilityNonMatchingInput() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.25), new Pair<>(2, 0.5),
                new Pair<>(3, 0.25));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        assertEquals(0.0, distribution.probability((Integer) 4, null), 0.001);
    }

    /**
     * Test sample method with valid input
     */
    @Test
    public void testSampleValidInput() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.25), new Pair<>(2, 0.5),
                new Pair<>(3, 0.25));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        Integer sample = distribution.sample();
        List<Integer> expectedSamples = new ArrayList<>();
        expectedSamples.add(1);
        expectedSamples.add(2);
        expectedSamples.add(1); // since we don't know the internal random generator state
        assertSame(expectedSamples.get(0), sample);
    }

    /**
     * Test sample method with different types of input
     */
    @Test(expected = ClassCastException.class)
    public void testSampleDifferentTypes() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.25), new Pair<>(2, 0.5),
                new Pair<>(3, 0.25));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        String sample = distribution.sample();
    }

    /**
     * Test sample method with null input
     */
    @Test(expected = NullPointerException.class)
    public void testSampleNullInput() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.25), new Pair<>(2, 0.5),
                new Pair<>(3, 0.25));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        distribution.sample(null);
    }

    /**
     * Test sample method with invalid input
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleInvalidInput() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.25), new Pair<>(2, 0.5),
                new Pair<>(3, 0.25));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        distribution.sample(-1);
    }

    /**
     * Test sample method with zero size sample
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSampleZeroSizeSample() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.25), new Pair<>(2, 0.5),
                new Pair<>(3, 0.25));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        distribution.sample(0);
    }

    /**
     * Test sample method with larger sample size than possible
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSampleLargerSampleSize() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.25), new Pair<>(2, 0.5),
                new Pair<>(3, 0.25));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        distribution.sample(Integer.MAX_VALUE);
    }

    /**
     * Test sample method with multiple samples
     */
    @Test
    public void testSampleMultipleSamples() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.25), new Pair<>(2, 0.5),
                new Pair<>(3, 0.25));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        Integer[] samplesArray = distribution.sample(5);
        List<Integer> expectedSamples = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            expectedSamples.add(1);
            expectedSamples.add(2);
            expectedSamples.add(3);
        }
        assertArrayEquals(expectedSamples.toArray(), samplesArray);
    }

    /**
     * Test reseedRandomGenerator method
     */
    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.25), new Pair<>(2, 0.5),
                new Pair<>(3, 0.25));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        long seed = 1234L;
        distribution.reseedRandomGenerator(seed);
        Integer sample1 = distribution.sample();
        distribution.reseedRandomGenerator(seed + 1);
        Integer sample2 = distribution.sample();
        distribution.reseedRandomGenerator(seed);
        Integer sample3 = distribution.sample();
        assertEquals(sample1, sample3);
        assertNotSame(sample1, sample2);
    }
}