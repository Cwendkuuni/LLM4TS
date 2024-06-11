// Import the necessary packages
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {
    
    // Helper method to compare two doubles with a given tolerance
    private boolean compareDoubles(double expected, double actual, double tolerance) {
        return Math.abs(expected - actual) <= tolerance;
    }
    
    // Test the cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        
        // Test values below the lower bound
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        
        // Test values within the bounds
        assertEquals(0.0909, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.6364, distribution.cumulativeProbability(2), 0.0001);
        
        // Test values above the upper bound
        assertEquals(1.0, distribution.cumulativeProbability(3), 0.0001);
    }
    
    // Test the probability method
    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        
        // Test values below the lower bound
        assertEquals(0.0, distribution.probability(0), 0.0001);
        
        // Test values within the bounds
        assertEquals(0.0909, distribution.probability(1), 0.0001);
        assertEquals(0.4545, distribution.probability(2), 0.0001);
        
        // Test values above the upper bound
        assertEquals(0.0, distribution.probability(3), 0.0001);
    }
    
    // Test the upperCumulativeProbability method
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        
        // Test values below the lower bound
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        
        // Test values within the bounds
        assertEquals(0.9091, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.3636, distribution.upperCumulativeProbability(2), 0.0001);
        
        // Test values above the upper bound
        assertEquals(0.0, distribution.upperCumulativeProbability(3), 0.0001);
    }
    
    // Test the getNumericalMean method
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        
        // Test the mean value
        assertEquals(1.0, distribution.getNumericalMean(), 0.0001);
    }
    
    // Test the getNumericalVariance method
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        
        // Test the variance value
        assertEquals(0.4268, distribution.getNumericalVariance(), 0.0001);
    }
    
    // Test the getSupportLowerBound method
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        
        // Test the lower bound value
        assertEquals(2, distribution.getSupportLowerBound());
    }
    
    // Test the getSupportUpperBound method
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        
        // Test the upper bound value
        assertEquals(2, distribution.getSupportUpperBound());
    }
    
    // Test the isSupportConnected method
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        
        // Test the connectedness of the support
        assertTrue(distribution.isSupportConnected());
    }
}