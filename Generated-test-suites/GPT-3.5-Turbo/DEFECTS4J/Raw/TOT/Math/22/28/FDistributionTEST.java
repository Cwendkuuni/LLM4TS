import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class FDistributionTest {

  FDistribution distribution;

  @Before
  public void setup() {
    distribution = new FDistribution(3, 5);
  }

  @Test
  public void testDensity() {
    // Typical use case
    double result = distribution.density(2.0);
    assertEquals(0.10434426948494873, result, 1e-9);

    // Edge case
    result = distribution.density(0.0);
    assertEquals(0.0, result, 0.0);

    // Error scenario
    boolean exceptionThrown = false;
    try {
      distribution.density(-1.0);
    } catch (IllegalArgumentException e) {
      exceptionThrown = true;
    }
    assertTrue(exceptionThrown);
  }

  @Test
  public void testCumulativeProbability() {
    // Typical use case
    double result = distribution.cumulativeProbability(2.0);
    assertEquals(0.9299760538930713, result, 1e-9);

    // Edge case
    result = distribution.cumulativeProbability(0.0);
    assertEquals(0.0, result, 0.0);

    // Error scenario
    boolean exceptionThrown = false;
    try {
      distribution.cumulativeProbability(-1.0);
    } catch (IllegalArgumentException e) {
      exceptionThrown = true;
    }
    assertTrue(exceptionThrown);
  }

  @Test
  public void testGetNumeratorDegreesOfFreedom() {
    double result = distribution.getNumeratorDegreesOfFreedom();
    assertEquals(3.0, result, 0.0);
  }

  @Test
  public void testGetDenominatorDegreesOfFreedom() {
    double result = distribution.getDenominatorDegreesOfFreedom();
    assertEquals(5.0, result, 0.0);
  }

  @Test
  public void testGetNumericalMean() {
    double result = distribution.getNumericalMean();
    assertEquals(1.6666666666666667, result, 1e-9);
  }

  @Test
  public void testGetNumericalVariance() {
    double result = distribution.getNumericalVariance();
    assertEquals(3.7037037037037033, result, 1e-9);
  }

  @Test
  public void testGetSupportLowerBound() {
    double result = distribution.getSupportLowerBound();
    assertEquals(0.0, result, 0.0);
  }

  @Test
  public void testGetSupportUpperBound() {
    double result = distribution.getSupportUpperBound();
    assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
  }

  @Test
  public void testIsSupportLowerBoundInclusive() {
    boolean result = distribution.isSupportLowerBoundInclusive();
    assertTrue(result);
  }

  @Test
  public void testIsSupportUpperBoundInclusive() {
    boolean result = distribution.isSupportUpperBoundInclusive();
    assertFalse(result);
  }

  @Test
  public void testIsSupportConnected() {
    boolean result = distribution.isSupportConnected();
    assertTrue(result);
  }

}