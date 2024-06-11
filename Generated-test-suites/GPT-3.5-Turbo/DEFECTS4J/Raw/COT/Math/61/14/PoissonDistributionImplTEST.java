import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.MathException;

public class PoissonDistributionImplTest {
  
  @Test
  public void testProbability() {
    PoissonDistributionImpl poisson = new PoissonDistributionImpl(2);
    Assert.assertEquals(0.2706705664732254, poisson.probability(2), 0.0001);
  }
  
  @Test
  public void testCumulativeProbability() throws MathException {
    PoissonDistributionImpl poisson = new PoissonDistributionImpl(2);
    Assert.assertEquals(0.5939941502901611, poisson.cumulativeProbability(2), 0.0001);
  }
  
  @Test
  public void testNormalApproximateProbability() throws MathException {
    PoissonDistributionImpl poisson = new PoissonDistributionImpl(2);
    Assert.assertEquals(0.6062638087591582, poisson.normalApproximateProbability(2), 0.0001);
  }
  
  @Test
  public void testSample() throws MathException {
    PoissonDistributionImpl poisson = new PoissonDistributionImpl(2);
    int value = poisson.sample();
    Assert.assertTrue(value >= 0 && value <= Integer.MAX_VALUE);
  }
  
  @Test
  public void testGetMean() {
    PoissonDistributionImpl poisson = new PoissonDistributionImpl(2);
    Assert.assertEquals(2, poisson.getMean(), 0.0001);
  }
  
  @Test
  public void testGetDomainLowerBound() {
    PoissonDistributionImpl poisson = new PoissonDistributionImpl(2);
    Assert.assertEquals(0, poisson.getDomainLowerBound(0.5), 0.0001);
  }
  
  @Test
  public void testGetDomainUpperBound() {
    PoissonDistributionImpl poisson = new PoissonDistributionImpl(2);
    Assert.assertEquals(Integer.MAX_VALUE, poisson.getDomainUpperBound(0.5), 0.0001);
  }
}