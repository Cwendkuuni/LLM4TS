import org.junit.Test;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        try {
            distribution = new DiscreteDistribution<>(samples);
            double probability1 = distribution.probability(1);
            double probability2 = distribution.probability(2);
            double probability3 = distribution.probability(3);

            assertEquals(0.2, probability1, 0.0);
            assertEquals(0.3, probability2, 0.0);
            assertEquals(0.5, probability3, 0.0);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        try {
            distribution = new DiscreteDistribution<>(samples);
            List<Pair<Integer, Double>> returnedSamples = distribution.getSamples();

            assertEquals(samples, returnedSamples);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        try {
            distribution = new DiscreteDistribution<>(samples);
            int sample1 = distribution.sample();
            int sample2 = distribution.sample();
            int sample3 = distribution.sample();

            assertTrue(samples.stream().anyMatch(pair -> pair.getKey() == sample1));
            assertTrue(samples.stream().anyMatch(pair -> pair.getKey() == sample2));
            assertTrue(samples.stream().anyMatch(pair -> pair.getKey() == sample3));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSampleWithSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        try {
            distribution = new DiscreteDistribution<>(samples);
            int sampleSize = 10;
            Integer[] sampleArray = distribution.sample(sampleSize);

            assertEquals(sampleSize, sampleArray.length);
            for (Integer sample : sampleArray) {
                assertTrue(samples.stream().anyMatch(pair -> pair.getKey() == sample));
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}