import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        // Test probability for each value
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(0, 0.2),
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.5)
        ));

        assertEquals(0.2, distribution.probability(0), 0);
        assertEquals(0.3, distribution.probability(1), 0);
        assertEquals(0.5, distribution.probability(2), 0);
    }

    @Test
    public void testGetSamples() {
        // Test retrieving the sample definition
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(0, 0.2),
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.5)
        ));

        List<Pair<Integer, Double>> samples = distribution.getSamples();
        assertEquals(3, samples.size());
        assertEquals(new Pair<>(0, 0.2), samples.get(0));
        assertEquals(new Pair<>(1, 0.3), samples.get(1));
        assertEquals(new Pair<>(2, 0.5), samples.get(2));
    }

    @Test
    public void testSample() {
        // Test generating a single random sample
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(0, 0.2),
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.5)
        ));

        // Repeat sampling multiple times to ensure randomness
        for (int i = 0; i < 100; i++) {
            Integer sample = distribution.sample();
            assertTrue(sample == 0 || sample == 1 || sample == 2);
        }
    }

    @Test
    public void testSampleSize() {
        // Test generating multiple random samples
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(0, 0.2),
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.5)
        ));

        int sampleSize = 10;
        Integer[] samples = distribution.sample(sampleSize);

        // Check correct size of samples array
        assertEquals(sampleSize, samples.length);

        // Check each sample is within the valid range
        for (Integer sample : samples) {
            assertTrue(sample == 0 || sample == 1 || sample == 2);
        }
    }
}