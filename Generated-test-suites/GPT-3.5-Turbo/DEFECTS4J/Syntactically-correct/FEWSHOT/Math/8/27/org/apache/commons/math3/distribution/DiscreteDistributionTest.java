import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {
    private final double EPSILON = 1E-6;

    // Helper method to create a DiscreteDistribution instance
    private DiscreteDistribution<Integer> createDistribution(List<Pair<Integer, Double>> samples) {
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
        return distribution;
    }

    // Helper method to create a list of Pair objects
    private List<Pair<Integer, Double>> createPairList(Integer[] values, Double[] probabilities) {
        List<Pair<Integer, Double>> pairs = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            pairs.add(new Pair<>(values[i], probabilities[i]));
        }
        return pairs;
    }

    @Test
    public void testProbability() {
        Integer[] values = {1, 2, 3, 4};
        Double[] probabilities = {0.1, 0.5, 0.2, 0.2};
        List<Pair<Integer, Double>> samples = createPairList(values, probabilities);
        DiscreteDistribution<Integer> distribution = createDistribution(samples);

        assertEquals(0.1, distribution.probability(1), EPSILON);
        assertEquals(0.5, distribution.probability(2), EPSILON);
        assertEquals(0.2, distribution.probability(3), EPSILON);
        assertEquals(0.2, distribution.probability(4), EPSILON);
        assertEquals(0.0, distribution.probability(5), EPSILON);
    }

    @Test
    public void testGetSamples() {
        Integer[] values = {1, 2, 3, 4};
        Double[] probabilities = {0.1, 0.5, 0.2, 0.2};
        List<Pair<Integer, Double>> expectedSamples = createPairList(values, probabilities);
        DiscreteDistribution<Integer> distribution = createDistribution(expectedSamples);

        List<Pair<Integer, Double>> actualSamples = distribution.getSamples();

        assertEquals(expectedSamples.size(), actualSamples.size());
        for (int i = 0; i < expectedSamples.size(); i++) {
            Pair<Integer, Double> expectedPair = expectedSamples.get(i);
            Pair<Integer, Double> actualPair = actualSamples.get(i);

            assertEquals(expectedPair.getKey(), actualPair.getKey());
            assertEquals(expectedPair.getValue(), actualPair.getValue());
        }
    }

    @Test
    public void testSample() {
        Integer[] values = {1, 2, 3, 4};
        Double[] probabilities = {0.1, 0.5, 0.2, 0.2};
        List<Pair<Integer, Double>> samples = createPairList(values, probabilities);
        DiscreteDistribution<Integer> distribution = createDistribution(samples);

        for (int i = 0; i < 100; i++) {
            Integer sample = distribution.sample();
            assertNotNull(sample);
            assertTrue(sample >= 1 && sample <= 4);
        }
    }

    @Test
    public void testSampleWithSize() {
        Integer[] values = {1, 2, 3, 4};
        Double[] probabilities = {0.1, 0.5, 0.2, 0.2};
        List<Pair<Integer, Double>> samples = createPairList(values, probabilities);
        DiscreteDistribution<Integer> distribution = createDistribution(samples);

        int sampleSize = 100;
        Integer[] sample = distribution.sample(sampleSize);
        assertNotNull(sample);
        assertEquals(sampleSize, sample.length);

        for (Integer value : sample) {
            assertNotNull(value);
            assertTrue(value >= 1 && value <= 4);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithInvalidSize() {
        Integer[] values = {1, 2, 3, 4};
        Double[] probabilities = {0.1, 0.5, 0.2, 0.2};
        List<Pair<Integer, Double>> samples = createPairList(values, probabilities);
        DiscreteDistribution<Integer> distribution = createDistribution(samples);

        int sampleSize = -5;
        distribution.sample(sampleSize);
    }
}