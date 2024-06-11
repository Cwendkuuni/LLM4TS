import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {
  
  @Test
  public void testDensity() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    
    // Typical use case
    double density1 = dist.density(0.5);
    assertEquals(1, density1, 0.0001);
    
    // Edge case: x < lower
    double density2 = dist.density(-1);
    assertEquals(0, density2, 0.0001);
    
    // Edge case: x > upper
    double density3 = dist.density(1.5);
    assertEquals(0, density3, 0.0001);
  }
  
  @Test
  public void testCumulativeProbability() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    
    // Typical use case
    double cp1 = dist.cumulativeProbability(0.5);
    assertEquals(0.5, cp1, 0.0001);
    
    // Edge case: x <= lower
    double cp2 = dist.cumulativeProbability(-1);
    assertEquals(0, cp2, 0.0001);
    
    // Edge case: x >= upper
    double cp3 = dist.cumulativeProbability(1.5);
    assertEquals(1, cp3, 0.0001);
  }
  
  @Test
  public void testGetNumericalMean() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    
    // Typical use case
    double mean1 = dist.getNumericalMean();
    assertEquals(0.5, mean1, 0.0001);
    
    // Edge case: lower = upper
    UniformRealDistribution dist2 = new UniformRealDistribution(2, 2);
    double mean2 = dist2.getNumericalMean();
    assertEquals(2, mean2, 0.0001);
  }
  
  @Test
  public void testGetNumericalVariance() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    
    // Typical use case
    double variance1 = dist.getNumericalVariance();
    assertEquals(0.083333, variance1, 0.0001);
    
    // Edge case: lower = upper
    UniformRealDistribution dist2 = new UniformRealDistribution(2, 2);
    double variance2 = dist2.getNumericalVariance();
    assertEquals(0, variance2, 0.0001);
  }
  
  @Test
  public void testGetSupportLowerBound() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    
    // Typical use case
    double lowerBound1 = dist.getSupportLowerBound();
    assertEquals(0, lowerBound1, 0.0001);
    
    // Edge case: lower = upper
    UniformRealDistribution dist2 = new UniformRealDistribution(2, 2);
    double lowerBound2 = dist2.getSupportLowerBound();
    assertEquals(2, lowerBound2, 0.0001);
  }
  
  @Test
  public void testGetSupportUpperBound() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    
    // Typical use case
    double upperBound1 = dist.getSupportUpperBound();
    assertEquals(1, upperBound1, 0.0001);
    
    // Edge case: lower = upper
    UniformRealDistribution dist2 = new UniformRealDistribution(2, 2);
    double upperBound2 = dist2.getSupportUpperBound();
    assertEquals(2, upperBound2, 0.0001);
  }
  
  @Test
  public void testIsSupportLowerBoundInclusive() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    
    // Typical use case
    boolean inclusive1 = dist.isSupportLowerBoundInclusive();
    assertTrue(inclusive1);
  }
  
  @Test
  public void testIsSupportUpperBoundInclusive() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    
    // Typical use case
    boolean inclusive1 = dist.isSupportUpperBoundInclusive();
    assertFalse(inclusive1);
  }
  
  @Test
  public void testIsSupportConnected() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    
    // Typical use case
    boolean connected1 = dist.isSupportConnected();
    assertTrue(connected1);
  }
  
  @Test
  public void testSample() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    
    // Typical use case
    double sample1 = dist.sample();
    assertTrue(sample1 >= 0 && sample1 < 1);
  }
  
}