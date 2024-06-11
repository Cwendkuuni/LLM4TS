import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.junit.Assert;
import org.junit.Test;

public class UniformRealDistributionTest {

    private static final double EPSILON = 1e-9;

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test value within the range
        double x1 = 0.5;
        Assert.assertEquals(1, distribution.density(x1), EPSILON);
        
        // Test value outside the range
        double x2 = 1.5;
        Assert.assertEquals(0, distribution.density(x2), EPSILON);
    }
    
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test value lower than lower bound
        double x1 = -0.5;
        Assert.assertEquals(0, distribution.cumulativeProbability(x1), EPSILON);
        
        // Test value within the range
        double x2 = 0.5;
        Assert.assertEquals(0.5, distribution.cumulativeProbability(x2), EPSILON);
        
        // Test value higher than upper bound
        double x3 = 1.5;
        Assert.assertEquals(1, distribution.cumulativeProbability(x3), EPSILON);
    }
    
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1, 1e-6);
        
        Assert.assertEquals(1e-6, distribution.getSolverAbsoluteAccuracy(), EPSILON);
    }
    
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        double expectedMean = 0.5 * (0 + 1);
        Assert.assertEquals(expectedMean, distribution.getNumericalMean(), EPSILON);
    }
    
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        double expectedVariance = (1 - 0) * (1 - 0) / 12;
        Assert.assertEquals(expectedVariance, distribution.getNumericalVariance(), EPSILON);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        Assert.assertEquals(0, distribution.getSupportLowerBound(), EPSILON);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        Assert.assertEquals(1, distribution.getSupportUpperBound(), EPSILON);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        Assert.assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        double sample = distribution.sample();
        Assert.assertTrue(sample >= 0 && sample < 1);
    }
}