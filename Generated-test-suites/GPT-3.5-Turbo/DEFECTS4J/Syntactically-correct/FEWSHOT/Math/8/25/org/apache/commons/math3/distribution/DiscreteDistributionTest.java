package org.apache.commons.math3.distribution;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testConstructor() {
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>();

        assertNotNull(distribution);
    }

    @Test
    public void testProbability() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("a", 0.2));
        samples.add(new Pair<>("b", 0.3));
        samples.add(new Pair<>("c", 0.5));

        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        assertEquals(0.2, distribution.probability("a"), 0.000001);
        assertEquals(0.3, distribution.probability("b"), 0.000001);
        assertEquals(0.5, distribution.probability("c"), 0.000001);
        assertEquals(0, distribution.probability("d"), 0.000001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("a", 0.1));
        samples.add(new Pair<>("b", 0.2));
        samples.add(new Pair<>("c", 0.3));
        samples.add(new Pair<>("d", 0.4));

        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        List<Pair<String, Double>> output = distribution.getSamples();

        assertEquals(samples, output);
    }

    @Test
    public void testSample() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("a", 0.1));
        samples.add(new Pair<>("b", 0.2));
        samples.add(new Pair<>("c", 0.3));
        samples.add(new Pair<>("d", 0.4));

        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        int sampleSize = 10000;
        String[] samplesOutput = distribution.sample(sampleSize);

        assertEquals(sampleSize, samplesOutput.length);
        for (String sample : samplesOutput) {
            assertTrue(sample.equals("a") || sample.equals("b") || sample.equals("c") || sample.equals("d"));
        }
    }
}