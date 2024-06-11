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
    public void testConstructorWithList() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Assert.assertArrayEquals(new Integer[]{1, 2, 3}, distribution.singletons.toArray(new Integer[0]));
        Assert.assertArrayEquals(new double[]{0.3, 0.4, 0.3}, distribution.probabilities, 0.001);
    }

    @Test
    public void testConstructorWithRandomGenerator() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        RandomGenerator rng = new Well19937c();

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);

        Assert.assertArrayEquals(new Integer[]{1, 2, 3}, distribution.singletons.toArray(new Integer[0]));
        Assert.assertArrayEquals(new double[]{0.3, 0.4, 0.3}, distribution.probabilities, 0.001);
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Assert.assertEquals(0.3, distribution.probability((Integer) 1), 0.001);
        Assert.assertEquals(0.4, distribution.probability((Integer) 2), 0.001);
        Assert.assertEquals(0.3, distribution.probability((Integer) 3), 0.001);
        Assert.assertEquals(0, distribution.probability((Integer) 4), 0.001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        List<Pair<Integer, Double>> expectedSamples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );

        Assert.assertEquals(expectedSamples, distribution.getSamples());
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Assert.assertEquals((Integer) 1, distribution.sample());
        Assert.assertEquals((Integer) 2, distribution.sample());
        Assert.assertEquals((Integer) 3, distribution.sample());
        Assert.assertEquals((Integer) 1, distribution.sample());
    }

    @Test
    public void testSampleArray() throws NotStrictlyPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        int sampleSize = 5;

        Integer[] expectedSample = new Integer[sampleSize];

        for (int i = 0; i < sampleSize; i++) {
            expectedSample[i] = distribution.sample();
        }

        Integer[] sample = distribution.sample(sampleSize);

        Assert.assertArrayEquals(expectedSample, sample);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSize() throws NotStrictlyPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        distribution.sample(-1);
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        RandomGenerator rng = new Well19937c();
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);

        long seed = System.currentTimeMillis();
        distribution.reseedRandomGenerator(seed);

        Assert.assertEquals(seed, rng.getSeed());
    }
}