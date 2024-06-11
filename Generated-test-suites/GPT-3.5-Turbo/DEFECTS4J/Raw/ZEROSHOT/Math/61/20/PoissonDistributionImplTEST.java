import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class PoissonDistributionImplTest {
  
  private PoissonDistributionImpl distribution;
  
  @Before
  public void setup() {
    distribution = new PoissonDistributionImpl(1.5);
  }
  
  @Test
  public void testGetMean() {
    double expected = 1.5;
    double actual = distribution.getMean();
    assertEquals(expected, actual, 0.0001);
  }
  
  @Test
  public void testProbability() {
    double expected = 0.22313016014843;
    double actual = distribution.probability(1);
    assertEquals(expected, actual, 0.0001);
  }
  
  @Test
  public void testCumulativeProbability() {
    double expected = 0.8160602794142788;
    double actual = distribution.cumulativeProbability(2);
    assertEquals(expected, actual, 0.0001);
  }
  
  @Test
  public void testNormalApproximateProbability() {
    double expected = 0.9280905588305632;
    double actual = distribution.normalApproximateProbability(2);
    assertEquals(expected, actual, 0.0001);
  }
  
  @Test
  public void testSample() {
    int sample = distribution.sample();
    assertEquals(true, sample >= 0 && sample <= Integer.MAX_VALUE);
  }
  
  @Test
  public void testGetDomainLowerBound() {
    int expected = 0;
    int actual = distribution.getDomainLowerBound(0.5);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetDomainUpperBound() {
    int expected = Integer.MAX_VALUE;
    int actual = distribution.getDomainUpperBound(0.5);
    assertEquals(expected, actual);
  }
}