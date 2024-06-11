package org.apache.commons.math3.distribution;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        
        assertEquals(0.2, distribution.probability(1), 0.0);
        assertEquals(0.3, distribution.probability(2), 0.0);
        assertEquals(0.5, distribution.probability(3), 0.0);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        assertEquals(samples, distribution.getSamples());
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Integer result = distribution.sample();

        assertTrue(result == 1 || result == 2 || result == 3);
    }

    @Test
    public void testSampleWithSize() throws NotStrictlyPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Integer[] result = distribution.sample(5);

        assertEquals(5, result.length);
    }

    @Test(expected = MathArithmeticException.class)
    public void testInvalidProbabilitySum() throws MathArithmeticException, NotPositiveException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.4));

        new DiscreteDistribution<>(samples);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.2));

        new DiscreteDistribution<>(samples);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSize() throws NotStrictlyPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        distribution.sample(-5);
    }
}