import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        assertEquals(0.0, distribution.density(-1), 0.00001);
        assertEquals(1.0, distribution.density(0), 0.00001);
        assertEquals(0.0, distribution.density(1), 0.00001);
        assertEquals(0.0, distribution.density(2), 0.00001);
        assertEquals(0.5, distribution.density(0.5), 0.00001);
    }
    
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.00001);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.00001);
        assertEquals(1.0, distribution.cumulativeProbability(1), 0.00001);
        assertEquals(1.0, distribution.cumulativeProbability(2), 0.00001);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.00001);
    }
    
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 2);
        
        assertEquals(1.0, distribution.getNumericalMean(), 0.00001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 2);
        
        assertEquals(0.33333, distribution.getNumericalVariance(), 0.00001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(-5, 5);
        
        assertEquals(-5.0, distribution.getSupportLowerBound(), 0.00001);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(-5, 5);
        
        assertEquals(5.0, distribution.getSupportUpperBound(), 0.00001);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        double sample = distribution.sample();
        
        assertTrue(sample >= 0 && sample < 1);
    }
}