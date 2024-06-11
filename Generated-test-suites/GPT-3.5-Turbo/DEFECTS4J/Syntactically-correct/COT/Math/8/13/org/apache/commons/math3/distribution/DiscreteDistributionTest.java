
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        // Create a list of pairs for testing
        List<Pair<String, Double>> samples = Arrays.asList(
                new Pair<>("A", 0.2),
                new Pair<>("B", 0.3),
                new Pair<>("C", 0.5)
        );

        // Create a DiscreteDistribution object for testing
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        // Test the probability method
        Assert.assertEquals(0.2, distribution.probability("A"), 0.0001);
        Assert.assertEquals(0.3, distribution.probability("B"), 0.0001);
        Assert.assertEquals(0.5, distribution.probability("C"), 0.0001);
        Assert.assertEquals(0.0, distribution.probability("D"), 0.0001);
    }

    @Test
    public void testGetSamples() {
        // Create a list of pairs for testing
        List<Pair<String, Double>> samples = Arrays.asList(
                new Pair<>("A", 0.2),
                new Pair<>("B", 0.3),
                new Pair<>("C", 0.5)
        );

        // Create a DiscreteDistribution object for testing
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        // Test the getSamples method
        List<Pair<String, Double>> actualSamples = distribution.getSamples();
        Assert.assertEquals(samples, actualSamples);
    }

    @Test
    public void testSample() {
        // Create a list of pairs for testing
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        );

        // Create a DiscreteDistribution object for testing
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        // Test the sample method
        Integer sample1 = distribution.sample();
        Integer sample2 = distribution.sample();
        Integer sample3 = distribution.sample();

        Assert.assertNotNull(sample1);
        Assert.assertNotNull(sample2);
        Assert.assertNotNull(sample3);
        Assert.assertTrue(samples.stream().anyMatch(pair -> pair.getKey().equals(sample1)));
        Assert.assertTrue(samples.stream().anyMatch(pair -> pair.getKey().equals(sample2)));
        Assert.assertTrue(samples.stream().anyMatch(pair -> pair.getKey().equals(sample3)));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithInvalidSampleSize() throws NotStrictlyPositiveException {
        // Create a list of pairs for testing
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        );

        // Create a DiscreteDistribution object for testing
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        // Test the sample method with an invalid sample size
        distribution.sample(0);
    }

    @Test
    public void testSampleWithValidSampleSize() throws NotStrictlyPositiveException {
        // Create a list of pairs for testing
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        );

        // Create a DiscreteDistribution object for testing
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        // Test the sample method with a valid sample size
        Integer[] sample = distribution.sample(10);

        Assert.assertEquals(10, sample.length);
        for (Integer value : sample) {
            Assert.assertTrue(samples.stream().anyMatch(pair -> pair.getKey().equals(value)));
        }
    }

}
