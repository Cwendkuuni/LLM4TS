import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {

  // Test Object
  private FDistribution fDistribution;

  @Test
  public void testConstructor() {
    try {
      fDistribution = new FDistribution(0, 0);
      Assert.fail("NotStrictlyPositiveException should be thrown");
    } catch (NotStrictlyPositiveException e) {
      // Test passed
    } catch (Exception e) {
      Assert.fail("Unexpected Exception: " + e.getClass().getSimpleName());
    }

    try {
      fDistribution = new FDistribution(-10, 20);
      Assert.fail("NotStrictlyPositiveException should be thrown");
    } catch (NotStrictlyPositiveException e) {
      // Test passed
    } catch (Exception e) {
      Assert.fail("Unexpected Exception: " + e.getClass().getSimpleName());
    }

    try {
      fDistribution = new FDistribution(10, -20);
      Assert.fail("NotStrictlyPositiveException should be thrown");
    } catch (NotStrictlyPositiveException e) {
      // Test passed
    } catch (Exception e) {
      Assert.fail("Unexpected Exception: " + e.getClass().getSimpleName());
    }

    try {
      fDistribution = new FDistribution(10, 10);
      Assert.assertEquals(10, fDistribution.getNumeratorDegreesOfFreedom(), 0);
      Assert.assertEquals(10, fDistribution.getDenominatorDegreesOfFreedom(), 0);
    } catch (Exception e) {
      Assert.fail("Unexpected Exception: " + e.getClass().getSimpleName());
    }
  }

  @Test
  public void testDensity() {
    fDistribution = new FDistribution(10, 20);

    // Test typical use case
    double density = fDistribution.density(1);
    Assert.assertEquals(0.059051, density, 0.000001);

    // Test edge case: x = 0
    density = fDistribution.density(0);
    Assert.assertEquals(0, density, 0);

    // Test error scenario: x < 0
    density = fDistribution.density(-1);
    Assert.assertEquals(0, density, 0);
  }

  @Test
  public void testCumulativeProbability() {
    fDistribution = new FDistribution(10, 20);

    // Test typical use case
    double probability = fDistribution.cumulativeProbability(1);
    Assert.assertEquals(0.705362, probability, 0.000001);

    // Test edge case: x = 0
    probability = fDistribution.cumulativeProbability(0);
    Assert.assertEquals(0, probability, 0);

    // Test error scenario: x < 0
    probability = fDistribution.cumulativeProbability(-1);
    Assert.assertEquals(0, probability, 0);
  }

  @Test
  public void testGetNumericalMean() {
    fDistribution = new FDistribution(10, 20);

    // Test typical use case
    double mean = fDistribution.getNumericalMean();
    Assert.assertEquals(1.25, mean, 0);

    // Test edge case: denominatorDF < 2
    fDistribution = new FDistribution(10, 1);
    mean = fDistribution.getNumericalMean();
    Assert.assertTrue(Double.isNaN(mean));
  }

  @Test
  public void testGetNumericalVariance() {
    fDistribution = new FDistribution(10, 20);

    // Test typical use case
    double variance = fDistribution.getNumericalVariance();
    Assert.assertEquals(15, variance, 0);

    // Test edge case: denominatorDF < 4
    fDistribution = new FDistribution(10, 3);
    variance = fDistribution.getNumericalVariance();
    Assert.assertTrue(Double.isNaN(variance));
  }

  @Test
  public void testGetSupportLowerBound() {
    fDistribution = new FDistribution(10, 20);

    // Test lower bound
    double lowerBound = fDistribution.getSupportLowerBound();
    Assert.assertEquals(0, lowerBound, 0);
  }

  @Test
  public void testGetSupportUpperBound() {
    fDistribution = new FDistribution(10, 20);

    // Test upper bound
    double upperBound = fDistribution.getSupportUpperBound();
    Assert.assertEquals(Double.POSITIVE_INFINITY, upperBound, 0);
  }


  @Test
  public void testisSupportLowerBoundInclusive() {
    fDistribution = new FDistribution(10, 20);

    // Test lower bound inclusivity
    boolean inclusive = fDistribution.isSupportLowerBoundInclusive();
    Assert.assertTrue(inclusive);
  }

  @Test
  public void tesIsSupportUpperBoundInclusive() {
    fDistribution = new FDistribution(10, 20);

    // Test upper bound inclusivity
    boolean inclusive = fDistribution.isSupportUpperBoundInclusive();
    Assert.assertFalse(inclusive);
  }

  @Test
  public void testIsSupportConnected() {
    fDistribution = new FDistribution(10, 20);

    // Test support connection
    boolean connected = fDistribution.isSupportConnected();
    Assert.assertTrue(connected);
  }
}