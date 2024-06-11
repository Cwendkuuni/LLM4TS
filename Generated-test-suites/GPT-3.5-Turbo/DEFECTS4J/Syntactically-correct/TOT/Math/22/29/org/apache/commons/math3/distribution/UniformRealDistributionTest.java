import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.UniformRealDistribution;

public class UniformRealDistributionTest {
    private UniformRealDistribution distribution;
    
    @Before
    public void setup() {
        distribution = new UniformRealDistribution(0, 1);
    }
    
    @Test
    public void testDensity() {
        // Typical use case
        Assert.assertEquals(1.0, distribution.density(0.5), 0.00001);
        
        // Edge case
        Assert.assertEquals(0.0, distribution.density(-0.1), 0.00001);
        Assert.assertEquals(0.0, distribution.density(1.1), 0.00001);
        
        // Error scenario
        Assert.assertEquals(0.0, distribution.density(Double.POSITIVE_INFINITY), 0.00001);
        Assert.assertEquals(0.0, distribution.density(Double.NEGATIVE_INFINITY), 0.00001);
        Assert.assertEquals(0.0, distribution.density(Double.NaN), 0.00001);
    }
    
    @Test
    public void testCumulativeProbability() {
        // Typical use case
        Assert.assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.00001);
        
        // Edge case
        Assert.assertEquals(0.0, distribution.cumulativeProbability(-0.1), 0.00001);
        Assert.assertEquals(1.0, distribution.cumulativeProbability(1.1), 0.00001);
        
        // Error scenario
        Assert.assertEquals(0.0, distribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0.00001);
        Assert.assertEquals(0.0, distribution.cumulativeProbability(Double.NEGATIVE_INFINITY), 0.00001);
        Assert.assertEquals(0.0, distribution.cumulativeProbability(Double.NaN), 0.00001);
    }
    
    @Test
    public void testNumericalMean() {
        // Typical use case
        Assert.assertEquals(0.5, distribution.getNumericalMean(), 0.00001);
        
        // Edge case
        UniformRealDistribution distribution2 = new UniformRealDistribution(5, 6);
        Assert.assertEquals(5.5, distribution2.getNumericalMean(), 0.00001);
    }
    
    @Test
    public void testNumericalVariance() {
        // Typical use case
        Assert.assertEquals(0.0833333333333333, distribution.getNumericalVariance(), 0.00001);
        
        // Edge case
        UniformRealDistribution distribution2 = new UniformRealDistribution(5, 6);
        Assert.assertEquals(0.0833333333333333, distribution2.getNumericalVariance(), 0.00001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        // Typical use case
        Assert.assertEquals(0.0, distribution.getSupportLowerBound(), 0.00001);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        // Typical use case
        Assert.assertEquals(1.0, distribution.getSupportUpperBound(), 0.00001);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        // Typical use case
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        // Typical use case
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        // Typical use case
        Assert.assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testSample() {
        // Typical use case
        double value = distribution.sample();
        Assert.assertTrue(value >= 0.0 && value < 1.0);
    }
}