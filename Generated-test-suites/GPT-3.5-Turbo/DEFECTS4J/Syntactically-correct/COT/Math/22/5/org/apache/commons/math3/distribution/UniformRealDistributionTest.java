
import static org.junit.Assert.*;
import org.junit.Test;

public class UniformRealDistributionTest {

  @Test
  public void testConstructor() {
    double lower = 0.0;
    double upper = 1.0;
    UniformRealDistribution dist = new UniformRealDistribution();
    assertEquals(lower, dist.getSupportLowerBound(), 0.0);
    assertEquals(upper, dist.getSupportUpperBound(), 0.0);
  }

  @Test
  public void testDensity() {
    double lower = 0.0;
    double upper = 1.0;
    UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
    double x = 0.5;
    assertEquals(1 / (upper - lower), dist.density(x), 0.0);
  }

  @Test
  public void testCumulativeProbability() {
    double lower = 0.0;
    double upper = 1.0;
    UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
    double x = 0.5;
    assertEquals(x - lower / (upper - lower), dist.cumulativeProbability(x), 0.0);
  }

  @Test
  public void testGetNumericalMean() {
    double lower = 0.0;
    double upper = 1.0;
    UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
    double mean = (lower + upper) / 2;
    assertEquals(mean, dist.getNumericalMean(), 0.0);
  }

  @Test
  public void testGetNumericalVariance() {
    double lower = 0.0;
    double upper = 1.0;
    UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
    double variance = Math.pow(upper - lower, 2) / 12;
    assertEquals(variance, dist.getNumericalVariance(), 0.0);
  }

  @Test
  public void testGetSupportLowerBound() {
    double lower = 0.0;
    double upper = 1.0;
    UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
    assertEquals(lower, dist.getSupportLowerBound(), 0.0);
  }

  @Test
  public void testGetSupportUpperBound() {
    double lower = 0.0;
    double upper = 1.0;
    UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
    assertEquals(upper, dist.getSupportUpperBound(), 0.0);
  }

  @Test
  public void testIsSupportLowerBoundInclusive() {
    double lower = 0.0;
    double upper = 1.0;
    UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
    assertTrue(dist.isSupportLowerBoundInclusive());
  }

  @Test
  public void testIsSupportUpperBoundInclusive() {
    double lower = 0.0;
    double upper = 1.0;
    UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
    assertFalse(dist.isSupportUpperBoundInclusive());
  }

  @Test
  public void testIsSupportConnected() {
    double lower = 0.0;
    double upper = 1.0;
    UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
    assertTrue(dist.isSupportConnected());
  }

  @Test
  public void testSample() {
    double lower = 0.0;
    double upper = 1.0;
    UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
    double sample = dist.sample();
    assertTrue(sample >= lower && sample < upper);
  }
}
