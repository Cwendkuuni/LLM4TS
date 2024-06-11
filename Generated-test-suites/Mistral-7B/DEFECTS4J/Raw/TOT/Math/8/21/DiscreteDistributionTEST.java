import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    /**
     * Test constructor with valid inputs.
     */
    @Test
    public void testValidConstructor() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );
        RandomGenerator rng = new Well19937c();

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);

        Assert.assertEquals(3, distribution.singletons.size());
        Assert.assertEquals(3, distribution.probabilities.length);
    }

    /**
     * Test constructor with negative probability.
     */
    @Test(expected = NotPositiveException.class)
    public void testNegativeProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, -0.4),
                new Pair<>(3, 0.3)
        );
        RandomGenerator rng = new Well19937c();

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);
    }

    /**
     * Test constructor with infinite probability.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInfiniteProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, Double.POSITIVE_INFINITY),
                new Pair<>(3, 0.3)
        );
        RandomGenerator rng = new Well19937c();

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);
    }

    /**
     * Test constructor with zero probabilities sum.
     */
    @Test(expected = MathArithmeticException.class)
    public void testZeroProbabilitiesSum() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.2),
                new Pair<>(3, -0.1)
        );
        RandomGenerator rng = new Well19937c();

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);
    }

    /**
     * Test probability method with valid input.
     */
    @Test
    public void testProbabilityValidInput() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );
        RandomGenerator rng = new Well19937c();

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);

        Assert.assertEquals(0.3, distribution.probability((Integer) 1), 0.001);
    }

    /**
     * Test probability method with invalid input.
     */
    @Test
    public void testProbabilityInvalidInput() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );
        RandomGenerator rng = new Well19937c();

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);

        Assert.assertEquals(0.0, distribution.probability((Integer) 4), 0.001);
    }

    /**
     * Test getSamples method.
     */
    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );
        RandomGenerator rng = new Well19937c();

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);

        List<Pair<Integer, Double>> actualSamples = distribution.getSamples();

        Assert.assertEquals(3, actualSamples.size());
        Assert.assertEquals(samples.get(0), actualSamples.get(0));
        Assert.assertEquals(samples.get(1), actualSamples.get(1));
        Assert.assertEquals(samples.get(2), actualSamples.get(2));
    }

    /**
     * Test sample method with valid input.
     */
    @Test
    public void testSampleValidInput() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );
        RandomGenerator rng = new Well19937c();

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);

        int actualSample = distribution.sample().intValue();

        Assert.assertEquals(1, actualSample);
        Assert.assertEquals(0.3, distribution.probability((Integer) actualSample), 0.001);
    }

    /**
     * Test sample method with invalid input.
     */
    @Test
    public void testSampleInvalidInput() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );
        RandomGenerator rng = new Well19937c();

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);

        Assert.assertEquals(3, distribution.singletons.size());

        try {
            distribution.sample();
            Assert.fail();
        } catch (Exception e) {
            // expected
        }
    }

    /**
     * Test sample method with zero sample size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleZeroSampleSize() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );
        RandomGenerator rng = new Well19937c();

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);

        distribution.sample(0);
    }

    /**
     * Test sample method with multiple samples.
     */
    @Test
    public void testSampleMultipleSamples() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );
        RandomGenerator rng = new Well19937c();

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(rng, samples);

        int[] actualSamples = distribution.sample(5).toArray(new Integer[0]);

        Assert.assertArrayEquals(new Integer[]{1, 1, 2, 3, 3}, actualSamples);
    }
}