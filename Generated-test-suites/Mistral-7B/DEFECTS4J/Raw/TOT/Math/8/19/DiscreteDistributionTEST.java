import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for DiscreteDistribution.
 */
public class DiscreteDistributionTest {

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testConstructorValidInput() {
        List<Pair<Comparable, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.4),
                new Pair<>(Integer.valueOf(2), 0.3),
                new Pair<>(Integer.valueOf(3), 0.3),
                new Pair<>(Integer.valueOf(4), 0.1));

        try {
            RandomGenerator rng = new Well19937c();
            DiscreteDistribution<Comparable> distribution = new DiscreteDistribution<>(rng, samples);
            assertNotNull(distribution);
            assertEquals(4, distribution.getSamples().size());
        } catch (Exception e) {
            // exceptions should not be thrown
            fail(e.getMessage());
        }
    }

    /**
     * Test constructor with invalid input - negative probability.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeProbability() {
        List<Pair<Comparable, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.4),
                new Pair<>(Integer.valueOf(2), 0.1),
                new Pair<>(Integer.valueOf(3), -0.2));

        try {
            RandomGenerator rng = new Well19937c();
            DiscreteDistribution<Comparable> distribution = new DiscreteDistribution<>(rng, samples);
        } catch (Exception e) {
            // expected exception
        }
    }

    /**
     * Test constructor with invalid input - infinite probability.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorInfiniteProbability() {
        List<Pair<Comparable, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.4),
                new Pair<>(Integer.valueOf(2), Double.POSITIVE_INFINITY),
                new Pair<>(Integer.valueOf(3), 0.3));

        try {
            RandomGenerator rng = new Well19937c();
            DiscreteDistribution<Comparable> distribution = new DiscreteDistribution<>(rng, samples);
        } catch (Exception e) {
            // expected exception
        }
    }

    /**
     * Test constructor with invalid input - zero probabilities sum.
     */
    @Test(expected = MathArithmeticException.class)
    public void testConstructorZeroProbabilitiesSum() {
        List<Pair<Comparable, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.4),
                new Pair<>(Integer.valueOf(2), 0.3),
                new Pair<>(Integer.valueOf(3), 0.3),
                new Pair<>(Integer.valueOf(4), -0.1));

        try {
            RandomGenerator rng = new Well19937c();
            DiscreteDistribution<Comparable> distribution = new DiscreteDistribution<>(rng, samples);
        } catch (Exception e) {
            // expected exception
        }
    }

    /**
     * Test probability method with valid input.
     */
    @Test
    public void testProbabilityValidInput() {
        List<Pair<Comparable, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.4),
                new Pair<>(Integer.valueOf(2), 0.3),
                new Pair<>(Integer.valueOf(3), 0.3),
                new Pair<>(Integer.valueOf(4), 0.1));

        try {
            RandomGenerator rng = new Well19937c();
            DiscreteDistribution<Comparable> distribution = new DiscreteDistribution<>(rng, samples);
            for (Comparable singleton : distribution.singletons) {
                Double probability = distribution.probability(singleton);
                assertTrue(probability >= 0.0 && probability <= 1.0);
            }
        } catch (Exception e) {
            // exceptions should not be thrown
            fail(e.getMessage());
        }
    }

    /**
     * Test probability method with invalid input - null value.
     */
    @Test(expected = NullPointerException.class)
    public void testProbabilityNullValue() {
        List<Pair<Comparable, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.4),
                new Pair<>(Integer.valueOf(2), 0.3),
                new Pair<>(Integer.valueOf(3), 0.3),
                new Pair<>(null, 0.1));

        try {
            RandomGenerator rng = new Well19937c();
            DiscreteDistribution<Comparable> distribution = new DiscreteDistribution<>(rng, samples);
            distribution.probability(null);
        } catch (Exception e) {
            // expected exception
        }
    }

    /**
     * Test probability method with invalid input - value not in distribution.
     */
    @Test
    public void testProbabilityValueNotInDistribution() {
        List<Pair<Comparable, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.4),
                new Pair<>(Integer.valueOf(2), 0.3),
                new Pair<>(Integer.valueOf(3), 0.3),
                new Pair<>(Integer.valueOf(4), 0.1));

        try {
            RandomGenerator rng = new Well19937c();
            DiscreteDistribution<Comparable> distribution = new DiscreteDistribution<>(rng, samples);
            Comparable notInDistribution = Integer.valueOf(5);
            Double probability = distribution.probability(notInDistribution);
            assertEquals(0.0, probability, 0.01);
        } catch (Exception e) {
            // exceptions should not be thrown
            fail(e.getMessage());
        }
    }

    /**
     * Test sample method with valid input.
     */
    @Test
    public void testSampleValidInput() {
        List<Pair<Comparable, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.4),
                new Pair<>(Integer.valueOf(2), 0.3),
                new Pair<>(Integer.valueOf(3), 0.3),
                new Pair<>(Integer.valueOf(4), 0.1));

        try {
            RandomGenerator rng = new Well19937c();
            DiscreteDistribution<Comparable> distribution = new DiscreteDistribution<>(rng, samples);

            Comparable sample = distribution.sample();
            for (Comparable singleton : distribution.singletons) {
                Double probability = distribution.probability(singleton);
                if (sample.equals(singleton)) {
                    assertEquals(probability, 1.0, 0.01);
                    break;
                }
            }
        } catch (Exception e) {
            // exceptions should not be thrown
            fail(e.getMessage());
        }
    }

    /**
     * Test sample method with invalid input - empty distribution.
     */
    @Test(expected = IllegalStateException.class)
    public void testSampleEmptyDistribution() {
        List<Pair<Comparable, Double>> samples = new ArrayList<>();

        try {
            RandomGenerator rng = new Well19937c();
            DiscreteDistribution<Comparable> distribution = new DiscreteDistribution<>(rng, samples);
            distribution.sample();
        } catch (Exception e) {
            // expected exception
        }
    }

    /**
     * Test sample method with invalid input - null value.
     */
    @Test(expected = NullPointerException.class)
    public void testSampleNullValue() {
        List<Pair<Comparable, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.4),
                new Pair<>(Integer.valueOf(2), 0.3),
                new Pair<>(Integer.valueOf(3), 0.3),
                new Pair<>(Integer.valueOf(4), 0.1));

        RandomGenerator rng = null;
        try {
            DiscreteDistribution<Comparable> distribution = new DiscreteDistribution<>(rng, samples);
            distribution.sample();
        } catch (Exception e) {
            // expected exception
        }
    }

    /**
     * Test sample method with large number of samples.
     */
    @Test
    public void testSampleLargeNumberOfSamples() {
        List<Pair<Comparable, Double>> samples = Arrays.asList(
                new Pair<>(Integer.valueOf(1), 0.4),
                new Pair<>(Integer.valueOf(2), 0.3),
                new Pair<>(Integer.valueOf(3), 0.3),
                new Pair<>(Integer.valueOf(4), 0.1));

        try {
            RandomGenerator rng = new Well19937c();
            DiscreteDistribution<Comparable> distribution = new DiscreteDistribution<>(rng, samples);
            T[] samplesArray = distribution.sample(100000);

            List<Integer> frequencies = new ArrayList<>();
            for (Comparable singleton : distribution.singletons) {
                frequencies.add(0);
            }

            for (T sample : samplesArray) {
                int index = distribution.singletons.indexOf(sample);
                frequencies.set(index, frequencies.get(index) + 1);
            }

            for (int i = 0; i < frequencies.size(); i++) {
                Double expectedProbability = distribution.probability(distribution.singletons.get(i));
                Double calculatedProbability = (double) frequencies.get(i) / samplesArray.length;
                assertEquals(expectedProbability, calculatedProbability, 0.01);
            }
        } catch (Exception e) {
            // exceptions should not be thrown
            fail(e.getMessage());
        }
    }
}