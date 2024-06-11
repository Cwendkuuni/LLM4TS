import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.DiscreteDistributionTest;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testConstructorGivenValidArguments() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        // Basic functionality test
        Assert.assertEquals(0.1, distribution.probability(1), 0.001);
        Assert.assertEquals(0.3, distribution.probability(2), 0.001);
        Assert.assertEquals(0.6, distribution.probability(3), 0.001);

        // Test sample method
        Assert.assertEquals(1, distribution.sample().intValue());
        Assert.assertEquals(2, distribution.sample().intValue());
        Assert.assertEquals(3, distribution.sample().intValue());

        // Test getSamples method
        List<Pair<Integer, Double>> samplesFromDistribution = distribution.getSamples();
        Assert.assertEquals(3, samplesFromDistribution.size());
        Assert.assertEquals(1, samplesFromDistribution.get(0).getKey().intValue());
        Assert.assertEquals(0.1, samplesFromDistribution.get(0).getValue(), 0.001);
        Assert.assertEquals(2, samplesFromDistribution.get(1).getKey().intValue());
        Assert.assertEquals(0.3, samplesFromDistribution.get(1).getValue(), 0.001);
        Assert.assertEquals(3, samplesFromDistribution.get(2).getKey().intValue());
        Assert.assertEquals(0.6, samplesFromDistribution.get(2).getValue(), 0.001);

        // Test sample method with a larger sample size
        T[] sample = distribution.sample(5);
        for (int i = 0; i < sample.length; i++) {
            Assert.assertNotNull(sample[i]);
            Assert.assertTrue(singletonInList(singletons, sample[i]));
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorGivenNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            Assert.fail();
        } catch (NotPositiveException e) {
            // Expected
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorGivenZeroProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.0));
        samples.add(new Pair<>(3, 0.4));
        samples.add(new Pair<>(4, 0.6));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            Assert.fail();
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorGivenInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            Assert.fail();
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.6));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        long initialSeed = distribution.random.getNextLong();

        distribution.reseedRandomGenerator(123456789L);
        Assert.assertNotEquals(initialSeed, distribution.random.getNextLong());
    }

    @Test
    public void testProbabilityWithNullValue() {
        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Assert.assertEquals(0.0, distribution.probability(null), 0.001);
    }

    private boolean singletonInList(List<T> list, T value) {
        for (T singleton : list) {
            if (value != null && !value.equals(singleton)) continue;
            return true;
        }
        return false;
    }
}