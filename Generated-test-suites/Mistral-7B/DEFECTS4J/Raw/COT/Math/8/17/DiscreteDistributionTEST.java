import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testConstructorWithValidArguments() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, -0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException e) {
            Assert.assertEquals(-0.4, e.getValue());
            throw e;
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithZeroProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.0));
        samples.add(new Pair<>(3, 0.3));

        try {
            new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (MathArithmeticException e) {
            Assert.assertEquals("Probabilities do not sum to 1", e.getMessage());
            throw e;
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithInfiniteProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(3, 0.3));

        try {
            new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (MathIllegalArgumentException e) {
            Assert.assertEquals("Probability cannot be infinite", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testProbabilityMethodWithValidArgument() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);

        Assert.assertEquals(0.3, distribution.probability((Integer) 1), 0.001);
    }

    @Test
    public void testProbabilityMethodWithNullArgument() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);

        Assert.assertEquals(0.3, distribution.probability((Integer) null), 0.001);
    }

    @Test
    public void testGetSamplesMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);

        List<Pair<Integer, Double>> result = distribution.getSamples();

        Assert.assertEquals(samples.size(), result.size());

        for (int i = 0; i < samples.size(); i++) {
            Assert.assertEquals(samples.get(i).getKey(), result.get(i).getKey());
            Assert.assertEquals(samples.get(i).getValue(), result.get(i).getValue(), 0.001);
        }
    }

    @Test
    public void testSampleMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);

        Integer result = distribution.sample();

        for (int i = 0; i < samples.size(); i++) {
            if (samples.get(i).getValue() > 0.5) {
                Assert.assertEquals(samples.get(i).getKey(), result);
                break;
            }
        }
    }

    @Test
    public void testSampleMethodWithMultipleSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);

        Integer[] results = distribution.sample(10);

        for (Integer result : results) {
            for (int i = 0; i < samples.size(); i++) {
                if (samples.get(i).getValue() > 0.5) {
                    Assert.assertEquals(samples.get(i).getKey(), result);
                    break;
                }
            }
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleMethodWithNegativeSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);

        try {
            distribution.sample(-1);
        } catch (NotStrictlyPositiveException e) {
            Assert.assertEquals("Number of samples must be positive", e.getMessage());
            throw e;
        }
    }
}