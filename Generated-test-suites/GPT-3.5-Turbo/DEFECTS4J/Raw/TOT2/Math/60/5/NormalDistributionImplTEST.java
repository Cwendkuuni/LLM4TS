import org.junit.Test;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {

  @Test
  public void testGetMean() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
    assertEquals(5, distribution.getMean(), 0.001);
  }

  @Test
  public void testGetStandardDeviation() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
    assertEquals(2, distribution.getStandardDeviation(), 0.001);
  }

  @Test
  public void testDensity() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
    assertEquals(0.24197072451914337, distribution.density(1), 0.0001);
  }

  @Test
  public void testCumulativeProbability() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
    try {
      assertEquals(0.5, distribution.cumulativeProbability(0), 0.0001);
    } catch (MathException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testInverseCumulativeProbability() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
    try {
      assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0), 0.0001);
      assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1), 0.0001);
    } catch (MathException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testSample() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
    try {
      double sample = distribution.sample();
      assertNotNull(sample);
    } catch (MathException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetSolverAbsoluteAccuracy() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
    assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 0.0001);
  }

  @Test
  public void testGetDomainLowerBound() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
    assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.25), 0.0001);
  }

  @Test
  public void testGetDomainUpperBound() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
    assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.75), 0.0001);
  }

  @Test
  public void testGetInitialDomain() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
    assertEquals(-1, distribution.getInitialDomain(0.25), 0.0001);
    assertEquals(1, distribution.getInitialDomain(0.75), 0.0001);
    assertEquals(0, distribution.getInitialDomain(0.5), 0.0001);
  }

}