package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = null;

        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (MathIllegalArgumentException e) {
            e.printStackTrace();
        }

        assertNotNull(distribution);

        assertEquals(0.5, distribution.probability(1), 0.001);
        assertEquals(0.3, distribution.probability(2), 0.001);
        assertEquals(0.2, distribution.probability(3), 0.001);
        assertEquals(0.0, distribution.probability(4), 0.001);
    }

    @Test
    public void testSampleSizePositive() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = null;

        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (MathIllegalArgumentException e) {
            e.printStackTrace();
        }

        assertNotNull(distribution);

        Integer[] sample = null;

        try {
            sample = distribution.sample(5);
        } catch (NotStrictlyPositiveException e) {
            e.printStackTrace();
        }

        assertNotNull(sample);
        assertEquals(5, sample.length);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleSizeZero() throws NotStrictlyPositiveException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.4));
        samples.add(new Pair<>("B", 0.6));

        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(new MersenneTwister(), samples);

        distribution.sample(0);
    }

    @Test
    public void testProbabilityExceptions() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.5));
        samples.add(new Pair<>(2, 0.7));
        samples.add(new Pair<>(3, 0.8));

        DiscreteDistribution<Integer> distribution = null;

        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (MathIllegalArgumentException e) {
            e.printStackTrace();
        }

        assertNull(distribution);
    }

    @Test
    public void testEmptySamples() {
        List<Pair<String, Double>> samples = new ArrayList<>();

        DiscreteDistribution<String> distribution = null;

        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (MathIllegalArgumentException e) {
            e.printStackTrace();
        }

        assertNull(distribution);
    }

}