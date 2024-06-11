package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

public class DiscreteDistributionTest {
    private DiscreteDistribution<String> discreteDistribution;

    @Before
    public void setup() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.3));
        samples.add(new Pair<String, Double>("B", 0.5));
        samples.add(new Pair<String, Double>("C", 0.2));

        try {
            discreteDistribution = new DiscreteDistribution<String>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProbability() {
        assertEquals(0.3, discreteDistribution.probability("A"), 0.0001);
        assertEquals(0.5, discreteDistribution.probability("B"), 0.0001);
        assertEquals(0.2, discreteDistribution.probability("C"), 0.0001);
        assertEquals(0.0, discreteDistribution.probability("D"), 0.0001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<String, Double>> samples = discreteDistribution.getSamples();
        assertArrayEquals(samples.toArray(), discreteDistribution.getSamples().toArray());
    }

    @Test
    public void testSample() {
        String sample = discreteDistribution.sample();
        assertNotEquals(null, sample);
        assertNull(discreteDistribution.sample(0));
        assertNull(discreteDistribution.sample(-1));

        String[] sampleArray = discreteDistribution.sample(5);
        assertEquals(5, sampleArray.length);
    }
}