import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {
  
  private static final double EPSILON = 1e-9;

  @Test
  public void testDensity() {
    // Test typical case
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    assertEquals(1.0, distribution.density(0.5), EPSILON);
    
    // Test values outside the bounds
    assertEquals(0.0, distribution.density(-0.5), EPSILON);
    assertEquals(0.0, distribution.density(1.5), EPSILON);
  }

  @Test
  public void testCumulativeProbability() {
    // Test typical case
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    assertEquals(0.2, distribution.cumulativeProbability(0.2), EPSILON);
    assertEquals(0.5, distribution.cumulativeProbability(0.5), EPSILON);
    assertEquals(0.8, distribution.cumulativeProbability(0.8), EPSILON);

    // Test values outside the bounds
    assertEquals(0.0, distribution.cumulativeProbability(-0.5), EPSILON);
    assertEquals(1.0, distribution.cumulativeProbability(1.5), EPSILON);
  }
  
  @Test
  public void testGetNumericalMean() {
    // Test typical case
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    assertEquals(0.5, distribution.getNumericalMean(), EPSILON);
  }
  
  @Test
  public void testGetNumericalVariance() {
    // Test typical case
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    assertEquals(0.08333333333, distribution.getNumericalVariance(), EPSILON);
  }
  
  @Test
  public void testGetSupportLowerBound() {
    // Test typical case
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    assertEquals(0.0, distribution.getSupportLowerBound(), EPSILON);
  }
  
  @Test
  public void testGetSupportUpperBound() {
    // Test typical case
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    assertEquals(1.0, distribution.getSupportUpperBound(), EPSILON);
  }
  
  @Test
  public void testIsSupportLowerBoundInclusive() {
    // Test typical case
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    assertTrue(distribution.isSupportLowerBoundInclusive());
  }
  
  @Test
  public void testIsSupportUpperBoundInclusive() {
    // Test typical case
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    assertFalse(distribution.isSupportUpperBoundInclusive());
  }
  
  @Test
  public void testIsSupportConnected() {
    // Test typical case
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    assertTrue(distribution.isSupportConnected());
  }
  
  @Test
  public void testSample() {
    // Test typical case
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
    double sample = distribution.sample();
    assertTrue(sample >= 0 && sample < 1);
  }
  
  @Test(expected = NumberIsTooLargeException.class)
  public void testConstructorWithInvalidBounds() {
    // Test lower >= upper
    UniformRealDistribution distribution = new UniformRealDistribution(1, 0);
  }
  
  @Test
  public void testConstructorWithInverseCumAccuracy() {
    // Test typical case
    UniformRealDistribution distribution = new UniformRealDistribution(0, 1, 1e-6);
    assertEquals(1e-6, distribution.getSolverAbsoluteAccuracy(), EPSILON);
  }
}