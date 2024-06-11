import org.junit.Test;
import org.apache.commons.math.MathException;

public class FDistributionImplTest {

  private final double EPSILON = 1e-6;

  @Test
  public void testCumulativeProbability() throws MathException {
    FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
    double result = fd.cumulativeProbability(3.0);
    assertEquals(0.703703, result, EPSILON);
  }

  @Test
  public void testInverseCumulativeProbability() throws MathException {
    FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
    double result = fd.inverseCumulativeProbability(0.5);
    assertEquals(1.317673, result, EPSILON);
  }

  @Test
  public void testGetDomainLowerBound() {
    FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
    double result = fd.getDomainLowerBound(0.2);
    assertEquals(0.0, result, EPSILON);
  }

  @Test
  public void testGetDomainUpperBound() {
    FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
    double result = fd.getDomainUpperBound(0.8);
    assertEquals(Double.MAX_VALUE, result, EPSILON);
  }

  @Test
  public void testGetInitialDomain() {
    FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
    double result = fd.getInitialDomain(0.5);
    assertEquals(1.333333, result, EPSILON);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetNumeratorDegreesOfFreedom() {
    FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
    fd.setNumeratorDegreesOfFreedom(-1.0);
  }
  
  @Test
  public void testGetNumeratorDegreesOfFreedom() {
    FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
    double result = fd.getNumeratorDegreesOfFreedom();
    assertEquals(3.0, result, EPSILON);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetDenominatorDegreesOfFreedom() {
    FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
    fd.setDenominatorDegreesOfFreedom(-1.0);
  }
  
  @Test
  public void testGetDenominatorDegreesOfFreedom() {
    FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
    double result = fd.getDenominatorDegreesOfFreedom();
    assertEquals(4.0, result, EPSILON);
  }

}