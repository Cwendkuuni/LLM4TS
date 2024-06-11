
import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

  @Test
  public void testCumulativeProbability() {
      HypergeometricDistribution hyperGeo = new HypergeometricDistribution(20, 8, 5);
      assertEquals(0.014244186355628593, hyperGeo.cumulativeProbability(2), 0.0001);
  }

  @Test
  public void testProbability() {
      HypergeometricDistribution hyperGeo = new HypergeometricDistribution(20, 8, 5);
      assertEquals(0.01920684398976982, hyperGeo.probability(2), 0.0001);
  }

  @Test
  public void testUpperCumulativeProbability() {
      HypergeometricDistribution hyperGeo = new HypergeometricDistribution(20, 8, 5);
      assertEquals(0.9042558136443714, hyperGeo.upperCumulativeProbability(2), 0.0001);
  }

  @Test
  public void testGetNumericalMean() {
      HypergeometricDistribution hyperGeo = new HypergeometricDistribution(20, 8, 5);
      assertEquals(2.0, hyperGeo.getNumericalMean(), 0.0001);
  }

  @Test
  public void testGetNumericalVariance() {
      HypergeometricDistribution hyperGeo = new HypergeometricDistribution(20, 8, 5);
      assertEquals(0.8000000000000002, hyperGeo.getNumericalVariance(), 0.0001);
  }

  @Test
  public void testGetSupportLowerBound() {
      HypergeometricDistribution hyperGeo = new HypergeometricDistribution(20, 8, 5);
      assertEquals(3, hyperGeo.getSupportLowerBound());
  }

  @Test
  public void testGetSupportUpperBound() {
      HypergeometricDistribution hyperGeo = new HypergeometricDistribution(20, 8, 5);
      assertEquals(5, hyperGeo.getSupportUpperBound());
  }

  @Test
  public void testIsSupportConnected() {
      HypergeometricDistribution hyperGeo = new HypergeometricDistribution(20, 8, 5);
      assertTrue(hyperGeo.isSupportConnected());
  }
}

