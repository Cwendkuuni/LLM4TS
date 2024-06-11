import org.junit.Assert;
import org.junit.Test;

public class UniformRealDistributionTest {
  
  @Test
  public void testDensity() {
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    
    // Test values within the range
    Assert.assertEquals(1.0, distribution.density(0.5), 0.000001);
    
    // Test values outside the range
    Assert.assertEquals(0.0, distribution.density(1.5), 0.000001);
  }
  
  @Test
  public void testCumulativeProbability() {
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    
    // Test values within the range
    Assert.assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.000001);
    
    // Test values outside the range
    Assert.assertEquals(0.0, distribution.cumulativeProbability(1.5), 0.000001);
  }
  
  @Test
  public void testGetNumericalMean() {
    UniformRealDistribution distribution = new UniformRealDistribution(1, 10);
    
    // Test the mean value
    Assert.assertEquals(5.5, distribution.getNumericalMean(), 0.000001);
  }
  
  @Test
  public void testGetNumericalVariance() {
    UniformRealDistribution distribution = new UniformRealDistribution(1, 10);
    
    // Test the variance value
    Assert.assertEquals(6.583333333333333, distribution.getNumericalVariance(), 0.000001);
  }
  
  @Test
  public void testGetSupportLowerBound() {
    UniformRealDistribution distribution = new UniformRealDistribution(1, 10);
    
    // Test the lower bound value
    Assert.assertEquals(1.0, distribution.getSupportLowerBound(), 0.000001);
  }
  
  @Test
  public void testGetSupportUpperBound() {
    UniformRealDistribution distribution = new UniformRealDistribution(1, 10);
    
    // Test the upper bound value
    Assert.assertEquals(10.0, distribution.getSupportUpperBound(), 0.000001);
  }
  
  @Test
  public void testIsSupportLowerBoundInclusive() {
    UniformRealDistribution distribution = new UniformRealDistribution(1, 10);
    
    // Test if the lower bound is inclusive
    Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
  }
  
  @Test
  public void testIsSupportUpperBoundInclusive() {
    UniformRealDistribution distribution = new UniformRealDistribution(1, 10);
    
    // Test if the upper bound is exclusive
    Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
  }
  
  @Test
  public void testIsSupportConnected() {
    UniformRealDistribution distribution = new UniformRealDistribution(1, 10);
    
    // Test if the support is connected
    Assert.assertTrue(distribution.isSupportConnected());
  }
  
  @Test
  public void testSample() {
    UniformRealDistribution distribution = new UniformRealDistribution(1, 10);
    
    // Test the sample function
    double sample = distribution.sample();
    Assert.assertTrue(sample >= 1 && sample < 10);
  }
}