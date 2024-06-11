import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.exception.NotStrictlyPositiveException;

public class NormalDistributionImplTest {

  @Test
  public void testGetMean() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10, 2);
    double expectedMean = 10;
    double actualMean = normalDistribution.getMean();

    assertEquals(expectedMean, actualMean, 0.0001);
  }

  @Test
  public void testGetStandardDeviation() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10, 2);
    double expectedStandardDeviation = 2;
    double actualStandardDeviation = normalDistribution.getStandardDeviation();

    assertEquals(expectedStandardDeviation, actualStandardDeviation, 0.0001);
  }

  @Test
  public void testDensity() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
    double x = 1;
    double expectedDensity = 0.24197072451914337;
    double actualDensity = normalDistribution.density(x);

    assertEquals(expectedDensity, actualDensity, 0.0001);
  }

  @Test
  public void testCumulativeProbability() throws MathException {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
    double x = 1;
    double expectedCumulativeProbability = 0.841344735;
    double actualCumulativeProbability = normalDistribution.cumulativeProbability(x);

    assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.0001);
  }

  @Test
  public void testCumulativeProbabilityWithMaxIterationsExceededException() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
    double x = -999999;
    double expectedCumulativeProbability = 0;
    double actualCumulativeProbability;
    try {
        actualCumulativeProbability = normalDistribution.cumulativeProbability(x);
    } catch (MathException e) {
        actualCumulativeProbability = -1;
    }

    assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.0001);
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testNormalDistributionWithNegativeStandardDeviation() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10, -2);
  }

  @Test
  public void testInverseCumulativeProbability() throws MathException {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
    double p = 0.5;
    double expectedInverseCumulativeProbability = 0;
    double actualInverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(p);

    assertEquals(expectedInverseCumulativeProbability, actualInverseCumulativeProbability, 0.0001);
  }

  @Test
  public void testInverseCumulativeProbabilityWithP0() throws MathException {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
    double p = 0;
    double expectedInverseCumulativeProbability = Double.NEGATIVE_INFINITY;
    double actualInverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(p);

    assertEquals(expectedInverseCumulativeProbability, actualInverseCumulativeProbability, 0.0001);
  }

  @Test
  public void testInverseCumulativeProbabilityWithP1() throws MathException {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
    double p = 1;
    double expectedInverseCumulativeProbability = Double.POSITIVE_INFINITY;
    double actualInverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(p);

    assertEquals(expectedInverseCumulativeProbability, actualInverseCumulativeProbability, 0.0001);
  }

  @Test
  public void testSample() throws MathException {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
    double randomSample = normalDistribution.sample();
    assertTrue(randomSample >= Double.NEGATIVE_INFINITY && randomSample <= Double.POSITIVE_INFINITY);
  }

  @Test
  public void testGetDomainLowerBound() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
    double p = 0.3;
    double expectedLowerBound = -Double.MAX_VALUE;
    double actualLowerBound = normalDistribution.getDomainLowerBound(p);

    assertEquals(expectedLowerBound, actualLowerBound, 0.0001);
  }

  @Test
  public void testGetDomainUpperBound() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
    double p = 0.7;
    double expectedUpperBound = Double.MAX_VALUE;
    double actualUpperBound = normalDistribution.getDomainUpperBound(p);

    assertEquals(expectedUpperBound, actualUpperBound, 0.0001);
  }

  @Test
  public void testGetInitialDomain() {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
    double p = 0.5;
    double expectedInitialDomain = 0;
    double actualInitialDomain = normalDistribution.getInitialDomain(p);

    assertEquals(expectedInitialDomain, actualInitialDomain, 0.0001);
  }

}