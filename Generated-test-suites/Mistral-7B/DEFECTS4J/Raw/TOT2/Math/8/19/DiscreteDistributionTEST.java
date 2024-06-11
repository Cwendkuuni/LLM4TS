import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test class for DiscreteDistribution.
 */
public class DiscreteDistributionTest {

    /**
     * Test constructor with empty list.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testEmptyConstructor() {
        new DiscreteDistribution<>(Collections.emptyList());
    }

    /**
     * Test constructor with negative probabilities.
     */
    @Test(expected = NotPositiveException.class)
    public void testNegativeProbabilities() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -1.0));
        new DiscreteDistribution<>(samples);
    }

    /**
     * Test constructor with infinite probabilities.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInfiniteProbabilities() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        new DiscreteDistribution<>(samples);
    }

    /**
     * Test constructor with probabilities summing to zero.
     */
    @Test(expected = MathArithmeticException.class)
    public void testProbabilitiesSumToZero() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.5));
        new DiscreteDistribution<>(samples);
    }

    /**
     * Test constructor with valid samples.
     */
    @Test
    public void testValidConstructor() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.25));

        try {
            new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test probability method with valid arguments.
     */
    @Test
    public void testProbabilityMethod() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.25));

        try {
            final DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
            Assert.assertEquals(0.25, dist.probability((Integer) 1), 0.0);
            Assert.assertEquals(0.5, dist.probability((Integer) 2), 0.0);
            Assert.assertEquals(0.25, dist.probability((Integer) 3), 0.0);
            Assert.assertEquals(0.0, dist.probability((Integer) 4), 0.0);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test probability method with null arguments.
     */
    @Test
    public void testProbabilityMethodNull() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.25));

        try {
            final DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
            Assert.assertEquals(0.25, dist.probability(1), 0.0);
            Assert.assertEquals(0.5, dist.probability(2), 0.0);
            Assert.assertEquals(0.25, dist.probability(3), 0.0);
            Assert.assertNull(dist.probability(null));
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getSamples method with valid arguments.
     */
    @Test
    public void testGetSamplesMethod() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.25));

        try {
            final DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
            final List<Pair<Integer, Double>> samplesFromDist = dist.getSamples();
            Assert.assertEquals(samples, samplesFromDist);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test sample method with valid arguments.
     */
    @Test
    public void testSampleMethod() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.25));

        try {
            final DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
            final Integer sample = dist.sample();
            final Pair<Integer, Double> samplePairFromDist = dist.getSamples().get(0);
            Assert.assertEquals(samplePairFromDist.getKey(), sample);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test sample method with invalid arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleMethodNegativeSize() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.25));

        try {
            final DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
            dist.sample(-1);
        } catch (Exception e) {
            Assert.assertEquals("number of samples must be positive", e.getMessage());
        }
    }

    /**
     * Test sample method with valid arguments, but with no samples matching the given value.
     */
    @Test
    public void testSampleMethodNoMatch() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.25));

        try {
            final DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
            final Integer sample = dist.sample();
            final Integer sampleValue = sample.intValue();
            Assert.assertNotEquals(2, sampleValue);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test reseedRandomGenerator method.
     */
    @Test
    public void testReseedRandomGenerator() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.25));

        final DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        final long seed = System.currentTimeMillis();

        dist.reseedRandomGenerator(seed);

        for (int i = 0; i < 100; i++) {
            final Integer sample = dist.sample();
            // No need to check the result as we cannot predict it, but we can check
            // that the distribution is properly resseeded and gives different samples
            // after reseeding.
            Assert.assertNotEquals(dist.sample(), sample);
        }
    }

    /**
     * Test sample method with custom random generator.
     */
    @Test
    public void testSampleMethodCustomRandomGenerator() {
        final List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.25));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.25));

        final Well19937c rng = new Well19937c();

        try {
            final DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(rng, samples);
            final Integer sample = dist.sample();
            final Pair<Integer, Double> samplePairFromDist = dist.getSamples().get(0);
            Assert.assertEquals(samplePairFromDist.getKey(), sample);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

}