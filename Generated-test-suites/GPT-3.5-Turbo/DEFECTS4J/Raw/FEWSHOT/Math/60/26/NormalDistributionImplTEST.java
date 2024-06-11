import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {
  
  @Test
  public void testGetMean() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
    assertEquals(10, distribution.getMean(), 0);
  }
  
  @Test
  public void testGetStandardDeviation() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
    assertEquals(2, distribution.getStandardDeviation(), 0);
  }
  
  @Test
  public void testDensity() {
    NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
    assertEquals(0.053991, distribution.density(8), 0.000001);
    assertEquals(0.120985, distribution.density(10), 0.000001);
    assertEquals(0.053991, distribution.density(12), 0.000001);
  }
  
  @Test
  public void testCumulativeProbability() throws MathException {
    NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
    assertEquals(0.022750, distribution.cumulativeProbability(8), 0.000001);
    assertEquals(0.5, distribution.cumulativeProbability(10), 0.000001);
    assertEquals(0.97725, distribution.cumulativeProbability(12), 0.000001);
  }
  
  @Test
  public void testInverseCumulativeProbability() throws MathException {
    NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
    assertEquals(8.0537, distribution.inverseCumulativeProbability(0.2), 0.0001);
    assertEquals(10, distribution.inverseCumulativeProbability(0.5), 0.0001);
    assertEquals(11.9463, distribution.inverseCumulativeProbability(0.8), 0.0001);
  }
  
  @Test
  public void testSample() throws MathException {
    NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
    double sample = distribution.sample();
    assertTrue(sample >= 6 && sample <= 14);
  }
}