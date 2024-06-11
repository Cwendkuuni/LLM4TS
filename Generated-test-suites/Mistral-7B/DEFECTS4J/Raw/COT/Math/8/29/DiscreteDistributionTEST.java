import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstructionWithValidArguments() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Assert.assertEquals(3, distribution.singletons.size());
        Assert.assertEquals(3, distribution.probabilities.length);
    }

    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionConstructionWithNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, -0.3));

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionConstructionWithZeroProbabilitiesSum() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, -0.1));

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionConstructionWithInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(Integer.MAX_VALUE, Double.POSITIVE_INFINITY));

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Assert.assertEquals(0.5, distribution.probability(1), 0.01);
        Assert.assertEquals(0.3, distribution.probability(2), 0.01);
        Assert.assertEquals(0.2, distribution.probability(3), 0.01);
        Assert.assertEquals(0, distribution.probability(4), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityWithNullArgument() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        distribution.probability(null);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        List<Pair<Integer, Double>> result = distribution.getSamples();

        Assert.assertEquals(samples.size(), result.size());
        for (int i = 0; i < samples.size(); i++) {
            Assert.assertEquals(samples.get(i).getKey(), result.get(i).getKey());
            Assert.assertEquals(samples.get(i).getValue(), result.get(i).getValue(), 0.01);
        }
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Integer sample = distribution.sample();

        for (Pair<Integer, Double> pair : samples) {
            if (pair.getValue() > 0 && sample.equals(pair.getKey())) {
                Assert.assertEquals(pair.getKey(), sample);
                return;
            }
        }

        Assert.fail("Sample is not in the list of possible samples");
    }

    @Test
    public void testSampleArray() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Integer[] sampleArray = distribution.sample(5);

        for (int i = 0; i < 5; i++) {
            for (Pair<Integer, Double> pair : samples) {
                if (pair.getValue() > 0 && sampleArray[i].equals(pair.getKey())) {
                    Assert.assertEquals(pair.getKey(), sampleArray[i]);
                    break;
                }
            }
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithNegativeSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        distribution.sample(-1);
    }

}