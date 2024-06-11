import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionCreation() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            Assert.assertNotNull(distribution);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        double probability = distribution.probability(Integer.valueOf(1));
        Assert.assertEquals(0.3, probability, 0.001);

        probability = distribution.probability(Integer.valueOf(2));
        Assert.assertEquals(0.4, probability, 0.001);

        probability = distribution.probability(Integer.valueOf(4));
        Assert.assertEquals(0.0, probability, 0.001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        List<Pair<Integer, Double>> expectedSamples = distribution.getSamples();
        List<Pair<Integer, Double>> actualSamples = new ArrayList<>();

        for (Pair<Integer, Double> sample : expectedSamples) {
            actualSamples.add(new Pair<>(sample.getKey(), sample.getValue()));
        }

        Assert.assertEquals(expectedSamples, actualSamples);
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        int expectedSample = 1;
        int actualSample = 0;

        for (int i = 0; i < 10000; i++) {
            actualSample = distribution.sample().intValue();
            if (actualSample == expectedSample) {
                i++;
            }
        }

        Assert.assertEquals(expectedSample, expectedSample);
    }

    @Test
    public void testSampleArray() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        int[] expectedSamples = new int[]{1, 1, 2, 2, 2, 3, 3, 3};
        int[] actualSamples = new int[8];

        for (int i = 0; i < 8; i++) {
            actualSamples[i] = distribution.sample().intValue();
        }

        Assert.assertArrayEquals(expectedSamples, actualSamples);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        distribution.sample(-1);
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        long seed = System.currentTimeMillis();
        distribution.reseedRandomGenerator(seed);

        int expectedSample = 1;
        int actualSample = 0;

        for (int i = 0; i < 10000; i++) {
            actualSample = distribution.sample().intValue();
            if (actualSample == expectedSample) {
                i++;
            }
        }

        Assert.assertEquals(expectedSample, expectedSample);
    }
}