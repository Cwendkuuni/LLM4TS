
package org.apache.commons.math3.distribution;

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
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    private RandomGenerator rng;

    @Before
    public void setUp() {
        rng = new Well19937c();
    }

    @Test
    public void testConstructor() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            assertEquals(samples, distribution.getSamples());
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Should not throw any exception");
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeProbability() throws NotPositiveException,
            MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", -0.2));
        samples.add(new Pair<>("B", 0.5));
        samples.add(new Pair<>("C", 0.7));

        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorZeroProbability() throws NotPositiveException,
            MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.0));
        samples.add(new Pair<>("B", 0.5));
        samples.add(new Pair<>("C", 0.7));

        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorInfiniteProbability() throws NotPositiveException,
            MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", Double.POSITIVE_INFINITY));
        samples.add(new Pair<>("B", 0.5));
        samples.add(new Pair<>("C", 0.7));

        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            long seed1 = rng.nextLong();
            long seed2 = rng.nextLong();

            distribution.reseedRandomGenerator(seed1);
            assertEquals(seed1, distribution.random.getSeed());

            distribution.reseedRandomGenerator(seed2);
            assertEquals(seed2, distribution.random.getSeed());
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Should not throw any exception");
        }
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            assertEquals(0.5, distribution.probability(1), 0);
            assertEquals(0.3, distribution.probability(2), 0);
            assertEquals(0.2, distribution.probability(3), 0);
            assertEquals(0, distribution.probability(4), 0);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Should not throw any exception");
        }
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);
            Integer sample = distribution.sample();
            assertTrue(samples.stream().anyMatch(pair -> pair.getKey().equals(sample)));
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Should not throw any exception");
        }
    }

    @Test
    public void testSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);
            int sampleSize = 10;
            Integer[] sampleArray = distribution.sample(sampleSize);
            assertEquals(sampleSize, sampleArray.length);
            for (Integer sample : sampleArray) {
                assertTrue(samples.stream().anyMatch(pair -> pair.getKey().equals(sample)));
            }
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Should not throw any exception");
        }
    }

}
