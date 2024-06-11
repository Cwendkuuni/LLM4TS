import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Pair;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistribution() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

            RandomGenerator rng = distribution.random;
            rng.reseedRandomGenerator(123);

            // Test getSamples()
            List<Pair<Integer, Double>> samplesFromDistribution = distribution.getSamples();
            Assert.assertEquals(samples, samplesFromDistribution);

            // Test probability()
            Assert.assertEquals(0.3, distribution.probability(1), 0.001);
            Assert.assertEquals(0.4, distribution.probability(2), 0.001);
            Assert.assertEquals(0.3, distribution.probability(3), 0.001);
            Assert.assertEquals(0.0, distribution.probability(4), 0.001);

            // Test sample()
            Integer value = distribution.sample();
            Assert.assertEquals(1, value.intValue());
            Assert.assertEquals(0.3, distribution.probability(value), 0.001);

            value = distribution.sample();
            Assert.assertEquals(2, value.intValue());
            Assert.assertEquals(0.4, distribution.probability(value), 0.001);

            value = distribution.sample();
            Assert.assertEquals(3, value.intValue());
            Assert.assertEquals(0.3, distribution.probability(value), 0.001);

            // Test sample(10)
            Integer[] sampledValues = distribution.sample(10);
            for (Integer sampledValue : sampledValues) {
                Assert.assertNotNull(sampledValue);
                Assert.assertEquals(true, distribution.probability(sampledValue) > 0.0);
            }
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSampleSize() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), new ArrayList<>());
        distribution.sample(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSingleton() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(null, 0.3));
        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (IllegalArgumentException e) {
            Assert.fail("Expected IllegalArgumentException, but got: " + e.getMessage());
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (NotPositiveException e) {
            Assert.fail("Expected NotPositiveException, but got: " + e.getMessage());
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testZeroProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.0));
        samples.add(new Pair<>(2, 0.0));
        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (MathArithmeticException e) {
            Assert.fail("Expected MathArithmeticException, but got: " + e.getMessage());
        }
    }
}