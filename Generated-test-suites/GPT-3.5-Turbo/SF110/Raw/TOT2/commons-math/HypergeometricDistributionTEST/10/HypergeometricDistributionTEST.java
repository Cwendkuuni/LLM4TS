import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstructor() {
        // Typical use case
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, hypergeometric.getPopulationSize());
        assertEquals(5, hypergeometric.getNumberOfSuccesses());
        assertEquals(3, hypergeometric.getSampleSize());
        
        // Edge case: all parameters are zero
        try {
            HypergeometricDistribution hypergeometricZero = new HypergeometricDistribution(0, 0, 0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Pass
        }
        
        // Error scenario: numberOfSuccesses > populationSize
        try {
            HypergeometricDistribution hypergeometricError = new HypergeometricDistribution(10, 15, 3);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Pass
        }
    }

    // Repeat similar tests for other public methods in the HypergeometricDistribution class

    @Test
    public void testHypergeometricDistributionCalculations() {
        // Typical use case
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.252, hypergeometric.probability(2), 0.001);
        
        // Edge case: x is outside the domain
        assertEquals(0.0, hypergeometric.probability(10), 0.0);
        
        // Error scenario: x is negative
        assertEquals(0.0, hypergeometric.probability(-5), 0.0);
    }

    // Repeat similar tests for other methods in the HypergeometricDistribution class

}