import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Failed to create distribution: " + e.getMessage());
        }
    }

    @Test
    public void testProbability() {
        Assert.assertEquals(0.2, distribution.probability(1), 0.0001);
        Assert.assertEquals(0.3, distribution.probability(2), 0.0001);
        Assert.assertEquals(0.5, distribution.probability(3), 0.0001);
        Assert.assertEquals(0, distribution.probability(0), 0.0001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = distribution.getSamples();

        Assert.assertEquals(3, samples.size());

        Assert.assertEquals(1, (int)samples.get(0).getKey());
        Assert.assertEquals(0.2, samples.get(0).getValue(), 0.0001);

        Assert.assertEquals(2, (int)samples.get(1).getKey());
        Assert.assertEquals(0.3, samples.get(1).getValue(), 0.0001);

        Assert.assertEquals(3, (int)samples.get(2).getKey());
        Assert.assertEquals(0.5, samples.get(2).getValue(), 0.0001);
    }

    @Test
    public void testSample() {
        int sampleSize = 1000;
        int[] counts = new int[3];

        for (int i = 0; i < sampleSize; i++) {
            int sample = distribution.sample();
            counts[sample-1]++;
        }

        double p1 = counts[0] / (double)sampleSize;
        double p2 = counts[1] / (double)sampleSize;
        double p3 = counts[2] / (double)sampleSize;

        Assert.assertEquals(0.2, p1, 0.05);
        Assert.assertEquals(0.3, p2, 0.05);
        Assert.assertEquals(0.5, p3, 0.05);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleInvalidSampleSize() throws NotStrictlyPositiveException {
        distribution.sample(0);
    }
}