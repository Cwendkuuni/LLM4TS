import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {
  
  private NormalDistributionImpl normalDist = new NormalDistributionImpl(1.5, 2.0);
  
  @Test
  public void testGetMean() {
    assertEquals(1.5, normalDist.getMean(), 0.0);
  }
  
  @Test
  public void testSetMean() {
    normalDist.setMean(2.0);
    assertEquals(2.0, normalDist.getMean(), 0.0);
  }
  
  @Test
  public void testGetStandardDeviation() {
    assertEquals(2.0, normalDist.getStandardDeviation(), 0.0);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetStandardDeviation_Negative() {
    normalDist.setStandardDeviation(-2.0);
  }
  
  @Test
  public void testSetStandardDeviation_Positive() {
    normalDist.setStandardDeviation(3.0);
    assertEquals(3.0, normalDist.getStandardDeviation(), 0.0);
  }
  
  @Test
  public void testCumulativeProbability() {
    try {
      assertEquals(0.694575, normalDist.cumulativeProbability(2.0), 0.000001);
    } catch (MathException e) {
      fail("MathException occurred during cumulativeProbability.");
    }
  }
  
  @Test
  public void testInverseCumulativeProbability_Zero() {
    try {
      assertEquals(Double.NEGATIVE_INFINITY, normalDist.inverseCumulativeProbability(0.0), 0.0);
    } catch (MathException e) {
      fail("MathException occurred during inverseCumulativeProbability.");
    }
  }
  
  @Test
  public void testInverseCumulativeProbability_One() {
    try {
      assertEquals(Double.POSITIVE_INFINITY, normalDist.inverseCumulativeProbability(1.0), 0.0);
    } catch (MathException e) {
      fail("MathException occurred during inverseCumulativeProbability.");
    }
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testInverseCumulativeProbability_InvalidProbability() {
    try {
      normalDist.inverseCumulativeProbability(2.0);
    } catch (MathException e) {
      fail("MathException occurred during inverseCumulativeProbability.");
    }
  }
  
  @Test
  public void testGetDomainLowerBound() {
    assertEquals(-Double.MAX_VALUE, normalDist.getDomainLowerBound(0.4), 0.0);
  }
  
  @Test
  public void testGetDomainUpperBound() {
    assertEquals(Double.MAX_VALUE, normalDist.getDomainUpperBound(0.6), 0.0);
  }
  
  @Test
  public void testGetInitialDomain() {
    assertEquals(-0.5, normalDist.getInitialDomain(0.4), 0.0);
  }
  
}