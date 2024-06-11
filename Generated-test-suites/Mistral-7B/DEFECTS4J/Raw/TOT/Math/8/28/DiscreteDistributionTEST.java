import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import org.junit.Assert.*;

/**
 * Test cases for DiscreteDistribution class.
 */
public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionWithValidInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
            RandomGenerator rnd = distribution.getRandomGenerator();

            // Test probability method for valid input
            assertEquals(0.3, distribution.probability(1), 0.001);
            assertEquals(0.4, distribution.probability(2), 0.001);
            assertEquals(0.3, distribution.probability(3), 0.001);

            // Test sample method for valid input
            int sample1 = distribution.sample().intValue();
            int sample2 = distribution.sample().intValue();
            int sample3 = distribution.sample().intValue();

            // Test sample method with multiple samples
            Integer[] samplesArray = distribution.sample(10);
            for (int sample : samplesArray) {
                assertTrue(singleton(samplesArray, sample));
            }

            // Test reseedRandomGenerator method
            rnd.setSeed(System.currentTimeMillis());
            samplesArray = distribution.sample(10);
            for (int sample : samplesArray) {
                assertTrue(singleton(samplesArray, sample));
            }
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testProbabilityNegativeInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            assertEquals("Probability must be nonnegative: -0.3", e.getMessage());
        }
    }

    @Test
    public void testProbabilitySumToZero() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, -0.001));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals("Probabilities must sum to 1.0 but found 0.999", e.getMessage());
        }
    }

    @Test
    public void testProbabilityInfiniteInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            assertEquals("Probability must be finite.", e.getMessage());
        }
    }

    @Test
    public void testSingletonSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 1));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
            T sample = distribution.sample();
            assertEquals(1, sample.intValue());
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMultipleSingletonSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 1));
        samples.add(new Pair<>(2, 1));
        samples.add(new Pair<>(3, 1));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
            T[] samplesArray = distribution.sample(10);
            for (int sample : samplesArray) {
                assertEquals(1, sample.intValue());
            }
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // Helper method for checking if all elements in the array are the same
    private boolean singleton(Object[] array, Object element) {
        for (Object obj : array) {
            if (!obj.equals(element)) {
                return false;
            }
        }
        return true;
    }

}