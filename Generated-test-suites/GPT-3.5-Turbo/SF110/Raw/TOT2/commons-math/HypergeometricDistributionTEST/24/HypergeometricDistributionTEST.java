import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        // Test constructor with valid input
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(3, distribution.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        // Test cumulative probability for different values of x
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(10), 0.0001);
        assertEquals(0.0625, distribution.cumulativeProbability(2), 0.0001);
    }

    @Test
    public void testProbability() {
        // Test probability for different values of x
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, distribution.probability(-1), 0.0001);
        assertEquals(0.0, distribution.probability(11), 0.0001);
        assertEquals(0.3968, distribution.probability(2), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        // Test upper cumulative probability for different values of x
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(11), 0.0001);
        assertEquals(0.9375, distribution.upperCumulativeProbability(8), 0.0001);
    }

    @Test
    public void testNumericalMean() {
        // Test numerical mean calculation
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testNumericalVariance() {
        // Test numerical variance calculation
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.6428, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testSupportLowerBound() {
        // Test lower bound of support
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testSupportUpperBound() {
        // Test upper bound of support
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        // Test if support is connected
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertTrue(distribution.isSupportConnected());
    }
}