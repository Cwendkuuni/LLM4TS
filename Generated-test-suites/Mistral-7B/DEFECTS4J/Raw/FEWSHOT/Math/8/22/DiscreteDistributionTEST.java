package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Pair;

import org.apache.commons.cli.DiscreteDistribution;

public class DiscreteDistributionTest {

    @Test
    public void testConstructorWithInvalidSamples() {
        assertThrows(MathIllegalArgumentException.class, () -> new DiscreteDistribution(new ArrayList<>()));
        assertThrows(MathIllegalArgumentException.class, () -> new DiscreteDistribution(new ArrayList<>(), null));
        assertThrows(NotPositiveException.class, () -> new DiscreteDistribution(new ArrayList<>(), List.of(new Pair<>(null, 1.0))));
    }

    @Test
    public void testConstructorWithValidSamples() {
        final List<Pair<Double, Double>> samples = List.of(
                new Pair<>(1.0, 0.1),
                new Pair<>(2.0, 0.3),
                new Pair<>(3.0, 0.5),
                new Pair<>(4.0, 0.1)
        );

        try {
            final DiscreteDistribution<Double> distribution = new DiscreteDistribution<Double>(new Well19937c(), samples);
            final List<Pair<Double, Double>> samplesFromDistribution = distribution.getSamples();
            assertEquals(samples, samplesFromDistribution);
        } catch (MathArithmeticException | MathIllegalArgumentException e) {
            // This should not happen
            assert false;
        }
    }

    @Test
    public void testProbabilityWithValidInput() {
        final List<Pair<Double, Double>> samples = List.of(
                new Pair<>(1.0, 0.1),
                new Pair<>(2.0, 0.3),
                new Pair<>(3.0, 0.5),
                new Pair<>(4.0, 0.1)
        );

        try {
            final DiscreteDistribution<Double> distribution = new DiscreteDistribution<Double>(new Well19937c(), samples);

            assertEquals(0.1, distribution.probability(1.0), 0.001);
            assertEquals(0.3, distribution.probability(2.0), 0.001);
            assertEquals(0.5, distribution.probability(3.0), 0.001);
            assertEquals(0.1, distribution.probability(4.0), 0.001);
            assertNull(distribution.probability(5.0));
        } catch (MathArithmeticException | MathIllegalArgumentException | NotPositiveException e) {
            // This should not happen
            assert false;
        }
    }

    @Test
    public void testProbabilityWithInvalidInput() {
        final List<Pair<Double, Double>> samples = List.of(
                new Pair<>(1.0, 0.1),
                new Pair<>(2.0, 0.3),
                new Pair<>(3.0, 0.5),
                new Pair<>(4.0, 0.1)
        );

        try {
            final DiscreteDistribution<Double> distribution = new DiscreteDistribution<Double>(new Well19937c(), samples);
            assertNull(distribution.probability(null));
        } catch (MathIllegalArgumentException e) {
            // This is expected
        }
    }

    @Test
    public void testSampleWithValidInput() {
        final List<Pair<Double, Double>> samples = List.of(
                new Pair<>(1.0, 0.1),
                new Pair<>(2.0, 0.3),
                new Pair<>(3.0, 0.5),
                new Pair<>(4.0, 0.1)
        );

        try {
            final DiscreteDistribution<Double> distribution = new DiscreteDistribution<Double>(new Well19937c(), samples);
            assertEquals(1.0, distribution.sample(), 0.001);
            assertEquals(2.0, distribution.sample(), 0.001);
            assertEquals(3.0, distribution.sample(), 0.001);
            assertEquals(4.0, distribution.sample(), 0.001);
            assertNull(distribution.sample()); // Should return null if all probabilities are 0
        } catch (MathArithmeticException | MathIllegalArgumentException | NotPositiveException e) {
            // This should not happen
            assert false;
        }
    }

    @Test
    public void testSampleWithInvalidInput() {
        final List<Pair<Double, Double>> samples = List.of(
                new Pair<>(1.0, 0.1),
                new Pair<>(2.0, 0.3),
                new Pair<>(3.0, 0.5),
                new Pair<>(4.0, 0.1)
        );

        try {
            final DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(new Well19937c(), samples);
            assertNull(distribution.sample()); // Should return null since the type of the sample is not compatible with String
        } catch (MathIllegalArgumentException e) {
            // This is expected
        }
    }

    @Test
    public void testSampleWithLargeInput() {
        final List<Pair<Double, Double>> samples = List.of(
                new Pair<>(1.0, 0.1),
                new Pair<>(2.0, 0.3),
                new Pair<>(3.0, 0.5),
                new Pair<>(4.0, 0.1)
        );

        try {
            final DiscreteDistribution<Double> distribution = new DiscreteDistribution<Double>(new Well19937c(), samples);
            final double[] sample = distribution.sample(100000);
            assertArrayEquals(new double[100000], sample);
        } catch (MathIllegalArgumentException | NotStrictlyPositiveException e) {
            // This should not happen
            assert false;
        }
    }
}