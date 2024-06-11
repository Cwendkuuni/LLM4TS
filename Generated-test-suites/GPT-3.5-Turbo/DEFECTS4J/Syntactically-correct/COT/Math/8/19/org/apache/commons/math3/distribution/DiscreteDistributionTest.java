import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.1));
        samples.add(new Pair<>(4, 0.4));
        distribution = new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbability() {
        double probability = distribution.probability(2);
        Assert.assertEquals(0.3, probability, 0);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        Assert.assertEquals(4, samples.size());
    }

    @Test
    public void testSample() {
        Integer sample = distribution.sample();
        Assert.assertNotNull(sample);
    }

    @Test
    public void testSampleWithSize() {
        Integer[] sample = distribution.sample(5);
        Assert.assertEquals(5, sample.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSampleWithInvalidSize() {
        Integer[] sample = distribution.sample(0);
    }

    @Test
    public void testReseedRandomGenerator() {
        long seed = 12345L;
        distribution.reseedRandomGenerator(seed);
        Integer sample = distribution.sample();
        Assert.assertNotNull(sample);
    }

}