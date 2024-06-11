import org.junit.*;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

  @Test
  public void testDensity() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    assertEquals(1.0, dist.density(0), 0);
    assertEquals(1.0, dist.density(0.5), 0);
    assertEquals(1.0, dist.density(1), 0);
    assertEquals(0.0, dist.density(-1), 0);
    assertEquals(0.0, dist.density(2), 0);
    assertEquals(0.0, dist.density(1.5), 0);
  }

  @Test
  public void testCumulativeProbability() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    assertEquals(0.0, dist.cumulativeProbability(0), 0);
    assertEquals(0.5, dist.cumulativeProbability(0.5), 0);
    assertEquals(1.0, dist.cumulativeProbability(1), 0);
    assertEquals(0.0, dist.cumulativeProbability(-1), 0);
    assertEquals(1.0, dist.cumulativeProbability(2), 0);
    assertEquals(1.0, dist.cumulativeProbability(1.5), 0);
  }

  @Test
  public void testGetNumericalMean() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    assertEquals(0.5, dist.getNumericalMean(), 0);
  }

  @Test
  public void testGetNumericalVariance() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    assertEquals(0.0833, dist.getNumericalVariance(), 0.0001);
  }

  @Test
  public void testGetSupportLowerBound() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    assertEquals(0.0, dist.getSupportLowerBound(), 0);
  }

  @Test
  public void testGetSupportUpperBound() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    assertEquals(1.0, dist.getSupportUpperBound(), 0);
  }

  @Test
  public void testIsSupportLowerBoundInclusive() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    assertTrue(dist.isSupportLowerBoundInclusive());
  }

  @Test
  public void testIsSupportUpperBoundInclusive() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    assertFalse(dist.isSupportUpperBoundInclusive());
  }

  @Test
  public void testIsSupportConnected() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    assertTrue(dist.isSupportConnected());
  }

  @Test
  public void testSample() {
    UniformRealDistribution dist = new UniformRealDistribution(0, 1);
    for (int i = 0; i < 1000; i++) {
      double sample = dist.sample();
      assertTrue(sample >= 0 && sample < 1);
    }
  }

}