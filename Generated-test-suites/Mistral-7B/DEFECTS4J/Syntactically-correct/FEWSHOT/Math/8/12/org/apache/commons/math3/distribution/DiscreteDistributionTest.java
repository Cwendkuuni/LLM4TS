package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionEmpty() {
        List<Pair<Object, Double>> samples = new ArrayList<>();
        DiscreteDistribution<Object> distribution = null;
        try {
            distribution = new DiscreteDistribution<Object>(samples);
            fail("Expected exception");
        } catch (NotPositiveException e) {
            // Expected exception
        } catch (MathArithmeticException e) {
            // Expected exception
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
        assertNull(distribution);
    }

    @Test
    public void testDiscreteDistributionWithNullProbabilities() {
        List<Pair<Object, Double>> samples = Arrays.asList(new Pair<>(1, null));
        try {
            new DiscreteDistribution<Object>(samples);
            fail("Expected exception");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testDiscreteDistributionWithNegativeProbabilities() {
        List<Pair<Object, Double>> samples = Arrays.asList(new Pair<>(1, -1.0), new Pair<>(2, 1.0));
        try {
            new DiscreteDistribution<Object>(samples);
            fail("Expected exception");
        } catch (NotPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testDiscreteDistributionWithZeroProbabilities() {
        List<Pair<Object, Double>> samples = Arrays.asList(new Pair<>(1, 0.0), new Pair<>(2, 1.0));
        DiscreteDistribution<Object> distribution = null;
        try {
            distribution = new DiscreteDistribution<Object>(samples);
            fail("Expected exception");
        } catch (MathArithmeticException e) {
            // Expected exception
        }
        assertNull(distribution);
    }

    @Test
    public void testDiscreteDistributionWithInfProbabilities() {
        List<Pair<Object, Double>> samples = Arrays.asList(new Pair<>(1, Double.POSITIVE_INFINITY), new Pair<>(2, 1.0));
        try {
            new DiscreteDistribution<Object>(samples);
            fail("Expected exception");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testDiscreteDistributionProbabilitiesSumToOne() throws NotPositiveException, MathArithmeticException {
        List<Pair<Object, Double>> samples = Arrays.asList(new Pair<>(1, 0.25), new Pair<>(2, 0.5), new Pair<>(3, 0.25));
        DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(samples);
        double sum = 0;
        for (double prob : distribution.probabilities) {
            sum += prob;
        }
        assertEquals(1.0, sum, 1e-10);
    }

    @Test
    public void testDiscreteDistributionGetSamples() {
        List<Pair<Object, Double>> samples = Arrays.asList(new Pair<>(1, 0.25), new Pair<>(2, 0.5), new Pair<>(3, 0.25));
        DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(samples);
        List<Pair<Object, Double>> result = distribution.getSamples();
        assertEquals(samples, result);
    }

    @Test
    public void testDiscreteDistributionSample() {
        List<Pair<Object, Double>> samples = Arrays.asList(new Pair<>(1, 0.25), new Pair<>(2, 0.5), new Pair<>(3, 0.25));
        DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(samples);
        Object result = distribution.sample();
        assertTrue(samples.stream().anyMatch(p -> p.getKey().equals(result)));
    }

    @Test
    public void testDiscreteDistributionSampleArray() throws NotStrictlyPositiveException {
        List<Pair<Object, Double>> samples = Arrays.asList(new Pair<>(1, 0.25), new Pair<>(2, 0.5), new Pair<>(3, 0.25));
        DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(samples);
        Object[] result = distribution.sample(5);
        assertArrayEquals(new Object[]{null, null, null, null, null}, result);
        for (int i = 0; i < 5; i++) {
            Object sample = distribution.sample();
            assertTrue(Arrays.stream(result).anyMatch(s -> s.equals(sample)));
        }
    }

    @Test
    public void testDiscreteDistributionReSeed() {
        final int seed = 1L;
        List<Pair<Object, Double>> samples = Arrays.asList(new Pair<>(1, 0.25), new Pair<>(2, 0.5), new Pair<>(3, 0.25));
        DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(samples);
        distribution.reseedRandomGenerator(seed);
        Object sample = distribution.sample();
        Object sample2 = distribution.sample();
        Object sample3 = distribution.sample();
        assertNotNull(sample);
        assertNotNull(sample2);
        assertNotNull(sample3);
        assertEquals(sample, sample2);
        assertEquals(sample, sample3);
        assertEquals(seed, distribution.random.getSeed());
    }
}