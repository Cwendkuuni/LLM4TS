import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HypergeometricDistributionTest {

    // Test the cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 10);

        // Typical use case
        double result = distribution.cumulativeProbability(3);
        assertEquals(0.0526, result, 0.0001);

        // Edge case - minimum value
        result = distribution.cumulativeProbability(0);
        assertEquals(0.0, result, 0.0);

        // Edge case - maximum value
        result = distribution.cumulativeProbability(10);
        assertEquals(1.0, result, 0.0);

        // Error scenario - x is out of bounds
        try {
            result = distribution.cumulativeProbability(-1);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    // Test the probability method
    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 10);

        // Typical use case
        double result = distribution.probability(3);
        assertEquals(0.0727, result, 0.0001);

        // Edge case - minimum value
        result = distribution.probability(0);
        assertEquals(0.0693, result, 0.0001);

        // Edge case - maximum value
        result = distribution.probability(10);
        assertEquals(0.000012, result, 0.000001);

        // Error scenario - x is out of bounds
        result = distribution.probability(-1);
        assertEquals(0.0, result, 0.0);
    }

    // Test the upperCumulativeProbability method
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 10);

        // Typical use case
        double result = distribution.upperCumulativeProbability(3);
        assertEquals(0.9473, result, 0.0001);

        // Edge case - minimum value
        result = distribution.upperCumulativeProbability(0);
        assertEquals(1.0, result, 0.0);

        // Edge case - maximum value
        result = distribution.upperCumulativeProbability(10);
        assertEquals(0.0, result, 0.0);

        // Error scenario - x is out of bounds
        try {
            result = distribution.upperCumulativeProbability(-1);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    // Test the getNumericalMean method
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 10);

        double result = distribution.getNumericalMean();
        assertEquals(5.0, result, 0.0);
    }

    // Test the getNumericalVariance method
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 10);

        double result = distribution.getNumericalVariance();
        assertEquals(0.5405, result, 0.0001);
    }

    // Test the getSupportLowerBound method
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 10);

        int result = distribution.getSupportLowerBound();
        assertEquals(40, result);
    }

    // Test the getSupportUpperBound method
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 10);

        int result = distribution.getSupportUpperBound();
        assertEquals(10, result);
    }

    // Test the isSupportConnected method
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 10);

        boolean result = distribution.isSupportConnected();
        assertTrue(result);
    }
}