import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

  // Test for density method
  @Test
  public void testDensity() {
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    
    // Typical use case
    double density1 = distribution.density(0.5);
    assertEquals(1.0, density1, 0.0001);
    
    // Edge case - lower bound
    double density2 = distribution.density(0);
    assertEquals(1.0, density2, 0.0001);
    
    // Edge case - upper bound
    double density3 = distribution.density(1);
    assertEquals(1.0, density3, 0.0001);
    
    // Error scenario - out of range
    double density4 = distribution.density(2);
    assertEquals(0.0, density4, 0.0001);
  }

  // Test for cumulativeProbability method
  @Test
  public void testCumulativeProbability() {
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    
    // Typical use case
    double prob1 = distribution.cumulativeProbability(0.5);
    assertEquals(0.5, prob1, 0.0001);
    
    // Edge case - lower bound
    double prob2 = distribution.cumulativeProbability(0);
    assertEquals(0.0, prob2, 0.0001);
    
    // Edge case - upper bound
    double prob3 = distribution.cumulativeProbability(1);
    assertEquals(1.0, prob3, 0.0001);
    
    // Error scenario - out of range
    double prob4 = distribution.cumulativeProbability(2);
    assertEquals(1.0, prob4, 0.0001);
  }

  // Test for getNumericalMean method
  @Test
  public void testGetNumericalMean() {
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    
    // Typical use case
    double mean = distribution.getNumericalMean();
    assertEquals(0.5, mean, 0.0001);
  }

  // Test for getNumericalVariance method
  @Test
  public void testGetNumericalVariance() {
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    
    // Typical use case
    double variance = distribution.getNumericalVariance();
    assertEquals(0.0833, variance, 0.0001);
  }

  // Test for getSupportLowerBound method
  @Test
  public void testGetSupportLowerBound() {
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    
    // Typical use case
    double lowerBound = distribution.getSupportLowerBound();
    assertEquals(0.0, lowerBound, 0.0001);
  }

  // Test for getSupportUpperBound method
  @Test
  public void testGetSupportUpperBound() {
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    
    // Typical use case
    double upperBound = distribution.getSupportUpperBound();
    assertEquals(1.0, upperBound, 0.0001);
  }

  // Test for isSupportLowerBoundInclusive method
  @Test
  public void testIsSupportLowerBoundInclusive() {
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    
    // Typical use case
    boolean isLowerBoundInclusive = distribution.isSupportLowerBoundInclusive();
    assertTrue(isLowerBoundInclusive);
  }

  // Test for isSupportUpperBoundInclusive method
  @Test
  public void testIsSupportUpperBoundInclusive() {
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    
    // Typical use case
    boolean isUpperBoundInclusive = distribution.isSupportUpperBoundInclusive();
    assertFalse(isUpperBoundInclusive);
  }

  // Test for isSupportConnected method
  @Test
  public void testIsSupportConnected() {
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    
    // Typical use case
    boolean isSupportConnected = distribution.isSupportConnected();
    assertTrue(isSupportConnected);
  }

  // Test for sample method
  @Test
  public void testSample() {
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    
    // Typical use case
    double sample = distribution.sample();
    assertTrue(sample >= 0 && sample < 1);
  }
}