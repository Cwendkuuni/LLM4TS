import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testValidConstructor() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Assert.assertEquals(3, distribution.singletons.size());
        Assert.assertArrayEquals(new Integer[]{1, 2, 3}, distribution.singletons.toArray(new Integer[0]));
        Assert.assertArrayEquals(new Double[]{0.5, 0.3, 0.2}, Arrays.stream(distribution.probabilities).mapToDouble(d -> d).toArray(), 0.001);
    }

    /**
     * Test constructor with negative probabilities.
     */
    @Test(expected = NotPositiveException.class)
    public void testNegativeProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    /**
     * Test constructor with probabilities summing to zero.
     */
    @Test(expected = MathArithmeticException.class)
    public void testSumOfProbabilitiesZero() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, -0.2));

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    /**
     * Test constructor with infinite probabilities.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInfiniteProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    /**
     * Test probability method with valid arguments.
     */
    @Test
    public void testProbabilityValid() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Assert.assertEquals(0.5, distribution.probability(1), 0.001);
        Assert.assertEquals(0.3, distribution.probability(2), 0.001);
        Assert.assertEquals(0.2, distribution.probability(3), 0.001);
        Assert.assertEquals(0.0, distribution.probability(4), 0.001);
    }

    /**
     * Test probability method with null value.
     */
    @Test
    public void testProbabilityNullValue() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Assert.assertEquals(0.0, distribution.probability(null), 0.001);
    }

    /**
     * Test probability method with value not in the list.
     */
    @Test
    public void testProbabilityValueNotInTheList() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Assert.assertEquals(0.0, distribution.probability(4), 0.001);
    }

    /**
     * Test getSamples method.
     */
    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        List<Pair<Integer, Double>> result = distribution.getSamples();

        Assert.assertEquals(3, result.size());
        Assert.assertEquals(samples.get(0).getKey(), result.get(0).getKey());
        Assert.assertEquals(Precision.abs(samples.get(0).getValue(), 0.001), result.get(0).getValue(), 0.001);
        Assert.assertEquals(samples.get(1).getKey(), result.get(1).getKey());
        Assert.assertEquals(Precision.abs(samples.get(1).getValue(), 0.001), result.get(1).getValue(), 0.001);
        Assert.assertEquals(samples.get(2).getKey(), result.get(2).getKey());
        Assert.assertEquals(Precision.abs(samples.get(2).getValue(), 0.001), result.get(2).getValue(), 0.001);
    }

    /**
     * Test sample method with valid arguments.
     */
    @Test
    public void testSampleValid() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Integer sample = distribution.sample();

        Assert.assertTrue(samples.contains(new Pair<>(sample, null)));
    }

    /**
     * Test sample method with single value.
     */
    @Test
    public void testSampleSingleValue() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 1.0));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Integer sample = distribution.sample();

        Assert.assertEquals(1, sample.intValue());
    }

    /**
     * Test sample method with multiple values.
     */
    @Test
    public void testSampleMultipleValues() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Integer[] samplesArray = distribution.sample(5);

        Assert.assertArrayEquals(new Integer[]{1, 1, 2, 2, 3}, samplesArray);
    }

    /**
     * Test sample method with negative sample size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        distribution.sample(-1);
    }
}