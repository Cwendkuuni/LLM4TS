import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.junit.Assert;
import org.junit.Test;

public class UniformRealDistributionTest {
  
  @Test
  public void testDensity() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    Assert.assertEquals(1.0, dist.density(0.5), 1e-9);
    Assert.assertEquals(0.0, dist.density(-1), 1e-9);
    Assert.assertEquals(0.0, dist.density(2), 1e-9);
  }
  
  @Test
  public void testCumulativeProbability() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    Assert.assertEquals(0.5, dist.cumulativeProbability(0.5), 1e-9);
    Assert.assertEquals(0.0, dist.cumulativeProbability(-1), 1e-9);
    Assert.assertEquals(1.0, dist.cumulativeProbability(2), 1e-9);
  }
  
  @Test
  public void testGetNumericalMean() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    Assert.assertEquals(0.5, dist.getNumericalMean(), 1e-9);
  }
  
  @Test
  public void testGetNumericalVariance() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    Assert.assertEquals(1.0/12, dist.getNumericalVariance(), 1e-9);
  }
  
  @Test
  public void testGetSupportLowerBound() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    Assert.assertEquals(0.0, dist.getSupportLowerBound(), 1e-9);
  }
  
  @Test
  public void testGetSupportUpperBound() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    Assert.assertEquals(1.0, dist.getSupportUpperBound(), 1e-9);
  }
  
  @Test
  public void testIsSupportLowerBoundInclusive() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    Assert.assertTrue(dist.isSupportLowerBoundInclusive());
  }
  
  @Test
  public void testIsSupportUpperBoundInclusive() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    Assert.assertFalse(dist.isSupportUpperBoundInclusive());
  }
  
  @Test
  public void testIsSupportConnected() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    Assert.assertTrue(dist.isSupportConnected());
  }
  
  @Test
  public void testSample() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    double sample = dist.sample();
    Assert.assertTrue(sample >= 0 && sample <= 1);
  }
  
}