package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;
    private List<Pair<Integer, Double>> samples;

    @Before
    public void setUp() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.25));
        samples.add(new Pair<Integer, Double>(2, 0.5));
        samples.add(new Pair<Integer, Double>(3, 0.25));

        distribution = new DiscreteDistribution<Integer>(samples);
    }

    @Test
    public void testProbability() {
        assertEquals(0.25, distribution.probability(1), 0.0001);
        assertEquals(0.5, distribution.probability(2), 0.0001);
        assertEquals(0.25, distribution.probability(3), 0.0001);
        assertEquals(0.0, distribution.probability(4), 0.0001);
    }

    @Test
    public void testGetSamples() {
        assertEquals(samples, distribution.getSamples());
    }

    @Test
    public void testSample() {
        RandomGenerator random = Mockito.mock(RandomGenerator.class);
        Mockito.when(random.nextDouble()).thenReturn(0.1);
        distribution.reseedRandomGenerator(123456789L);

        assertEquals(1, (int) distribution.sample());
        
        Mockito.when(random.nextDouble()).thenReturn(0.3);
        assertEquals(2, (int) distribution.sample());
        
        Mockito.when(random.nextDouble()).thenReturn(0.6);
        assertEquals(2, (int) distribution.sample());
        
        Mockito.when(random.nextDouble()).thenReturn(0.9);
        assertEquals(3, (int) distribution.sample());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleInvalidSampleSize() throws NotStrictlyPositiveException {
        distribution.sample(0);
    }

    @Test
    public void testSampleArray() throws NotStrictlyPositiveException {
        Integer[] expected = {1, 2, 2, 3, 1};
        RandomGenerator random = Mockito.mock(RandomGenerator.class);
        distribution.reseedRandomGenerator(123456789L);

        Mockito.when(random.nextDouble()).thenReturn(0.1, 0.3, 0.6, 0.9, 0.2);
        assertArrayEquals(expected, distribution.sample(5));
    }

    @Test(expected = ClassCastException.class)
    public void testSampleInvalidSampleType() throws NotStrictlyPositiveException {
        DiscreteDistribution<String> stringDistribution = new DiscreteDistribution<String>(samples);
        stringDistribution.sample(5);
    }
}