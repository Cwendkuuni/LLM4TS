import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    private DiscreteDistribution<String> distribution;

    @Before
    public void setUp() throws Exception {
        List<Pair<String, Double>> samples = new ArrayList<>();

        samples.add(new Pair<>("A", 0.1));
        samples.add(new Pair<>("B", 0.2));
        samples.add(new Pair<>("C", 0.3));
        samples.add(new Pair<>("D", 0.4));

        distribution = new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbability_ExistingValue() {
        String value = "B";
        double expectedProbability = 0.2;

        double actualProbability = distribution.probability(value);

        Assert.assertEquals(expectedProbability, actualProbability, 0.0001);
    }

    @Test
    public void testProbability_NonExistingValue() {
        String value = "E";
        double expectedProbability = 0;

        double actualProbability = distribution.probability(value);

        Assert.assertEquals(expectedProbability, actualProbability, 0.0001);
    }

    @Test
    public void testProbability_NullValue() {
        String value = null;
        double expectedProbability = 0;

        double actualProbability = distribution.probability(value);

        Assert.assertEquals(expectedProbability, actualProbability, 0.0001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<String, Double>> expectedSamples = new ArrayList<>();

        expectedSamples.add(new Pair<>("A", 0.1));
        expectedSamples.add(new Pair<>("B", 0.2));
        expectedSamples.add(new Pair<>("C", 0.3));
        expectedSamples.add(new Pair<>("D", 0.4));

        List<Pair<String, Double>> actualSamples = distribution.getSamples();

        Assert.assertEquals(expectedSamples.size(), actualSamples.size());
        for (int i = 0; i < expectedSamples.size(); i++) {
            Assert.assertEquals(expectedSamples.get(i).getKey(), actualSamples.get(i).getKey());
            Assert.assertEquals(expectedSamples.get(i).getValue(), actualSamples.get(i).getValue(), 0.0001);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSample_NegativeSampleSize() throws NotStrictlyPositiveException {
        int sampleSize = -1;

        distribution.sample(sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSample_ZeroSampleSize() throws NotStrictlyPositiveException {
        int sampleSize = 0;

        distribution.sample(sampleSize);
    }

    @Test
    public void testSample_PositiveSampleSize() throws NotStrictlyPositiveException {
        int sampleSize = 3;

        String[] samples = distribution.sample(sampleSize);

        Assert.assertEquals(sampleSize, samples.length);
    }

}