import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testConstructorWithValidArguments() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Assert.assertEquals(3, distribution.singletons.size());
        Assert.assertEquals(3, distribution.probabilities.length);
        Assert.assertArrayEquals(new double[]{0.3, 0.4, 0.3}, distribution.probabilities, 0.0);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, -0.4),
                new Pair<>(3, 0.3)
        );

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithSumZeroProbabilities() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.2),
                new Pair<>(3, -0.1),
                new Pair<>(4, 0.1)
        );

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithInfiniteProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, Double.POSITIVE_INFINITY),
                new Pair<>(3, 0.3)
        );

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test
    public void testProbabilityWithValidArguments() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        ));

        Assert.assertEquals(0.3, distribution.probability((Integer) 1), 0.01);
        Assert.assertEquals(0.4, distribution.probability((Integer) 2), 0.01);
        Assert.assertEquals(0.3, distribution.probability((Integer) 3), 0.01);
        Assert.assertEquals(0, distribution.probability((Integer) 4), 0.01);
    }

    @Test
    public void testProbabilityWithNullValue() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        ));

        Assert.assertEquals(0, distribution.probability(null), 0.01);
    }

    @Test
    public void testGetSamples() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        ));

        List<Pair<Integer, Double>> samples = distribution.getSamples();

        Assert.assertEquals(3, samples.size());
        Assert.assertEquals(Collections.singletonList(new Pair<>(1, 0.3)), samples.subList(0, 1));
        Assert.assertEquals(Collections.singletonList(new Pair<>(2, 0.4)), samples.subList(1, 2));
        Assert.assertEquals(Collections.singletonList(new Pair<>(3, 0.3)), samples.subList(2, 3));
    }

    @Test
    public void testSample() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        ));

        int sampledValue = distribution.sample().intValue();

        List<Integer> singletons = distribution.singletons;

        Assert.assertTrue(singletons.contains(sampledValue));
    }

    @Test
    public void testSampleWithMultipleCalls() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        ));

        List<Integer> samples = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            samples.add(distribution.sample().intValue());
        }

        Assert.assertEquals(3, samples.size());
        Assert.assertFalse(samples.contains(null));
        for (Integer sample : samples) {
            List<Integer> singletons = distribution.singletons;
            Assert.assertTrue(singletons.contains(sample));
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithNegativeSampleSize() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        ));

        distribution.sample(-1);
    }

    @Test
    public void testSampleWithValidSampleSize() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        ));

        T[] samples = distribution.sample(3);

        Assert.assertArrayEquals(new Integer[]{1, 1, 2}, Arrays.stream(samples).mapToInt(i -> i.intValue()).toArray());
    }

    @Test
    public void testReseedRandomGenerator() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        ));

        long seed = 1234567890L;
        distribution.reseedRandomGenerator(seed);

        T[] samples = distribution.sample(3);

        Assert.assertArrayEquals(new Integer[]{1, 2, 3}, Arrays.stream(samples).mapToInt(i -> i.intValue()).toArray());
    }

}