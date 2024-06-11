// JUnit 4 test cases for DiscreteDistribution class

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() throws Exception {
        // Define the samples for the distribution
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(0, 0.2));
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.5));

        // Create the distribution
        distribution = new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbability() {
        // Test the probability method for each value in the distribution
        assertEquals(0.2, distribution.probability(0), 0.0001);
        assertEquals(0.3, distribution.probability(1), 0.0001);
        assertEquals(0.5, distribution.probability(2), 0.0001);
    }

    @Test
    public void testGetSamples() {
        // Test the getSamples method
        List<Pair<Integer, Double>> expectedSamples = new ArrayList<>();
        expectedSamples.add(new Pair<>(0, 0.2));
        expectedSamples.add(new Pair<>(1, 0.3));
        expectedSamples.add(new Pair<>(2, 0.5));

        List<Pair<Integer, Double>> actualSamples = distribution.getSamples();

        assertEquals(expectedSamples, actualSamples);
    }

    @Test
    public void testSample() {
        // Test the sample method
        RandomGenerator rng = new MockRandomGenerator(new double[]{0.1, 0.4, 0.7});
        distribution.reseedRandomGenerator(123456789L);
        distribution.random = rng;

        assertEquals(0, (int) distribution.sample());
        assertEquals(1, (int) distribution.sample());
        assertEquals(2, (int) distribution.sample());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSampleNegativeSize() {
        // Test the sample method with a negative sample size
        distribution.sample(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSampleZeroSize() {
        // Test the sample method with a zero sample size
        distribution.sample(0);
    }

    @Test
    public void testSamplePositiveSize() {
        // Test the sample method with a positive sample size
        RandomGenerator rng = new MockRandomGenerator(new double[]{0.1, 0.4, 0.7});
        distribution.reseedRandomGenerator(123456789L);
        distribution.random = rng;

        Integer[] expectedSample = {0, 1, 2};
        Integer[] actualSample = distribution.sample(3);

        assertArrayEquals(expectedSample, actualSample);
    }

    private class MockRandomGenerator implements RandomGenerator {

        private double[] values;
        private int index;

        public MockRandomGenerator(double[] values) {
            this.values = values;
            this.index = 0;
        }

        @Override
        public void setSeed(long seed) {
            // Not implemented
        }

        @Override
        public void setSeed(int[] seed) {
            // Not implemented
        }

        @Override
        public void setSeed(int seed) {
            // Not implemented
        }

        @Override
        public void nextBytes(byte[] bytes) {
            // Not implemented
        }

        @Override
        public int nextInt() {
            // Not implemented
            return 0;
        }

        @Override
        public int nextInt(int n) {
            // Not implemented
            return 0;
        }

        @Override
        public long nextLong() {
            // Not implemented
            return 0;
        }

        @Override
        public boolean nextBoolean() {
            // Not implemented
            return false;
        }

        @Override
        public float nextFloat() {
            // Not implemented
            return 0;
        }

        @Override
        public double nextDouble() {
            double value = values[index];
            index++;
            return value;
        }

        @Override
        public double nextGaussian() {
            // Not implemented
            return 0;
        }
    }
}