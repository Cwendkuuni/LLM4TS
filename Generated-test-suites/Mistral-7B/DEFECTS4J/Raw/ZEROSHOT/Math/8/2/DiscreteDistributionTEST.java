import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testValidConstruction() throws NotPositiveException, NotStrictlyPositiveException, MathArithmeticException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Assert.assertNotNull(distribution);
        Assert.assertSame(samples.size(), distribution.singletons.size());
        Assert.assertArrayEquals(samples.toArray(), distribution.getSamples().toArray());
    }

    @Test(expected = NotPositiveException.class)
    public void testInvalidConstructionWithNegativeProbability() throws NotPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), -0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));

        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testInvalidConstructionWithZeroProbabilities() throws NotPositiveException, MathArithmeticException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.3));
        samples.add(new Pair<>(Integer.valueOf(3), 0));

        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidConstructionWithInfiniteProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));

        new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbabilityMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Assert.assertEquals(0.3, distribution.probability(Integer.valueOf(1)), 0.01);
        Assert.assertEquals(0.4, distribution.probability(Integer.valueOf(2)), 0.01);
        Assert.assertEquals(0.3, distribution.probability(Integer.valueOf(3)), 0.01);
    }

    @Test
    public void testSampleMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        Well19937c rng = new Well19937c();
        distribution.reseedRandomGenerator(1L);

        for (int i = 0; i < 100; i++) {
            int sample = distribution.sample();
            Assert.assertTrue(distribution.probability(sample) > 0);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleSizeNegative() throws NotStrictlyPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        distribution.sample(-1);
    }

    @Test
    public void testSampleSizeValid() throws NotStrictlyPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        int[] samplesArray = distribution.sample(5);

        for (int i = 0; i < samplesArray.length; i++) {
            Assert.assertTrue(distribution.probability(samplesArray[i]) > 0);
        }
    }
}