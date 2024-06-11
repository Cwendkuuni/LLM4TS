import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    // Test case for cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        // Typical use case
        assertEquals(0.003662541912181505, fDistribution.cumulativeProbability(2.0), 0.0001);
        
        // Edge case
        assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 0.0001);
        
        // Error scenario - negative input
        try {
            fDistribution.cumulativeProbability(-1.0);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Exception caught, test passed
        }

    }
    
    // Test case for inverseCumulativeProbability method
    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        // Typical use case
        assertEquals(0.9370438862688706, fDistribution.inverseCumulativeProbability(0.8), 0.0001);
        
        // Edge case - p = 0
        assertEquals(0.0, fDistribution.inverseCumulativeProbability(0.0), 0.0001);
        
        // Edge case - p = 1
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1.0), 0.0001);
        
        // Error scenario - p < 0
        try {
            fDistribution.inverseCumulativeProbability(-0.1);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Exception caught, test passed
        }
        
        // Error scenario - p > 1
        try {
            fDistribution.inverseCumulativeProbability(1.1);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Exception caught, test passed
        }
    }
    
    // Test case for getDomainLowerBound method
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        // Typical use case
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.5), 0.0001);
    }
    
    // Test case for getDomainUpperBound method
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        // Typical use case
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.5), 0.0001);
    }
    
    // Test case for getInitialDomain method
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        // Typical use case
        assertEquals(1.25, fDistribution.getInitialDomain(0.5), 0.0001);
    }
    
    // Test case for setNumeratorDegreesOfFreedom method
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        // Typical use case
        fDistribution.setNumeratorDegreesOfFreedom(7);
        assertEquals(7, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
        
        // Error scenario - negative input
        try {
            fDistribution.setNumeratorDegreesOfFreedom(-1);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Exception caught, test passed
        }
    }
    
    // Test case for setDenominatorDegreesOfFreedom method
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        // Typical use case
        fDistribution.setDenominatorDegreesOfFreedom(12);
        assertEquals(12, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
        
        // Error scenario - negative input
        try {
            fDistribution.setDenominatorDegreesOfFreedom(-1);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Exception caught, test passed
        }
    }

}