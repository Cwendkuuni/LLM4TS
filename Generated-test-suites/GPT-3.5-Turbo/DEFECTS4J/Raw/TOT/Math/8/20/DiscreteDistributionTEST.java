import org.junit.Assert;
import org.junit.Test;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testProbability_NullArgument() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.5));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.2));

        DiscreteDistribution<String> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Exception thrown when initializing DiscreteDistribution.");
        }

        double result = distribution.probability(null);

        Assert.assertEquals(0.0, result, 1e-15);
    }

    @Test
    public void testProbability_ValidArgument() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.5));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.2));

        DiscreteDistribution<String> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Exception thrown when initializing DiscreteDistribution.");
        }

        double result = distribution.probability("A");

        Assert.assertEquals(0.5, result, 1e-15);
    }

    @Test
    public void testGetSamples() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.5));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.2));

        DiscreteDistribution<String> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Exception thrown when initializing DiscreteDistribution.");
        }

        List<Pair<String, Double>> result = distribution.getSamples();

        Assert.assertEquals(samples, result);
    }

    @Test
    public void testSample() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.5));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.2));

        DiscreteDistribution<String> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Exception thrown when initializing DiscreteDistribution.");
        }

        RandomGenerator random = new Well19937c();
        distribution.reseedRandomGenerator(12345L);

        String result = distribution.sample();

        Assert.assertNotNull(result);
        Assert.assertTrue(samples.stream().anyMatch(sample -> sample.getKey().equals(result)));
    }

    @Test
    public void testSample_InvalidSampleSize() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.5));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.2));

        DiscreteDistribution<String> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Exception thrown when initializing DiscreteDistribution.");
        }

        RandomGenerator random = new Well19937c();
        distribution.reseedRandomGenerator(12345L);

        try {
            String[] result = distribution.sample(-1);
            Assert.fail("NotStrictlyPositiveException expected but not thrown.");
        } catch (NotStrictlyPositiveException e) {
            // Exception expected and caught
        }
    }

    @Test
    public void testSample_ValidSampleSize() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.5));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.2));

        DiscreteDistribution<String> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Exception thrown when initializing DiscreteDistribution.");
        }

        RandomGenerator random = new Well19937c();
        distribution.reseedRandomGenerator(12345L);

        int sampleSize = 3;
        String[] result = new String[0];
        try {
            result = distribution.sample(sampleSize);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("NotStrictlyPositiveException thrown when sample size is valid.");
        }

        Assert.assertEquals(sampleSize, result.length);
        for (String sample : result) {
            Assert.assertTrue(samples.stream().anyMatch(pair -> pair.getKey().equals(sample)));
        }
    }

}