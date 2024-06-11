import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstructorWithValidArguments() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // Should not reach here
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.assertNotNull(distribution);
        Assert.assertEquals(3, distribution.singletons.size());
        Assert.assertArrayEquals(new Integer[]{1, 2, 3}, distribution.singletons.toArray(new Integer[0]));
        Assert.assertArrayEquals(new double[]{0.2, 0.3, 0.5}, distribution.probabilities, 0.001);
    }

    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionConstructorWithNegativeProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, -0.3),
                new Pair<>(3, 0.5));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (NotPositiveException e) {
            // Expected exception
            Assert.assertEquals("-0.3", e.getMessage());
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionConstructorWithZeroProbabilities() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.0),
                new Pair<>(3, 0.5));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (NotPositiveException e) {
            // Should not reach here
            Assert.fail("Unexpected exception: " + e.getMessage());
        } catch (MathArithmeticException e) {
            // Expected exception
            Assert.assertEquals("Probabilities do not sum to 1.0", e.getMessage());
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionConstructorWithInfiniteProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, Double.POSITIVE_INFINITY),
                new Pair<>(3, 0.5));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (NotPositiveException e) {
            // Should not reach here
            Assert.fail("Unexpected exception: " + e.getMessage());
        } catch (MathIllegalArgumentException e) {
            // Expected exception
            Assert.assertEquals("Probability is infinite", e.getMessage());
        }
    }

    @Test
    public void testProbabilityMethod() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // Should not reach here
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        for (int i = 0; i < distribution.singletons.size(); i++) {
            double probability = distribution.probability(distribution.singletons.get(i));
            Assert.assertEquals(distribution.probabilities[i], probability, 0.001);
        }

        // Test with null input
        Assert.assertEquals(0.0, distribution.probability(null), 0.001);

        // Test with value not in the distribution
        Assert.assertEquals(0.0, distribution.probability(new Integer(4)), 0.001);
    }

    @Test
    public void testGetSamplesMethod() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // Should not reach here
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        List<Pair<Integer, Double>> result = distribution.getSamples();
        Assert.assertEquals(distribution.singletons.size(), result.size());
        Assert.assertEquals(distribution.singletons, result);
    }

    @Test
    public void testSampleMethod() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // Should not reach here
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Integer sample = distribution.sample();
        for (int i = 0; i < distribution.singletons.size(); i++) {
            Assert.assertEquals(distribution.singletons.get(i), sample);
            sample = distribution.sample();
        }
    }

    @Test
    public void testSampleMethodWithBigSample() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // Should not reach here
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        T[] sample = distribution.sample(1000);
        Assert.assertArrayEquals(Arrays.asList(distribution.singletons.toArray(new Integer[0])).subList(0, 1000), Arrays.asList(sample));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleMethodWithNegativeSampleSize() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // Should not reach here
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        try {
            distribution.sample(-1);
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
            Assert.assertEquals("-1", e.getMessage());
        }
    }

    @Test
    public void testReseedRandomGeneratorMethod() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // Should not reach here
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        long seed = 12345;
        distribution.reseedRandomGenerator(seed);
        Integer sample = distribution.sample();
        sample = distribution.sample();
        sample = distribution.sample();

        long originalSeed = distribution.random.getSeed();
        Assert.assertNotEquals(originalSeed, seed);
        distribution.reseedRandomGenerator(originalSeed);
        Assert.assertEquals(sample, distribution.sample());
        Assert.assertEquals(sample, distribution.sample());
        Assert.assertEquals(sample, distribution.sample());
    }
}