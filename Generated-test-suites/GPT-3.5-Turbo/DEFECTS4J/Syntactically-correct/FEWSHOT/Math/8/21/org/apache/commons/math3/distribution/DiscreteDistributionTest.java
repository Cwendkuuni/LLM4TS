package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

public class DiscreteDistributionTest {
    
    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.5));
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProbability() {
        assertEquals(0.2, distribution.probability(1), 0.000001);
        assertEquals(0.3, distribution.probability(2), 0.000001);
        assertEquals(0.5, distribution.probability(3), 0.000001);
        assertEquals(0.0, distribution.probability(4), 0.000001);
        assertEquals(0.0, distribution.probability(null), 0.000001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        assertEquals(1, (int) samples.get(0).getKey());
        assertEquals(0.2, samples.get(0).getValue(), 0.000001);
        assertEquals(2, (int) samples.get(1).getKey());
        assertEquals(0.3, samples.get(1).getValue(), 0.000001);
        assertEquals(3, (int) samples.get(2).getKey());
        assertEquals(0.5, samples.get(2).getValue(), 0.000001);
    }

    @Test
    public void testSample() {
        int n = 100000;
        int count1 = 0, count2 = 0, count3 = 0;
        for (int i = 0; i < n; i++) {
            int sample = distribution.sample();
            if (sample == 1) {
                count1++;
            } else if (sample == 2) {
                count2++;
            } else if (sample == 3) {
                count3++;
            }
        }
        assertEquals(0.2, (double) count1 / n, 0.01);
        assertEquals(0.3, (double) count2 / n, 0.01);
        assertEquals(0.5, (double) count3 / n, 0.01);
    }

    @Test
    public void testSampleArray() {
        int n = 100;
        Integer[] samples = distribution.sample(n);
        assertEquals(n, samples.length);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleArrayInvalidSize() {
        int n = -1;
        distribution.sample(n);
    }
}