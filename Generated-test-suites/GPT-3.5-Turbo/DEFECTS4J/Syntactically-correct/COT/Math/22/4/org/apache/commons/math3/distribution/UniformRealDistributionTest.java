import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test for x = 0
        assertEquals(1.0, distribution.density(0), 0.0001);
        
        // Test for x = 0.5
        assertEquals(1.0, distribution.density(0.5), 0.0001);
        
        // Test for x = 1
        assertEquals(1.0, distribution.density(1), 0.0001);
        
        // Test for x = -1
        assertEquals(0.0, distribution.density(-1), 0.0001);
        
        // Test for x = 2
        assertEquals(0.0, distribution.density(2), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test for x = 0
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        
        // Test for x = 0.5
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.0001);
        
        // Test for x = 1
        assertEquals(1.0, distribution.cumulativeProbability(1), 0.0001);
        
        // Test for x = -1
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        
        // Test for x = 2
        assertEquals(1.0, distribution.cumulativeProbability(2), 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test mean for lower = 0 and upper = 1
        assertEquals(0.5, distribution.getNumericalMean(), 0.0001);
        
        // Test mean for lower = 1 and upper = 5
        distribution = new UniformRealDistribution(1, 5);
        assertEquals(3.0, distribution.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test variance for lower = 0 and upper = 1
        assertEquals(0.083333, distribution.getNumericalVariance(), 0.0001);
        
        // Test variance for lower = 1 and upper = 5
        distribution = new UniformRealDistribution(1, 5);
        assertEquals(2.0, distribution.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test lower bound for lower = 0 and upper = 1
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0001);
        
        // Test lower bound for lower = -5 and upper = 5
        distribution = new UniformRealDistribution(-5, 5);
        assertEquals(-5.0, distribution.getSupportLowerBound(), 0.0001);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test upper bound for lower = 0 and upper = 1
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.0001);
        
        // Test upper bound for lower = -5 and upper = 5
        distribution = new UniformRealDistribution(-5, 5);
        assertEquals(5.0, distribution.getSupportUpperBound(), 0.0001);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test if lower bound is inclusive for lower = 0 and upper = 1
        assertTrue(distribution.isSupportLowerBoundInclusive());
        
        // Test if lower bound is inclusive for lower = -5 and upper = 5
        distribution = new UniformRealDistribution(-5, 5);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        
        // Test if lower bound is not inclusive for lower = 0 and upper = 5
        distribution = new UniformRealDistribution(0, 5);
        assertFalse(distribution.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test if upper bound is exclusive for lower = 0 and upper = 1
        assertFalse(distribution.isSupportUpperBoundInclusive());
        
        // Test if upper bound is exclusive for lower = -5 and upper = 5
        distribution = new UniformRealDistribution(-5, 5);
        assertFalse(distribution.isSupportUpperBoundInclusive());
        
        // Test if upper bound is inclusive for lower = 0 and upper = 5
        distribution = new UniformRealDistribution(0, 5);
        assertTrue(distribution.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test if support is connected for lower = 0 and upper = 1
        assertTrue(distribution.isSupportConnected());
        
        // Test if support is connected for lower = -5 and upper = 5
        distribution = new UniformRealDistribution(-5, 5);
        assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        for (int i = 0; i < 100; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= 0 && sample < 1);
        }
        
        distribution = new UniformRealDistribution(-5, 5);
        
        for (int i = 0; i < 100; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= -5 && sample < 5);
        }
    }
}