import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    private RandomGenerator rng;
    private List<Pair<String, Double>> samples;
    private DiscreteDistribution<String> distribution;

    @Before
    public void setUp() {
        rng = new Well19937c();

        samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.25));
        samples.add(new Pair<>("B", 0.25));
        samples.add(new Pair<>("C", 0.5));

        try {
            distribution = new DiscreteDistribution<>(rng, samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testProbability_SingleValue() {
        double expected = 0.25;
        double actual = distribution.probability("A");
        Assert.assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testProbability_NullValue() {
        double expected = 0.0;
        double actual = distribution.probability(null);
        Assert.assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testProbability_InvalidValue() {
        double expected = 0.0;
        double actual = distribution.probability("D");
        Assert.assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<String, Double>> expected = samples;
        List<Pair<String, Double>> actual = distribution.getSamples();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSample_SingleValue() {
        String[] expected = {"A"};
        String[] actual = distribution.sample(1);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSample_InvalidSampleSize() {
        distribution.sample(-1);
    }

    @Test
    public void testSample_MultipleValues() {
        String[] expected = {"B", "C", "A"};
        String[] actual = distribution.sample(3);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testSample_EdgeCase() {
        String[] expected = {"C"};
        String[] actual = distribution.sample(100);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testReseedRandomGenerator() {
        long seed = 12345;
        distribution.reseedRandomGenerator(seed);

        Assert.assertEquals(seed, ((Well19937c) distribution.random).getSeed());
    }

    @Test
    public void testConstructor_InvalidProbability() {
        List<Pair<String, Double>> invalidSamples = new ArrayList<>();
        invalidSamples.add(new Pair<>("A", -0.5));

        try {
            distribution = new DiscreteDistribution<>(rng, invalidSamples);
            Assert.fail("Expected exception NotPositiveException was not thrown.");
        } catch (NotPositiveException e) {
            Assert.assertTrue(true);
        } catch (MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testConstructor_ProbabilitiesSumToZero() {
        List<Pair<String, Double>> invalidSamples = new ArrayList<>();
        invalidSamples.add(new Pair<>("A", 0.0));
        invalidSamples.add(new Pair<>("B", 0.0));
        invalidSamples.add(new Pair<>("C", 0.0));

        try {
            distribution = new DiscreteDistribution<>(rng, invalidSamples);
            Assert.fail("Expected exception MathArithmeticException was not thrown.");
        } catch (MathArithmeticException e) {
            Assert.assertTrue(true);
        } catch (NotPositiveException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testConstructor_InfiniteProbability() {
        List<Pair<String, Double>> invalidSamples = new ArrayList<>();
        invalidSamples.add(new Pair<>("A", Double.POSITIVE_INFINITY));

        try {
            distribution = new DiscreteDistribution<>(rng, invalidSamples);
            Assert.fail("Expected exception MathIllegalArgumentException was not thrown.");
        } catch (MathIllegalArgumentException e) {
            Assert.assertTrue(true);
        } catch (NotPositiveException | MathArithmeticException e) {
            Assert.fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

}