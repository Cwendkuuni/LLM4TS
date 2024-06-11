import org.junit.Test;
import org.junit.Assert;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        // Test the cumulative probability at various points
        Assert.assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        Assert.assertEquals(0.02727, distribution.cumulativeProbability(1), 0.0001);
        Assert.assertEquals(0.45455, distribution.cumulativeProbability(2), 0.0001);
        Assert.assertEquals(0.90909, distribution.cumulativeProbability(3), 0.0001);
        Assert.assertEquals(1.0, distribution.cumulativeProbability(4), 0.0001);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        // Test the probability at various points
        Assert.assertEquals(0.05454, distribution.probability(0), 0.0001);
        Assert.assertEquals(0.27273, distribution.probability(1), 0.0001);
        Assert.assertEquals(0.45455, distribution.probability(2), 0.0001);
        Assert.assertEquals(0.27273, distribution.probability(3), 0.0001);
        Assert.assertEquals(0.05454, distribution.probability(4), 0.0001);
    }
    
    @Test
    public void testNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        // Test the numerical mean
        Assert.assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        // Test the numerical variance
        Assert.assertEquals(0.94737, distribution.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        // Test the lower bound of the support
        Assert.assertEquals(0, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        // Test the upper bound of the support
        Assert.assertEquals(3, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        // Test if the support is connected
        Assert.assertTrue(distribution.isSupportConnected());
    }
    
    // Add test cases for identified edge cases and exception handling scenarios
    
}