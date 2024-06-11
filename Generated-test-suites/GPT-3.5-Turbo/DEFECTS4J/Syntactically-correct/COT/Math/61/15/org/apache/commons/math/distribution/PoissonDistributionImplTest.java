
import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.util.LocalizedFormats;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    private PoissonDistributionImpl distribution;

    @Before
    public void setUp() {
        // Create a new Poisson distribution with mean 5
        distribution = new PoissonDistributionImpl(5);
    }

    @Test
    public void testMean() {
        assertEquals(5.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        // Test the probability for x = 0
        assertEquals(0.0067, distribution.probability(0), 0.0001);

        // Test the probability for x = 2
        assertEquals(0.0842, distribution.probability(2), 0.0001);

        // Test the probability for negative x
        assertEquals(0.0, distribution.probability(-1), 0.0001);

        // Test the probability for maximum integer value
        assertEquals(0.0, distribution.probability(Integer.MAX_VALUE), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        // Test the cumulative probability for x = 0
        assertEquals(0.0067, distribution.cumulativeProbability(0), 0.0001);

        // Test the cumulative probability for x = 2
        assertEquals(0.1247, distribution.cumulativeProbability(2), 0.0001);

        // Test the cumulative probability for negative x
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);

        // Test the cumulative probability for maximum integer value
        assertEquals(1.0, distribution.cumulativeProbability(Integer.MAX_VALUE), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        // Test the normal approximate probability for x = 0
        assertEquals(0.0031, distribution.normalApproximateProbability(0), 0.0001);

        // Test the normal approximate probability for x = 2
        assertEquals(0.101, distribution.normalApproximateProbability(2), 0.0001);

        // Test the normal approximate probability for negative x
        assertEquals(0.0, distribution.normalApproximateProbability(-1), 0.0001);

        // Test the normal approximate probability for maximum integer value
        assertEquals(1.0, distribution.normalApproximateProbability(Integer.MAX_VALUE), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        // Test that sample() returns a valid value
        int sample = distribution.sample();
        assertTrue(sample >= 0);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityException() throws MathException {
        // Test that cumulativeProbability() throws an exception for negative x
        distribution.cumulativeProbability(-1);
    }

    @Test(expected = MathException.class)
    public void testNormalApproximateProbabilityException() throws MathException {
        // Test that normalApproximateProbability() throws an exception for negative x
        distribution.normalApproximateProbability(-1);
    }
}
