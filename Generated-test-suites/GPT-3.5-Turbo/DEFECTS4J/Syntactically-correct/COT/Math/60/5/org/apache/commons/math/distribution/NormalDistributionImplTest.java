import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

  // Test getMean() method
  @Test
  public void testGetMean() {
    NormalDistributionImpl dist = new NormalDistributionImpl(10, 5);
    assertEquals(10, dist.getMean(), 0);
  }

  // Test getStandardDeviation() method
  @Test
  public void testGetStandardDeviation() {
    NormalDistributionImpl dist = new NormalDistributionImpl(10, 5);
    assertEquals(5, dist.getStandardDeviation(), 0);
  }

  // Test density() method
  @Test
  public void testDensity() {
    NormalDistributionImpl dist = new NormalDistributionImpl(10, 5);
    assertEquals(0.019498, dist.density(5), 0.000001);
  }

  // Test cumulativeProbability() method
  @Test
  public void testCumulativeProbability() {
    NormalDistributionImpl dist = new NormalDistributionImpl(10, 5);
    assertEquals(0.158655, dist.cumulativeProbability(5), 0.000001);
  }

  // Test inverseCumulativeProbability() method
  @Test
  public void testInverseCumulativeProbability() throws MathException {
    NormalDistributionImpl dist = new NormalDistributionImpl(10, 5);
    assertEquals(5, dist.inverseCumulativeProbability(0.158655), 0.000001);
  }

  // Test sample() method
  @Test
  public void testSample() throws MathException {
    NormalDistributionImpl dist = new NormalDistributionImpl(10, 5);
    double sample = dist.sample();
    assertTrue(sample >= -Double.MAX_VALUE && sample <= Double.MAX_VALUE);
  }

  // Test getDomainLowerBound() method
  @Test
  public void testGetDomainLowerBound() {
    NormalDistributionImpl dist = new NormalDistributionImpl(10, 5);
    assertEquals(-Double.MAX_VALUE, dist.getDomainLowerBound(0.1), 0);
  }

  // Test getDomainUpperBound() method
  @Test
  public void testGetDomainUpperBound() {
    NormalDistributionImpl dist = new NormalDistributionImpl(10, 5);
    assertEquals(Double.MAX_VALUE, dist.getDomainUpperBound(0.1), 0);
  }

  // Test getInitialDomain() method
  @Test
  public void testGetInitialDomain() {
    NormalDistributionImpl dist = new NormalDistributionImpl(10, 5);
    assertEquals(5, dist.getInitialDomain(0.5), 0);
  }
}