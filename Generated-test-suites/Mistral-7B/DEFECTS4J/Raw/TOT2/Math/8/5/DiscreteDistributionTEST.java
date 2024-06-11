import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testCreateDiscreteDistribution() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);
            Assert.assertNotNull(discreteDistribution);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testProbabilityZeroOrNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.1));
        try {
            DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException e) {
            // Expected
        }

        samples.set(0, new Pair<>(1, 0));
        try {
            DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException e) {
            // Expected
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testProbabilitiesSumToZero() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.1));
        samples.add(new Pair<>(3, 0.0));

        try {
            DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testProbabilityInfinite() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));

        try {
            DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testProbability() {
        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        ));

        Assert.assertEquals(0.3, discreteDistribution.probability((Integer) 1, null), 0.001);
        Assert.assertEquals(0.4, discreteDistribution.probability((Integer) 2, null), 0.001);
        Assert.assertEquals(0.3, discreteDistribution.probability((Integer) 3, null), 0.001);

        Assert.assertEquals(0.3, discreteDistribution.probability((Integer) 1), 0.001);
        Assert.assertEquals(0.4, discreteDistribution.probability((Integer) 2), 0.001);
        Assert.assertEquals(0.3, discreteDistribution.probability((Integer) 3), 0.001);
    }

    @Test
    public void testGetSamples() {
        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        ));

        List<Pair<Integer, Double>> samples = discreteDistribution.getSamples();

        Assert.assertEquals(3, samples.size());
        Assert.assertEquals(1, samples.get(0).getKey().intValue());
        Assert.assertEquals(0.3, samples.get(0).getValue(), 0.001);
        Assert.assertEquals(2, samples.get(1).getKey().intValue());
        Assert.assertEquals(0.4, samples.get(1).getValue(), 0.001);
        Assert.assertEquals(3, samples.get(2).getKey().intValue());
        Assert.assertEquals(0.3, samples.get(2).getValue(), 0.001);
    }

    @Test
    public void testSample() {
        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        ));

        int sample;
        for (int i = 0; i < 100; i++) {
            sample = discreteDistribution.sample();
            Assert.assertEquals(3, discreteDistribution.probability(sample), 0.001);
        }
    }

    @Test
    public void testSampleArray() {
        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        ));

        try {
            discreteDistribution.sample(-1);
            Assert.fail("Sample with negative size should throw exception");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        int[] samples = discreteDistribution.sample(10);

        for (int i = 0; i < samples.length; i++) {
            Assert.assertEquals(3, discreteDistribution.probability(samples[i]), 0.001);
        }

        Arrays.sort(samples);

        for (int i = 1; i < samples.length; i++) {
            Assert.assertTrue(samples[i] > samples[i - 1]);
        }
    }

    @Test
    public void testReseedRandomGenerator() {
        DiscreteDistribution<Integer> discreteDistribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        ));

        discreteDistribution.reseedRandomGenerator(1234L);

        int sample;
        for (int i = 0; i < 100; i++) {
            sample = discreteDistribution.sample();
            // This will not guarantee that these specific values are sampled,
            // but it should ensure that the distribution is not affected.
            Assert.assertEquals(3, discreteDistribution.probability(sample), 0.001);
        }
    }
}