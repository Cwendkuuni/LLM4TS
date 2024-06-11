
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

    private DiscreteDistribution<Integer> discreteDistribution;

    @Before
    public void setUp() {
        RandomGenerator random = new Well19937c();
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        
        try {
            discreteDistribution = new DiscreteDistribution<>(random, samples);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void probabilityTest() {
        Assert.assertEquals(0.5, discreteDistribution.probability(1), 0.0);
        Assert.assertEquals(0.3, discreteDistribution.probability(2), 0.0);
        Assert.assertEquals(0.2, discreteDistribution.probability(3), 0.0);
        Assert.assertEquals(0.0, discreteDistribution.probability(4), 0.0);
    }

    @Test
    public void getSamplesTest() {
        List<Pair<Integer, Double>> samples = discreteDistribution.getSamples();
        Assert.assertEquals(3, samples.size());

        for (Pair<Integer, Double> sample : samples) {
            Assert.assertTrue(sample.getKey() >= 1 && sample.getKey() <= 3);
            Assert.assertTrue(sample.getValue() >= 0 && sample.getValue() <= 1);
        }
    }

    @Test
    public void sampleTest() {
        Integer sample = discreteDistribution.sample();
        Assert.assertTrue(sample >= 1 && sample <= 3);
    }

    @Test
    public void sampleSizeTest() {
        int sampleSize = 5;
        Integer[] samples = null;
        try {
            samples = discreteDistribution.sample(sampleSize);
        } catch (NotStrictlyPositiveException e) {
            e.printStackTrace();
            Assert.fail();
        }

        Assert.assertEquals(sampleSize, samples.length);

        for (Integer sample : samples) {
            Assert.assertTrue(sample >= 1 && sample <= 3);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void sampleSizeNegativeTest() throws NotStrictlyPositiveException {
        int sampleSize = -1;
        Integer[] samples = discreteDistribution.sample(sampleSize);
    }

    @Test
    public void reseedRandomGeneratorTest() {
        long seed = 12345;
        discreteDistribution.reseedRandomGenerator(seed);
        // Cannot test for randomness, just ensuring the method does not throw any exception
    }
}