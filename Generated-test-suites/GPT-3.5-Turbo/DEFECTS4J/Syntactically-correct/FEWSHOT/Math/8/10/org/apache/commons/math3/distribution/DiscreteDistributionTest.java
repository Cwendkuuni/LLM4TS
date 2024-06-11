package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals; 
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    private List<Pair<String, Double>> samples;
    private DiscreteDistribution<String> distribution;

    @Before
    public void setUp() {
        samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));

        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProbability() {
        assertEquals(0.2, distribution.probability("A"), 0);
        assertEquals(0.3, distribution.probability("B"), 0);
        assertEquals(0.5, distribution.probability("C"), 0);
        assertEquals(0, distribution.probability("D"), 0);
    }

    @Test
    public void testGetSamples() {
        List<Pair<String, Double>> actualSamples = distribution.getSamples();

        assertNotNull(actualSamples);
        assertEquals(samples, actualSamples);
    }

    @Test
    public void testSample() {
        String sample = distribution.sample();

        assertNotNull(sample);
        assertTrue(sample.equals("A") || sample.equals("B") || sample.equals("C"));
    }

    @Test
    public void testSampleWithSize() throws NotStrictlyPositiveException {
        int sampleSize = 5;

        String[] sample = distribution.sample(sampleSize);

        assertNotNull(sample);
        assertEquals(sampleSize, sample.length);

        for (String s : sample) {
            assertTrue(s.equals("A") || s.equals("B") || s.equals("C"));
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithNegativeSize() throws NotStrictlyPositiveException {
        distribution.sample(-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithZeroSize() throws NotStrictlyPositiveException {
        distribution.sample(0);
    }

    @Test
    public void testReseedRandomGenerator() {
        RandomGenerator rng = distribution.random;
        long seed = 12345;

        distribution.reseedRandomGenerator(seed);

        assertNotEquals(rng, distribution.random);
    }
}