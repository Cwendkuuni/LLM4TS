import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
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
    public void setup() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.2));
        samples.add(new Pair<>(3, 0.3));
        samples.add(new Pair<>(4, 0.4));

        try {
            discreteDistribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void probability_validValue_expectedProbability() {
        // typical case
        Assert.assertEquals(0.1, discreteDistribution.probability(1), 0.000001);

        // edge cases
        Assert.assertEquals(0.4, discreteDistribution.probability(4), 0.000001);
        Assert.assertEquals(0.3, discreteDistribution.probability(3), 0.000001);
    }

    @Test
    public void probability_invalidValue_zeroProbability() {
        // typical case
        Assert.assertEquals(0, discreteDistribution.probability(0), 0.000001);

        // edge case
        Assert.assertEquals(0, discreteDistribution.probability(5), 0.000001);
    }

    @Test
    public void getSamples_correctSamples() {
        // typical case
        List<Pair<Integer, Double>> expectedSamples = new ArrayList<>();
        expectedSamples.add(new Pair<>(1, 0.1));
        expectedSamples.add(new Pair<>(2, 0.2));
        expectedSamples.add(new Pair<>(3, 0.3));
        expectedSamples.add(new Pair<>(4, 0.4));

        List<Pair<Integer, Double>> actualSamples = discreteDistribution.getSamples();

        Assert.assertEquals(expectedSamples.size(), actualSamples.size());

        for (int i = 0; i < expectedSamples.size(); i++) {
            Assert.assertEquals(expectedSamples.get(i).getKey(), actualSamples.get(i).getKey());
            Assert.assertEquals(expectedSamples.get(i).getValue(), actualSamples.get(i).getValue(), 0.000001);
        }
    }

    @Test
    public void reseedRandomGenerator_validSeed_updatedSeed() {
        // typical case
        long initialSeed = discreteDistribution.random.getSeed();
        long newSeed = 12345;

        discreteDistribution.reseedRandomGenerator(newSeed);

        Assert.assertNotEquals(initialSeed, discreteDistribution.random.getSeed());
        Assert.assertEquals(newSeed, discreteDistribution.random.getSeed());
    }

    @Test
    public void sample_validSampleSize_arrayOfRandomValues() {
        // typical case
        int sampleSize = 5;

        Integer[] samples = discreteDistribution.sample(sampleSize);

        Assert.assertEquals(sampleSize, samples.length);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void sample_invalidSampleSize_exceptionThrown() throws NotStrictlyPositiveException {
        // error case
        int sampleSize = -5;

        discreteDistribution.sample(sampleSize);
    }
}