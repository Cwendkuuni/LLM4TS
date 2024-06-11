package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.random.Well19937c;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistribution() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            assertEquals(3, distribution.singletons.size());
            assertEquals(0.3, distribution.probability(1), 0.01);
            assertEquals(0.4, distribution.probability(2), 0.01);
            assertEquals(0.3, distribution.probability(3), 0.01);
            assertArrayEquals(new Integer[]{1, 2, 3}, distribution.singletons.toArray());
        } catch (MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        try {
            new DiscreteDistribution<>(samples);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            //expected
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testZeroProbabilitiesSum() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.2));
        samples.add(new Pair<>(3, -0.4));
        try {
            new DiscreteDistribution<>(samples);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            //expected
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        try {
            new DiscreteDistribution<>(samples);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        long seed = System.currentTimeMillis();
        distribution.reseedRandomGenerator(seed);

        // The random generator should be reseeded, but the distribution itself
        // should remain the same.
        assertEquals(0.3, distribution.probability(1), 0.01);
        assertEquals(0.4, distribution.probability(2), 0.01);
        assertEquals(0.3, distribution.probability(3), 0.01);
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        Integer sample = distribution.sample();
        assertEquals(1, sample.intValue());
        assertEquals(0.3, distribution.probability(1), 0.01);
    }

    @Test
    public void testSampleArray() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        Integer[] sampleArray = distribution.sample(5);
        for (int i = 0; i < sampleArray.length; i++) {
            assertEquals(1 + i, sampleArray[i].intValue());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        distribution.sample(-1);
        fail("Expected NotStrictlyPositiveException");
    }
}