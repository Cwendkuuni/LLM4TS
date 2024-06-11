import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

import static org.junit.Assert.*;

public class HypergeometricDistributionTest {
  private HypergeometricDistribution distribution;

  @Before
  public void setUp() {
    // Initialize HypergeometricDistribution
    int populationSize = 10;
    int numberOfSuccesses = 5;
    int sampleSize = 3;
    distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
  }

  @Test(expected = NotPositiveException.class)
  public void testConstructorThrowsNotPositiveException() {
    HypergeometricDistribution distribution = new HypergeometricDistribution(-1, 5, 3);
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testConstructorThrowsNotStrictlyPositiveException() {
    HypergeometricDistribution distribution = new HypergeometricDistribution(10, -5, 3);
  }

  @Test(expected = NumberIsTooLargeException.class)
  public void testConstructorThrowsNumberIsTooLargeException() {
    HypergeometricDistribution distribution = new HypergeometricDistribution(10, 15, 3);
  }

  @Test
  public void testCumulativeProbability() {
    // Test cumulative probability for different values
    assertEquals(1.0, distribution.cumulativeProbability(3), 0.00001);
    assertEquals(0.50000, distribution.cumulativeProbability(2), 0.00001);
    assertEquals(0.00000, distribution.cumulativeProbability(1), 0.00001);
  }

  @Test
  public void testProbability() {
    // Test probability for different values
    assertEquals(0.00000, distribution.probability(4), 0.00001);
    assertEquals(0.01587, distribution.probability(3), 0.00001);
    assertEquals(0.07937, distribution.probability(2), 0.00001);
  }

  @Test
  public void testUpperCumulativeProbability() {
    // Test upper cumulative probability for different values
    assertEquals(0.01587, distribution.upperCumulativeProbability(3), 0.00001);
    assertEquals(0.00000, distribution.upperCumulativeProbability(2), 0.00001);
    assertEquals(0.00000, distribution.upperCumulativeProbability(1), 0.00001);
  }

  @Test
  public void testGetNumericalMean() {
    // Test numerical mean calculation
    assertEquals(1.50000, distribution.getNumericalMean(), 0.00001);
  }

  @Test
  public void testGetNumericalVariance() {
    // Test numerical variance calculation
    assertEquals(0.87500, distribution.getNumericalVariance(), 0.00001);
  }

  @Test
  public void testGetSupportLowerBound() {
    // Test lower bound of the support
    assertEquals(3, distribution.getSupportLowerBound());
  }

  @Test
  public void testGetSupportUpperBound() {
    // Test upper bound of the support
    assertEquals(3, distribution.getSupportUpperBound());
  }

  @Test
  public void testIsSupportConnected() {
    // Test if the support is connected
    assertTrue(distribution.isSupportConnected());
  }
}