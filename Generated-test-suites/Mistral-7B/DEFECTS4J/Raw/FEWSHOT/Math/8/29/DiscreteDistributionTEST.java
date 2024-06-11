package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.DiscreteDistribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

public class DiscreteDistributionTest {

    @Test
    public void testProbabilityNullInput() {
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(new Well19937c(), new ArrayList<Pair<String, Double>>());
        assertNull(distribution.probability(null));
    }

    @Test
    public void testProbabilitySingleValue() {
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(new Well19937c(), new ArrayList<Pair<String, Double>>() {{
            add(new Pair<String, Double>("a", 1.0));
        }});
        assertEquals(1.0, distribution.probability("a"), 0.001);
    }

    @Test
    public void testProbabilityMultipleValues() {
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(new Well19937c(), new ArrayList<Pair<String, Double>>() {{
            add(new Pair<String, Double>("a", 0.3));
            add(new Pair<String, Double>("b", 0.7));
        }});
        assertEquals(0.3, distribution.probability("a"), 0.001);
        assertEquals(0.7, distribution.probability("b"), 0.001);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeProbability() {
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(new Well19937c(), new ArrayList<Pair<String, Double>>() {{
            add(new Pair<String, Double>("a", -0.1));
        }});
    }

    @Test(expected = MathArithmeticException.class)
    public void testZeroProbabilities() {
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(new Well19937c(), new ArrayList<Pair<String, Double>>() {{
            add(new Pair<String, Double>("a", 0.0));
            add(new Pair<String, Double>("b", 0.0));
        }});
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInfiniteProbability() {
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(new Well19937c(), new ArrayList<Pair<String, Double>>() {{
            add(new Pair<String, Double>("a", Double.POSITIVE_INFINITY));
        }});
    }

    @Test
    public void testGetSamplesNullInput() {
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(new Well19937c(), null);
        assertNull(distribution.getSamples());
    }

    @Test
    public void testGetSamplesValidInput() {
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(new Well19937c(), new ArrayList<Pair<String, Double>>() {{
            add(new Pair<String, Double>("a", 0.3));
            add(new Pair<String, Double>("b", 0.7));
        }});
        List<Pair<String, Double>> samples = distribution.getSamples();
        assertEquals(2, samples.size());
        assertEquals("a", samples.get(0).getKey());
        assertEquals(0.3, samples.get(0).getValue(), 0.001);
        assertEquals("b", samples.get(1).getKey());
        assertEquals(0.7, samples.get(1).getValue(), 0.001);
    }

    @Test
    public void testSampleNullInput() {
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(new Well19937c(), null);
        assertNull(distribution.sample());
    }

    @Test
    public void testSampleSingleValue() {
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(new Well19937c(), new ArrayList<Pair<String, Double>>() {{
            add(new Pair<String, Double>("a", 1.0));
        }});
        assertEquals("a", distribution.sample());
    }

    @Test
    public void testSampleMultipleValues() {
        Random random = new Random();
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), new ArrayList<Pair<Integer, Double>>() {{
            add(new Pair<Integer, Double>(-1, 0.1));
            add(new Pair<Integer, Double>(0, 0.3));
            add(new Pair<Integer, Double>(1, 0.6));
        }});
        for (int i = 0; i < 1000; i++) {
            int sample = distribution.sample();
            assertEquals(Integer.class, sample.getClass());
        }
    }

    @Test
    public void testSampleSameValueMultipleTimes() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), new ArrayList<Pair<Integer, Double>>() {{
            add(new Pair<Integer, Double>(1, 1.0));
        }});
        for (int i = 0; i < 10; i++) {
            assertSame(1, distribution.sample());
        }
    }

    @Test
    public void testSampleDifferentValues() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), new ArrayList<Pair<Integer, Double>>() {{
            add(new Pair<Integer, Double>(1, 0.4));
            add(new Pair<Integer, Double>(2, 0.6));
        }});
        for (int i = 0; i < 10; i++) {
            int sample = distribution.sample();
            if (sample == 1) {
                assertEquals(1, sample);
            } else {
                assertEquals(2, sample);
            }
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSampleSize() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), new ArrayList<Pair<Integer, Double>>() {{
            add(new Pair<Integer, Double>(1, 1.0));
        }});
        distribution.sample(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputSample() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), new ArrayList<Pair<Integer, Double>>() {{
            add(new Pair<Integer, Double>(1, 1.0));
        }});
        distribution.sample(null);
    }

    @Test
    public void testSampleArrayNullInput() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), new ArrayList<Pair<Integer, Double>>() {{
            add(new Pair<Integer, Double>(1, 1.0));
        }});
        assertArrayEquals(new Integer[0], distribution.sample((Integer[]) null));
    }

    @Test
    public void testSampleArraySizeZero() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), new ArrayList<Pair<Integer, Double>>() {{
            add(new Pair<Integer, Double>(1, 1.0));
        }});
        T[] sample = distribution.sample(0);
        assertArrayEquals(new Integer[0], sample);
    }

    @Test
    public void testSampleArrayProperSize() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), new ArrayList<Pair<Integer, Double>>() {{
            add(new Pair<Integer, Double>(1, 0.4));
            add(new Pair<Integer, Double>(2, 0.6));
        }});
        T[] sample = distribution.sample(2);
        assertEquals(2, sample.length);
        for (int i = 0; i < sample.length; i++) {
            int sampleValue = (Integer) sample[i];
            if (sampleValue == 1) {
                assertEquals(1, sampleValue);
            } else {
                assertEquals(2, sampleValue);
            }
        }
    }
}