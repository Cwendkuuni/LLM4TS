import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for DiscreteDistribution.
 */
public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstruction() throws Exception {
        // Test empty list construction
        List<Pair<Integer, Double>> emptyList = new ArrayList<>();
        try {
            new DiscreteDistribution<>(emptyList);
            fail("Expected NotPositiveException or MathArithmeticException");
        } catch (NotPositiveException | MathArithmeticException e) {
            // Expected
        }

        // Test null values in list construction
        List<Pair<Integer, Double>> nullList = new ArrayList<>();
        nullList.add(new Pair<>(null, 1.0));
        try {
            new DiscreteDistribution<>(nullList);
            fail("Expected NotPositiveException or MathIllegalArgumentException");
        } catch (NotPositiveException | MathIllegalArgumentException e) {
            // Expected
        }

        // Test valid construction
        List<Pair<Integer, Double>> validList = new ArrayList<>();
        validList.add(new Pair<>(1, 0.25));
        validList.add(new Pair<>(2, 0.5));
        validList.add(new Pair<>(3, 0.25));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), validList);

        // Test getSamples() method
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        assertEquals(validList, samples);
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        );

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        // Test probability of known value
        assertEquals(0.1, distribution.probability((Integer) 1), 0.001);

        // Test probability of unknown value
        assertNull(distribution.probability((Integer) 4));

        // Test probability of null value
        assertNull(distribution.probability(null));
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        );

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        // Test sample() method with known values
        List<Integer> samplesList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            samplesList.add(distribution.sample());
        }
        assertEquals(samples.size(), samplesList.size());
        for (Pair<Integer, Double> sample : samples) {
            boolean found = false;
            for (Integer s : samplesList) {
                if (s.equals(sample.getKey())) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }

        // Test sample() method with null value
        assertNull(distribution.sample());

        // Test sample() method with infinite value
        try {
            distribution.sample((Integer) Long.MAX_VALUE);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSampleSize() throws NotStrictlyPositiveException {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.6)
        );

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        // Test sampleSize() method with valid input
        int[] result = distribution.sample(5);
        assertArrayEquals(new Integer[]{1, 1, 2, 2, 3}, result);

        // Test sampleSize() method with non-positive input
        try {
            distribution.sample(-1);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test sampleSize() method with zero input
        try {
            distribution.sample(0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }
}