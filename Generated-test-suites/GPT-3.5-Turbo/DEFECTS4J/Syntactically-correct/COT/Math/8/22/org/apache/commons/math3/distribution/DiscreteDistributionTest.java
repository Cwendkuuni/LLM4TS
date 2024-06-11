import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    // Test probability() method
    @Test
    public void testProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        double probability1 = distribution.probability(1);
        Assert.assertEquals(0.2, probability1, 0);

        double probability2 = distribution.probability(2);
        Assert.assertEquals(0.3, probability2, 0);

        double probability3 = distribution.probability(3);
        Assert.assertEquals(0.5, probability3, 0);
    }

    // Test getSamples() method
    @Test
    public void testGetSamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        List<Pair<Integer, Double>> expectedSamples = new ArrayList<>();
        expectedSamples.add(new Pair<>(1, 0.2));
        expectedSamples.add(new Pair<>(2, 0.3));
        expectedSamples.add(new Pair<>(3, 0.5));

        List<Pair<Integer, Double>> actualSamples = distribution.getSamples();

        Assert.assertEquals(expectedSamples, actualSamples);
    }

    // Test sample() method
    @Test
    public void testSample() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        for (int i = 0; i < 100; i++) {
            Integer sample = distribution.sample();
            Assert.assertTrue(sample >= 1 && sample <= 3);
        }
    }

    // Test sample(int sampleSize) method
    @Test
    public void testSampleSize() throws NotStrictlyPositiveException, NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        int sampleSize = 10;
        Integer[] randomSamples = distribution.sample(sampleSize);

        Assert.assertEquals(sampleSize, randomSamples.length);
        for (Integer sample : randomSamples) {
            Assert.assertTrue(sample >= 1 && sample <= 3);
        }
    }

    // Test reseedRandomGenerator(long seed) method
    @Test
    public void testReseedRandomGenerator() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        distribution.reseedRandomGenerator(12345);
        Integer sample1 = distribution.sample();

        distribution.reseedRandomGenerator(12345);
        Integer sample2 = distribution.sample();

        Assert.assertEquals(sample1, sample2);
    }
}