import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DiscreteDistributionTest {

    private List<Pair<Integer, Double>> samples;
    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setup() {
        samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProbability() {
        Assert.assertEquals(0.2, distribution.probability(1), 0.0001);
        Assert.assertEquals(0.3, distribution.probability(2), 0.0001);
        Assert.assertEquals(0.5, distribution.probability(3), 0.0001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> expectedSamples = new ArrayList<>();
        expectedSamples.add(new Pair<>(1, 0.2));
        expectedSamples.add(new Pair<>(2, 0.3));
        expectedSamples.add(new Pair<>(3, 0.5));

        List<Pair<Integer, Double>> actualSamples = distribution.getSamples();

        Assert.assertEquals(expectedSamples.size(), actualSamples.size());
        for (int i = 0; i < expectedSamples.size(); i++) {
            Assert.assertEquals(expectedSamples.get(i).getKey(), actualSamples.get(i).getKey());
            Assert.assertEquals(expectedSamples.get(i).getValue(), actualSamples.get(i).getValue(), 0.0001);
        }
    }

    @Test
    public void testSample() {
        int numSamples = 10000;
        int[] observedCounts = new int[4]; // 1 extra index to handle edge cases

        for (int i = 0; i < numSamples; i++) {
            int sample = distribution.sample();
            observedCounts[sample]++;
        }

        int totalCount = 0;
        for (int count : observedCounts) {
            totalCount += count;
        }

        for (int i = 1; i <= 3; i++) {
            double expectedProbability = distribution.probability(i);
            double observedProbability = (double) observedCounts[i] / totalCount;
            Assert.assertEquals(expectedProbability, observedProbability, 0.01);
        }
    }

    @Test
    public void testSampleWithSampleSize() {
        int sampleSize = 100;

        try {
            Integer[] samples = distribution.sample(sampleSize);

            Assert.assertEquals(sampleSize, samples.length);

            for (int i = 0; i < sampleSize; i++) {
                Assert.assertTrue(samples[i] == 1 || samples[i] == 2 || samples[i] == 3);
            }
        } catch (NotStrictlyPositiveException e) {
            e.printStackTrace();
        }
    }
}