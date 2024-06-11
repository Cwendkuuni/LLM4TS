import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstructionWithValidArguments() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);

        Assert.assertEquals(3, discreteDistribution.singletons.size());
        Assert.assertEquals(0.2, discreteDistribution.probabilities[0], 0.001);
        Assert.assertEquals(0.3, discreteDistribution.probabilities[1], 0.001);
        Assert.assertEquals(0.5, discreteDistribution.probabilities[2], 0.001);
    }

    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionConstructionWithNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionConstructionWithZeroProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.0));
        samples.add(new Pair<>(4, 0.5));

        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionConstructionWithInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, Double.POSITIVE_INFINITY));

        new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbabilityMethodWithValidArguments() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);

        Assert.assertEquals(0.2, discreteDistribution.probability((Integer) 1), 0.001);
    }

    @Test
    public void testProbabilityMethodWithNullArgument() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);

        Assert.assertEquals(0.0, discreteDistribution.probability(null), 0.001);
    }

    @Test
    public void testProbabilityMethodWithWrongArgumentType() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);

        Assert.assertEquals(0.0, discreteDistribution.probability((Object) (new Object())), 0.001);
    }

    @Test
    public void testGetSamplesMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);

        List<Pair<Integer, Double>> retrievedSamples = discreteDistribution.getSamples();

        Assert.assertEquals(3, retrievedSamples.size());
        Assert.assertArrayEquals(new Integer[]{1, 2, 3}, retrievedSamples.stream().map(Pair::getKey).mapToInt(Integer::intValue).toArray());
    }

    @Test
    public void testSampleMethodWithValidArguments() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);

        Assert.assertEquals(1, discreteDistribution.sample().intValue());
    }

    @Test
    public void testSampleMethodWithWrongArgumentType() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);

        Assert.assertEquals(1, discreteDistribution.sample().intValue());
        Assert.assertNotSame(new Object(), discreteDistribution.sample());
    }

    @Test
    public void testSampleMethodWithNullArgument() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);

        Assert.assertEquals(1, discreteDistribution.sample().intValue());
        Assert.assertNotSame(null, discreteDistribution.sample());
    }

    @Test
    public void testSampleMethodWithSamplingTooManyTimes() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);

        int[] samplesArray = new int[100000];
        for (int i = 0; i < samplesArray.length; i++) {
            samplesArray[i] = discreteDistribution.sample().intValue();
        }

        Assert.assertArrayEquals(Arrays.stream(samplesArray).distinct().toArray(), samples.stream().map(Pair::getKey).mapToInt(Integer::intValue).toArray());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleMethodWithNegativeSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);

        discreteDistribution.sample(-1);
    }

    @Test
    public void testSampleMethodWithValidSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);

        T[] samplesArray = discreteDistribution.sample(5);

        Assert.assertArrayEquals(Arrays.stream(samplesArray).distinct().toArray(), samples.stream().map(Pair::getKey).mapToInt(Integer::intValue).toArray());
    }

    @Test
    public void testReseedRandomGeneratorMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);

        long seed = 123456789;
        discreteDistribution.reseedRandomGenerator(seed);

        int sample = discreteDistribution.sample().intValue();
        Assert.assertNotEquals(1, sample);
    }
}