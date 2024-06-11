import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;

public class FDistributionTest {

  // Test for constructor
  @Test(expected = NotStrictlyPositiveException.class)
  public void testConstructorWithNegativeNumeratorDF() {
    new FDistribution(-1, 2);
  }

  // Test for constructor
  @Test(expected = NotStrictlyPositiveException.class)
  public void testConstructorWithNegativeDenominatorDF() {
    new FDistribution(1, -2);
  }

  // Test for constructor
  @Test
  public void testConstructor() {
    FDistribution fDistribution = new FDistribution(3, 5);
    assertEquals(3.0, fDistribution.getNumeratorDegreesOfFreedom(), 0);
    assertEquals(5.0, fDistribution.getDenominatorDegreesOfFreedom(), 0);
  }

  // Test for density
  @Test
  public void testDensity() {
    FDistribution fDistribution = new FDistribution(3, 3);
    assertEquals(0.153, fDistribution.density(0.5), 0.001);
  }

  // Test for cumulativeProbability
  @Test
  public void testCumulativeProbability() {
    FDistribution fDistribution = new FDistribution(3, 3);
    assertEquals(0.357, fDistribution.cumulativeProbability(0.5), 0.001);
  }

  // Test for getNumericalMean
  @Test
  public void testGetNumericalMean() {
    FDistribution fDistribution = new FDistribution(3, 3);
    assertEquals(1.5, fDistribution.getNumericalMean(), 0);
  }

  // Test for getNumericalVariance
  @Test
  public void testGetNumericalVariance() {
    FDistribution fDistribution = new FDistribution(3, 3);
    assertEquals(4.5, fDistribution.getNumericalVariance(), 0);
  }

  // Test for getSupportLowerBound
  @Test
  public void testGetSupportLowerBound() {
    FDistribution fDistribution = new FDistribution(3, 3);
    assertEquals(0.0, fDistribution.getSupportLowerBound(), 0);
  }

  // Test for getSupportUpperBound
  @Test
  public void testGetSupportUpperBound() {
    FDistribution fDistribution = new FDistribution(3, 3);
    assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0);
  }

  // Test for isSupportLowerBoundInclusive
  @Test
  public void testIsSupportLowerBoundInclusive() {
    FDistribution fDistribution = new FDistribution(3, 3);
    assertEquals(true, fDistribution.isSupportLowerBoundInclusive());
  }

  // Test for isSupportUpperBoundInclusive
  @Test
  public void testIsSupportUpperBoundInclusive() {
    FDistribution fDistribution = new FDistribution(3, 3);
    assertEquals(false, fDistribution.isSupportUpperBoundInclusive());
  }

  // Test for isSupportConnected
  @Test
  public void testIsSupportConnected() {
    FDistribution fDistribution = new FDistribution(3, 3);
    assertEquals(true, fDistribution.isSupportConnected());
  }

}