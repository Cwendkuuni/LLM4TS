import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    // Create an instance of HypergeometricDistribution
    private HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

    @Test
    public void testCumulativeProbability() {
        // Test the cumulative probability function
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.1429, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.4286, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(4), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.0001);
    }

    @Test
    public void testProbability() {
        // Test the probability function
        assertEquals(0.0, distribution.probability(-1), 0.0001);
        assertEquals(0.0, distribution.probability(0), 0.0001);
        assertEquals(0.2143, distribution.probability(1), 0.0001);
        assertEquals(0.5714, distribution.probability(2), 0.0001);
        assertEquals(0.2143, distribution.probability(3), 0.0001);
        assertEquals(0.0, distribution.probability(4), 0.0001);
        assertEquals(0.0, distribution.probability(5), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        // Test the upper cumulative probability function
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.8571, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.5714, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.1429, distribution.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(4), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(5), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        // Test the numerical mean function
        assertEquals(1, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        // Test the numerical variance function
        assertEquals(0.6, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        // Test the support lower bound function
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        // Test the support upper bound function
        assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        // Test the support connected function
        assertTrue(distribution.isSupportConnected());
    }
}