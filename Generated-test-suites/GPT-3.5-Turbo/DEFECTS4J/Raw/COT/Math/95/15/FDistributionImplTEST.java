import org.junit.BeforeClass;
import org.junit.Test;

public class FDistributionImplTest {
  
  private static FDistributionImpl fDist;

  @BeforeClass
  public static void setUp() {
    fDist = new FDistributionImpl(2.0, 3.0);
  }

  @Test
  public void testCumulativeProbability() throws MathException {
    double result = fDist.cumulativeProbability(1.0);
    assertEquals(result, 0.8063828961057111, 0.00001);
  }

  @Test
  public void testInverseCumulativeProbability() throws MathException {
    double result = fDist.inverseCumulativeProbability(0.5);
    assertEquals(result, 0.6063790901171008, 0.00001);
  }

  @Test
  public void testGetDomainLowerBound() {
    double result = fDist.getDomainLowerBound(0.2);
    assertEquals(result, 0.0, 0.0);
  }

  @Test
  public void testGetDomainUpperBound() {
    double result = fDist.getDomainUpperBound(0.8);
    assertEquals(result, Double.MAX_VALUE, 0.0);
  }

  @Test
  public void testGetInitialDomain() {
    double result = fDist.getInitialDomain(0.5);
    assertEquals(result, 3.0, 0.0);
  }

  @Test
  public void testSetNumeratorDegreesOfFreedom() {
    fDist.setNumeratorDegreesOfFreedom(1.5);
    double result = fDist.getNumeratorDegreesOfFreedom();
    assertEquals(result, 1.5, 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeNumeratorDegreesOfFreedom() {
    fDist.setNumeratorDegreesOfFreedom(-1.0);
  }

  @Test
  public void testSetDenominatorDegreesOfFreedom() {
    fDist.setDenominatorDegreesOfFreedom(4.0);
    double result = fDist.getDenominatorDegreesOfFreedom();
    assertEquals(result, 4.0, 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeDenominatorDegreesOfFreedom() {
    fDist.setDenominatorDegreesOfFreedom(-2.0);
  }
}