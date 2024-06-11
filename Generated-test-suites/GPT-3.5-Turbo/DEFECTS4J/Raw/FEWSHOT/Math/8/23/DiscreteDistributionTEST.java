package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

public class DiscreteDistributionTest {
    @Test
    public void testConstructorWithSamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.4));
        samples.add(new Pair<>(2, 0.6));
        
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        
        assertNotNull(distribution);
    }

    @Test
    public void testConstructorWithSamplesAndRng() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        RandomGenerator rng = new Well19937c();
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.4));
        samples.add(new Pair<>(2, 0.6));
        
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);
        
        assertNotNull(distribution);
    }

    @Test
    public void testProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.4));
        samples.add(new Pair<>(2, 0.6));
        
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        
        assertEquals(0.4, distribution.probability(1), 0);
        assertEquals(0.6, distribution.probability(2), 0);
    }

    @Test
    public void testGetSamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.4));
        samples.add(new Pair<>(2, 0.6));
        
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        
        List<Pair<Integer, Double>> result = distribution.getSamples();
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, (int) result.get(0).getKey());
        assertEquals(0.4, result.get(0).getValue(), 0);
        assertEquals(2, (int) result.get(1).getKey());
        assertEquals(0.6, result.get(1).getValue(), 0);
    }

    @Test
    public void testSample() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.4));
        samples.add(new Pair<>(2, 0.6));
        
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        
        Integer result = distribution.sample();
        
        assertNotNull(result);
    }

    @Test
    public void testSampleWithSampleSize() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.4));
        samples.add(new Pair<>(2, 0.6));
        
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        
        Integer[] result = distribution.sample(5);
        
        assertNotNull(result);
        assertEquals(5, result.length);
    }
}