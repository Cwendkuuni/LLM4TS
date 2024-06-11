import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionWithValidInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.2));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);

            // Test probability method
            double probability = distribution.probability(1);
            assertEquals(0.3, probability, 0.001);

            // Test getSamples method
            List<Pair<Integer, Double>> samplesFromDistribution = distribution.getSamples();
            assertEquals(samples, samplesFromDistribution);

            // Test sample method
            Integer sample = distribution.sample();
            assertEquals(1, sample.intValue());

            // Test sample method with array
            Integer[] samplesArray = distribution.sample(3);
            int[] expected = {1, 1, 2};
            assertArrayEquals(expected, samplesArray);

        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDiscreteDistributionWithNegativeProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.2));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testDiscreteDistributionWithZeroProbabilitiesSum() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.1));
        samples.add(new Pair<>(3, 0.1));
        samples.add(new Pair<>(4, -0.1)); // Negative probability to check error handling

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testDiscreteDistributionWithInfProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDiscreteDistributionWithReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.2));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);

        long seed = 123456789;
        distribution.reseedRandomGenerator(seed);

        // Test that seeding doesn't change distribution properties
        double probability = distribution.probability(1);
        assertEquals(0.3, probability, 0.001);
        Integer sample = distribution.sample();
        assertEquals(1, sample.intValue());
    }
}