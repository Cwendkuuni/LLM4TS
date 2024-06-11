import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testConstructWithValidSamples() throws NotPositiveException, NotStrictlyPositiveException, MathArithmeticException {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Assert.assertArrayEquals(new Integer[]{1, 2, 3}, distribution.singletons.toArray());
        Assert.assertArrayEquals(new double[]{0.3, 0.4, 0.3}, distribution.probabilities, 0.001);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructWithNegativeProbabilities() throws NotPositiveException, NotStrictlyPositiveException, MathArithmeticException {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, -0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructWithZeroProbabilities() throws NotPositiveException, NotStrictlyPositiveException, MathArithmeticException {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0)
        );

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructWithInfiniteProbabilities() throws NotPositiveException, NotStrictlyPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, Double.POSITIVE_INFINITY),
                new Pair<>(3, 0.3)
        );

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Assert.assertEquals(0.3, distribution.probability((Integer) 1), 0.001);
        Assert.assertEquals(0.4, distribution.probability((Integer) 2), 0.001);
        Assert.assertEquals(0.3, distribution.probability((Integer) 3), 0.001);
        Assert.assertEquals(0, distribution.probability((Integer) 4), 0.001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        List<Pair<Integer, Double>> result = distribution.getSamples();

        Assert.assertEquals(samples, result);
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        List<Integer> samplesList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            samplesList.add(distribution.sample().intValue());
        }

        double[] frequencies = new double[3];
        for (Integer sample : samplesList) {
            if (sample == 1) {
                frequencies[0]++;
            } else if (sample == 2) {
                frequencies[1]++;
            } else {
                frequencies[2]++;
            }
        }

        double relativeError = 0.1;
        Assert.assertArrayEquals(new double[]{0.3, 0.4, 0.3}, MathArrays.normalizeArray(frequencies), relativeError);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithNegativeSampleSize() throws NotStrictlyPositiveException {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        distribution.sample(-1);
    }

}