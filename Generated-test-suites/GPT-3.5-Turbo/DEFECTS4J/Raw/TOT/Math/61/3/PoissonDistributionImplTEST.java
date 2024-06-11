import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;

public class PoissonDistributionImplTest {
  
  private static final double DELTA = 1e-15;
  private PoissonDistributionImpl distribution;

  @Before
  public void setUp() {
    distribution = new PoissonDistributionImpl(5.0);
  }

  @Test
  public void testGetMean() {
    assertEquals(5.0, distribution.getMean(), DELTA);
  }

  @Test
  public void testProbability() {
    assertEquals(0, distribution.probability(-5), DELTA);
    assertEquals(0.006737946999085467, distribution.probability(0), DELTA);
    // Edge case: maximum number of iterations
    assertEquals(6.995678673532704e-7, distribution.probability(Integer.MAX_VALUE), DELTA);
  }

  @Test
  public void testCumulativeProbability() throws MathException {
    assertEquals(0.993262053, distribution.cumulativeProbability(-5), DELTA);
    assertEquals(0.037833274, distribution.cumulativeProbability(0), DELTA);
    // Edge case: maximum int value
    assertEquals(1.0000000000, distribution.cumulativeProbability(Integer.MAX_VALUE), DELTA);
  }

  @Test
  public void testNormalApproximateProbability() throws MathException {
    assertEquals(1.0, distribution.normalApproximateProbability(0), DELTA);
    assertEquals(0.9999683287581669, distribution.normalApproximateProbability(5), DELTA);
    assertEquals(8.973717348070409e-8, distribution.normalApproximateProbability(Integer.MAX_VALUE), DELTA);
  }

  @Test
  public void testSample() throws MathException {
    int sample = distribution.sample();
    assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
  }

  @Test
  public void testGetDomainLowerBound() {
    assertEquals(0, distribution.getDomainLowerBound(0.5));
  }

  @Test
  public void testGetDomainUpperBound() {
    assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5));
  }

}