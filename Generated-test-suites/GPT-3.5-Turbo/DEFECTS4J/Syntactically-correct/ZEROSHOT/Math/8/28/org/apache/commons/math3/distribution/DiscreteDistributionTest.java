import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> discreteDistribution;

    @Before
    public void setUp() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        discreteDistribution = new DiscreteDistribution<>(samples);
    }

    @After
    public void tearDown() throws Exception {
        discreteDistribution = null;
    }

    @Test
    public void probability_Null_ReturnsZero() {
        double actual = discreteDistribution.probability(null);
        Assert.assertEquals(0, actual, 0);
    }

    @Test
    public void probability_ExistentValue_ReturnsProbability() {
        double actual = discreteDistribution.probability(3);
        Assert.assertEquals(0.5, actual, 0);
    }

    @Test
    public void probability_NonExistentValue_ReturnsZero() {
        double actual = discreteDistribution.probability(4);
        Assert.assertEquals(0, actual, 0);
    }

    @Test
    public void getSamples_ReturnsSamples() {
        List<Pair<Integer, Double>> actual = discreteDistribution.getSamples();

        Assert.assertEquals(3, actual.size(), 0);
        Assert.assertEquals(new Pair<>(1, 0.2), actual.get(0));
        Assert.assertEquals(new Pair<>(2, 0.3), actual.get(1));
        Assert.assertEquals(new Pair<>(3, 0.5), actual.get(2));
    }

    @Test
    public void sample_ReturnsRandomValue() {
        Integer actual = discreteDistribution.sample();
        Assert.assertNotNull(actual);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void sample_NonPositiveSampleSize_ThrowsException() {
        discreteDistribution.sample(0);
    }

    @Test
    public void sample_returnSampleArrayWithCorrectSize() {
        int sampleSize = 5;
        Integer[] actual = discreteDistribution.sample(sampleSize);

        Assert.assertEquals(sampleSize, actual.length);
    }
}