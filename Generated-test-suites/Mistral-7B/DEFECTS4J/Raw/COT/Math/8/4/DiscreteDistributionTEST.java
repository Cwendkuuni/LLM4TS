import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;
    private List<Pair<Integer, Double>> samples;

    public DiscreteDistributionTest(List<Pair<Integer, Double>> samples) {
        this.samples = samples;
    }

    @Parameterized.Parameters(name = "{index}: samples={0}")
    public static List<Object[]> data() {
        List<Object[]> params = new ArrayList<>();
        // Add test data here
        params.add(Arrays.asList(Arrays.asList(new Pair<>(1, 0.3), new Pair<>(2, 0.4), new Pair<>(3, 0.3))));
        params.add(Arrays.asList(Arrays.asList(new Pair<>(0, 1.0), new Pair<>(1, 0.5), new Pair<>(2, 0.5))));
        return params;
    }

    @Test
    public void testConstructor() throws NotPositiveException, NotStrictlyPositiveException, MathArithmeticException {
        distribution = new DiscreteDistribution<Integer>(samples);
    }

    @Test(expected = NotPositiveException.class)
    public void testNotPositiveException() throws NotPositiveException {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, -0.5), new Pair<>(2, 0.4), new Pair<>(3, 0.3));
        new DiscreteDistribution<Integer>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testSumOfProbabilitiesZero() throws NotPositiveException, NotStrictlyPositiveException, MathArithmeticException {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.3), new Pair<>(2, 0.3), new Pair<>(3, -0.01));
        new DiscreteDistribution<Integer>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testProbabilityInfinite() throws NotPositiveException, NotStrictlyPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, Double.POSITIVE_INFINITY), new Pair<>(2, 0.4), new Pair<>(3, 0.3));
        new DiscreteDistribution<Integer>(samples);
    }

    @Test
    public void testProbability() throws NotPositiveException, NotStrictlyPositiveException, MathArithmeticException {
        for (int i = 0; i < samples.size(); i++) {
            int x = samples.get(i).getKey();
            double expectedProbability = samples.get(i).getValue();
            double actualProbability = distribution.probability(x);
            Assert.assertEquals(expectedProbability, actualProbability, 0.001);
        }
    }

    @Test
    public void testSample() throws NotPositiveException, NotStrictlyPositiveException {
        int expectedValue;
        double expectedProbability;
        int actualValue;

        for (int i = 0; i < 1000; i++) {
            actualValue = distribution.sample().intValue();
            expectedValue = samples.get((int) (Math.random() * samples.size())).getKey().intValue();
            expectedProbability = samples.get((int) (Math.random() * samples.size())).getValue();
            Assert.assertEquals(expectedValue, actualValue);
            Assert.assertNotSame(expectedValue, actualValue);
            Assert.assertEquals(expectedProbability, distribution.probability(expectedValue), 0.001);
        }
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> expectedSamples = distribution.getSamples();
        List<Pair<Integer, Double>> actualSamples = samples;
        Assert.assertEquals(expectedSamples, actualSamples);
    }

    @Test
    public void testReseedRandomGenerator() throws NotPositiveException, NotStrictlyPositiveException, MathArithmeticException {
        long seed = 123456789;
        distribution.reseedRandomGenerator(seed);
        // Add test for checking if the seed is set correctly
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSize() throws NotPositiveException, NotStrictlyPositiveException {
        distribution.sample(-1);
    }

    @Test
    public void testSampleZeroSize() {
        T[] sample = distribution.sample(0);
        Assert.assertArrayEquals(Collections.emptyList().toArray(), sample);
    }

}