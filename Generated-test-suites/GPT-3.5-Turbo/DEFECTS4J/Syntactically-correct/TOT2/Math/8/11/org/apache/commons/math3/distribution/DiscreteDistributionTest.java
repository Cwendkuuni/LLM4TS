import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        // Create a sample DiscreteDistribution
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        // Test probability for a specific value
        assertEquals(0.5, distribution.probability(1), 0.0);
        assertEquals(0.3, distribution.probability(2), 0.0);
        assertEquals(0.2, distribution.probability(3), 0.0);
        assertEquals(0.0, distribution.probability(4), 0.0);
    }

    @Test
    public void testGetSamples() {
        // Create a sample DiscreteDistribution
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        // Test the getSamples method
        List<Pair<Integer, Double>> retrievedSamples = distribution.getSamples();
        assertEquals(samples, retrievedSamples);
    }

    @Test
    public void testSample() {
        // Create a sample DiscreteDistribution
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        // Test the sample method
        Integer sampledValue = distribution.sample();
        assertTrue(samples.stream().anyMatch(pair -> pair.getKey().equals(sampledValue)));
    }

    @Test
    public void testSampleWithSize() {
        // Create a sample DiscreteDistribution
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        // Test the sample method with a sample size
        Integer[] sampledValues = distribution.sample(5);
        assertEquals(5, sampledValues.length);
        for (Integer value : sampledValues) {
            assertTrue(samples.stream().anyMatch(pair -> pair.getKey().equals(value)));
        }
    }

}