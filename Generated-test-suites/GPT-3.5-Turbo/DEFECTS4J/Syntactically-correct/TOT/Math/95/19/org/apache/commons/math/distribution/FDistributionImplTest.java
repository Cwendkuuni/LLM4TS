import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        // Typical use case
        FDistributionImpl distribution = new FDistributionImpl(2, 3);
        double result = distribution.cumulativeProbability(0.5);
        assertEquals(0.7305, result, 0.0001);
        
        // Edge case
        distribution = new FDistributionImpl(1, 1);
        result = distribution.cumulativeProbability(100);
        assertEquals(1.0, result, 0.0001);
        
        // Error scenario
        distribution = new FDistributionImpl(0, 0);
        try {
            result = distribution.cumulativeProbability(0.5);
            fail("IllegalArgumentException must be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        // Typical use case
        FDistributionImpl distribution = new FDistributionImpl(2, 3);
        double result = distribution.inverseCumulativeProbability(0.7);
        assertEquals(1.6681, result, 0.0001);
        
        // Edge case
        distribution = new FDistributionImpl(1, 1);
        result = distribution.inverseCumulativeProbability(1);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0001);
        
        // Error scenario
        distribution = new FDistributionImpl(0, 0);
        try {
            result = distribution.inverseCumulativeProbability(0.5);
            fail("IllegalArgumentException must be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    
    @Test
    public void testGetDomainLowerBound() {
        // Typical use case
        FDistributionImpl distribution = new FDistributionImpl(2, 3);
        double result = distribution.getDomainLowerBound(0.8);
        assertEquals(0.0, result, 0.0001);
        
        // Edge case
        distribution = new FDistributionImpl(1, 1);
        result = distribution.getDomainLowerBound(0.5);
        assertEquals(0.0, result, 0.0001);
        
        // Error scenario
        distribution = new FDistributionImpl(0, 0);
        result = distribution.getDomainLowerBound(0.5);
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        // Typical use case
        FDistributionImpl distribution = new FDistributionImpl(2, 3);
        double result = distribution.getDomainUpperBound(0.8);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
        
        // Edge case
        distribution = new FDistributionImpl(1, 1);
        result = distribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
        
        // Error scenario
        distribution = new FDistributionImpl(0, 0);
        result = distribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        // Typical use case
        FDistributionImpl distribution = new FDistributionImpl(2, 3);
        double result = distribution.getInitialDomain(0.8);
        assertEquals(1.5, result, 0.0001);
        
        // Edge case
        distribution = new FDistributionImpl(1, 1);
        result = distribution.getInitialDomain(0.5);
        assertEquals(0.0, result, 0.0001);
        
        // Error scenario
        distribution = new FDistributionImpl(0, 0);
        result = distribution.getInitialDomain(0.5);
        assertEquals(-0.0, result, 0.0001);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        // Typical use case
        FDistributionImpl distribution = new FDistributionImpl(2, 3);
        distribution.setNumeratorDegreesOfFreedom(5);
        assertEquals(5, distribution.getNumeratorDegreesOfFreedom(), 0.0001);
        
        // Edge case
        distribution.setNumeratorDegreesOfFreedom(1);
        assertEquals(1, distribution.getNumeratorDegreesOfFreedom(), 0.0001);
        
        // Error scenario
        try {
            distribution.setNumeratorDegreesOfFreedom(-1);
            fail("IllegalArgumentException must be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        // Typical use case
        FDistributionImpl distribution = new FDistributionImpl(2, 3);
        distribution.setDenominatorDegreesOfFreedom(5);
        assertEquals(5, distribution.getDenominatorDegreesOfFreedom(), 0.0001);
        
        // Edge case
        distribution.setDenominatorDegreesOfFreedom(1);
        assertEquals(1, distribution.getDenominatorDegreesOfFreedom(), 0.0001);
        
        // Error scenario
        try {
            distribution.setDenominatorDegreesOfFreedom(-1);
            fail("IllegalArgumentException must be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
}