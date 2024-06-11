import org.junit.Test;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        // Typical use case
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(3, distribution.getSampleSize());

        // Edge case: smallest population, successes, and sample size
        try {
            distribution = new HypergeometricDistribution(1, 0, 0);
        } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
            assertEquals("Expected exception", e.getMessage());
        }

        // Error scenario: negative number of successes
        try {
            distribution = new HypergeometricDistribution(10, -5, 3);
        } catch (NotPositiveException e) {
            assertEquals("Expected exception", e.getMessage());
        }
        
        // Error scenario: negative sample size
        try {
            distribution = new HypergeometricDistribution(10, 5, -3);
        } catch (NotPositiveException e) {
            assertEquals("Expected exception", e.getMessage());
        }

        // Error scenario: number of successes greater than population size
        try {
            distribution = new HypergeometricDistribution(10, 15, 3);
        } catch (NumberIsTooLargeException e) {
            assertEquals("Expected exception", e.getMessage());
        }
        
        // Error scenario: sample size greater than population size
        try {
            distribution = new HypergeometricDistribution(10, 5, 20);
        } catch (NumberIsTooLargeException e) {
            assertEquals("Expected exception", e.getMessage());
        }
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        // Typical use case
        double probability = distribution.cumulativeProbability(2);
        assertEquals(0.8333333333333333, probability, 0.000001);

        // Edge case: smallest x
        probability = distribution.cumulativeProbability(0);
        assertEquals(0.009523809523809523, probability, 0.000001);

        // Edge case: largest x
        probability = distribution.cumulativeProbability(3);
        assertEquals(1.0, probability, 0.000001);

        // Error scenario: x less than lower bound
        probability = distribution.cumulativeProbability(-1);
        assertEquals(0.0, probability, 0.000001);

        // Error scenario: x greater than upper bound
        probability = distribution.cumulativeProbability(5);
        assertEquals(1.0, probability, 0.000001);
    }
    
    // Same pattern as before for the remaining methods...

}