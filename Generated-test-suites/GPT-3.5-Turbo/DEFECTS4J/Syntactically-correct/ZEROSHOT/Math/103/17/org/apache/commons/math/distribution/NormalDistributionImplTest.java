import static org.junit.Assert.*;
import org.junit.Test;

public class NormalDistributionImplTest {

  @Test
  public void testGetMean() {
    NormalDistributionImpl normal = new NormalDistributionImpl(2.0, 1.0);
    assertEquals(2.0, normal.getMean(), 0.0001);
  }

  @Test
  public void testSetMean() {
    NormalDistributionImpl normal = new NormalDistributionImpl();
    normal.setMean(3.0);
    assertEquals(3.0, normal.getMean(), 0.0001);
  }

  @Test
  public void testGetStandardDeviation() {
    NormalDistributionImpl normal = new NormalDistributionImpl(2.0, 1.5);
    assertEquals(1.5, normal.getStandardDeviation(), 0.0001);
  }

  @Test
  public void testSetStandardDeviation() {
    NormalDistributionImpl normal = new NormalDistributionImpl();
    normal.setStandardDeviation(2.5);
    assertEquals(2.5, normal.getStandardDeviation(), 0.0001);
  }

  @Test
  public void testCumulativeProbability() {
    NormalDistributionImpl normal = new NormalDistributionImpl(2.0, 1.0);
    try {
      assertEquals(0.5, normal.cumulativeProbability(2.0), 0.0001);
    } catch (MathException e) {
      fail("An exception was thrown: " + e.getMessage());
    }
  }

  @Test
  public void testInverseCumulativeProbability() {
    NormalDistributionImpl normal = new NormalDistributionImpl(2.0, 1.0);
    try {
      assertEquals(2.0, normal.inverseCumulativeProbability(0.5), 0.0001);
    } catch (MathException e) {
      fail("An exception was thrown: " + e.getMessage());
    }
  }

  @Test
  public void testGetDomainLowerBound() {
    NormalDistributionImpl normal = new NormalDistributionImpl(2.0, 1.0);
    assertEquals(-Double.MAX_VALUE, normal.getDomainLowerBound(0.4), 0.0001);
  }

  @Test
  public void testGetDomainUpperBound() {
    NormalDistributionImpl normal = new NormalDistributionImpl(2.0, 1.0);
    assertEquals(Double.MAX_VALUE, normal.getDomainUpperBound(0.6), 0.0001);
  }

  @Test
  public void testGetInitialDomain() {
    NormalDistributionImpl normal = new NormalDistributionImpl(2.0, 1.0);
    assertEquals(1.0, normal.getInitialDomain(0.2), 0.0001);
  }
  
}