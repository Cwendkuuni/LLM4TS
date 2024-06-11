package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

public class DiscreteDistributionTest {
    private DiscreteDistribution<Integer> dist;
    private List<Pair<Integer, Double>> samples;
    
    @Before
    public void setUp() {
        samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.5));
        try {
            dist = new DiscreteDistribution<Integer>(samples);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testProbability() {
        assertEquals(0.2, dist.probability(1), 0.0001);
        assertEquals(0.3, dist.probability(2), 0.0001);
        assertEquals(0.5, dist.probability(3), 0.0001);
        assertEquals(0.0, dist.probability(0), 0.0001);
        assertEquals(0.0, dist.probability(4), 0.0001);
        assertEquals(0.0, dist.probability(null), 0.0001);
    }
    
    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> distSamples = dist.getSamples();
        assertEquals(samples.size(), distSamples.size());
        for (int i = 0; i < samples.size(); i++) {
            Pair<Integer, Double> expected = samples.get(i);
            Pair<Integer, Double> actual = distSamples.get(i);
            assertEquals(expected.getKey(), actual.getKey());
            assertEquals(expected.getValue(), actual.getValue(), 0.0001);
        }
    }
    
    @Test
    public void testSample() {
        int sampleSize = 1000;
        Integer[] result = dist.sample(sampleSize);
        int[] countMap = new int[samples.size()];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < samples.size(); j++) {
                if (samples.get(j).getKey() == result[i]) {
                    countMap[j]++;
                }
            }
        }
        
        for (int i = 0; i < samples.size(); i++) {
            double expected = samples.get(i).getValue() * sampleSize;
            double actual = countMap[i];
            assertTrue(Math.abs(expected - actual) / expected < 0.1);
        }
    }
    
    @Test(expected=NotStrictlyPositiveException.class)
    public void testSampleInvalidSampleSize() throws NotStrictlyPositiveException {
        int sampleSize = -1;
        dist.sample(sampleSize);
    }
    
    @Test
    public void testReseedRandomGenerator() {
        long seed = 12345;
        dist.reseedRandomGenerator(seed);
        RandomGenerator generator = dist.random;
        assertTrue(generator instanceof Well19937c);
        assertEquals(seed, generator.getSeed());
    }
    
    @Test
    public void testConstructorWithInvalidSamples() {
        try {
            samples.add(new Pair<Integer, Double>(4, -0.1));
            dist = new DiscreteDistribution<Integer>(samples);
        } catch (NotPositiveException e) {
            // Expected exception
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            samples.remove(3);
            samples.add(new Pair<Integer, Double>(5, 0.5));
            dist = new DiscreteDistribution<Integer>(samples);
        } catch (MathArithmeticException e) {
            // Expected exception
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            samples.remove(2);
            samples.add(new Pair<Integer, Double>(6, Double.POSITIVE_INFINITY));
            dist = new DiscreteDistribution<Integer>(samples);
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}