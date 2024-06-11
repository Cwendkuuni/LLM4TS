
package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    private DiscreteDistribution<String> distribution;
    private final double DELTA = 1e-15;

    @Before
    public void setUp() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.25));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.45));
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception occurred during initialization: " + e.getMessage());
        }
    }

    @Test
    public void testProbability() {
        assertEquals(0.25, distribution.probability("A"), DELTA);
        assertEquals(0.3, distribution.probability("B"), DELTA);
        assertEquals(0.45, distribution.probability("C"), DELTA);
        assertEquals(0, distribution.probability("D"), DELTA);
    }

    @Test
    public void testGetSamples() {
        List<Pair<String, Double>> expectedSamples = new ArrayList<>();
        expectedSamples.add(new Pair<>("A", 0.25));
        expectedSamples.add(new Pair<>("B", 0.3));
        expectedSamples.add(new Pair<>("C", 0.45));
        List<Pair<String, Double>> actualSamples = distribution.getSamples();
        assertEquals(expectedSamples, actualSamples);
    }

    @Test
    public void testSample() {
        RandomGenerator rng = new Well19937c(123);
        distribution.reseedRandomGenerator(rng.nextLong());
        String sample = distribution.sample();
        List<String> possibleSamples = Arrays.asList("A", "B", "C");
        assertTrue(possibleSamples.contains(sample));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleInvalidSampleSize() throws NotStrictlyPositiveException {
        distribution.sample(0);
    }

    @Test
    public void testSampleSize() throws NotStrictlyPositiveException {
        int sampleSize = 10000;
        RandomGenerator rng = new Well19937c(123);
        distribution.reseedRandomGenerator(rng.nextLong());
        String[] samples = distribution.sample(sampleSize);
        assertEquals(sampleSize, samples.length);
    }
}
