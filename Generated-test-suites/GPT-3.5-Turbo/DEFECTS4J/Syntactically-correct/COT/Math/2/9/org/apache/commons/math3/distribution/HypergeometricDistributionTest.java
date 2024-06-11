import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;

public class HypergeometricDistributionTest {
  @Test
  public void testConstructor() {
    try {
      HypergeometricDistribution distr = new HypergeometricDistribution(-1, 5, 3);
      fail("Expected NotStrictlyPositiveException");
    } catch (NotStrictlyPositiveException e) {
      // success
    }

    try {
      HypergeometricDistribution distr = new HypergeometricDistribution(10, -5, 3);
      fail("Expected NotPositiveException");
    } catch (NotPositiveException e) {
      // success
    }

    try {
      HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, -3);
      fail("Expected NotPositiveException");
    } catch (NotPositiveException e) {
      // success
    }

    try {
      HypergeometricDistribution distr = new HypergeometricDistribution(10, 15, 3);
      fail("Expected NumberIsTooLargeException");
    } catch (NumberIsTooLargeException e) {
      // success
    }

    try {
      HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, 10);
      fail("Expected NumberIsTooLargeException");
    } catch (NumberIsTooLargeException e) {
      // success
    }

    HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, 3);
    assertEquals(10, distr.getPopulationSize());
    assertEquals(5, distr.getNumberOfSuccesses());
    assertEquals(3, distr.getSampleSize());
  }

  @Test
  public void testCumulativeProbability() {
    HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, 3);

    assertEquals(0.0, distr.cumulativeProbability(-1), 0.0001);
    assertEquals(0.0, distr.cumulativeProbability(0), 0.0001);
    assertEquals(0.005785, distr.cumulativeProbability(1), 0.0001);
    assertEquals(0.087156, distr.cumulativeProbability(2), 0.0001);
    assertEquals(0.432332, distr.cumulativeProbability(3), 0.0001);
    assertEquals(0.932332, distr.cumulativeProbability(4), 0.0001);
    assertEquals(1.0, distr.cumulativeProbability(5), 0.0001);
    assertEquals(1.0, distr.cumulativeProbability(6), 0.0001);
  }

  @Test
  public void testProbability() {
    HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, 3);

    assertEquals(0.0, distr.probability(-1), 0.0001);
    assertEquals(0.014778, distr.probability(0), 0.0001);
    assertEquals(0.115452, distr.probability(1), 0.0001);
    assertEquals(0.347355, distr.probability(2), 0.0001);
    assertEquals(0.411765, distr.probability(3), 0.0001);
    assertEquals(0.110368, distr.probability(4), 0.0001);
    assertEquals(0.000282, distr.probability(5), 0.0001);
  }

  @Test
  public void testUpperCumulativeProbability() {
    HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, 3);

    assertEquals(1.0, distr.upperCumulativeProbability(-1), 0.0001);
    assertEquals(1.0, distr.upperCumulativeProbability(0), 0.0001);
    assertEquals(0.994215, distr.upperCumulativeProbability(1), 0.0001);
    assertEquals(0.906455, distr.upperCumulativeProbability(2), 0.0001);
    assertEquals(0.567668, distr.upperCumulativeProbability(3), 0.0001);
    assertEquals(0.067668, distr.upperCumulativeProbability(4), 0.0001);
    assertEquals(0.0, distr.upperCumulativeProbability(5), 0.0001);
    assertEquals(0.0, distr.upperCumulativeProbability(6), 0.0001);
  }

  @Test
  public void testGetNumericalMean() {
    HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, 3);

    assertEquals(1.5, distr.getNumericalMean(), 0.0001);
  }

  @Test
  public void testGetNumericalVariance() {
    HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, 3);

    assertEquals(0.9375, distr.getNumericalVariance(), 0.0001);
  }

  @Test
  public void testGetSupportLowerBound() {
    HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, 3);

    assertEquals(0, distr.getSupportLowerBound());
  }

  @Test
  public void testGetSupportUpperBound() {
    HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, 3);

    assertEquals(3, distr.getSupportUpperBound());
  }

  @Test
  public void testIsSupportConnected() {
    HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, 3);

    assertTrue(distr.isSupportConnected());
  }
}