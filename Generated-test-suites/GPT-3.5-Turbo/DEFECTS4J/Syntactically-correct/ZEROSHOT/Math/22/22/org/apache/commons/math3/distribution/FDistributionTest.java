import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionTest {

  @Test(expected = NotStrictlyPositiveException.class)
  public void testNegativeNumeratorDegrees() {
    FDistribution fDistribution = new FDistribution(-1, 2);
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testNegativeDenominatorDegrees() {
    FDistribution fDistribution = new FDistribution(1, -2);
  }

  @Test
  public void testDensity() {
    FDistribution fDistribution = new FDistribution(4, 6);
    double density = fDistribution.density(2);
    assertEquals(0.10861586331287392, density, 0.00001);
  }

  @Test
  public void testCumulativeProbability() {
    FDistribution fDistribution = new FDistribution(4, 6);
    double cumulativeProbability = fDistribution.cumulativeProbability(2);
    assertEquals(0.7347313925850287, cumulativeProbability, 0.00001);
  }

  @Test
  public void testGetNumeratorDegreesOfFreedom() {
    FDistribution fDistribution = new FDistribution(4, 6);
    double numeratorDegreesOfFreedom = fDistribution.getNumeratorDegreesOfFreedom();
    assertEquals(4, numeratorDegreesOfFreedom, 0.00001);
  }

  @Test
  public void testGetDenominatorDegreesOfFreedom() {
    FDistribution fDistribution = new FDistribution(4, 6);
    double denominatorDegreesOfFreedom = fDistribution.getDenominatorDegreesOfFreedom();
    assertEquals(6, denominatorDegreesOfFreedom, 0.00001);
  }

  @Test
  public void testGetNumericalMean() {
    FDistribution fDistribution = new FDistribution(4, 6);
    double numericalMean = fDistribution.getNumericalMean();
    assertEquals(1.5, numericalMean, 0.00001);
  }

  @Test
  public void testGetNumericalVariance() {
    FDistribution fDistribution = new FDistribution(6, 10);
    double numericalVariance = fDistribution.getNumericalVariance();
    assertEquals(15.0, numericalVariance, 0.00001);
  }

  @Test
  public void testGetSupportLowerBound() {
    FDistribution fDistribution = new FDistribution(4, 6);
    double supportLowerBound = fDistribution.getSupportLowerBound();
    assertEquals(0, supportLowerBound, 0.00001);
  }

  @Test
  public void testGetSupportUpperBound() {
    FDistribution fDistribution = new FDistribution(4, 6);
    double supportUpperBound = fDistribution.getSupportUpperBound();
    assertEquals(Double.POSITIVE_INFINITY, supportUpperBound, 0.00001);
  }

  @Test
  public void testIsSupportLowerBoundInclusive() {
    FDistribution fDistribution = new FDistribution(4, 6);
    boolean isSupportLowerBoundInclusive = fDistribution.isSupportLowerBoundInclusive();
    assertEquals(true, isSupportLowerBoundInclusive);
  }

  @Test
  public void testIsSupportUpperBoundInclusive() {
    FDistribution fDistribution = new FDistribution(4, 6);
    boolean isSupportUpperBoundInclusive = fDistribution.isSupportUpperBoundInclusive();
    assertEquals(false, isSupportUpperBoundInclusive);
  }

  @Test
  public void testIsSupportConnected() {
    FDistribution fDistribution = new FDistribution(4, 6);
    boolean isSupportConnected = fDistribution.isSupportConnected();
    assertEquals(true, isSupportConnected);
  }
}