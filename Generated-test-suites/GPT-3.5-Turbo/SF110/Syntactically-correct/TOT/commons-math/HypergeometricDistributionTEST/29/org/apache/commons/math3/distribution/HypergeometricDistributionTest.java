import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        // Typical use case
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
            assertNotNull(distribution);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Edge case: Population size is 0
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(0, 5, 3);
            fail("Expected NotStrictlyPositiveException not thrown");
        } catch (NotPositiveException e) {
            assertEquals("Population size must be positive", e.getMessage());
        } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        
        // Edge case: Number of successes is negative
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, -5, 3);
            fail("Expected NotPositiveException not thrown");
        } catch (NotPositiveException e) {
            assertEquals("Number of successes must be non-negative", e.getMessage());
        } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        
        // Edge case: Sample size is negative
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, -3);
            fail("Expected NotPositiveException not thrown");
        } catch (NotPositiveException e) {
            assertEquals("Sample size must be non-negative", e.getMessage());
        } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        
        // Edge case: Number of successes is greater than population size
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, 15, 3);
            fail("Expected NumberIsTooLargeException not thrown");
        } catch (NumberIsTooLargeException e) {
            assertEquals("Number of successes must be less than or equal to population size", e.getMessage());
        } catch (NotPositiveException | NotStrictlyPositiveException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        
        // Edge case: Sample size is greater than population size
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 15);
            fail("Expected NumberIsTooLargeException not thrown");
        } catch (NumberIsTooLargeException e) {
            assertEquals("Sample size must be less than or equal to population size", e.getMessage());
        } catch (NotPositiveException | NotStrictlyPositiveException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        // Typical use case
        assertEquals(0.16666666666666666, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.6666666666666666, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(3), 0.0001);
        
        // Edge case: x is less than lower domain
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        
        // Edge case: x is greater than or equal to upper domain
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.0001);
    }
    
    @Test
    public void testGetDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        // Typical use case
        int[] domain = distribution.getDomain(7, 4, 2);
        assertEquals(2, domain[0]);
        assertEquals(4, domain[1]);
        
        // Edge case: n is less than m
        domain = distribution.getDomain(3, 4, 2);
        assertEquals(2, domain[0]);
        assertEquals(3, domain[1]);
    }
    
    // Test cases for other methods...

}