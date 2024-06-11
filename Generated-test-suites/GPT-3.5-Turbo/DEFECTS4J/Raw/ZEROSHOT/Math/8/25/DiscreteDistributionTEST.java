import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        // Test the probability method

        // Create a DiscreteDistribution object
        DiscreteDistribution<Integer> dist = createTestDistribution();

        // Test probability for valid values
        double probability1 = dist.probability(1);
        assertEquals("Unexpected probability value", 0.1, probability1, 0.0001);

        double probability2 = dist.probability(2);
        assertEquals("Unexpected probability value", 0.4, probability2, 0.0001);

        double probability3 = dist.probability(3);
        assertEquals("Unexpected probability value", 0.3, probability3, 0.0001);

        double probability4 = dist.probability(4);
        assertEquals("Unexpected probability value", 0.2, probability4, 0.0001);

        // Test probability for invalid values
        double probability5 = dist.probability(0);
        assertEquals("Unexpected probability value", 0.0, probability5, 0.0001);

        double probability6 = dist.probability(5);
        assertEquals("Unexpected probability value", 0.0, probability6, 0.0001);
    }

    @Test
    public void testGetSamples() {
        // Test the getSamples method

        // Create a DiscreteDistribution object
        DiscreteDistribution<Integer> dist = createTestDistribution();

        // Get the samples
        List<Pair<Integer, Double>> samples = dist.getSamples();

        // Check the size of the samples list
        assertEquals("Unexpected samples size", 4, samples.size());

        // Check the values of the samples
        Pair<Integer, Double> sample1 = samples.get(0);
        assertEquals("Unexpected sample value", Integer.valueOf(1), sample1.getKey());
        assertEquals("Unexpected sample probability", 0.1, sample1.getValue(), 0.0001);

        Pair<Integer, Double> sample2 = samples.get(1);
        assertEquals("Unexpected sample value", Integer.valueOf(2), sample2.getKey());
        assertEquals("Unexpected sample probability", 0.4, sample2.getValue(), 0.0001);

        Pair<Integer, Double> sample3 = samples.get(2);
        assertEquals("Unexpected sample value", Integer.valueOf(3), sample3.getKey());
        assertEquals("Unexpected sample probability", 0.3, sample3.getValue(), 0.0001);

        Pair<Integer, Double> sample4 = samples.get(3);
        assertEquals("Unexpected sample value", Integer.valueOf(4), sample4.getKey());
        assertEquals("Unexpected sample probability", 0.2, sample4.getValue(), 0.0001);
    }

    @Test
    public void testSample() {
        // Test the sample method

        // Create a DiscreteDistribution object
        DiscreteDistribution<Integer> dist = createTestDistribution();

        // Generate a random sample
        Integer sample = dist.sample();

        // Check that the sample is a valid value
        assertTrue("Sample value is not valid", sample >= 1 && sample <= 4);
    }

    @Test
    public void testSampleSize() {
        // Test the sample method with sample size

        // Create a DiscreteDistribution object
        DiscreteDistribution<Integer> dist = createTestDistribution();

        // Generate a random sample with sample size
        Integer[] sample = dist.sample(10);

        // Check that the sample size is correct
        assertEquals("Unexpected sample size", 10, sample.length);

        // Check that all the samples are valid values
        for (Integer value : sample) {
            assertTrue("Sample value is not valid", value >= 1 && value <= 4);
        }
    }

    private DiscreteDistribution<Integer> createTestDistribution() {
        // Create a test distribution
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        samples.add(new Pair<>(4, 0.2));

        try {
            return new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            return null;
        }
    }
}