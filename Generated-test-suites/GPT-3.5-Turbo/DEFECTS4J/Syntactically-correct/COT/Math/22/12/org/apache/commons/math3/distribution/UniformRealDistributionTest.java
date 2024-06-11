import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {
    
    // Test density method
    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test value inside the range
        assertEquals(1.0, distribution.density(0.5), 0.00001);
        
        // Test value outside the range
        assertEquals(0.0, distribution.density(1.5), 0.00001);
    }
    
    // Test cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test value less than lower bound
        assertEquals(0.0, distribution.cumulativeProbability(-0.5), 0.00001);
        
        // Test value within the range
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.00001);
        
        // Test value greater than upper bound
        assertEquals(1.0, distribution.cumulativeProbability(1.5), 0.00001);
    }
    
    // Test getNumericalMean method
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test mean calculation
        assertEquals(0.5, distribution.getNumericalMean(), 0.00001);
    }
    
    // Test getNumericalVariance method
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 2);
        
        // Test variance calculation
        assertEquals(0.333333, distribution.getNumericalVariance(), 0.00001);
    }
    
    // Test getSupportLowerBound method
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test lower bound
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.00001);
    }
    
    // Test getSupportUpperBound method
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test upper bound
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.00001);
    }
    
    // Test isSupportLowerBoundInclusive method
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test lower bound inclusivity
        assertEquals(true, distribution.isSupportLowerBoundInclusive());
    }
    
    // Test isSupportUpperBoundInclusive method
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test upper bound inclusivity
        assertEquals(false, distribution.isSupportUpperBoundInclusive());
    }
    
    // Test isSupportConnected method
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test support connectivity
        assertEquals(true, distribution.isSupportConnected());
    }
    
    // Test sample method
    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test sample within the range
        double sample = distribution.sample();
        assertTrue(sample >= 0.0);
        assertTrue(sample < 1.0);
    }
    
    // Test constructor with lower bound equal to upper bound
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithEqualBounds() {
        new UniformRealDistribution(0, 0);
    }
    
    // Test constructor with lower bound greater than upper bound
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithGreaterBounds() {
        new UniformRealDistribution(2, 1);
    }
}