import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        // Typical use case
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(5, distribution.getMean(), 0);
        
        // Edge cases
        distribution = new NormalDistributionImpl(Double.NEGATIVE_INFINITY, 2);
        assertEquals(Double.NEGATIVE_INFINITY, distribution.getMean(), 0);
        
        distribution = new NormalDistributionImpl(Double.POSITIVE_INFINITY, 2);
        assertEquals(Double.POSITIVE_INFINITY, distribution.getMean(), 0);
    }
    
    @Test
    public void testSetMean() {
        // Typical use case
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 2);
        distribution.setMean(5);
        assertEquals(5, distribution.getMean(), 0);
        
        // Edge cases
        distribution.setMean(Double.NEGATIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, distribution.getMean(), 0);
        
        distribution.setMean(Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, distribution.getMean(), 0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        // Typical use case
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(2, distribution.getStandardDeviation(), 0);
        
        // Edge cases
        distribution = new NormalDistributionImpl(5, Double.NEGATIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, distribution.getStandardDeviation(), 0);
        
        distribution = new NormalDistributionImpl(5, Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, distribution.getStandardDeviation(), 0);
    }
    
    @Test
    public void testSetStandardDeviation() {
        // Typical use case
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 0);
        distribution.setStandardDeviation(2);
        assertEquals(2, distribution.getStandardDeviation(), 0);
        
        // Edge cases
        distribution.setStandardDeviation(Double.NEGATIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, distribution.getStandardDeviation(), 0);
        
        distribution.setStandardDeviation(Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, distribution.getStandardDeviation(), 0);
        
        // Error scenario
        try {
            distribution.setStandardDeviation(-1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testCumulativeProbability() {
        // Typical use case
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double probability = distribution.cumulativeProbability(0);
        assertEquals(0.5, probability, 0);
        
        // Edge cases
        distribution = new NormalDistributionImpl(0, 1);
        probability = distribution.cumulativeProbability(Double.POSITIVE_INFINITY);
        assertEquals(1.0, probability, 0);
        
        distribution = new NormalDistributionImpl(0, 1);
        probability = distribution.cumulativeProbability(Double.NEGATIVE_INFINITY);
        assertEquals(0.0, probability, 0);
        
        // Error scenario
        try {
            distribution.cumulativeProbability(Double.NaN);
            fail("Expected MathException to be thrown");
        } catch (MathException e) {
            // Expected
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        // Typical use case
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double value = distribution.inverseCumulativeProbability(0.5);
        assertEquals(0.0, value, 0);
        
        // Edge cases
        distribution = new NormalDistributionImpl(0, 1);
        value = distribution.inverseCumulativeProbability(0);
        assertEquals(Double.NEGATIVE_INFINITY, value, 0);
        
        distribution = new NormalDistributionImpl(0, 1);
        value = distribution.inverseCumulativeProbability(1);
        assertEquals(Double.POSITIVE_INFINITY, value, 0);
        
        // Error scenario
        try {
            distribution.inverseCumulativeProbability(2);
            fail("Expected MathException to be thrown");
        } catch (MathException e) {
            // Expected
        }
    }
    
    @Test
    public void testGetDomainLowerBound() {
        // Typical use case
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double lowerBound = distribution.getDomainLowerBound(0.3);
        assertEquals(Double.NEGATIVE_INFINITY, lowerBound, 0);
        
        // Edge case
        distribution = new NormalDistributionImpl(0, 1);
        lowerBound = distribution.getDomainLowerBound(0);
        assertEquals(-Double.MAX_VALUE, lowerBound, 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        // Typical use case
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double upperBound = distribution.getDomainUpperBound(0.3);
        assertEquals(0, upperBound, 0);
        
        // Edge case
        distribution = new NormalDistributionImpl(0, 1);
        upperBound = distribution.getDomainUpperBound(1);
        assertEquals(Double.MAX_VALUE, upperBound, 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        // Typical use case
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double initialDomain = distribution.getInitialDomain(0.7);
        assertEquals(1, initialDomain, 0);
        
        // Edge cases
        distribution = new NormalDistributionImpl(0, 1);
        initialDomain = distribution.getInitialDomain(0);
        assertEquals(-1, initialDomain, 0);
        
        distribution = new NormalDistributionImpl(0, 1);
        initialDomain = distribution.getInitialDomain(1);
        assertEquals(1, initialDomain, 0);
    }
}