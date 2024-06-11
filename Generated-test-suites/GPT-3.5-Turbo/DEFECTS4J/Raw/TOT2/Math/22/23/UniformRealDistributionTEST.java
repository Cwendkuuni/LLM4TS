import static org.junit.Assert.*;
import org.junit.Test;

public class UniformRealDistributionTest {
  
  @Test
  public void testDensity() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    assertEquals(1.0, dist.density(0), 0.01);
    assertEquals(1.0, dist.density(0.5), 0.01);
    assertEquals(1.0, dist.density(1), 0.01);
    assertEquals(0.0, dist.density(-1), 0.01);
    assertEquals(0.0, dist.density(2), 0.01);
  }
  
  @Test
  public void testCumulativeProbability() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    assertEquals(0.0, dist.cumulativeProbability(0), 0.01);
    assertEquals(0.5, dist.cumulativeProbability(0.5), 0.01);
    assertEquals(1.0, dist.cumulativeProbability(1), 0.01);
    assertEquals(0.0, dist.cumulativeProbability(-1), 0.01);
    assertEquals(1.0, dist.cumulativeProbability(2), 0.01);
  }
  
  @Test
  public void testGetNumericalMean() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    assertEquals(0.5, dist.getNumericalMean(), 0.01);
  }
  
  @Test
  public void testGetNumericalVariance() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    assertEquals(1.0/12, dist.getNumericalVariance(), 0.01);
  }
  
  @Test
  public void testGetSupportLowerBound() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    assertEquals(0.0, dist.getSupportLowerBound(), 0.01);
  }
  
  @Test
  public void testGetSupportUpperBound() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    assertEquals(1.0, dist.getSupportUpperBound(), 0.01);
  }
  

  @Test
  public void testIsSupportConnected() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    assertTrue(dist.isSupportConnected());
  }
}