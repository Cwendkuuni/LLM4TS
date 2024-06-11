import org.apache.commons.math.MathException;

import static org.junit.Assert.*;
import org.junit.Test;

public class PoissonDistributionImplTest {
    
    // Test case for probability method
    @Test
    public void testProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        
        // Test for positive input
        assertEquals(0.36787944117144233, dist.probability(1), 1e-15);
        assertEquals(0.1353352832366127, dist.probability(2), 1e-15);
        
        // Test for zero input
        assertEquals(0.36787944117144233, dist.probability(0), 1e-15);
        
        // Test for negative input
        assertEquals(0.0, dist.probability(-1), 1e-15);
    }
    
    // Test case for cumulativeProbability method
    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        
        // Test for positive input
        assertEquals(0.7357588823428847, dist.cumulativeProbability(1), 1e-15);
        assertEquals(0.9196986029286057, dist.cumulativeProbability(2), 1e-15);
        
        // Test for zero input
        assertEquals(0.36787944117144233, dist.cumulativeProbability(0), 1e-15);
        
        // Test for negative input
        assertEquals(0.0, dist.cumulativeProbability(-1), 1e-15);
    }
    
    // Test case for normalApproximateProbability method
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        
        // Test for positive input
        assertEquals(0.6536447040748788, dist.normalApproximateProbability(1), 1e-15);
        assertEquals(0.8413447460685429, dist.normalApproximateProbability(2), 1e-15);
        
        // Test for zero input
        assertEquals(0.5, dist.normalApproximateProbability(0), 1e-15);
        
        // Test for negative input
        assertEquals(0.0, dist.normalApproximateProbability(-1), 1e-15);
    }
    
    // Test case for sample method
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        
        // Test sample value
        int sample = dist.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }
    
    // Test case for getMean method
    @Test
    public void testGetMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        assertEquals(1.0, dist.getMean(), 1e-15);
    }
    
    // Test case for getDomainLowerBound method
    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        assertEquals(0, dist.getDomainLowerBound(0.5));
    }
    
    // Test case for getDomainUpperBound method
    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(0.5));
    }
}