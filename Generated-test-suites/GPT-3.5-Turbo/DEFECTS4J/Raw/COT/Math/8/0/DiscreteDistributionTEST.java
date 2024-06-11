import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() {
        distribution = new DiscreteDistribution<>(new Well19937c());
    }

    @Test
    public void testProbability() {
        // Test with valid input
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        assertEquals(0.5, distribution.probability(1), 0);
        assertEquals(0.3, distribution.probability(2), 0);
        assertEquals(0.2, distribution.probability(3), 0);

        // Test with invalid input
        samples.clear();
        samples.add(new Pair<>(1, -0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        try {
            distribution = new DiscreteDistribution<>(samples);
            fail("Exception should be thrown");
        } catch (NotPositiveException e) {
            // Expected exception
        }

    }

    @Test
    public void testGetSamples() {
        // Create a sample distribution
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        List<Pair<Integer, Double>> returnedSamples = distribution.getSamples();
        assertEquals(samples, returnedSamples);
    }

    @Test
    public void testSample() {
        // Create a sample distribution
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        // Test random sampling
        Integer result = distribution.sample();
        assertTrue(samples.stream().anyMatch(x -> x.getKey().equals(result)));
    }

    @Test
    public void testSampleWithSize() {
        // Create a sample distribution
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        // Test sample with size
        int sampleSize = 10;
        Integer[] result = distribution.sample(sampleSize);
        assertEquals(sampleSize, result.length);
        assertTrue(Arrays.stream(result).allMatch(x -> samples.stream().anyMatch(y -> y.getKey().equals(x))));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithInvalidSize() {
        // Create a sample distribution
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        // Test sample with invalid size
        int sampleSize = -10;
        Integer[] result = distribution.sample(sampleSize);
    }

    @Test
    public void testReseedRandomGenerator() {
        // Create a first sample distribution
        List<Pair<Integer, Double>> samples1 = new ArrayList<>();
        samples1.add(new Pair<>(1, 0.5));
        samples1.add(new Pair<>(2, 0.3));
        samples1.add(new Pair<>(3, 0.2));
        try {
            distribution = new DiscreteDistribution<>(samples1);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        // Create a second sample distribution
        List<Pair<Integer, Double>> samples2 = new ArrayList<>();
        samples2.add(new Pair<>(1, 1.0));
        try {
            distribution = new DiscreteDistribution<>(samples2);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        // Reseed the random generator
        long seed = 12345L;
        distribution.reseedRandomGenerator(seed);

        // Check if the random generator is reseeded
        assertNotEquals(samples1, distribution.getSamples());
        assertEquals(samples2, distribution.getSamples());
    }
}