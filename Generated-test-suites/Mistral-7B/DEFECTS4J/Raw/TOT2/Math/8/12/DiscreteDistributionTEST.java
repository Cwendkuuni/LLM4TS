import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;
    private List<Pair<Integer, Double>> samples;
   
    public DiscreteDistributionTest(List<Pair<Integer, Double>> samples) {
        this.samples = samples;
    }

    @Parameterized.Parameters(name = "{index}: samples={0}")
    public static List<Object[]> data() {
        List<Object[]> params = new ArrayList<>();
        List<Pair<Integer, Double>> sample1 = new ArrayList<>();
        sample1.add(new Pair<>(1, 0.1));
        sample1.add(new Pair<>(2, 0.3));
        sample1.add(new Pair<>(3, 0.6));
        params.add(new Object[]{sample1});

        List<Object[]> params2 = new ArrayList<>();
        List<Pair<Integer, Double>> sample2 = new ArrayList<>();
        sample2.add(new Pair<>(null, 0.1));
        sample2.add(new Pair<>(1, 0.3));
        sample2.add(new Pair<>(2, 0.3));
        sample2.add(new Pair<>(3, 0.4));
        params2.add(new Object[]{sample2});

        List<Object[]> params3 = new ArrayList<>();
        List<Pair<Integer, Double>> sample3 = new ArrayList<>();
        sample3.add(new Pair<>(1, -0.1));
        params3.add(new Object[]{sample3});

        return params;
    }

    @Test
    public void testConstructor() throws NotPositiveException, NotStrictlyPositiveException, MathArithmeticException {
        try {
            distribution = new DiscreteDistribution<>(samples);
            Assert.assertNotNull(distribution);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testProbability() throws NotStrictlyPositiveException {
        for (Pair<Integer, Double> pair : samples) {
            double probability = distribution.probability(pair.getKey());
            Assert.assertEquals(pair.getValue(), probability, 0.0);
        }
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samplesFromDistribution = distribution.getSamples();
        Assert.assertEquals(samples, samplesFromDistribution);
    }

    @Test
    public void testSample() {
        for (int i = 0; i < 100; i++) {
            int sample = distribution.sample().intValue();
            boolean found = false;
            for (Pair<Integer, Double> pair : samples) {
                if (pair.getKey().equals(sample)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                Assert.fail("Unexpected sample: " + sample);
            }
        }
    }

    @Test
    public void testSampleArray() throws NotStrictlyPositiveException {
        int sampleSize = 5;
        T[] samples = distribution.sample(sampleSize);
        for (int i = 0; i < sampleSize; i++) {
            boolean found = false;
            for (Pair<Integer, Double> pair : samples) {
                if (pair.getKey().equals(samples[i])) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                Assert.fail("Unexpected sample: " + samples[i]);
            }
        }
    }

    @Test
    public void testReseedRandomGenerator() {
        long seed = 1L;
        distribution.reseedRandomGenerator(seed);
        RandomGenerator rng = distribution.random;
        for (int i = 0; i < 10; i++) {
            int sample = rng.nextInt();
            Assert.assertEquals(seed++, sample);
        }
    }
}