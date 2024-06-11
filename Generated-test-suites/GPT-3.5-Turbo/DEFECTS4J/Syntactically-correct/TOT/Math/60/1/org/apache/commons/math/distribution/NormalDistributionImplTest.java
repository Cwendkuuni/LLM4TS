import static org.junit.Assert.*;
import org.junit.Test;

public class NormalDistributionImplTest {

  // Test case for constructor with mean and standard deviation
  @Test
  public void testConstructorMeanAndStandardDeviation() {
    NormalDistributionImpl normal = new NormalDistributionImpl(5, 3);
    assertEquals(5, normal.getMean(), 0.001);
    assertEquals(3, normal.getStandardDeviation(), 0.001);
  }

  // Test case for constructor with mean, standard deviation, and inverseCumAccuracy
  @Test
  public void testConstructorMeanStandardDeviationInverseCumAccuracy() {
    NormalDistributionImpl normal = new NormalDistributionImpl(7, 4, 1e-5);
    assertEquals(7, normal.getMean(), 0.001);
    assertEquals(4, normal.getStandardDeviation(), 0.001);
  }

  // Test case for constructor with default mean and standard deviation
  @Test
  public void testConstructorDefaultMeanAndStandardDeviation() {
    NormalDistributionImpl normal = new NormalDistributionImpl();
    assertEquals(0, normal.getMean(), 0.001);
    assertEquals(1, normal.getStandardDeviation(), 0.001);
  }

  // Test case for density method
  @Test
  public void testDensity() {
    NormalDistributionImpl normal = new NormalDistributionImpl(2, 2);
    assertEquals(0.006, normal.density(1), 0.001);
  }

  // Test case for cumulativeProbability method - x < mean-20*stdDev
  @Test
  public void testCumulativeProbabilityBound1() throws MathException {
    NormalDistributionImpl normal = new NormalDistributionImpl(5, 1);
    assertEquals(0, normal.cumulativeProbability(-25), 0.001);
  }

  // Test case for cumulativeProbability method - mean+20*stdDev < x
  @Test
  public void testCumulativeProbabilityBound2() throws MathException {
    NormalDistributionImpl normal = new NormalDistributionImpl(5, 1);
    assertEquals(1, normal.cumulativeProbability(25), 0.001);
  }

  // Test case for inverseCumulativeProbability method
  @Test
  public void testInverseCumulativeProbability() throws MathException {
    NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
    assertEquals(-1.959964, normal.inverseCumulativeProbability(0.025), 0.001);
  }

  // Test case for sample method
  @Test
  public void testSample() throws MathException {
    NormalDistributionImpl normal = new NormalDistributionImpl(1, 2);
    double sample = normal.sample();
    assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
  }

  // Test case for getDomainLowerBound method - p < 0.5
  @Test
  public void testGetDomainLowerBound1() {
    NormalDistributionImpl normal = new NormalDistributionImpl(3, 2);
    assertEquals(-Double.MAX_VALUE, normal.getDomainLowerBound(0.3), 0.001);
  }

  // Test case for getDomainLowerBound method - p > 0.5
  @Test
  public void testGetDomainLowerBound2() {
    NormalDistributionImpl normal = new NormalDistributionImpl(3, 2);
    assertEquals(3, normal.getDomainLowerBound(0.7), 0.001);
  }

  // Test case for getDomainUpperBound method - p < 0.5
  @Test
  public void testGetDomainUpperBound1() {
    NormalDistributionImpl normal = new NormalDistributionImpl(3, 2);
    assertEquals(3, normal.getDomainUpperBound(0.3), 0.001);
  }

  // Test case for getDomainUpperBound method - p > 0.5
  @Test
  public void testGetDomainUpperBound2() {
    NormalDistributionImpl normal = new NormalDistributionImpl(3, 2);
    assertEquals(Double.MAX_VALUE, normal.getDomainUpperBound(0.7), 0.001);
  }

  // Test case for getInitialDomain method - p < 0.5
  @Test
  public void testGetInitialDomain1() {
    NormalDistributionImpl normal = new NormalDistributionImpl(3, 2);
    assertEquals(1, normal.getInitialDomain(0.3), 0.001);
  }

  // Test case for getInitialDomain method - p > 0.5
  @Test
  public void testGetInitialDomain2() {
    NormalDistributionImpl normal = new NormalDistributionImpl(3, 2);
    assertEquals(5, normal.getInitialDomain(0.7), 0.001);
  }

}