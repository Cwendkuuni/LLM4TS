import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;

public class NormalDistributionImplTest {

  private static final double DELTA = 1e-15;
  private NormalDistributionImpl normalDist;

  @Before
  public void setUp() {
    normalDist = new NormalDistributionImpl();
  }

  @Test
  public void testGetMean() {
    assertEquals(0.0, normalDist.getMean(), DELTA);
  }

  @Test
  public void testSetMean() {
    normalDist.setMean(1.25);
    assertEquals(1.25, normalDist.getMean(), DELTA);
  }

  @Test
  public void testInvalidSetMean() {
    try {
      normalDist.setMean(Double.NaN);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
  }

  @Test
  public void testGetStandardDeviation() {
    assertEquals(1.0, normalDist.getStandardDeviation(), DELTA);
  }

  @Test
  public void testSetStandardDeviation() {
    normalDist.setStandardDeviation(2.5);
    assertEquals(2.5, normalDist.getStandardDeviation(), DELTA);
  }

  @Test
  public void testInvalidSetStandardDeviation() {
    try {
      normalDist.setStandardDeviation(0.0);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
  }

  @Test
  public void testCumulativeProbability() {
    try {
      double prob = normalDist.cumulativeProbability(0.0);
      assertEquals(0.5, prob, DELTA);
    } catch (MathException e) {
      fail("Failed to calculate cumulative probability");
    }
  }

  @Test
  public void testInverseCumulativeProbability() {
    try {
      double prob = normalDist.inverseCumulativeProbability(0.5);
      assertEquals(normalDist.getMean(), prob, DELTA);
    } catch (MathException e) {
      fail("Failed to calculate inverse cumulative probability");
    }
  }

  @Test
  public void testInverseCumulativeProbabilityMin() {
    try {
      double prob = normalDist.inverseCumulativeProbability(0.0);
      assertEquals(Double.NEGATIVE_INFINITY, prob, DELTA);
    } catch (MathException e) {
      fail("Failed to calculate inverse cumulative probability");
    }
  }

  @Test
  public void testInverseCumulativeProbabilityMax() {
    try {
      double prob = normalDist.inverseCumulativeProbability(1.0);
      assertEquals(Double.POSITIVE_INFINITY, prob, DELTA);
    } catch (MathException e) {
      fail("Failed to calculate inverse cumulative probability");
    }
  }

  @Test
  public void testGetDomainLowerBound() {
    double lowerBound = normalDist.getDomainLowerBound(0.4);
    assertEquals(-Double.MAX_VALUE, lowerBound, DELTA);
  }

  @Test
  public void testGetDomainUpperBound() {
    double upperBound = normalDist.getDomainUpperBound(0.6);
    assertEquals(Double.MAX_VALUE, upperBound, DELTA);
  }

  @Test
  public void testGetInitialDomain() {
    double initialDomain = normalDist.getInitialDomain(0.5);
    assertEquals(normalDist.getMean(), initialDomain, DELTA);
  }
}