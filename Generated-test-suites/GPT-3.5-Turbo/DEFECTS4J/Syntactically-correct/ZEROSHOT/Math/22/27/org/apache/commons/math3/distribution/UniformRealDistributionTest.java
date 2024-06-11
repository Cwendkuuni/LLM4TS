import static org.junit.Assert.*;
import org.junit.Test;

public class UniformRealDistributionTest {

    // Test density method
    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // test value within range
        assertEquals(1.0, distribution.density(0.5), 0.0001);
        
        // test value outside of range
        assertEquals(0.0, distribution.density(-1), 0.0001);
        assertEquals(0.0, distribution.density(2), 0.0001);
    }
    
    // Test cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // test value within range
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.0001);
        
        // test value below range
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        
        // test value above range
        assertEquals(1.0, distribution.cumulativeProbability(2), 0.0001);
    }
    
    // Test getNumericalMean method
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        assertEquals(0.5, distribution.getNumericalMean(), 0.0001);
    }
    
    // Test getNumericalVariance method
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        assertEquals(0.083333, distribution.getNumericalVariance(), 0.0001);
    }
    
    // Test getSupportLowerBound method
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        assertEquals(0, distribution.getSupportLowerBound(), 0.0001);
    }
    
    // Test getSupportUpperBound method
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        assertEquals(1, distribution.getSupportUpperBound(), 0.0001);
    }
    
    // Test isSupportLowerBoundInclusive method
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }
    
    // Test isSupportUpperBoundInclusive method
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }
    
    // Test isSupportConnected method
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        assertTrue(distribution.isSupportConnected());
    }
    
    // Test sample method
    @Test
    public void testSample() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution distribution = new UniformRealDistribution(rng, 0, 1);
        
        // check if sample value is within range
        double sample = distribution.sample();
        assertTrue(sample >= 0 && sample < 1);
    }
}