import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class FDistributionTest {

  // Test cases for constructor //

  // Typical case
  @Test
  public void testConstructor_typical() {
    FDistribution f = new FDistribution(5, 10);
    assertNotNull(f);
  }

  // Edge case: numeratorDegreesOfFreedom = 0
  @Test(expected = NotStrictlyPositiveException.class)
  public void testConstructor_edgeCaseNumeratorZero() {
    FDistribution f = new FDistribution(0, 10);
  }

  // Edge case: denominatorDegreesOfFreedom = 0
  @Test(expected = NotStrictlyPositiveException.class)
  public void testConstructor_edgeCaseDenominatorZero() {
    FDistribution f = new FDistribution(5, 0);
  }

  // Error scenario: Both degrees of freedom are negative
  @Test(expected = NotStrictlyPositiveException.class)
  public void testConstructor_errorBothNegative() {
    FDistribution f = new FDistribution(-5, -10);
  }

  // Test cases for density() method //

  // Typical case
  @Test
  public void testDensity_typical() {
    FDistribution f = new FDistribution(5, 10);
    double result = f.density(2.5);
    assertEquals(0.134 , result, 0.001);
  }

  // Edge case: x is 0
  @Test
  public void testDensity_edgeCaseXZero() {
    FDistribution f = new FDistribution(5, 10);
    double result = f.density(0);
    assertEquals(0 , result, 0);
  }

  // Error scenario: Negative x value
  @Test
  public void testDensity_errorNegativeX() {
    FDistribution f = new FDistribution(5, 10);
    double result = f.density(-2.5);
    assertEquals(0 , result, 0);
  }

  // Test cases for cumulativeProbability() method //

  // Typical case
  @Test
  public void testCumulativeProbability_typical() {
    FDistribution f = new FDistribution(5, 10);
    double result = f.cumulativeProbability(2.5);
    assertEquals(0.828 , result, 0.001);
  }

  // Edge case: x is 0
  @Test
  public void testCumulativeProbability_edgeCaseXZero() {
    FDistribution f = new FDistribution(5, 10);
    double result = f.cumulativeProbability(0);
    assertEquals(0 , result, 0);
  }

  // Error scenario: Negative x value
  @Test
  public void testCumulativeProbability_errorNegativeX() {
    FDistribution f = new FDistribution(5, 10);
    double result = f.cumulativeProbability(-2.5);
    assertEquals(0 , result, 0);
  }

  // Test cases for getNumeratorDegreesOfFreedom() method //

  // Typical case
  @Test
  public void testGetNumeratorDegreesOfFreedom_typical() {
    FDistribution f = new FDistribution(5, 10);
    double result = f.getNumeratorDegreesOfFreedom();
    assertEquals(5 , result, 0);
  }

  // Edge case: Numerator degrees of freedom is 1
  @Test
  public void testGetNumeratorDegreesOfFreedom_edgeCaseOne() {
    FDistribution f = new FDistribution(1, 10);
    double result = f.getNumeratorDegreesOfFreedom();
    assertEquals(1 , result, 0);
  }

  // Test cases for getDenominatorDegreesOfFreedom() method //

  // Typical case
  @Test
  public void testGetDenominatorDegreesOfFreedom_typical() {
    FDistribution f = new FDistribution(5, 10);
    double result = f.getDenominatorDegreesOfFreedom();
    assertEquals(10 , result, 0);
  }

  // Edge case: Denominator degrees of freedom is 1
  @Test
  public void testGetDenominatorDegreesOfFreedom_edgeCaseOne() {
    FDistribution f = new FDistribution(5, 1);
    double result = f.getDenominatorDegreesOfFreedom();
    assertEquals(1 , result, 0);
  }

  // Test cases for getNumericalMean() method //

  // Typical case
  @Test
  public void testGetNumericalMean_typical() {
    FDistribution f = new FDistribution(5, 10);
    double result = f.getNumericalMean();
    assertEquals(1.25 , result, 0.001);
  }

  // Edge case: Denominator degrees of freedom is 2
  @Test
  public void testGetNumericalMean_edgeCaseDenominatorTwo() {
    FDistribution f = new FDistribution(5, 2);
    double result = f.getNumericalMean();
    assertEquals(Double.NaN , result, 0);
  }

  // Test cases for getNumericalVariance() method //

  // Typical case
  @Test
  public void testGetNumericalVariance_typical() {
    FDistribution f = new FDistribution(5, 10);
    double result = f.getNumericalVariance();
    assertEquals(4.270 , result, 0.001);
  }

  // Edge case: Denominator degrees of freedom is 3
  @Test
  public void testGetNumericalVariance_edgeCaseDenominatorThree() {
    FDistribution f = new FDistribution(5, 3);
    double result = f.getNumericalVariance();
    assertEquals(Double.NaN , result, 0);
  }

  // Test cases for getSupportLowerBound() method //

  // Typical case
  @Test
  public void testGetSupportLowerBound_typical() {
    FDistribution f = new FDistribution(5, 10);
    double result = f.getSupportLowerBound();
    assertEquals(0 , result, 0);
  }

  // Test cases for getSupportUpperBound() method //

  // Typical case
  @Test
  public void testGetSupportUpperBound_typical() {
    FDistribution f = new FDistribution(5, 10);
    double result = f.getSupportUpperBound();
    assertEquals(Double.POSITIVE_INFINITY , result, 0);
  }

  // Test cases for isSupportLowerBoundInclusive() method //

  // Typical case
  @Test
  public void testIsSupportLowerBoundInclusive_typical() {
    FDistribution f = new FDistribution(5, 10);
    boolean result = f.isSupportLowerBoundInclusive();
    assertTrue(result);
  }

  // Test cases for isSupportUpperBoundInclusive() method //

  // Typical case
  @Test
  public void testIsSupportUpperBoundInclusive_typical() {
    FDistribution f = new FDistribution(5, 10);
    boolean result = f.isSupportUpperBoundInclusive();
    assertFalse(result);
  }

  // Test cases for isSupportConnected() method //

  // Typical case
  @Test
  public void testIsSupportConnected_typical() {
    FDistribution f = new FDistribution(5, 10);
    boolean result = f.isSupportConnected();
    assertTrue(result);
  }

}