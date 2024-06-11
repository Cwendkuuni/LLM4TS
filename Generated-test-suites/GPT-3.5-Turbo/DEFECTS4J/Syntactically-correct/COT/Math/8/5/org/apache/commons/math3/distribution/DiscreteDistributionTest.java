
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DiscreteDistributionTest {

    private static final double EPSILON = 1e-9;

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Failed to initialize DiscreteDistribution: " + e.getMessage());
        }
    }

    @Test
    public void testProbability() {
        assertEquals(0.2, distribution.probability(1), EPSILON);
        assertEquals(0.3, distribution.probability(2), EPSILON);
        assertEquals(0.5, distribution.probability(3), EPSILON);
        assertEquals(0.0, distribution.probability(4), EPSILON);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        assertNotNull(samples);
        assertEquals(3, samples.size());
        assertEquals(1, (int) samples.get(0).getKey());
        assertEquals(0.2, samples.get(0).getValue(), EPSILON);
        assertEquals(2, (int) samples.get(1).getKey());
        assertEquals(0.3, samples.get(1).getValue(), EPSILON);
        assertEquals(3, (int) samples.get(2).getKey());
        assertEquals(0.5, samples.get(2).getValue(), EPSILON);
    }

    @Test
    public void testSample() {
        int numSamples = 100000;
        int[] counts = new int[4];
        for (int i = 0; i < numSamples; i++) {
            int sample = distribution.sample();
            counts[sample]++;
        }
        assertTrue(counts[1] > 0.19 * numSamples && counts[1] < 0.21 * numSamples);
        assertTrue(counts[2] > 0.29 * numSamples && counts[2] < 0.31 * numSamples);
        assertTrue(counts[3] > 0.49 * numSamples && counts[3] < 0.51 * numSamples);
    }

    @Test
    public void testSampleWithInvalidSampleSize() {
        try {
            distribution.sample(0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testSampleWithValidSampleSize() {
        int sampleSize = 100;
        Integer[] sample = null;
        try {
            sample = distribution.sample(sampleSize);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected NotStrictlyPositiveException: " + e.getMessage());
        }
        assertNotNull(sample);
        assertEquals(sampleSize, sample.length);
    }

    @Test
    public void testReseedRandomGenerator() {
        long seed = 123456789L;
        distribution.reseedRandomGenerator(seed);
        int sample1 = distribution.sample();
        int sample2 = distribution.sample();
        int sample3 = distribution.sample();
        distribution.reseedRandomGenerator(seed);
        int resampled1 = distribution.sample();
        int resampled2 = distribution.sample();
        int resampled3 = distribution.sample();
        assertEquals(sample1, resampled1);
        assertEquals(sample2, resampled2);
        assertEquals(sample3, resampled3);
    }
}
