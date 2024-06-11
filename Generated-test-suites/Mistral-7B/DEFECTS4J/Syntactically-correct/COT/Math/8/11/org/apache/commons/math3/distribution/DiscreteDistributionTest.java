import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstructorWithValidArguments() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        // Add other assertions here to test the initial state of the object
    }

    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionConstructorWithNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.1));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            new DiscreteDistribution<>(samples);
        } catch (NotPositiveException e) {
            // Expected exception
            Assert.assertEquals(-0.1, e.getValue(), 0.0);
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionConstructorWithZeroProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.0));
        samples.add(new Pair<>(3, 0.1));

        try {
            new DiscreteDistribution<>(samples);
        } catch (MathArithmeticException e) {
            // Expected exception
            Assert.assertEquals("Probabilities do not sum to 1.0", e.getMessage());
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionConstructorWithInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            new DiscreteDistribution<>(samples);
        } catch (MathIllegalArgumentException e) {
            // Expected exception
            Assert.assertEquals("Probability cannot be Infinity", e.getMessage());
        }
    }

    @Test
    public void testProbabilityMethodWithValidArguments() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        int testValue = 1;
        double expectedProbability = 0.3;
        double actualProbability = distribution.probability(testValue);

        Assert.assertEquals(expectedProbability, actualProbability, 0.0);
    }

    @Test
    public void testProbabilityMethodWithNullValue() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        double expectedProbability = 0.0;
        double actualProbability = distribution.probability(null);

        Assert.assertEquals(expectedProbability, actualProbability, 0.0);
    }

    @Test
    public void testProbabilityMethodWithNonMatchingValue() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        int testValue = 4;
        double expectedProbability = 0.0;
        double actualProbability = distribution.probability(testValue);

        Assert.assertEquals(expectedProbability, actualProbability, 0.0);
    }

    @Test
    public void testGetSamplesMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        List<Pair<Integer, Double>> actualSamples = distribution.getSamples();

        List<Pair<Integer, Double>> expectedSamples = samples;

        Assert.assertEquals(expectedSamples.size(), actualSamples.size());

        for (int i = 0; i < expectedSamples.size(); i++) {
            Pair<Integer, Double> expectedSample = expectedSamples.get(i);
            Pair<Integer, Double> actualSample = actualSamples.get(i);

            Assert.assertEquals(expectedSample.getKey(), actualSample.getKey());
            Assert.assertEquals(expectedSample.getValue(), actualSample.getValue(), 0.0);
        }
    }

    @Test
    public void testSampleMethodWithValidArguments() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        int expectedValue = 1;
        int actualValue = ((Integer) distribution.sample()).intValue();

        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testSampleMethodWithMultipleSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        int[] expectedValues = {1, 1, 2, 3, 3};
        Integer[] actualValues = new Integer[5];

        for (int i = 0; i < 5; i++) {
            actualValues[i] = distribution.sample();
        }

        for (int i = 0; i < 5; i++) {
            Assert.assertTrue(containsExpectedValue(expectedValues, actualValues[i].intValue()));
        }
    }

    private boolean containsExpectedValue(int[] expectedValues, int value) {
        for (int expectedValue : expectedValues) {
            if (expectedValue == value) {
                return true;
            }
        }
        return false;
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleMethodWithNegativeSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        int sampleSize = -1;

        try {
            distribution.sample(sampleSize);
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
            Assert.assertEquals(LocalizedFormats.NUMBER_OF_SAMPLES, e.getMessage());
        }
    }
}