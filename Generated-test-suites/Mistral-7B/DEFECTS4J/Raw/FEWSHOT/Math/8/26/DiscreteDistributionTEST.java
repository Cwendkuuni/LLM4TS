package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class DiscreteDistributionTest {

    @Test
    public void testProbabilityNullSingleton() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(null, 0.1));
        assertThrows(MathIllegalArgumentException.class, () -> new DiscreteDistribution<>(new Well19937c(), samples));
    }

    @Test
    public void testProbabilityNegativeSingletonProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), -0.1));
        assertThrows(NotPositiveException.class, () -> new DiscreteDistribution<>(new Well19937c(), samples));
    }

    @Test
    public void testProbabilityZeroProbabilitiesSum() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.1));
        samples.add(new Pair<>(Integer.valueOf(2), 0.1));
        samples.add(new Pair<>(Integer.valueOf(3), -0.1));
        assertThrows(MathArithmeticException.class, () -> new DiscreteDistribution<>(new Well19937c(), samples));
    }

    @Test
    public void testProbabilityInfiniteSingletonProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), Double.POSITIVE_INFINITY));
        assertThrows(MathIllegalArgumentException.class, () -> new DiscreteDistribution<>(new Well19937c(), samples));
    }

    @Test
    public void testProbabilitySingletonValueAndProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        assertEquals(0.3, distribution.probability(Integer.valueOf(1)));
    }

    @Test
    public void testProbabilityDifferentSingletonValues() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.2));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        assertEquals(0.3, distribution.probability(Integer.valueOf(1)));
        assertEquals(0.4, distribution.probability(Integer.valueOf(2)));
        assertEquals(0.2, distribution.probability(Integer.valueOf(3)));
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.2));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        List<Pair<Integer, Double>> result = distribution.getSamples();
        assertEquals(samples.size(), result.size());
        for (int i = 0; i < samples.size(); i++) {
            assertEquals(samples.get(i).getKey(), result.get(i).getKey());
            assertEquals(samples.get(i).getValue(), result.get(i).getValue(), 0.001);
        }
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.2));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        Integer sample = distribution.sample();
        for (Pair<Integer, Double> pair : samples) {
            if (pair.getValue() > 0.0) {
                assertEquals(pair.getKey(), sample);
                break;
            }
        }
    }

    @Test
    public void testSampleArray() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.2));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        Integer[] samplesArray = distribution.sample(5);
        assertArrayEquals(new Integer[]{null, null, null, null, null}, samplesArray, 0);
        for (int i = 0; i < samples.size(); i++) {
            for (int j = 0; j < 5; j++) {
                if (samplesArray[j] != null) {
                    assertEquals(samples.get(i).getKey(), samplesArray[j]);
                    break;
                }
            }
        }
    }

    @Test
    public void testSampleArrayWithNegativeSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.2));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        assertThrows(NotStrictlyPositiveException.class, () -> distribution.sample(-1));
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.2));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        long seed = System.currentTimeMillis();
        distribution.reseedRandomGenerator(seed);
        Integer sample = distribution.sample();
        assertEquals(sample, null);
        // Check that new seed was set
        assertEquals(seed, distribution.random.getSeed());
    }
}