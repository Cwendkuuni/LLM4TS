import static org.junit.Assert.*;
import org.junit.Test;

public class NormalDistributionImplTest {

  @Test
  public void testGetMean() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    assertEquals(0.0, normalDistribution.getMean(), 0.0001);
  }

  @Test
  public void testSetMean() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
    normalDistribution.setMean(2.5);
    assertEquals(2.5, normalDistribution.getMean(), 0.0001);
  }

  @Test
  public void testGetStandardDeviation() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0001);
  }

  @Test
  public void testSetStandardDeviation() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
    normalDistribution.setStandardDeviation(2.0);
    assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.0001);
  }

  @Test
  public void testCumulativeProbability() throws MathException {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    assertEquals(0.5, normalDistribution.cumulativeProbability(0.0), 0.0001);
  }

  @Test
  public void testInverseCumulativeProbability() throws MathException {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0.0), 0.0001);
    assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1.0), 0.0001);
  }

  @Test
  public void testGetDomainLowerBound() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.25), 0.0001);
    assertEquals(0.0, normalDistribution.getDomainLowerBound(0.75), 0.0001);
  }

  @Test
  public void testGetDomainUpperBound() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    assertEquals(0.0, normalDistribution.getDomainUpperBound(0.25), 0.0001);
    assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.75), 0.0001);
  }

  @Test
  public void testGetInitialDomain() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    assertEquals(-1.0, normalDistribution.getInitialDomain(0.25), 0.0001);
    assertEquals(1.0, normalDistribution.getInitialDomain(0.75), 0.0001);
    assertEquals(0.0, normalDistribution.getInitialDomain(0.5), 0.0001);
  }
}