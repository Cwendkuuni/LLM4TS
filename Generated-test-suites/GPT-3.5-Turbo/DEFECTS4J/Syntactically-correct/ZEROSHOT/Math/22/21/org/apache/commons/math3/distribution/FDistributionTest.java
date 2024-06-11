import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FDistributionTest {

  private FDistribution fDistribution;

  @Before
  public void setup() {
    fDistribution = new FDistribution(5, 10);
  }

  @Test
  public void testDensity() {
    double density = fDistribution.density(1.0);
    assertEquals(0.09375, density, 0.0001);
  }

  @Test
  public void testCumulativeProbability() {
    double cumulativeProbability = fDistribution.cumulativeProbability(1.0);
    assertEquals(0.4832072259, cumulativeProbability, 0.0001);
  }

  @Test
  public void testGetNumeratorDegreesOfFreedom() {
    double numeratorDegreesOfFreedom = fDistribution.getNumeratorDegreesOfFreedom();
    assertEquals(5.0, numeratorDegreesOfFreedom, 0.0001);
  }

  @Test
  public void testGetDenominatorDegreesOfFreedom() {
    double denominatorDegreesOfFreedom = fDistribution.getDenominatorDegreesOfFreedom();
    assertEquals(10.0, denominatorDegreesOfFreedom, 0.0001);
  }

  @Test
  public void testGetNumericalMean() {
    double numericalMean = fDistribution.getNumericalMean();
    assertEquals(1.25, numericalMean, 0.0001);
  }

  @Test
  public void testGetNumericalVariance() {
    double numericalVariance = fDistribution.getNumericalVariance();
    assertEquals(6.25, numericalVariance, 0.0001);
  }

  @Test
  public void testGetSupportLowerBound() {
    double supportLowerBound = fDistribution.getSupportLowerBound();
    assertEquals(0.0, supportLowerBound, 0.0001);
  }

  @Test
  public void testGetSupportUpperBound() {
    double supportUpperBound = fDistribution.getSupportUpperBound();
    assertEquals(Double.POSITIVE_INFINITY, supportUpperBound, 0.0001);
  }

  @Test
  public void testIsSupportLowerBoundInclusive() {
    boolean isSupportLowerBoundInclusive = fDistribution.isSupportLowerBoundInclusive();
    assertTrue(isSupportLowerBoundInclusive);
  }

  @Test
  public void testIsSupportUpperBoundInclusive() {
    boolean isSupportUpperBoundInclusive = fDistribution.isSupportUpperBoundInclusive();
    assertTrue(!isSupportUpperBoundInclusive);
  }

  @Test
  public void testIsSupportConnected() {
    boolean isSupportConnected = fDistribution.isSupportConnected();
    assertTrue(isSupportConnected);
  }

}