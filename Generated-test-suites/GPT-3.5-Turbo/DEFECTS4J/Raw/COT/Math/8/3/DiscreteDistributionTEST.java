
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {
    private RandomGenerator random;
    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setup() {
        random = new Well19937c();
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_negativeProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.5));
        distribution = new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_zeroSumProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.5));
        distribution = new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructor_infiniteProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, Double.POSITIVE_INFINITY));
        distribution = new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(0.2, distribution.probability(1), 0.001);
        Assert.assertEquals(0.3, distribution.probability(2), 0.001);
        Assert.assertEquals(0.5, distribution.probability(3), 0.001);
        Assert.assertEquals(0.0, distribution.probability(4), 0.001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }

        List<Pair<Integer, Double>> retrievedSamples = distribution.getSamples();

        Assert.assertEquals(samples.size(), retrievedSamples.size());

        for (int i = 0; i < samples.size(); i++) {
            Assert.assertEquals(samples.get(i).getKey(), retrievedSamples.get(i).getKey());
            Assert.assertEquals(samples.get(i).getValue(), retrievedSamples.get(i).getValue(), 0.001);
        }

    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }

        int sampleSize = 1000;
        int[] samplesCount = new int[samples.size()];

        for (int i = 0; i < sampleSize; i++) {
            int sample = distribution.sample();
            samplesCount[sample - 1]++;
        }

        for (int i = 0; i < samples.size(); i++) {
            double expectedCount = sampleSize * samples.get(i).getValue();
            Assert.assertEquals(expectedCount, samplesCount[i], 0.1 * expectedCount);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSample_negativeSampleSize() throws NotStrictlyPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }

        distribution.sample(-10);
    }

    @Test
    public void testSample_sizeOne() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }

        int sampleSize = 1;
        int[] samplesCount = new int[samples.size()];

        for (int i = 0; i < sampleSize; i++) {
            int sample = distribution.sample()[0];
            samplesCount[sample - 1]++;
        }

        for (int i = 0; i < samples.size(); i++) {
            double expectedCount = sampleSize * samples.get(i).getValue();
            Assert.assertEquals(expectedCount, samplesCount[i], 0.1 * expectedCount);
        }
    }

    @Test
    public void testSample_sizeThree() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }

        int sampleSize = 3;
        int[] samplesCount = new int[samples.size()];

        for (int i = 0; i < sampleSize; i++) {
            int[] sample = distribution.sample(sampleSize);
            samplesCount[sample[i] - 1]++;
        }

        for (int i = 0; i < samples.size(); i++) {
            double expectedCount = sampleSize * samples.get(i).getValue();
            Assert.assertEquals(expectedCount, samplesCount[i], 0.1 * expectedCount);
        }
    }

}
