import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class DiscreteDistributionTest {

    private static final double EPSILON = 1e-10;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Set up any necessary resources before running the tests
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Failed to create distribution");
        }
        assertEquals(0.2, distribution.probability(1), EPSILON);
        assertEquals(0.3, distribution.probability(2), EPSILON);
        assertEquals(0.5, distribution.probability(3), EPSILON);
        assertEquals(0.0, distribution.probability(4), EPSILON);
    }

    @Test(expected = NotPositiveException.class)
    public void testProbability_NegativeProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.2));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Failed to create distribution");
        }
        List<Pair<Integer, Double>> result = distribution.getSamples();
        assertEquals(samples, result);
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Failed to create distribution");
        }
        Integer sample = distribution.sample();
        assertTrue(samples.contains(new Pair<>(sample, null)));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSample_NegativeSampleSize() throws NotStrictlyPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        distribution.sample(-1);
    }

    @Test
    public void testSample_SampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Failed to create distribution");
        }
        Integer[] result = distribution.sample(10);
        assertEquals(10, result.length);
        for (Integer num : result) {
            assertTrue(samples.contains(new Pair<>(num, null)));
        }
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Failed to create distribution");
        }
        long seed1 = 1234;
        long seed2 = 5678;
        distribution.reseedRandomGenerator(seed1);
        Integer[] result1 = distribution.sample(10);
        distribution.reseedRandomGenerator(seed2);
        Integer[] result2 = distribution.sample(10);
        assertFalse(Arrays.equals(result1, result2));
    }

    @Test
    public void testEdgeCase() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.0));
        samples.add(new Pair<>(1, 1.0));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Failed to create distribution");
        }
        assertEquals(0.0, distribution.probability(1), EPSILON);
    }

    @Test
    public void testErrorScenarios() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Failed to create distribution");
        }
        assertEquals(0.0, distribution.probability(null), EPSILON);
    }

}