import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test cases for DiscreteDistribution class.
 */
public class DiscreteDistributionTest {

    @Test
    public void testEmptyDistribution() {
        try {
            new DiscreteDistribution(new ArrayList<>());
            Assert.fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testNegativeProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), -0.1));
        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            Assert.fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testZeroProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.0));
        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            Assert.fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testInfProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), Double.POSITIVE_INFINITY));
        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            Assert.fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBasicUsage() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.2),
                new Pair<>(Integer.valueOf(2), 0.3),
                new Pair<>(Integer.valueOf(3), 0.5),
                new Pair<>(Integer.valueOf(4), 0.2)
        );
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.assertEquals(4, distribution.singletons.size());
        Assert.assertEquals(0.2, distribution.probability(Integer.valueOf(1)), 0.001);
        Assert.assertEquals(0.3, distribution.probability(Integer.valueOf(2)), 0.001);
        Assert.assertEquals(0.5, distribution.probability(Integer.valueOf(3)), 0.001);
        Assert.assertEquals(0.2, distribution.probability(Integer.valueOf(4)), 0.001);

        List<Integer> sample = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            sample.add(distribution.sample().intValue());
        }
        Assert.assertArrayEquals(distribution.singletons.toArray(), sample.stream().distinct().toArray());
    }

    @Test
    public void testReseeding() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.2),
                new Pair<>(Integer.valueOf(2), 0.3),
                new Pair<>(Integer.valueOf(3), 0.5),
                new Pair<>(Integer.valueOf(4), 0.2)
        );
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        long seed = System.nanoTime();
        distribution.reseedRandomGenerator(seed);

        List<Integer> sample = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            sample.add(distribution.sample().intValue());
        }
        Assert.assertFalse("Expected different seeds to generate different samples",
                Arrays.equals(distribution.singletons.toArray(), sample.stream().distinct().toArray()));

        distribution.reseedRandomGenerator(seed);
        sample.clear();
        for (int i = 0; i < 100; i++) {
            sample.add(distribution.sample().intValue());
        }
        Assert.assertEquals("Expected same seeds to generate the same samples",
                Arrays.equals(distribution.singletons.toArray(), sample.stream().distinct().toArray()));
    }
}