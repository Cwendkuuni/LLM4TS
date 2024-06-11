import org.junit.Test;
import org.apache.commons.math3.distribution.FDistribution;

public class FDistributionTest {

  // Test public FDistribution(double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom)
  @Test
  public void testConstructor() {
    FDistribution f = new FDistribution(5, 10);

    // Check that numeratorDegreesOfFreedom and denominatorDegreesOfFreedom are set correctly
    assert(f.getNumeratorDegreesOfFreedom() == 5);
    assert(f.getDenominatorDegreesOfFreedom() == 10);
  }

  // Test public double density(double x)
  @Test
  public void testDensity() {
    FDistribution f = new FDistribution(5, 10);

    // Test some specific values
    assert(f.density(0) == Double.POSITIVE_INFINITY);
    assert(f.density(1) == 0.08944271909999159);
    assert(f.density(5) == 0.21820321057778807);
  }

  // Test public double cumulativeProbability(double x)
  @Test
  public void testCumulativeProbability() {
    FDistribution f = new FDistribution(5, 10);

    // Test some specific values
    assert(f.cumulativeProbability(0) == 0);
    assert(f.cumulativeProbability(1) == 0.17313448880249728);
    assert(f.cumulativeProbability(5) == 0.910957373024936);
  }

  // Test public double getNumericalMean()
  @Test
  public void testGetNumericalMean() {
    FDistribution f = new FDistribution(5, 10);

    // Check that the mean is calculated correctly
    assert(f.getNumericalMean() == 1.25);
  }

  // Test public double getNumericalVariance()
  @Test
  public void testGetNumericalVariance() {
    FDistribution f = new FDistribution(5, 10);

    // Check that the variance is calculated correctly
    assert(f.getNumericalVariance() == 6.944444444444444);
  }

  // Test public double getSupportLowerBound()
  @Test
  public void testGetSupportLowerBound() {
    FDistribution f = new FDistribution(5, 10);

    // Check that the lower bound is always 0
    assert(f.getSupportLowerBound() == 0);
  }

  // Test public double getSupportUpperBound()
  @Test
  public void testGetSupportUpperBound() {
    FDistribution f = new FDistribution(5, 10);

    // Check that the upper bound is always positive infinity
    assert(f.getSupportUpperBound() == Double.POSITIVE_INFINITY);
  }

  // Test public boolean isSupportLowerBoundInclusive()
  @Test
  public void testIsSupportLowerBoundInclusive() {
    FDistribution f = new FDistribution(5, 10);

    // Check that the lower bound is inclusive
    assert(f.isSupportLowerBoundInclusive() == true);
  }

  // Test public boolean isSupportUpperBoundInclusive()
  @Test
  public void testIsSupportUpperBoundInclusive() {
    FDistribution f = new FDistribution(5, 10);

    // Check that the upper bound is exclusive
    assert(f.isSupportUpperBoundInclusive() == false);
  }

  // Test public boolean isSupportConnected()
  @Test
  public void testIsSupportConnected() {
    FDistribution f = new FDistribution(5, 10);

    // Check that the support is connected
    assert(f.isSupportConnected() == true);
  }

}