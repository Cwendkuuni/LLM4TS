import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    private List<Pair<Integer, Double>> samples;
    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() {
        samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.5));
        distribution = new DiscreteDistribution<Integer>(samples);
    }

    @Test
    public void testProbability() {
        Assert.assertEquals(0.2, distribution.probability(1), 0.0001);
        Assert.assertEquals(0.3, distribution.probability(2), 0.0001);
        Assert.assertEquals(0.5, distribution.probability(3), 0.0001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> expectedSamples = new ArrayList<Pair<Integer, Double>>();
        expectedSamples.add(new Pair<Integer, Double>(1, 0.2));
        expectedSamples.add(new Pair<Integer, Double>(2, 0.3));
        expectedSamples.add(new Pair<Integer, Double>(3, 0.5));
        Assert.assertEquals(expectedSamples, distribution.getSamples());
    }

    @Test
    public void testSample() {
        int[] counts = new int[3];
        for (int i = 0; i < 10000; i++) {
            int sample = distribution.sample();
            if (sample == 1) {
                counts[0]++;
            } else if (sample == 2) {
                counts[1]++;
            } else if (sample == 3) {
                counts[2]++;
            }
        }
        Assert.assertEquals(2000, counts[0], 100);
        Assert.assertEquals(3000, counts[1], 100);
        Assert.assertEquals(5000, counts[2], 100);
    }

    @Test
    public void testSampleWithSampleSize() {
        Integer[] samples = distribution.sample(100);
        int[] counts = new int[3];
        for (int i = 0; i < samples.length; i++) {
            if (samples[i] == 1) {
                counts[0]++;
            } else if (samples[i] == 2) {
                counts[1]++;
            } else if (samples[i] == 3) {
                counts[2]++;
            }
        }
        Assert.assertEquals(20, counts[0], 10);
        Assert.assertEquals(30, counts[1], 10);
        Assert.assertEquals(50, counts[2], 10);
    }
}