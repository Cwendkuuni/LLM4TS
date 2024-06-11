import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testConstructionWithValidArguments() throws NotPositiveException, NotStrictlyPositiveException, MathArithmeticException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Assert.assertEquals(3, distribution.singletons.size());
        Assert.assertArrayEquals(new Integer[]{1, 2, 3}, distribution.singletons.toArray(new Integer[3]));
        Assert.assertArrayEquals(new double[]{0.1, 0.3, 0.6}, distribution.probabilities, 0.001);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructionWithNegativeProbability() throws NotPositiveException, NotStrictlyPositiveException, MathArithmeticException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructionWithZeroProbabilities() throws NotPositiveException, NotStrictlyPositiveException, MathArithmeticException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.0));
        samples.add(new Pair<>(3, 0.6));

        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructionWithInfiniteProbability() throws NotPositiveException, NotStrictlyPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Assert.assertEquals(0.1, distribution.probability((Integer)1), 0.001);
        Assert.assertEquals(0.3, distribution.probability((Integer)2), 0.001);
        Assert.assertEquals(0.6, distribution.probability((Integer)3), 0.001);
        Assert.assertEquals(0.0, distribution.probability((Integer)4), 0.001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        List<Pair<Integer, Double>> result = distribution.getSamples();

        Assert.assertEquals(3, result.size());
        Assert.assertArrayEquals(new Pair[] {new Pair<>(1, 0.1), new Pair<>(2, 0.3), new Pair<>(3, 0.6)}, result.toArray());
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        int sample = distribution.sample().intValue();

        Assert.assertTrue(samples.stream().mapToInt(p -> p.getKey().intValue()).anyMatch(i -> i == sample));
    }

    @Test
    public void testSampleArray() throws NotStrictlyPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Integer[] samplesArray = distribution.sample(5);

        Assert.assertArrayEquals(new Integer[] {1, 1, 2, 3, 3}, samplesArray);
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        int seed = 123456789;
        distribution.reseedRandomGenerator(seed);

        int sample1 = distribution.sample().intValue();
        int sample2 = distribution.sample().intValue();

        Assert.assertNotEquals(sample1, sample2);
    }

}