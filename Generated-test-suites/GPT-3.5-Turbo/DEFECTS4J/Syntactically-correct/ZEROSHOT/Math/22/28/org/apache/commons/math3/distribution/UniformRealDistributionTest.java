import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.junit.Assert;
import org.junit.Test;

public class UniformRealDistributionTest {
    
    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test values less than lower bound
        Assert.assertEquals(0.0, distribution.density(-0.5), 0.00001);
        
        // Test values greater than upper bound
        Assert.assertEquals(0.0, distribution.density(1.5), 0.00001);
        
        // Test values between lower bound and upper bound
        Assert.assertEquals(1.0, distribution.density(0.5), 0.00001);
        
        // Test lower bound
        Assert.assertEquals(1.0, distribution.density(0.0), 0.00001);
        
        // Test upper bound
        Assert.assertEquals(1.0, distribution.density(1.0), 0.00001);
    }
    
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test values less than lower bound
        Assert.assertEquals(0.0, distribution.cumulativeProbability(-0.5), 0.00001);
        
        // Test values greater than upper bound
        Assert.assertEquals(1.0, distribution.cumulativeProbability(1.5), 0.00001);
        
        // Test values between lower bound and upper bound
        Assert.assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.00001);
        
        // Test lower bound
        Assert.assertEquals(0.0, distribution.cumulativeProbability(0.0), 0.00001);
        
        // Test upper bound
        Assert.assertEquals(1.0, distribution.cumulativeProbability(1.0), 0.00001);
    }
    
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test mean
        Assert.assertEquals(0.5, distribution.getNumericalMean(), 0.00001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test variance
        Assert.assertEquals(0.083333333, distribution.getNumericalVariance(), 0.00001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test lower bound
        Assert.assertEquals(0.0, distribution.getSupportLowerBound(), 0.00001);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test upper bound
        Assert.assertEquals(1.0, distribution.getSupportUpperBound(), 0.00001);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test lower bound inclusive
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test upper bound exclusive
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test support connected
        Assert.assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test sample range
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            Assert.assertTrue(sample >= 0.0 && sample <= 1.0);
        }
    }
}