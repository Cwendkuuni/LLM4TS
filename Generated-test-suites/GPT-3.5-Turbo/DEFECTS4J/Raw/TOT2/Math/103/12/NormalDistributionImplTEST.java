import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {

  @Test
  public void testCumulativeProbability() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
    try {
      assertEquals(0.5, normalDistribution.cumulativeProbability(0), 0.001);
    } catch (MathException e) {
      fail("Exception thrown");
    }
  }
  
  @Test
  public void testInverseCumulativeProbability() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
    try {
      assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0), 0.001);
      assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1), 0.001);
    } catch (MathException e) {
      fail("Exception thrown");
    }
  }
  
  @Test
  public void testSetStandardDeviation() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
    normalDistribution.setStandardDeviation(2);
    assertEquals(2, normalDistribution.getStandardDeviation(), 0.001);
  }

  @Test
  public void testInvalidStandardDeviation() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
    try {
      normalDistribution.setStandardDeviation(0);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

}