import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        distribution = new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbability() {
        assertEquals(0.2, distribution.probability(1), 0);
        assertEquals(0.3, distribution.probability(2), 0);
        assertEquals(0.5, distribution.probability(3), 0);
        assertEquals(0, distribution.probability(4), 0);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> expectedSamples = new ArrayList<>();
        expectedSamples.add(new Pair<>(1, 0.2));
        expectedSamples.add(new Pair<>(2, 0.3));
        expectedSamples.add(new Pair<>(3, 0.5));

        List<Pair<Integer, Double>> actualSamples = distribution.getSamples();

        assertEquals(expectedSamples.size(), actualSamples.size());
        for (int i = 0; i < expectedSamples.size(); i++) {
            assertEquals(expectedSamples.get(i).getKey(), actualSamples.get(i).getKey());
            assertEquals(expectedSamples.get(i).getValue(), actualSamples.get(i).getValue(), 0);
        }
    }

    @Test
    public void testSample() {
        int sampleSize = 1000;
        int[] count = new int[4];

        for (int i = 0; i < sampleSize; i++) {
            int sample = distribution.sample();
            count[sample]++;
        }

        assertEquals(0, count[0]);
        assertEquals(200, count[1], 50);
        assertEquals(300, count[2], 50);
        assertEquals(500, count[3], 50);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSampleSizeZero() {
        distribution.sample(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSampleSize() {
        distribution.sample(-5);
    }
}