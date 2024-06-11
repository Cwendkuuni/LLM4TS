import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

  @Test
  public void testGetMean() {
    NormalDistributionImpl dist = new NormalDistributionImpl(10, 2);
    Assert.assertEquals(10, dist.getMean(), 0);
  }

  @Test
  public void testGetStandardDeviation() {
    NormalDistributionImpl dist = new NormalDistributionImpl(10, 2);
    Assert.assertEquals(2, dist.getStandardDeviation(), 0);
  }

  @Test
  public void testDensity() {
    NormalDistributionImpl dist = new NormalDistributionImpl(10, 2);
    Assert.assertEquals(0.19947114020071635, dist.density(1), 0.0001);
  }

  @Test
  public void testCumulativeProbability() throws MathException {
    NormalDistributionImpl dist = new NormalDistributionImpl(10, 2);
    Assert.assertEquals(0.022750131948179195, dist.cumulativeProbability(8), 0.0001);
  }

  @Test
  public void testInverseCumulativeProbability() throws MathException {
    NormalDistributionImpl dist = new NormalDistributionImpl(10, 2);
    Assert.assertEquals(10.614513285055528, dist.inverseCumulativeProbability(0.8), 0.0001);
  }

  @Test
  public void testSample() throws MathException {
    NormalDistributionImpl dist = new NormalDistributionImpl(10, 2);
    double value = dist.sample();
    Assert.assertTrue(value >= 4 && value <= 16);
  }

  @Test
  public void testGetSolverAbsoluteAccuracy() {
    NormalDistributionImpl dist = new NormalDistributionImpl(10, 2);
    Assert.assertEquals(1e-9, dist.getSolverAbsoluteAccuracy(), 0);
  }

  @Test
  public void testGetDomainLowerBound() {
    NormalDistributionImpl dist = new NormalDistributionImpl(10, 2);
    Assert.assertEquals(-Double.MAX_VALUE, dist.getDomainLowerBound(0.2), 0);
  }

  @Test
  public void testGetDomainUpperBound() {
    NormalDistributionImpl dist = new NormalDistributionImpl(10, 2);
    Assert.assertEquals(Double.MAX_VALUE, dist.getDomainUpperBound(0.7), 0);
  }

  @Test
  public void testGetInitialDomain() {
    NormalDistributionImpl dist = new NormalDistributionImpl(10, 2);
    Assert.assertEquals(12, dist.getInitialDomain(0.6), 0);
  }

}