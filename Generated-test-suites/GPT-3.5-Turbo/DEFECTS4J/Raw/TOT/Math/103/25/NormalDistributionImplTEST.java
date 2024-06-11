import static org.junit.Assert.*;
import org.junit.Test;

public class NormalDistributionImplTest {

  @Test
  public void testGetMean() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
    assertEquals(5.0, distribution.getMean(), 0.01);
  }

  @Test
  public void testSetMean() {
    NormalDistributionImpl distribution = new NormalDistributionImpl();
    distribution.setMean(10.0);
    assertEquals(10.0, distribution.getMean(), 0.01);
  }

  @Test
  public void testGetStandardDeviation() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
    assertEquals(2.0, distribution.getStandardDeviation(), 0.01);
  }

  @Test
  public void testSetStandardDeviation() {
    NormalDistributionImpl distribution = new NormalDistributionImpl();
    distribution.setStandardDeviation(3.0);
    assertEquals(3.0, distribution.getStandardDeviation(), 0.01);
  }

  @Test
  public void testCumulativeProbability_typicalCase() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
    double result = distribution.cumulativeProbability(1.0);
    assertEquals(0.8413, result, 0.01);
  }

  @Test
  public void testCumulativeProbability_edgeCase() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
    double result = distribution.cumulativeProbability(0.0);
    assertEquals(0.5, result, 0.01);
  }

  @Test(expected = MathException.class)
  public void testCumulativeProbability_errorCase() throws MathException {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
    double result = distribution.cumulativeProbability(30.0);
  }

  @Test
  public void testInverseCumulativeProbability_typicalCase() throws MathException {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
    double result = distribution.inverseCumulativeProbability(0.8413);
    assertEquals(1.0, result, 0.01);
  }

  @Test
  public void testInverseCumulativeProbability_edgeCase() throws MathException {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
    double result = distribution.inverseCumulativeProbability(0.0);
    assertEquals(Double.NEGATIVE_INFINITY, result, 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInverseCumulativeProbability_errorCase() throws MathException {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
    double result = distribution.inverseCumulativeProbability(1.5);
  }

  @Test
  public void testGetDomainLowerBound_typicalCase() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
    double result = distribution.getDomainLowerBound(0.3);
    assertEquals(-Double.MAX_VALUE, result, 0.01);
  }

  @Test
  public void testGetDomainLowerBound_edgeCase() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
    double result = distribution.getDomainLowerBound(0.5);
    assertEquals(0.0, result, 0.01);
  }

  @Test
  public void testGetDomainUpperBound_typicalCase() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
    double result = distribution.getDomainUpperBound(0.3);
    assertEquals(Double.MAX_VALUE, result, 0.01);
  }

  @Test
  public void testGetDomainUpperBound_edgeCase() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
    double result = distribution.getDomainUpperBound(0.5);
    assertEquals(0.0, result, 0.01);
  }

  @Test
  public void testGetInitialDomain_typicalCase() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
    double result = distribution.getInitialDomain(0.3);
    assertEquals(-1.0, result, 0.01);
  }

  @Test
  public void testGetInitialDomain_edgeCase() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
    double result = distribution.getInitialDomain(0.5);
    assertEquals(1.0, result, 0.01);
  }

}