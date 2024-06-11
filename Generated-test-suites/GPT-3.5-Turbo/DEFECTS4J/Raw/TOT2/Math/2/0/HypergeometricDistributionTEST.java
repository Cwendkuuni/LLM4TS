
import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstructor() {
        // Basic constructor test
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertNotNull(distribution);
    }

    @Test
    public void testCumulativeProbability() {
        // Testing cumulative probability for various values
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(3), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        // Testing upper cumulative probability for various values
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(3), 0.0001);
    }

    @Test
    public void testProbability() {
        // Testing probability for various values
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, distribution.probability(-1), 0.0001);
        assertEquals(0.0018, distribution.probability(2), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        // Testing numerical mean calculation
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        // Testing numerical variance calculation
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.375, distribution.getNumericalVariance(), 0.0001);
    }
}
