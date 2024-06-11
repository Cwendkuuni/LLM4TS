import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

  @Test
  public void testDensity() {
    // Test case 1
    FDistribution fd1 = new FDistribution(1, 1);
    assertEquals(0.0, fd1.density(0.0), 0.000001);
    assertEquals(0.25, fd1.density(1.0), 0.000001);
    assertEquals(Double.NaN, fd1.density(-1.0), 0.000001);

    // Test case 2
    FDistribution fd2 = new FDistribution(0.5, 0.5);
    assertEquals(0.3894004, fd2.density(1.0), 0.000001);
    assertEquals(Double.NaN, fd2.density(-1.0), 0.000001);

    // Test case 3
    FDistribution fd3 = new FDistribution(10, 5);
    assertEquals(0.00001589, fd3.density(2.0), 0.00000001);
    assertEquals(Double.NaN, fd3.density(-1.0), 0.000001);
  }


  @Test
  public void testCumulativeProbability() {
    // Test case 1
    FDistribution fd1 = new FDistribution(1, 1);
    assertEquals(0.0, fd1.cumulativeProbability(0.0), 0.000001);
    assertEquals(0.5, fd1.cumulativeProbability(1.0), 0.000001);
    assertEquals(1.0, fd1.cumulativeProbability(100.0), 0.000001);

    // Test case 2
    FDistribution fd2 = new FDistribution(0.5, 0.5);
    assertEquals(0.0, fd2.cumulativeProbability(0.0), 0.000001);
    assertEquals(0.5000005, fd2.cumulativeProbability(1.0), 0.000001);
    assertEquals(0.9999995, fd2.cumulativeProbability(100.0), 0.000001);

    // Test case 3
    FDistribution fd3 = new FDistribution(10, 5);
    assertEquals(0.0, fd3.cumulativeProbability(0.0), 0.000001);
    assertEquals(0.0179805, fd3.cumulativeProbability(2.0), 0.000001);
    assertEquals(0.4426104, fd3.cumulativeProbability(20.0), 0.000001);
    assertEquals(0.8308904, fd3.cumulativeProbability(100.0), 0.000001);
  }

  @Test
  public void testGetNumeratorDegreesOfFreedom() {
    // Test case 1
    FDistribution fd1 = new FDistribution(1, 1);
    assertEquals(1.0, fd1.getNumeratorDegreesOfFreedom(), 0.000001);

    // Test case 2
    FDistribution fd2 = new FDistribution(0.5, 0.5);
    assertEquals(0.5, fd2.getNumeratorDegreesOfFreedom(), 0.000001);

    // Test case 3
    FDistribution fd3 = new FDistribution(10, 5);
    assertEquals(10.0, fd3.getNumeratorDegreesOfFreedom(), 0.000001);
  }

  @Test
  public void testGetDenominatorDegreesOfFreedom() {
    // Test case 1
    FDistribution fd1 = new FDistribution(1, 1);
    assertEquals(1.0, fd1.getDenominatorDegreesOfFreedom(), 0.000001);

    // Test case 2
    FDistribution fd2 = new FDistribution(0.5, 0.5);
    assertEquals(0.5, fd2.getDenominatorDegreesOfFreedom(), 0.000001);

    // Test case 3
    FDistribution fd3 = new FDistribution(10, 5);
    assertEquals(5.0, fd3.getDenominatorDegreesOfFreedom(), 0.000001);
  }

  @Test
  public void testGetSupportLowerBound() {
    FDistribution fd1 = new FDistribution(1, 1);
    assertEquals(0.0, fd1.getSupportLowerBound(), 0.000001);
  }

  @Test
  public void testGetSupportUpperBound() {
    FDistribution fd1 = new FDistribution(1, 1);
    assertEquals(Double.POSITIVE_INFINITY, fd1.getSupportUpperBound(), 0.000001);
  }

  @Test
  public void testIsSupportLowerBoundInclusive() {
    FDistribution fd1 = new FDistribution(1, 1);
    assertTrue(fd1.isSupportLowerBoundInclusive());
  }

  @Test
  public void testIsSupportUpperBoundInclusive() {
    FDistribution fd1 = new FDistribution(1, 1);
    assertFalse(fd1.isSupportUpperBoundInclusive());
  }

  @Test
  public void testIsSupportConnected() {
    FDistribution fd1 = new FDistribution(1, 1);
    assertTrue(fd1.isSupportConnected());
  }
}