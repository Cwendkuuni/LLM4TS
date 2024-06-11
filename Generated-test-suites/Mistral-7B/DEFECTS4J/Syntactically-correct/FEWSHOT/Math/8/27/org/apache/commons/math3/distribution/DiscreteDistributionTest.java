package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class DiscreteDistributionTest {

    private static final double DELTA = 1e-10;

    private DiscreteDistribution<Integer> dist;
    private RandomGenerator random;

    @Before
    public void setup() {
        random = new Well19937c();
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3),
                new Pair<>(4, 0.1));
        try {
            dist = new DiscreteDistribution<>(random, samples);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testProbability() {
        assertEquals(0.3, dist.probability((Integer) 1), DELTA);
        assertEquals(0.4, dist.probability((Integer) 2), DELTA);
        assertEquals(0.3, dist.probability((Integer) 3), DELTA);
        assertEquals(0.1, dist.probability((Integer) 4), DELTA);
        assertNull(dist.probability((Integer) 5));
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = dist.getSamples();
        assertEquals(4, samples.size());
        assertEquals((Integer) 1, samples.get(0).getKey());
        assertEquals(0.3, samples.get(0).getValue(), DELTA);
        assertEquals((Integer) 2, samples.get(1).getKey());
        assertEquals(0.4, samples.get(1).getValue(), DELTA);
        assertEquals((Integer) 3, samples.get(2).getKey());
        assertEquals(0.3, samples.get(2).getValue(), DELTA);
        assertEquals((Integer) 4, samples.get(3).getKey());
        assertEquals(0.1, samples.get(3).getValue(), DELTA);
    }

    @Test
    public void testSample() {
        Integer sample = dist.sample();
        List<Integer> samples = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            samples.add(dist.sample());
        }
        int[] sampleArray = samples.stream().mapToInt(i -> i).toArray();
        assertArrayEquals(new Integer[]{1, 2, 3, 4}, sampleArray);
    }

    @Test
    public void testSampleNegativeSize() {
        assertThrows(NotStrictlyPositiveException.class, () -> dist.sample(-1));
    }

    @Test
    public void testReseedRandomGenerator() {
        long seed = 123;
        dist.reseedRandomGenerator(seed);
        Integer sample = dist.sample();
        assertNotNull(sample);
    }
}