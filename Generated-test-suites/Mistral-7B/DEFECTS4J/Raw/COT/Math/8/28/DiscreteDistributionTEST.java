import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setup() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.4));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.3));

        try {
            distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProbability() {
        for (int i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++) {
            double probability = distribution.probability(i);
            if (probability > 0) {
                Assert.assertEquals(distribution.probability(i), distribution.getSamples().stream()
                        .filter(p -> p.getKey().equals(i)).mapToDouble(p -> p.getValue()).sum(), 0.0001);
            } else {
                Assert.assertEquals(probability, 0.0, 0.0001);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityWithNegativeValue() {
        distribution.probability(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityWithNullValue() {
        distribution.probability(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSampleWithEmptyList() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        DiscreteDistribution<Integer> emptyDistribution = new DiscreteDistribution<>(new Well19937c(), samples);
        emptyDistribution.sample();
    }

    @Test
    public void testSample() {
        List<Integer> sample = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            sample.add(distribution.sample());
        }

        Assert.assertArrayEquals(sample.stream().mapToInt(i -> i).toArray(),
                Arrays.stream(distribution.sample(sample.size())).mapToInt(i -> ((Integer) i).intValue()).toArray());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithNegativeSize() {
        distribution.sample(-1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithZeroSize() {
        distribution.sample(0);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        List<Pair<Integer, Double>> expectedSamples = new ArrayList<>();
        expectedSamples.add(new Pair<>(1, 0.4));
        expectedSamples.add(new Pair<>(2, 0.3));
        expectedSamples.add(new Pair<>(3, 0.3));
        Assert.assertEquals(samples, expectedSamples);
    }

    @Test
    public void testReseedRandomGenerator() {
        long seed = 12345;
        distribution.reseedRandomGenerator(seed);
        // It's not possible to check the result directly, but we can check
        // that the seed has been set correctly
        Assert.assertEquals(distribution.random.getSeed(), seed);
    }
}