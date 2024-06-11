
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        distribution = new DiscreteDistribution<>(samples);
        distribution.reseedRandomGenerator(12345);
    }

    @Test
    public void testProbability() {
        Assert.assertEquals(0.2, distribution.probability(1), 0.0);
        Assert.assertEquals(0.3, distribution.probability(2), 0.0);
        Assert.assertEquals(0.5, distribution.probability(3), 0.0);
        Assert.assertEquals(0.0, distribution.probability(4), 0.0);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        Assert.assertEquals(3, samples.size());
        Assert.assertEquals(new Pair<>(1, 0.2), samples.get(0));
        Assert.assertEquals(new Pair<>(2, 0.3), samples.get(1));
        Assert.assertEquals(new Pair<>(3, 0.5), samples.get(2));
    }

    @Test
    public void testSample() {
        Integer sample = distribution.sample();
        Assert.assertTrue(sample >= 1 && sample <= 3);
    }

    @Test
    public void testSampleWithSize() throws NotStrictlyPositiveException {
        Integer[] sample = distribution.sample(5);
        Assert.assertEquals(5, sample.length);
        for (int i = 0; i < 5; i++) {
            Assert.assertTrue(sample[i] >= 1 && sample[i] <= 3);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithSizeZero() throws NotStrictlyPositiveException {
        distribution.sample(0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithNegativeSize() throws NotStrictlyPositiveException {
        distribution.sample(-5);
    }
}