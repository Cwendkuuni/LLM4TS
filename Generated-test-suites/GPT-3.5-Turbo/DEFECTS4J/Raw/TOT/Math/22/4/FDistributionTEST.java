import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class FDistributionTest {

  private FDistribution fDistribution;

  @Before
  public void setup() {
    fDistribution = new FDistribution(3, 4);
  }

  @Test
  public void testDensity() {
    assertEquals(0.031254, fDistribution.density(0.5), 1e-6);
  }

  @Test
  public void testCumulativeProbability() {
    assertEquals(0.141931, fDistribution.cumulativeProbability(0.5), 1e-6);
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testInvalidNumeratorDegreesOfFreedom() {
    new FDistribution(-1, 4);
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testInvalidDenominatorDegreesOfFreedom() {
    new FDistribution(3, 0);
  }

  @Test
  public void testGetNumeratorDegreesOfFreedom() {
    assertEquals(3, fDistribution.getNumeratorDegreesOfFreedom(), 1e-6);
  }

  @Test
  public void testGetDenominatorDegreesOfFreedom() {
    assertEquals(4, fDistribution.getDenominatorDegreesOfFreedom(), 1e-6);
  }

  @Test
  public void testGetNumericalMean() {
    assertEquals(2, fDistribution.getNumericalMean(), 1e-6);
  }

  @Test
  public void testGetNumericalVariance() {
    assertEquals(10, fDistribution.getNumericalVariance(), 1e-6);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetSupportLowerBound() {
    fDistribution.getSupportLowerBound();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetSupportUpperBound() {
    fDistribution.getSupportUpperBound();
  }

  @Test
  public void testIsSupportLowerBoundInclusive() {
    assertTrue(fDistribution.isSupportLowerBoundInclusive());
  }

  @Test
  public void testIsSupportUpperBoundInclusive() {
    assertFalse(fDistribution.isSupportUpperBoundInclusive());
  }

  @Test
  public void testIsSupportConnected() {
    assertTrue(fDistribution.isSupportConnected());
  }

}