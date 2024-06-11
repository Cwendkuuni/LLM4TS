import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
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
            DiscreteDistribution distribution = new DiscreteDistribution<>(samples);
            Assert.assertNotNull(distribution);
        } catch (MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            Assert.assertEquals("Probability for value 1 is negative: -0.3", e.getMessage());
            throw e;
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionZeroProbabilitySum() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, -0.1));

        try {
            DiscreteDistribution distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            if (e instanceof NotStrictlyPositiveException) {
                Assert.fail("Unexpected exception: " + e.getMessage());
            }
            throw e;
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            Assert.assertEquals("Probability for value 1 is infinite", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        double probability = distribution.probability(1);
        Assert.assertEquals(0.3, probability, 0.001);
        probability = distribution.probability(2);
        Assert.assertEquals(0.4, probability, 0.001);
        probability = distribution.probability(3);
        Assert.assertEquals(0.3, probability, 0.001);
        probability = distribution.probability(4);
        Assert.assertEquals(0.0, probability, 0.001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        List<Pair<Integer, Double>> result = distribution.getSamples();
        Assert.assertEquals(samples.size(), result.size());
        for (int i = 0; i < samples.size(); i++) {
            Assert.assertEquals(samples.get(i).getKey(), result.get(i).getKey());
            Assert.assertEquals(samples.get(i).getValue(), result.get(i).getValue(), 0.001);
        }
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        int sample = distribution.sample();
        Assert.assertTrue(samples.contains(new Pair<>(sample, null)));
    }

    @Test
    public void testSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        int[] samplesArray = distribution.sample(5);
        for (int i = 0; i < samplesArray.length; i++) {
            Assert.assertTrue(samples.contains(new Pair<>(samplesArray[i], null)));
        }
    }

    @Test
    public void testSampleReseeding() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        long seed = 12345;
        distribution.reseedRandomGenerator(seed);
        int sample = distribution.sample();

        Assert.assertFalse(samples.contains(new Pair<>(sample, null)));
    }
}