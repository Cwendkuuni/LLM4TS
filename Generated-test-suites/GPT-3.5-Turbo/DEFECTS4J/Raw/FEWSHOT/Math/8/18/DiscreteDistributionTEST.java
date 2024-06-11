package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;
    private List<Pair<Integer, Double>> samples;
    private final double EPSILON = 1e-6;

    @Before
    public void setUp() throws Exception {
        samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.5));
        samples.add(new Pair<Integer, Double>(3, 0.3));
        distribution = new DiscreteDistribution<Integer>(samples);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_NotPositiveException() throws Exception {
        samples.add(new Pair<Integer, Double>(4, -0.2)); // negative probability
        distribution = new DiscreteDistribution<Integer>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_MathArithmeticException() throws Exception {
        samples.clear();
        distribution = new DiscreteDistribution<Integer>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructor_MathIllegalArgumentException() throws Exception {
        samples.add(new Pair<Integer, Double>(4, Double.POSITIVE_INFINITY)); // infinite probability
        distribution = new DiscreteDistribution<Integer>(samples);
    }

    @Test
    public void testReseedRandomGenerator() {
        long seed = 123456789L;
        distribution.reseedRandomGenerator(seed);
        assertNotNull(distribution.random);
        distribution.reseedRandomGenerator(seed);
        assertNotEquals(distribution.random, new DiscreteDistribution<Integer>(samples).random);
    }

    @Test
    public void testProbability() {
        assertEquals(0.2, distribution.probability(1), EPSILON);
        assertEquals(0.5, distribution.probability(2), EPSILON);
        assertEquals(0.3, distribution.probability(3), EPSILON);
        assertEquals(0.0, distribution.probability(4), EPSILON);
        assertEquals(0.0, distribution.probability(null), EPSILON);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        assertEquals(3, samples.size());
        assertEquals(samples.get(0).getKey(), Integer.valueOf(1));
        assertEquals(samples.get(0).getValue(), Double.valueOf(0.2));
        assertEquals(samples.get(1).getKey(), Integer.valueOf(2));
        assertEquals(samples.get(1).getValue(), Double.valueOf(0.5));
        assertEquals(samples.get(2).getKey(), Integer.valueOf(3));
        assertEquals(samples.get(2).getValue(), Double.valueOf(0.3));
    }

    @Test
    public void testSample() {
        Integer sample = distribution.sample();
        assertNotNull(sample);
        boolean validSample = false;
        for (Pair<Integer, Double> pair : samples) {
            if (pair.getKey().equals(sample)) {
                validSample = true;
                break;
            }
        }
        assertEquals(true, validSample);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSample_NotStrictlyPositiveException() throws Exception {
        distribution.sample(0);
    }

    @Test
    public void testSample_Array() throws Exception {
        int sampleSize = 5;
        Integer[] sample = distribution.sample(sampleSize);
        assertEquals(sampleSize, sample.length);
        for (Integer value : sample) {
            boolean validSample = false;
            for (Pair<Integer, Double> pair : samples) {
                if (pair.getKey().equals(value)) {
                    validSample = true;
                    break;
                }
            }
            assertEquals(true, validSample);
        }
    }

}