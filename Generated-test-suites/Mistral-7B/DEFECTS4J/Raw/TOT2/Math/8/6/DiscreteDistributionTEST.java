import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testConstructorWithValidSamples() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5)
        );

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Assert.assertEquals(Arrays.asList(1, 2, 3), distribution.singletons);
        Assert.assertArrayEquals(new double[]{0.2, 0.3, 0.5}, distribution.probabilities, 0.0);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, -0.3),
                new Pair<>(3, 0.5)
        );

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithZeroProbabilities() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.0),
                new Pair<>(3, 0.5)
        );

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (NotPositiveException | MathIllegalArgumentException e) {
            throw new AssertionError("Expected MathArithmeticException", e);
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithInfiniteProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, Double.POSITIVE_INFINITY),
                new Pair<>(3, 0.5)
        );

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (NotPositiveException e) {
            throw new AssertionError("Expected MathIllegalArgumentException", e);
        }
    }

    @Test
    public void testProbability() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5)
        ));

        Assert.assertEquals(0.2, distribution.probability((Integer)1), 0.001);
        Assert.assertEquals(0.3, distribution.probability((Integer)2), 0.001);
        Assert.assertEquals(0.5, distribution.probability((Integer)3), 0.001);
        Assert.assertEquals(0.0, distribution.probability((Integer)4), 0.001);
    }

    @Test
    public void testSample() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5)
        ));

        List<Integer> samples = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            samples.add(distribution.sample());
        }

        List<Integer> expectedValues = Arrays.asList(1, 2, 3);
        List<Integer> actualValues = new ArrayList<>(samples);
        Assert.assertEquals(expectedValues, actualValues);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithNegativeSampleSize() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5)
        ));

        distribution.sample(-1);
    }

    @Test
    public void testGetSamples() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5)
        ));

        List<Pair<Integer, Double>> expectedSamples = Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5)
        );

        List<Pair<Integer, Double>> actualSamples = distribution.getSamples();

        Assert.assertEquals(expectedSamples, actualSamples);
    }

    @Test
    public void testReseedRandomGenerator() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(
                new Pair<>(1, 0.2),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5)
        ));

        long seed = System.currentTimeMillis();
        distribution.reseedRandomGenerator(seed);

        Assert.assertEquals(seed, distribution.random.getSeed());
    }
}