import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2, 3);
        
        // Test for x = 0
        double result1 = distribution.cumulativeProbability(0);
        assertEquals(0.0, result1, 0.00001);
        
        // Test for x = 1
        double result2 = distribution.cumulativeProbability(1);
        assertEquals(0.44444, result2, 0.00001);
        
        // Test for x = 5
        double result3 = distribution.cumulativeProbability(5);
        assertEquals(0.88889, result3, 0.00001);
        
        // Test for x = 10
        double result4 = distribution.cumulativeProbability(10);
        assertEquals(0.97222, result4, 0.00001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2, 3);
        
        // Test for p = 0
        double result1 = distribution.inverseCumulativeProbability(0);
        assertEquals(0, result1, 0.00001);
        
        // Test for p = 0.5
        double result2 = distribution.inverseCumulativeProbability(0.5);
        assertEquals(0.57143, result2, 0.00001);
        
        // Test for p = 0.9
        double result3 = distribution.inverseCumulativeProbability(0.9);
        assertEquals(5, result3, 0.00001);
        
        // Test for p = 1
        double result4 = distribution.inverseCumulativeProbability(1);
        assertEquals(Double.POSITIVE_INFINITY, result4, 0.00001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl distribution = new FDistributionImpl(2, 3);
        
        // Test for p = 0
        double result1 = distribution.getDomainLowerBound(0);
        assertEquals(0, result1, 0.00001);
        
        // Test for p = 0.5
        double result2 = distribution.getDomainLowerBound(0.5);
        assertEquals(0, result2, 0.00001);
        
        // Test for p = 0.9
        double result3 = distribution.getDomainLowerBound(0.9);
        assertEquals(0, result3, 0.00001);
        
        // Test for p = 1
        double result4 = distribution.getDomainLowerBound(1);
        assertEquals(0, result4, 0.00001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl distribution = new FDistributionImpl(2, 3);
        
        // Test for p = 0
        double result1 = distribution.getDomainUpperBound(0);
        assertEquals(Double.MAX_VALUE, result1, 0.00001);
        
        // Test for p = 0.5
        double result2 = distribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, result2, 0.00001);
        
        // Test for p = 0.9
        double result3 = distribution.getDomainUpperBound(0.9);
        assertEquals(Double.MAX_VALUE, result3, 0.00001);
        
        // Test for p = 1
        double result4 = distribution.getDomainUpperBound(1);
        assertEquals(Double.MAX_VALUE, result4, 0.00001);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl distribution = new FDistributionImpl(2, 3);
        
        // Test for p = 0
        double result1 = distribution.getInitialDomain(0);
        assertEquals(1.5, result1, 0.00001);
        
        // Test for p = 0.5
        double result2 = distribution.getInitialDomain(0.5);
        assertEquals(1.5, result2, 0.00001);
        
        // Test for p = 0.9
        double result3 = distribution.getInitialDomain(0.9);
        assertEquals(1.5, result3, 0.00001);
        
        // Test for p = 1
        double result4 = distribution.getInitialDomain(1);
        assertEquals(1.5, result4, 0.00001);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(2, 3);
        
        // Test for positive input
        distribution.setNumeratorDegreesOfFreedom(4);
        assertEquals(4, distribution.getNumeratorDegreesOfFreedom(), 0.00001);
        
        // Test for negative input
        try {
            distribution.setNumeratorDegreesOfFreedom(-1);
        } catch (IllegalArgumentException e) {
            assertEquals("degrees of freedom must be positive.", e.getMessage());
        }
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(2, 3);
        
        // Test for positive input
        distribution.setDenominatorDegreesOfFreedom(4);
        assertEquals(4, distribution.getDenominatorDegreesOfFreedom(), 0.00001);
        
        // Test for negative input
        try {
            distribution.setDenominatorDegreesOfFreedom(-1);
        } catch (IllegalArgumentException e) {
            assertEquals("degrees of freedom must be positive.", e.getMessage());
        }
    }
}