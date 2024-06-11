import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionCreation() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        samples.add(new Pair<>(4, 0.1));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        Assert.assertEquals(4, distribution.singletons.size());
        Assert.assertEquals(0.2, distribution.probabilities[0], 0.001);
        Assert.assertEquals(0.4, distribution.probabilities[1], 0.001);
        Assert.assertEquals(0.3, distribution.probabilities[2], 0.001);
        Assert.assertEquals(0.1, distribution.probabilities[3], 0.001);
    }

    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionWithNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        samples.add(new Pair<>(4, 0.1));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            Assert.fail();
        } catch (NotPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionWithZeroProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        samples.add(new Pair<>(4, 0.3)); // Adding one more with zero probability

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            Assert.fail();
        } catch (MathArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        samples.add(new Pair<>(4, 0.1));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        Assert.assertEquals(0.2, distribution.probability((Integer) 1), 0.001);
        Assert.assertEquals(0.4, distribution.probability((Integer) 2), 0.001);
        Assert.assertEquals(0.3, distribution.probability((Integer) 3), 0.001);
        Assert.assertEquals(0.1, distribution.probability((Integer) 4), 0.001);
        Assert.assertEquals(0, distribution.probability((Integer) 5), 0.001);
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        samples.add(new Pair<>(4, 0.1));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        int sampledValue;
        for (int i = 0; i < 1000; i++) {
            sampledValue = distribution.sample().intValue();
            Assert.assertEquals(distribution.singletons.size(), sampledValue);
        }
    }

    @Test
    public void testSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        samples.add(new Pair<>(4, 0.1));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        int[] sampledValues = distribution.sample(10);
        Assert.assertArrayEquals(new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, sampledValues);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleSizeNegative() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        samples.add(new Pair<>(4, 0.1));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        try {
            distribution.sample(-1);
            Assert.fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        samples.add(new Pair<>(4, 0.1));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        List<Pair<Integer, Double>> expectedSamples = distribution.getSamples();
        Assert.assertEquals(samples.size(), expectedSamples.size());
        for (int i = 0; i < samples.size(); i++) {
            Assert.assertEquals(samples.get(i).getKey(), expectedSamples.get(i).getKey());
            Assert.assertEquals(samples.get(i).getValue(), expectedSamples.get(i).getValue(), 0.001);
        }
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        samples.add(new Pair<>(4, 0.1));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        long seed = System.nanoTime();
        distribution.reseedRandomGenerator(seed);
        int sampledValue = distribution.sample().intValue();
        Assert.assertNotEquals(distribution.singletons.get(0).intValue(), sampledValue);
    }
}