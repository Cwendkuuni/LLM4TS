import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Beta;

public class FDistributionImplTest {

    // Test case for cumulativeProbability() method
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2, 5);
        
        // Typical use case
        double result1 = distribution.cumulativeProbability(3.0);
        assertEquals(0.7200874236010913, result1, 0.0001);
        
        // Edge case: x = 0
        double result2 = distribution.cumulativeProbability(0.0);
        assertEquals(0.0, result2, 0.0001);
        
        // Error scenario: x < 0
        try {
            double result3 = distribution.cumulativeProbability(-1.0);
            fail("Expected MathException to be thrown");
        } catch (MathException e) {
            assertEquals("No exception message specified", e.getMessage());
        }
    }
    
    // Test case for inverseCumulativeProbability() method
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2, 5);
        
        // Typical use case
        double result1 = distribution.inverseCumulativeProbability(0.5);
        assertEquals(0.9886854721359543, result1, 0.0001);
        
        // Edge case: p = 0
        double result2 = distribution.inverseCumulativeProbability(0.0);
        assertEquals(0.0, result2, 0.0001);
        
        // Edge case: p = 1
        double result3 = distribution.inverseCumulativeProbability(1.0);
        assertEquals(Double.POSITIVE_INFINITY, result3, 0.0001);
        
        // Error scenario: p < 0
        try {
            double result4 = distribution.inverseCumulativeProbability(-1.0);
            fail("Expected MathException to be thrown");
        } catch (MathException e) {
            assertEquals("No exception message specified", e.getMessage());
        }
    }
    
    // Test case for getDomainLowerBound() method
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl distribution = new FDistributionImpl(2, 5);
        
        // Typical use case
        double result1 = distribution.getDomainLowerBound(0.6);
        assertEquals(0.0, result1, 0.0001);
        
        // Edge case: p = 0
        double result2 = distribution.getDomainLowerBound(0.0);
        assertEquals(0.0, result2, 0.0001);
    }
    
    // Test case for getDomainUpperBound() method
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl distribution = new FDistributionImpl(2, 5);
        
        // Typical use case
        double result1 = distribution.getDomainUpperBound(0.6);
        assertEquals(Double.MAX_VALUE, result1, 0.0001);
        
        // Edge case: p = 0
        double result2 = distribution.getDomainUpperBound(0.0);
        assertEquals(Double.MAX_VALUE, result2, 0.0001);
    }
    
    // Test case for getInitialDomain() method
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl distribution = new FDistributionImpl(2, 5);
        
        // Typical use case
        double result1 = distribution.getInitialDomain(0.6);
        assertEquals(2.5, result1, 0.0001);
        
        // Edge case: p = 0
        double result2 = distribution.getInitialDomain(0.0);
        assertEquals(2.5, result2, 0.0001);
    }
    
    // Test case for setNumeratorDegreesOfFreedom() method
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(2, 5);
        
        // Typical use case
        distribution.setNumeratorDegreesOfFreedom(3);
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0001);
        
        // Error scenario: degreesOfFreedom <= 0
        try {
            distribution.setNumeratorDegreesOfFreedom(-1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("degrees of freedom must be positive.", e.getMessage());
        }
    }
    
    // Test case for setDenominatorDegreesOfFreedom() method
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(2, 5);
        
        // Typical use case
        distribution.setDenominatorDegreesOfFreedom(4);
        assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0001);
        
        // Error scenario: degreesOfFreedom <= 0
        try {
            distribution.setDenominatorDegreesOfFreedom(-1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("degrees of freedom must be positive.", e.getMessage());
        }
    }
}