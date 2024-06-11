package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;

public class DiscreteDistributionTest {

    @Test
    public void testProbabilityNullSingletonAndProbability() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(null, 0.2),
                new Pair<>(null, 0.5),
                new Pair<>(null, 0.3)
        );

        assertThrows(MathIllegalArgumentException.class, () -> new DiscreteDistribution<Integer>(samples));

        final List<Pair<Integer, Double>> samplesNullProb = Arrays.asList(
                new Pair<>(null, null),
                new Pair<>(null, 0.5),
                new Pair<>(null, 0.3)
        );

        assertThrows(MathIllegalArgumentException.class, () -> new DiscreteDistribution<Integer>(samplesNullProb));

        final List<Pair<Integer, Double>> samplesPositiveProb = Arrays.asList(
                new Pair<>(null, 0.2),
                new Pair<>(null, 0.5),
                new Pair<>(null, 0.3)
        );

        assertThrows(NotPositiveException.class, () -> new DiscreteDistribution<Integer>(samplesPositiveProb));
    }

    @Test
    public void testProbabilityZeroSum() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.1),
                new Pair<>(3, -0.1)
        );

        assertThrows(MathArithmeticException.class, () -> new DiscreteDistribution<Integer>(samples));
    }

    @Test
    public void testProbabilityInfiniteProbability() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, Double.POSITIVE_INFINITY),
                new Pair<>(3, 0.1)
        );

        assertThrows(MathIllegalArgumentException.class, () -> new DiscreteDistribution<Integer>(samples));
    }

    @Test
    public void testProbability() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.2),
                new Pair<>(3, 0.3)
        );

        try {
            final DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);

            assertEquals(0.1, distribution.probability(1), 0.001);
            assertEquals(0.2, distribution.probability(2), 0.001);
            assertEquals(0.3, distribution.probability(3), 0.001);
            assertNull(distribution.probability(4));
        } catch (Exception e) {
            // Should not reach here
        }
    }

    @Test
    public void testGetSamples() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.2),
                new Pair<>(3, 0.3)
        );

        try {
            final DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);

            List<Pair<Integer, Double>> expected = Arrays.asList(
                    new Pair<>(1, 0.1),
                    new Pair<>(2, 0.2),
                    new Pair<>(3, 0.3)
            );

            assertEquals(expected, distribution.getSamples());
        } catch (Exception e) {
            // Should not reach here
        }
    }

    @Test
    public void testSample() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.2),
                new Pair<>(3, 0.3)
        );

        try {
            final DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);

            int result;
            for (int i = 0; i < 100; i++) {
                result = distribution.sample().intValue();
                assertEquals(1, 4, Collections.frequency(Arrays.asList(samples.stream().map(p -> p.getKey().intValue()).toArray()), result));
            }
        } catch (Exception e) {
            // Should not reach here
        }
    }

    @Test
    public void testSampleSizeZero() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.2),
                new Pair<>(3, 0.3)
        );

        try {
            final DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            assertThrows(NotStrictlyPositiveException.class, () -> distribution.sample(0));
        } catch (Exception e) {
            // Should not reach here
        }
    }

    @Test
    public void testSampleSize() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.2),
                new Pair<>(3, 0.3)
        );

        try {
            final DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);

            final T[] result = distribution.sample(5);
            for (int i = 0; i < result.length; i++) {
                assertEquals(1, 4, Collections.frequency(Arrays.asList(samples.stream().map(p -> p.getKey().intValue()).toArray()), result[i].intValue()));
            }
        } catch (Exception e) {
            // Should not reach here
        }
    }

    @Test
    public void testReseedRandomGenerator() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.2),
                new Pair<>(3, 0.3)
        );

        final RandomGenerator rng = new Well19937c();

        try {
            final DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(rng, samples);

            distribution.reseedRandomGenerator(1);

            final T[] sample = distribution.sample(10);
            for (int i = 0; i < sample.length; i++) {
                assertEquals(1, 4, Collections.frequency(Arrays.asList(samples.stream().map(p -> p.getKey().intValue()).toArray()), sample[i].intValue()));
            }
        } catch (Exception e) {
            // Should not reach here
        }
    }
}