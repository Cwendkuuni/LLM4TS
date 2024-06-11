import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {
    private final double DELTA = 1e-9;

    // Create a sample distribution with two possible values
    private final List<Pair<String, Double>> sampleDistribution = Arrays.asList(
            new Pair<>("A", 0.3),
            new Pair<>("B", 0.7)
    );

    @Test
    public void testConstructor() {
        try {
            // Create the distribution object
            DiscreteDistribution<String> distribution = new DiscreteDistribution<>(sampleDistribution);

            // Test that the distribution object is created successfully
            Assert.assertNotNull(distribution);

            // Test that the probabilities are normalized correctly
            double[] expectedProbabilities = {0.3, 0.7};
            Assert.assertArrayEquals(expectedProbabilities, distribution.getProbabilities(), DELTA);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_NegativeProbability() throws NotPositiveException {
        // Add a negative probability to the sample distribution
        List<Pair<String, Double>> invalidDistribution = new ArrayList<>(sampleDistribution);
        invalidDistribution.add(new Pair<>("C", -0.2));

        // Try to create the distribution object with the invalid sample distribution
        new DiscreteDistribution<>(invalidDistribution);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_SumOfProbabilitiesIsZero() throws MathArithmeticException {
        // Create a distribution with all probabilities set to zero
        List<Pair<String, Double>> zeroDistribution = Arrays.asList(
                new Pair<>("A", 0.0),
                new Pair<>("B", 0.0)
        );

        // Try to create the distribution object with the zero distribution
        new DiscreteDistribution<>(zeroDistribution);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructor_InfiniteProbability() throws MathIllegalArgumentException {
        // Set one of the probabilities to infinity
        List<Pair<String, Double>> invalidDistribution = new ArrayList<>(sampleDistribution);
        invalidDistribution.set(0, new Pair<>("A", Double.POSITIVE_INFINITY));

        // Try to create the distribution object with the invalid sample distribution
        new DiscreteDistribution<>(invalidDistribution);
    }

    @Test
    public void testProbability() {
        try {
            // Create the distribution object
            DiscreteDistribution<String> distribution = new DiscreteDistribution<>(sampleDistribution);

            // Test the probability calculation for each value in the distribution
            // Test the probability for A
            Assert.assertEquals(0.3, distribution.probability("A"), DELTA);

            // Test the probability for B
            Assert.assertEquals(0.7, distribution.probability("B"), DELTA);

            // Test a value that is not in the distribution
            Assert.assertEquals(0.0, distribution.probability("C"), DELTA);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetSamples() {
        try {
            // Create the distribution object
            DiscreteDistribution<String> distribution = new DiscreteDistribution<>(sampleDistribution);

            // Get the samples
            List<Pair<String, Double>> samples = distribution.getSamples();

            // Test that the samples are returned correctly
            Assert.assertEquals(sampleDistribution, samples);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testSample() {
        try {
            // Create the distribution object
            DiscreteDistribution<String> distribution = new DiscreteDistribution<>(sampleDistribution);

            // Generate a random sample
            String sample = distribution.sample();

            // Test that the sample is one of the possible values in the distribution
            Assert.assertTrue(sample.equals("A") || sample.equals("B"));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSample_InvalidSampleSize() throws NotStrictlyPositiveException {
        // Create the distribution object
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(sampleDistribution);

        // Try to generate a random sample with an invalid sample size
        distribution.sample(0);
    }

    @Test
    public void testSample_ValidSampleSize() {
        try {
            // Create the distribution object
            DiscreteDistribution<String> distribution = new DiscreteDistribution<>(sampleDistribution);

            // Generate a random sample with a valid sample size
            String[] sample = distribution.sample(5);

            // Test that the sample size is correct
            Assert.assertEquals(5, sample.length);

            // Test that each value in the sample is one of the possible values in the distribution
            for (String value : sample) {
                Assert.assertTrue(value.equals("A") || value.equals("B"));
            }
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}