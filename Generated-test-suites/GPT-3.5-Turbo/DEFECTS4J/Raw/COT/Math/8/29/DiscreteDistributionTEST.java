
import static org.junit.Assert.*;
import org.junit.Test;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        // Test the probability method
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.3));
        samples.add(new Pair<Integer, Double>(2, 0.6));
        samples.add(new Pair<Integer, Double>(3, 0.1));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        // Test for some specific values
        double p1 = distribution.probability(1);
        assertEquals(0.3, p1, 0.0001);
        double p2 = distribution.probability(2);
        assertEquals(0.6, p2, 0.0001);
        double p3 = distribution.probability(3);
        assertEquals(0.1, p3, 0.0001);
    }

    @Test
    public void testGetSamples() {
        // Test the getSamples method
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.3));
        samples.add(new Pair<Integer, Double>(2, 0.6));
        samples.add(new Pair<Integer, Double>(3, 0.1));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        List<Pair<Integer, Double>> returnedSamples = distribution.getSamples();
        assertEquals(samples.size(), returnedSamples.size());
        for (int i = 0; i < samples.size(); i++) {
            assertEquals(samples.get(i), returnedSamples.get(i));
        }
    }

    @Test
    public void testSample() {
        // Test the sample method
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.3));
        samples.add(new Pair<Integer, Double>(2, 0.6));
        samples.add(new Pair<Integer, Double>(3, 0.1));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        // Test for a large number of samples
        int sampleSize = 1000;
        Integer[] sample = distribution.sample(sampleSize);
        assertTrue(sample.length == sampleSize);

        // Check if the values generated are within the correct range
        for (int i = 0; i < sampleSize; i++) {
            assertTrue(samples.stream().anyMatch(p -> p.getKey().equals(sample[i])));
        }
    }

    @Test
    public void testSampleWithNegativeSampleSize() {
        // Test the sample method with a negative sample size
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.3));
        samples.add(new Pair<Integer, Double>(2, 0.6));
        samples.add(new Pair<Integer, Double>(3, 0.1));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        try {
            distribution.sample(-10);
            fail("The sample method should throw a NotStrictlyPositiveException when sample size is negative");
        } catch (NotStrictlyPositiveException e) {
            // Successfully caught the exception
        }
    }

    @Test
    public void testSampleWithZeroSampleSize() {
        // Test the sample method with a zero sample size
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.3));
        samples.add(new Pair<Integer, Double>(2, 0.6));
        samples.add(new Pair<Integer, Double>(3, 0.1));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        try {
            distribution.sample(0);
            fail("The sample method should throw a NotStrictlyPositiveException when sample size is zero");
        } catch (NotStrictlyPositiveException e) {
            // Successfully caught the exception
        }
    }

}
