import static org.junit.Assert.*;
import org.junit.Test;

public class PoissonDistributionImplTest {
    
    // Test probability method
    
    @Test
    public void testProbability() {
        PoissonDistributionImpl posDist = new PoissonDistributionImpl(2.5);
        
        // Typical use case
        assertEquals(0.0668, posDist.probability(3), 0.0001);
        
        // Edge case: x = 0
        assertEquals(0.082, posDist.probability(0), 0.001);
        
        // Edge case: x = Integer.MAX_VALUE
        assertEquals(0.0, posDist.probability(Integer.MAX_VALUE), 0.0001);
        
        // Error scenario: x < 0
        assertEquals(0.0, posDist.probability(-1), 0.0001);
    }
    
    // Test cumulativeProbability method
    
    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl posDist = new PoissonDistributionImpl(2.5);
        
        // Typical use case
        assertEquals(0.543, posDist.cumulativeProbability(3), 0.001);
        
        // Edge case: x = 0
        assertEquals(0.082, posDist.cumulativeProbability(0), 0.001);
        
        // Edge case: x = Integer.MAX_VALUE
        assertEquals(1.0, posDist.cumulativeProbability(Integer.MAX_VALUE), 0.0001);
        
        // Error scenario: x < 0
        assertEquals(0.0, posDist.cumulativeProbability(-1), 0.0001);
    }
    
    // Test normalApproximateProbability method
    
    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl posDist = new PoissonDistributionImpl(2.5);
        
        // Typical use case
        assertEquals(0.933, posDist.normalApproximateProbability(3), 0.001);
        
        // Edge case: x = 0
        assertEquals(0.5, posDist.normalApproximateProbability(0), 0.0001);
        
        // Edge case: x = Integer.MAX_VALUE
        assertEquals(1.0, posDist.normalApproximateProbability(Integer.MAX_VALUE), 0.0001);
        
        // Error scenario: x < 0
        assertEquals(0.0, posDist.normalApproximateProbability(-1), 0.0001);
    }
    
    // Test sample method
    
    @Test
    public void testSample() {
        PoissonDistributionImpl posDist = new PoissonDistributionImpl(2.5);
        
        // Random test
        int sample = posDist.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }
    
    // Test getMean method
    
    @Test
    public void testGetMean() {
        PoissonDistributionImpl posDist = new PoissonDistributionImpl(2.5);
        
        // Typical use case
        assertEquals(2.5, posDist.getMean(), 0.0001);
    }
    
    // Test getDomainLowerBound method
    
    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl posDist = new PoissonDistributionImpl(2.5);
        
        // Typical use case
        assertEquals(0, posDist.getDomainLowerBound(0.0), 0.0001);
    }
    
    // Test getDomainUpperBound method
    
    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl posDist = new PoissonDistributionImpl(2.5);
        
        // Typical use case
        assertEquals(Integer.MAX_VALUE, posDist.getDomainUpperBound(0.0), 0.0001);
    }
}