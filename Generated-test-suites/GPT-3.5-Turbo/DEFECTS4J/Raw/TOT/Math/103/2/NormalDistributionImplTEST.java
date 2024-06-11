import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

  private static final double EPSILON = 1e-10;

  // Test case for getMean() method
  @Test
  public void testGetMean() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10.0, 2.0);
    assertEquals(10.0, normalDistribution.getMean(), EPSILON);
  }

  // Test case for setMean() method
  @Test
  public void testSetMean() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10.0, 2.0);
    normalDistribution.setMean(15.0);
    assertEquals(15.0, normalDistribution.getMean(), EPSILON);
  }

  // Test case for getStandardDeviation() method
  @Test
  public void testGetStandardDeviation() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10.0, 2.0);
    assertEquals(2.0, normalDistribution.getStandardDeviation(), EPSILON);
  }

  // Test case for setStandardDeviation() method with valid input
  @Test
  public void testSetStandardDeviationValid() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10.0, 2.0);
    normalDistribution.setStandardDeviation(3.0);
    assertEquals(3.0, normalDistribution.getStandardDeviation(), EPSILON);
  }

  // Test case for setStandardDeviation() method with invalid input (<=0)
  @Test(expected = IllegalArgumentException.class)
  public void testSetStandardDeviationInvalid() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10.0, 2.0);
    normalDistribution.setStandardDeviation(-1.0);
  }

  // Test case for cumulativeProbability() method with typical positive input
  @Test
  public void testCumulativeProbabilityTypicalPositive() throws MathException {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    assertEquals(0.1586552539314571, normalDistribution.cumulativeProbability(-1.0), EPSILON);
  }

  // Test case for cumulativeProbability() method with typical negative input
  @Test
  public void testCumulativeProbabilityTypicalNegative() throws MathException {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    assertEquals(0.8413447460685429, normalDistribution.cumulativeProbability(1.0), EPSILON);
  }

  // Test case for cumulativeProbability() method with edge input (x = 0)
  @Test
  public void testCumulativeProbabilityEdge() throws MathException {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    assertEquals(0.5, normalDistribution.cumulativeProbability(0.0), EPSILON);
  }

  // Test case for inverseCumulativeProbability() method with typical probability
  @Test
  public void testInverseCumulativeProbabilityTypical() throws MathException {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    assertEquals(0.6744897501960817, normalDistribution.inverseCumulativeProbability(0.75), EPSILON);
  }

  // Test case for inverseCumulativeProbability() method with probability = 0
  @Test
  public void testInverseCumulativeProbabilityZero() throws MathException {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0.0), EPSILON);
  }

  // Test case for inverseCumulativeProbability() method with probability = 1
  @Test
  public void testInverseCumulativeProbabilityOne() throws MathException {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1.0), EPSILON);
  }
  
  // Test case for getDomainLowerBound() method with p < 0.5
  @Test
  public void testGetDomainLowerBoundPLessThanHalf() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.25), EPSILON);
  }

  // Test case for getDomainLowerBound() method with p >= 0.5
  @Test
  public void testGetDomainLowerBoundPGreaterOrEqualToHalf() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    assertEquals(0.0, normalDistribution.getDomainLowerBound(0.75), EPSILON);
  }

  // Test case for getDomainUpperBound() method with p < 0.5
  @Test
  public void testGetDomainUpperBoundPLessThanHalf() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    assertEquals(0.0, normalDistribution.getDomainUpperBound(0.25), EPSILON);
  }

  // Test case for getDomainUpperBound() method with p >= 0.5
  @Test
  public void testGetDomainUpperBoundPGreaterOrEqualToHalf() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.75), EPSILON);
  }

  // Test case for getInitialDomain() method with p < 0.5
  @Test
  public void testGetInitialDomainPLessThanHalf() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    assertEquals(-1.0, normalDistribution.getInitialDomain(0.25), EPSILON);
  }

  // Test case for getInitialDomain() method with p > 0.5
  @Test
  public void testGetInitialDomainPGreaterThanHalf() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    assertEquals(1.0, normalDistribution.getInitialDomain(0.75), EPSILON);
  }

}