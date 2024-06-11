package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class DiscreteDistributionTest<T> {

    @SuppressWarnings("unchecked")
    private static <T> DiscreteDistribution<T> createSampleDistribution(List<Pair<T, Double>> samples) {
        RandomGenerator rng = new Well19937c();
        return new DiscreteDistribution<T>(rng, samples);
    }

    @Test
    public void testProbabilityNullValue() {
        List<Pair<T, Double>> samples = new ArrayList<Pair<T, Double>>();
        samples.add(new Pair<T, Double>(null, 0.2));
        DiscreteDistribution<Object> distribution = createSampleDistribution(samples);
        assertNull(distribution.sample());
    }

    @Test
    public void testProbabilitySameValue() {
        List<Pair<T, Double>> samples = new ArrayList<Pair<T, Double>>();
        samples.add(new Pair<T, Double>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<T, Double>(Integer.valueOf(1), 0.7));
        DiscreteDistribution<Integer> distribution = createSampleDistribution(samples);
        assertEquals(Integer.valueOf(1), distribution.sample());
    }

    @Test
    public void testProbabilityDifferentValues() {
        List<Pair<T, Double>> samples = new ArrayList<Pair<T, Double>>();
        samples.add(new Pair<T, Double>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<T, Double>(Integer.valueOf(2), 0.7));
        DiscreteDistribution<Integer> distribution = createSampleDistribution(samples);
        List<Integer> samplesFromDistribution = new ArrayList<Integer>();
        for (int i = 0; i < 100; i++) {
            samplesFromDistribution.add(distribution.sample());
        }
        assertEquals(57, samplesFromDistribution.stream().filter(i -> i.equals(Integer.valueOf(1))).count());
        assertEquals(43, samplesFromDistribution.stream().filter(i -> i.equals(Integer.valueOf(2))).count());
    }

    @Test
    public void testGetSamples() {
        List<Pair<T, Double>> samples = new ArrayList<Pair<T, Double>>();
        samples.add(new Pair<T, Double>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<T, Double>(Integer.valueOf(2), 0.7));
        DiscreteDistribution<Integer> distribution = createSampleDistribution(samples);
        List<Pair<T, Double>> getSamples = distribution.getSamples();
        assertEquals(samples.size(), getSamples.size());
        for (int i = 0; i < samples.size(); i++) {
            assertEquals(samples.get(i).getKey(), getSamples.get(i).getKey());
            assertEquals(samples.get(i).getValue(), getSamples.get(i).getValue(), 0.001);
        }
    }

    @Test
    public void testSampleArray() {
        List<Pair<T, Double>> samples = new ArrayList<Pair<T, Double>>();
        samples.add(new Pair<T, Double>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<T, Double>(Integer.valueOf(2), 0.7));
        DiscreteDistribution<Integer> distribution = createSampleDistribution(samples);
        T[] sampleArray = distribution.sample(10);
        List<Integer> sampleList = new ArrayList<Integer>();
        for (int i = 0; i < sampleArray.length; i++) {
            sampleList.add((Integer) sampleArray[i]);
        }
        assertEquals(samples.size(), sampleList.size());
        for (int i = 0; i < samples.size(); i++) {
            int count = 0;
            for (int j = 0; j < sampleList.size(); j++) {
                if (samples.get(i).getKey().equals(sampleList.get(j))) {
                    count++;
                }
            }
            assertTrue(count > 0);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSize() {
        List<Pair<T, Double>> samples = new ArrayList<Pair<T, Double>>();
        samples.add(new Pair<T, Double>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<T, Double>(Integer.valueOf(2), 0.7));
        DiscreteDistribution<Integer> distribution = createSampleDistribution(samples);
        distribution.sample(-1);
    }

}