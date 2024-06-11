import org.junit.Test;
import org.apache.commons.math.distribution.PoissonDistributionImpl;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

  @Test
  public void testGetMean() {
    PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
    assertEquals(2.5, distribution.getMean(), 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMean() {
    PoissonDistributionImpl distribution = new PoissonDistributionImpl(0);
  }

  @Test
  public void testProbability() {
    PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
    assertEquals(0.0820849986238988, distribution.probability(0), 0.000001);
    assertEquals(0.2052124965597471, distribution.probability(1), 0.000001);
    assertEquals(0.2565156206996838, distribution.probability(2), 0.000001);
    assertEquals(0.2137630189164033, distribution.probability(3), 0.000001);
    assertEquals(0.1336012611977520, distribution.probability(4), 0.000001);
  }

  @Test
  public void testCumulativeProbability() {
    PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
    assertEquals(0.0820849986238988, distribution.cumulativeProbability(0), 0.000001);
    assertEquals(0.2872974951836459, distribution.cumulativeProbability(1), 0.000001);
    assertEquals(0.5438131158833297, distribution.cumulativeProbability(2), 0.000001);
    assertEquals(0.7575761347997331, distribution.cumulativeProbability(3), 0.000001);
    assertEquals(0.8911773969974851, distribution.cumulativeProbability(4), 0.000001);
  }

  @Test
  public void testNormalApproximateProbability() {
    PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
    assertEquals(0.2027942886064498, distribution.normalApproximateProbability(0), 0.000001);
    assertEquals(0.5328071953168225, distribution.normalApproximateProbability(1), 0.000001);
    assertEquals(0.7487304112499465, distribution.normalApproximateProbability(2), 0.000001);
    assertEquals(0.8907551452619520, distribution.normalApproximateProbability(3), 0.000001);
    assertEquals(0.9600035658135057, distribution.normalApproximateProbability(4), 0.000001);
  }

  @Test
  public void testSample() {
    PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
    int value = distribution.sample();
    assertTrue(value >= 0);
    assertTrue(value < Integer.MAX_VALUE);
  }

  @Test
  public void testGetDomainLowerBound() {
    PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
    assertEquals(0, distribution.getDomainLowerBound(0), 0);
    assertEquals(0, distribution.getDomainLowerBound(1), 0);
    assertEquals(0, distribution.getDomainLowerBound(0.5), 0);
  }

  @Test
  public void testGetDomainUpperBound() {
    PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
    assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0), 0);
    assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(1), 0);
    assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0);
  }

}