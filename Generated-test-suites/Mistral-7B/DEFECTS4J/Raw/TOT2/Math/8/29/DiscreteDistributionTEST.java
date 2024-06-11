import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for DiscreteDistribution class.
 */
public class DiscreteDistributionTest {

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testConstructorWithValidInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // Exception should not be thrown here
            e.printStackTrace();
            assertNull(distribution);
            return;
        }

        assertNotNull(distribution);
        assertEquals(3, distribution.singletons.size());
        assertEquals(0.2, distribution.probabilities[0], 0.01);
        assertEquals(0.3, distribution.probabilities[1], 0.01);
        assertEquals(0.5, distribution.probabilities[2], 0.01);
    }

    /**
     * Test constructor with invalid input.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidInput() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, -0.2), new Pair<>(2, 0.5));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException e) {
            distribution = e.getSourceObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected exception", e);
        }

        // Test that constructor was thrown an exception
        assertNotNull(distribution);
    }

    /**
     * Test probability method with valid input.
     */
    @Test
    public void testProbabilityWithValidInput() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.2), new Pair<>(2, 0.3),
                new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
            assertNull(distribution);
            return;
        }

        int x = 2;
        double probability = distribution.probability((Integer) x);
        assertTrue(probability >= 0.3 && probability <= 0.31);
    }

    /**
     * Test probability method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityWithInvalidInput() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.2), new Pair<>(2, 0.3),
                new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
            assertNull(distribution);
            return;
        }

        int x = 4;
        distribution.probability(x);
    }

    /**
     * Test getSamples method with valid input.
     */
    @Test
    public void testGetSamplesWithValidInput() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.2), new Pair<>(2, 0.3),
                new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
            assertNull(distribution);
            return;
        }

        List<Pair<Integer, Double>> result = distribution.getSamples();
        assertEquals(3, result.size());
        assertEquals(0.2, result.get(0).getValue(), 0.01);
        assertEquals(0.3, result.get(1).getValue(), 0.01);
        assertEquals(0.5, result.get(2).getValue(), 0.01);
    }

    /**
     * Test sample method with valid input.
     */
    @Test
    public void testSampleWithValidInput() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.2), new Pair<>(2, 0.3),
                new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
            assertNull(distribution);
            return;
        }

        int x = distribution.sample();
        assertNotNull(x);
        assertTrue(x >= 1 && x <= 3);
    }

    /**
     * Test sample method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSampleWithInvalidInput() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.2), new Pair<>(2, 0.3),
                new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
            assertNull(distribution);
            return;
        }

        distribution.sample(4);
    }

    /**
     * Test sample method with different types.
     */
    @Test(expected = ClassCastException.class)
    public void testSampleWithDifferentTypes() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.2), new Pair<>(2, 0.3),
                new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
            assertNull(distribution);
            return;
        }

        int sampleSize = 1;
        Double[] output = (Double[]) distribution.sample(sampleSize);
    }

    /**
     * Test reseedRandomGenerator method with valid input.
     */
    @Test
    public void testReseedRandomGeneratorWithValidInput() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.2), new Pair<>(2, 0.3),
                new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
            assertNull(distribution);
            return;
        }

        long seed = 123456;
        distribution.reseedRandomGenerator(seed);
    }

    /**
     * Test reseedRandomGenerator method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReseedRandomGeneratorWithInvalidInput() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.2), new Pair<>(2, 0.3),
                new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
            assertNull(distribution);
            return;
        }

        distribution.reseedRandomGenerator("not a seed");
    }

    /**
     * Test sample method with large sample size.
     */
    @Test
    public void testSampleWithLargeSampleSize() throws NotStrictlyPositiveException {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.2), new Pair<>(2, 0.3),
                new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
            assertNull(distribution);
            return;
        }

        int sampleSize = 100000;
        Integer[] output = (Integer[]) distribution.sample(sampleSize);
        assertNotNull(output);
        assertEquals(sampleSize, output.length);
        for (int i = 0; i < sampleSize; i++) {
            assertTrue(output[i] >= 1 && output[i] <= 3);
        }
    }
}