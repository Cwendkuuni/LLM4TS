import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {
  
  @Test
  public void testCumulativeProbability() throws MathException {
    FDistributionImpl fd = new FDistributionImpl(3, 5);
    double result = fd.cumulativeProbability(2.0);
    assertEquals(0.878479, result, 0.000001);
    
    double result2 = fd.cumulativeProbability(0.0);
    assertEquals(0.0, result2, 0.000001);
    
    double result3 = fd.cumulativeProbability(-1.0);
    assertEquals(0.0, result3, 0.000001);
  }
  
  @Test
  public void testInverseCumulativeProbability() throws MathException {
    FDistributionImpl fd = new FDistributionImpl(3, 5);
    double result = fd.inverseCumulativeProbability(0.5);
    assertEquals(0.979382, result, 0.000001);
    
    double result2 = fd.inverseCumulativeProbability(0.0);
    assertEquals(0.0, result2, 0.000001);
    
    double result3 = fd.inverseCumulativeProbability(1.0);
    assertEquals(Double.POSITIVE_INFINITY, result3, 0.000001);
  }
  
  @Test
  public void testGetDomainLowerBound() {
    FDistributionImpl fd = new FDistributionImpl(3, 5);
    double result = fd.getDomainLowerBound(0.5);
    assertEquals(0.0, result, 0.000001);
  }
  
  @Test
  public void testGetDomainUpperBound() {
    FDistributionImpl fd = new FDistributionImpl(3, 5);
    double result = fd.getDomainUpperBound(0.5);
    assertEquals(Double.MAX_VALUE, result, 0.000001);
  }
  
  @Test
  public void testGetInitialDomain() {
    FDistributionImpl fd = new FDistributionImpl(3, 5);
    double result = fd.getInitialDomain(0.5);
    assertEquals(1.666667, result, 0.000001);
  }
  
  @Test
  public void testSetNumeratorDegreesOfFreedom() {
    FDistributionImpl fd = new FDistributionImpl(3, 5);
    fd.setNumeratorDegreesOfFreedom(7);
    double result = fd.getNumeratorDegreesOfFreedom();
    assertEquals(7, result, 0.000001);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetNumeratorDegreesOfFreedomInvalid() {
    FDistributionImpl fd = new FDistributionImpl(3, 5);
    fd.setNumeratorDegreesOfFreedom(0);
  }
  
  @Test
  public void testGetNumeratorDegreesOfFreedom() {
    FDistributionImpl fd = new FDistributionImpl(3, 5);
    double result = fd.getNumeratorDegreesOfFreedom();
    assertEquals(3, result, 0.000001);
  }
  
  @Test
  public void testSetDenominatorDegreesOfFreedom() {
    FDistributionImpl fd = new FDistributionImpl(3, 5);
    fd.setDenominatorDegreesOfFreedom(10);
    double result = fd.getDenominatorDegreesOfFreedom();
    assertEquals(10, result, 0.000001);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetDenominatorDegreesOfFreedomInvalid() {
    FDistributionImpl fd = new FDistributionImpl(3, 5);
    fd.setDenominatorDegreesOfFreedom(0);
  }
  
  @Test
  public void testGetDenominatorDegreesOfFreedom() {
    FDistributionImpl fd = new FDistributionImpl(3, 5);
    double result = fd.getDenominatorDegreesOfFreedom();
    assertEquals(5, result, 0.000001);
  }
}