import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionCreation() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
            Assert.assertArrayEquals(new Integer[]{1, 2, 3}, dist.singletons.toArray());
            Assert.assertArrayEquals(new double[]{0.3, 0.4, 0.3}, dist.probabilities, 0.001);
        } catch (MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testProbabilityCalculation() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
            Assert.assertEquals(0.3, dist.probability(1), 0.001);
            Assert.assertEquals(0.4, dist.probability(2), 0.001);
            Assert.assertEquals(0.3, dist.probability(3), 0.001);
        } catch (MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
            RandomGenerator rng = new Well19937c();
            dist.reseedRandomGenerator(123456789);

            int sample = dist.sample().intValue();
            Assert.assertEquals(1, sample);
            Assert.assertEquals(0.3, dist.probability(sample), 0.001);

            sample = dist.sample().intValue();
            Assert.assertEquals(2, sample);
            Assert.assertEquals(0.4, dist.probability(sample), 0.001);

            sample = dist.sample().intValue();
            Assert.assertEquals(3, sample);
            Assert.assertEquals(0.3, dist.probability(sample), 0.001);
        } catch (MathIllegalArgumentException | NotPositiveException e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSampleArray() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
            RandomGenerator rng = new Well19937c();

            int[] samplesArray = dist.sample(10).toArray();
            for (int i = 0; i < samplesArray.length; i++) {
                Assert.assertEquals(1, samplesArray[i]);
                Assert.assertEquals(0.3, dist.probability(samplesArray[i]), 0.001);
            }
        } catch (MathIllegalArgumentException | NotPositiveException e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = RuntimeException.class)
    public void testSampleNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        } catch (MathIllegalArgumentException e) {
            Assert.assertEquals("Probabilities must be non-negative.", e.getMessage());
            throw new RuntimeException("Expected NegativeException", e);
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testZeroProbabilitySum() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.1));
        samples.add(new Pair<>(3, 0.0));

        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        } catch (MathArithmeticException e) {
            Assert.assertEquals("Probabilities must sum to 1.", e.getMessage());
            throw new RuntimeException("Expected ArithmeticException", e);
        }
    }
}