import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

  private HypergeometricDistribution hypergeometricDistribution;

  @Before
  public void setUp() {
    hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
  }

  @Test
  public void testGetNumberOfSuccesses() {
    assertEquals(5, hypergeometricDistribution.getNumberOfSuccesses());
  }

  @Test
  public void testGetPopulationSize() {
    assertEquals(10, hypergeometricDistribution.getPopulationSize());
  }

  @Test
  public void testGetSampleSize() {
    assertEquals(3, hypergeometricDistribution.getSampleSize());
  }

  @Test
  public void testGetSupportLowerBound() {
    assertEquals(2, hypergeometricDistribution.getSupportLowerBound());
  }

  @Test
  public void testGetSupportUpperBound() {
    assertEquals(3, hypergeometricDistribution.getSupportUpperBound());
  }

  @Test
  public void testIsSupportConnected() {
    assertTrue(hypergeometricDistribution.isSupportConnected());
  }

  @Test
  public void testGetNumericalMean() {
    assertEquals(1.5, hypergeometricDistribution.getNumericalMean(), 0.001);
  }

  @Test
  public void testGetNumericalVariance() {
    assertEquals(0.45, hypergeometricDistribution.getNumericalVariance(), 0.001);
  }

  @Test
  public void testProbability() {
    assertEquals(0.2, hypergeometricDistribution.probability(2), 0.001);
    assertEquals(0.6, hypergeometricDistribution.probability(3), 0.001);
    assertEquals(0.2, hypergeometricDistribution.probability(4), 0.001);
  }

  @Test
  public void testCumulativeProbability() {
    assertEquals(0.2, hypergeometricDistribution.cumulativeProbability(2), 0.001);
    assertEquals(0.8, hypergeometricDistribution.cumulativeProbability(3), 0.001);
    assertEquals(1.0, hypergeometricDistribution.cumulativeProbability(4), 0.001);
  }

  @Test
  public void testUpperCumulativeProbability() {
    assertEquals(1.0, hypergeometricDistribution.upperCumulativeProbability(2), 0.001);
    assertEquals(0.8, hypergeometricDistribution.upperCumulativeProbability(3), 0.001);
    assertEquals(0.2, hypergeometricDistribution.upperCumulativeProbability(4), 0.001);
  }

}