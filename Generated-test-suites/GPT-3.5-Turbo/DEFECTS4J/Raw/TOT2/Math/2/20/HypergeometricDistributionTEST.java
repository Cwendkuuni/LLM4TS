import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        // Basic test case
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.3, distribution.cumulativeProbability(1), 0.0001);
        
        // Edge cases
        // Test when x is outside the domain
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.0001);
    }

    @Test
    public void testProbability() {
        // Basic test case
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.42857, distribution.probability(2), 0.0001);

        // Edge cases
        assertEquals(0.0, distribution.probability(6), 0.0001);
        assertEquals(0.0, distribution.probability(-1), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        // Basic test case
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.7, distribution.upperCumulativeProbability(1), 0.0001);

        // Edge cases
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(5), 0.0001);
    }

    @Test
    public void testNumericalMean() {
        // Basic test case
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testNumericalVariance() {
        // Basic test case
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.64285, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testSupportLowerBound() {
        // Basic test case
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testSupportUpperBound() {
        // Basic test case
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        // Basic test case
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertTrue(distribution.isSupportConnected());
    }

}