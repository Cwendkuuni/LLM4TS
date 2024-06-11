import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.distribution.*;

public class DiscreteDistributionTest {

    /**
     * Test the probability method for a given value.
     */
    @Test
    public void testProbability() {
        // Setup
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(new Pair<>(1, 0.5), new Pair<>(2, 0.5)));

        // Execute
        double probability1 = distribution.probability(1);
        double probability2 = distribution.probability(2);

        // Verify
        assertEquals(0.5, probability1, 0.0);
        assertEquals(0.5, probability2, 0.0);
    }

    /**
     * Test the sample method to generate a random value.
     */
    @Test
    public void testSample() {
        // Setup
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(new Pair<>(1, 0.5), new Pair<>(2, 0.5)));

        // Execute
        Integer value = distribution.sample();

        // Verify
        assertNotNull(value);
    }

    /**
     * Test the sample method to generate an array of random values.
     */
    @Test
    public void testSampleArray() {
        // Setup
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(new Pair<>(1, 0.5), new Pair<>(2, 0.5)));
        int sampleSize = 5;

        // Execute
        Integer[] values = distribution.sample(sampleSize);

        // Verify
        assertNotNull(values);
        assertEquals(sampleSize, values.length);
    }

}