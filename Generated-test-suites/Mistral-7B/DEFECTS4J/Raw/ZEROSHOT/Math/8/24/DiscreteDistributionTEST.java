import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstructionWithValidArguments() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Assert.assertEquals(3, distribution.singletons.size());
        Assert.assertEquals(1, distribution.singletons.get(0).intValue());
        Assert.assertEquals(2, distribution.singletons.get(1).intValue());
        Assert.assertEquals(3, distribution.singletons.get(2).intValue());

        Assert.assertArrayEquals(new double[]{0.3, 0.4, 0.3}, distribution.probabilities, 0.0);
    }

    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionConstructionWithNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionConstructionWithZeroSumProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.1));
        samples.add(new Pair<>(3, 0.1));
        samples.add(new Pair<>(4, -0.1));

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionConstructionWithInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test
    public void testProbabilityMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Assert.assertEquals(0.3, distribution.probability((Integer) 1), 0.0);
        Assert.assertEquals(0.4, distribution.probability((Integer) 2), 0.0);
        Assert.assertEquals(0.3, distribution.probability((Integer) 3), 0.0);
        Assert.assertEquals(0.0, distribution.probability((Integer) 4), 0.0);
    }

    @Test
    public void testSampleMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(distribution.singletons.get(randomIndex(distribution.probabilities)), distribution.sample());
        }
    }

    @Test
    public void testSampleMethodWithMultipleValues() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Integer[] samplesArray = distribution.sample(5);

        for (int i = 0; i < samplesArray.length; i++) {
            Assert.assertEquals(distribution.singletons.get(randomIndex(distribution.probabilities)), samplesArray[i]);
        }
    }

    @Test
    public void testGetSamplesMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        List<Pair<Integer, Double>> actualSamples = distribution.getSamples();
        List<Pair<Integer, Double>> expectedSamples = samples;

        Assert.assertEquals(expectedSamples, actualSamples);
    }

    private int randomIndex(double[] probabilities) {
        double randomValue = new Well19937c().nextDouble();
        double sum = 0;

        for (int i = 0; i < probabilities.length; i++) {
            sum += probabilities[i];
            if (randomValue < sum) {
                return i;
            }
        }

        return probabilities.length - 1;
    }
}