package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.DiscreteDistribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Pair;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionEmptyConstructor() {
        assertNull(new DiscreteDistribution<Object>(null));
    }

    @Test
    public void testDiscreteDistributionConstructorWithNullSingletons() {
        assertThrows(NotStrictlyPositiveException.class, () -> new DiscreteDistribution<Object>(new Well19937c(), null));
    }

    @Test
    public void testDiscreteDistributionConstructorWithNullProbabilities() {
        List<Pair<Object, Double>> samples = Arrays.asList(Pair.of(null, 1.0));
        assertThrows(MathIllegalArgumentException.class, () -> new DiscreteDistribution<Object>(new Well19937c(), samples));
    }

    @Test
    public void testDiscreteDistributionConstructorWithInvalidProbability() {
        List<Pair<Object, Double>> samples = Arrays.asList(Pair.of(1, -1.0), Pair.of(2, 1.0));
        assertThrows(NotPositiveException.class, () -> new DiscreteDistribution<Object>(new Well19937c(), samples));
    }

    @Test
    public void testDiscreteDistributionConstructorWithProbabilitiesSumToZero() {
        List<Pair<Object, Double>> samples = Arrays.asList(Pair.of(1, 1.0), Pair.of(2, -1.0));
        assertThrows(MathArithmeticException.class, () -> new DiscreteDistribution<Object>(new Well19937c(), samples));
    }

    @Test
    public void testDiscreteDistributionConstructorWithInvalidSingleton() {
        List<Pair<Object, Double>> samples = Arrays.asList(Pair.of(null, 1.0), Pair.of(1, Double.POSITIVE_INFINITY));
        assertThrows(MathIllegalArgumentException.class, () -> new DiscreteDistribution<Object>(new Well19937c(), samples));
    }

    @Test
    public void testDiscreteDistributionConstructor() {
        List<Pair<Object, Double>> samples = Arrays.asList(Pair.of(1, 0.1), Pair.of(2, 0.3), Pair.of(3, 0.6));
        DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(new Well19937c(), samples);

        assertEquals(new Well19937c(), distribution.random);
        assertEquals(Arrays.asList(1, 2, 3), distribution.singletons);
        assertArrayEquals(new double[]{0.1, 0.3, 0.6}, distribution.probabilities, 0.001);
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Object, Double>> samples = Arrays.asList(Pair.of(1, 0.1), Pair.of(2, 0.3), Pair.of(3, 0.6));
        DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(new Well19937c(), samples);

        long seed = 12345L;
        distribution.reseedRandomGenerator(seed);
    }

    @Test
    public void testProbability() {
        List<Pair<Object, Double>> samples = Arrays.asList(Pair.of(1, 0.1), Pair.of(2, 0.3), Pair.of(3, 0.6));
        DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(new Well19937c(), samples);

        assertEquals(0.1, distribution.probability(1), 0.001);
        assertEquals(0.3, distribution.probability(2), 0.001);
        assertEquals(0.6, distribution.probability(3), 0.001);
        assertEquals(0.0, distribution.probability(4), 0.001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Object, Double>> samples = Arrays.asList(Pair.of(1, 0.1), Pair.of(2, 0.3), Pair.of(3, 0.6));
        DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(new Well19937c(), samples);

        List<Pair<Object, Double>> resultSamples = distribution.getSamples();

        assertEquals(samples, resultSamples);
    }

    @Test
    public void testSample() {
        List<Pair<Object, Double>> samples = Arrays.asList(Pair.of(1, 0.1), Pair.of(2, 0.3), Pair.of(3, 0.6));
        DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(new Well19937c(), samples);

        assertEquals(1, distribution.sample());
        assertEquals(2, distribution.sample());
        assertEquals(3, distribution.sample());
        assertEquals(3, distribution.sample());
    }

    @Test
    public void testSampleArray() {
        List<Pair<Object, Double>> samples = Arrays.asList(Pair.of(1, 0.1), Pair.of(2, 0.3), Pair.of(3, 0.6));
        DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(new Well19937c(), samples);

        try {
            distribution.sample(0);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        Object[] resultSample = distribution.sample(3);

        for (int i = 0; i < resultSample.length; i++) {
            assertEquals(samples.get(i).getKey(), resultSample[i]);
        }
    }
}