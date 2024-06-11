import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {
  
  private static final double DELTA = 1e-9;
  
  private NormalDistributionImpl distribution;
  
  @Test
  public void testGetMean() {
    distribution = new NormalDistributionImpl(1, 2);
    assertEquals(1, distribution.getMean(), DELTA);
  }
  
  @Test
  public void testGetStandardDeviation() {
    distribution = new NormalDistributionImpl(1, 2);
    assertEquals(2, distribution.getStandardDeviation(), DELTA);
  }
  
  @Test
  public void testDensity() {
    distribution = new NormalDistributionImpl(1, 2);
    assertEquals(0.19947114020071635, distribution.density(1), DELTA);
  }
  
  @Test
  public void testCumulativeProbability() throws MathException {
    distribution = new NormalDistributionImpl(1, 2);
    assertEquals(0.3085375387259869, distribution.cumulativeProbability(1), DELTA);
  }
  
  @Test
  public void testInverseCumulativeProbability() throws MathException {
    distribution = new NormalDistributionImpl(1, 2);
    assertEquals(1.138386670824913, distribution.inverseCumulativeProbability(0.9), DELTA);
  }
  
  @Test
  public void testSample() throws MathException {
    distribution = new NormalDistributionImpl(1, 2);
    double sample = distribution.sample();
    assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
  }
}