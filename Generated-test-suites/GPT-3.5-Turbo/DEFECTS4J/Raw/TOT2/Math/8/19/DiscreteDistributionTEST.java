package org.apache.commons.math3.distribution;

import org.junit.Test;
import org.mockito.Mockito;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));

        DiscreteDistribution<String> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException e) {
            e.printStackTrace();
        }

        assertNotNull(distribution);

        assertEquals(0.2, distribution.probability("A"), 0.0);
        assertEquals(0.3, distribution.probability("B"), 0.0);
        assertEquals(0.5, distribution.probability("C"), 0.0);
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException e) {
            e.printStackTrace();
        }

        assertNotNull(distribution);

        Integer sample = distribution.sample();
        assertNotNull(sample);
        System.out.println("Sampled value: " + sample);
    }

    @Test
    public void testSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException e) {
            e.printStackTrace();
        }

        assertNotNull(distribution);

        Integer[] sampleArray = null;
        try {
            sampleArray = distribution.sample(5);
        } catch (NotStrictlyPositiveException e) {
            e.printStackTrace();
        }

        assertNotNull(sampleArray);
        System.out.println("Sampled array: " + Arrays.toString(sampleArray));
    }

}