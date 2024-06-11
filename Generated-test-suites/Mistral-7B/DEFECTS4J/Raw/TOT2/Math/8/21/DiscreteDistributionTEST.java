import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test case for DiscreteDistribution class.
 */
public class DiscreteDistributionTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testValidConstructor() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3));

        RandomGenerator rng = new Well19937c();
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(rng, samples);
        } catch (NotPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // basic checks
        Assert.assertNotNull(distribution);
        Assert.assertEquals(rng, distribution.random);
        Assert.assertNotNull(distribution.singletons);
        Assert.assertEquals(samples.size(), distribution.singletons.size());

        for (int i = 0; i < samples.size(); i++) {
            Assert.assertEquals(samples.get(i).getKey(), distribution.singletons.get(i));
            Assert.assertEquals(samples.get(i).getValue(), distribution.probabilities[i], 0.001);
        }
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidConstructorNegativeProbability() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, -0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3));

        RandomGenerator rng = new Well19937c();
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(rng, samples);
        } catch (NotPositiveException e) {
            // expected exception
        }

        Assert.assertNull(distribution);
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = MathArithmeticException.class)
    public void testInvalidConstructorZeroProbabilities() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.0),
                new Pair<>(3, 0.3));

        RandomGenerator rng = new Well19937c();
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(rng, samples);
        } catch (NotPositiveException | MathArithmeticException e) {
            // expected exception
        }

        Assert.assertNull(distribution);
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidConstructorInfiniteProbability() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, Double.POSITIVE_INFINITY),
                new Pair<>(3, 0.3));

        RandomGenerator rng = new Well19937c();
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(rng, samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalargumentException e) {
            // expected exception
        }

        Assert.assertNull(distribution);
    }

    /**
     * Test probability method with valid arguments.
     */
    @Test
    public void testValidProbability() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3));

        RandomGenerator rng = new Well19937c();
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(rng, samples);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // basic checks
        Assert.assertNotNull(distribution);

        // test probability with valid arguments
        Integer testValue = 2;
        double testProbability = distribution.probability(testValue);
        Assert.assertEquals(testProbability, 0.4, 0.001);

        testValue = null;
        testProbability = distribution.probability(testValue);
        Assert.assertEquals(testProbability, 0.0, 0.001);
    }

    /**
     * Test probability method with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidProbabilityNullValue() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3));

        RandomGenerator rng = new Well19937c();
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(rng, samples);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // test probability with invalid arguments
        Integer testValue = null;
        distribution.probability(testValue);
    }

    /**
     * Test sample method with valid arguments.
     */
    @Test
    public void testValidSample() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3));

        RandomGenerator rng = new Well19937c();
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(rng, samples);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // basic checks
        Assert.assertNotNull(distribution);

        // test sample method with valid arguments
        Integer testValue = null;
        distribution.sample();
        testValue = distribution.sample();
        Assert.assertNotNull(testValue);

        // test sample method with valid arguments (sample size > 1)
        Integer[] testValues = distribution.sample(10);
        Assert.assertArrayNotNull(testValues);
        for (Integer value : testValues) {
            Assert.assertNotNull(value);
        }
    }

    /**
     * Test sample method with invalid arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidSampleNegativeSampleSize() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3));

        RandomGenerator rng = new Well19937c();
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(rng, samples);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // test sample method with invalid arguments
        distribution.sample(-1);
    }

    /**
     * Test reseedRandomGenerator method with valid arguments.
     */
    @Test
    public void testValidReseedRandomGenerator() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3));

        RandomGenerator rng = new Well19937c();
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(rng, samples);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // test reseedRandomGenerator method with valid arguments
        long seed = 42;
        distribution.reseedRandomGenerator(seed);
        // no assertions here, as it's not trivial to check if the RNG has been seeded correctly
    }

    /**
     * Test getSamples method with valid arguments.
     */
    @Test
    public void testValidGetSamples() {
        final List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3));

        RandomGenerator rng = new Well19937c();
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(rng, samples);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // test getSamples method with valid arguments
        List<Pair<Integer, Double>> testSamples = distribution.getSamples();
        Assert.assertNotNull(testSamples);
        Assert.assertEquals(samples.size(), testSamples.size());

        for (int i = 0; i < samples.size(); i++) {
            Assert.assertEquals(samples.get(i).getKey(), testSamples.get(i).getKey());
            Assert.assertEquals(samples.get(i).getValue(), testSamples.get(i).getValue(), 0.001);
        }
    }
}