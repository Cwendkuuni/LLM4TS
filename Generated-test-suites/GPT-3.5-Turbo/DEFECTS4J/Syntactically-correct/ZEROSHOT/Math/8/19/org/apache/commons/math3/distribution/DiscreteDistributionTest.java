import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.random.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.4));
        samples.add(new Pair<Integer, Double>(3, 0.4));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        assertEquals(0.2, distribution.probability(1), 0.0001);
        assertEquals(0.4, distribution.probability(2), 0.0001);
        assertEquals(0.4, distribution.probability(3), 0.0001);
        assertEquals(0, distribution.probability(4), 0.0001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.4));
        samples.add(new Pair<Integer, Double>(3, 0.4));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        List<Pair<Integer, Double>> actualSamples = distribution.getSamples();

        assertEquals(samples.size(), actualSamples.size());
        for (int i = 0; i < samples.size(); i++) {
            assertEquals(samples.get(i).getKey(), actualSamples.get(i).getKey());
            assertEquals(samples.get(i).getValue(), actualSamples.get(i).getValue(), 0.0001);
        }
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.4));
        samples.add(new Pair<Integer, Double>(3, 0.4));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Integer sample1 = distribution.sample();
        Integer sample2 = distribution.sample();
        Integer sample3 = distribution.sample();

        assertNotNull(sample1);
        assertNotNull(sample2);
        assertNotNull(sample3);
        assertTrue(samples.contains(new Pair<>(sample1, 0.0)));
        assertTrue(samples.contains(new Pair<>(sample2, 0.0)));
        assertTrue(samples.contains(new Pair<>(sample3, 0.0)));
    }

    @Test
    public void testSampleWithSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.4));
        samples.add(new Pair<Integer, Double>(3, 0.4));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Integer[] actualSamples = distribution.sample(10);

        assertNotNull(actualSamples);
        assertEquals(10, actualSamples.length);
        for (Integer sample : actualSamples) {
            assertTrue(samples.contains(new Pair<>(sample, 0.0)));
        }
    }
}