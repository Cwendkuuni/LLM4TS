import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

  NormalDistributionImpl distribution = new NormalDistributionImpl();

  @Test
  public void testGetMean() {
    double mean = distribution.getMean();
    assertEquals(0.0, mean, 0.0);
  }

  @Test
  public void testSetMean() {
    distribution.setMean(5.0);
    double mean = distribution.getMean();
    assertEquals(5.0, mean, 0.0);
  }

  @Test
  public void testGetStandardDeviation() {
    double sd = distribution.getStandardDeviation();
    assertEquals(1.0, sd, 0.0);
  }

  @Test
  public void testSetStandardDeviation() {
    distribution.setStandardDeviation(2.0);
    double sd = distribution.getStandardDeviation();
    assertEquals(2.0, sd, 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetStandardDeviationNegative() {
    distribution.setStandardDeviation(-1.0);
  }

  @Test
  public void testCumulativeProbability() {
    double cumulativeProbability = distribution.cumulativeProbability(0.0);
    assertEquals(0.5, cumulativeProbability, 0.0);
  }

  @Test
  public void testInverseCumulativeProbability() {
    double inverseCumulativeProbability = distribution.inverseCumulativeProbability(0.5);
    assertEquals(0.0, inverseCumulativeProbability, 0.0);
  }

  @Test(expected = MathException.class)
  public void testInverseCumulativeProbabilityInvalidP() throws MathException {
    distribution.inverseCumulativeProbability(-1.0);
  }

  @Test
  public void testGetDomainLowerBound() {
    double domainLowerBound = distribution.getDomainLowerBound(0.1);
    assertEquals(Double.NEGATIVE_INFINITY, domainLowerBound, 0.0);
  }

  @Test
  public void testGetDomainUpperBound() {
    double domainUpperBound = distribution.getDomainUpperBound(0.1);
    assertEquals(Double.MAX_VALUE, domainUpperBound, 0.0);
  }

  @Test
  public void testGetInitialDomain() {
    double initialDomain = distribution.getInitialDomain(0.1);
    assertEquals(-1.0, initialDomain, 0.0);
  }

}