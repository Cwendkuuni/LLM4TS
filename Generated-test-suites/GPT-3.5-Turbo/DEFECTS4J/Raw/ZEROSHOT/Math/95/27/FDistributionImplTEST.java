import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FDistributionImplTest {
  
  private FDistributionImpl distribution;
  
  @Before
  public void setUp() {
    distribution = new FDistributionImpl(5, 10);
  }
  
  @Test
  public void testCumulativeProbability() throws MathException {
    double result = distribution.cumulativeProbability(0);
    assertEquals(0.0, result, 0.0001);
    
    result = distribution.cumulativeProbability(1);
    assertEquals(0.107062834, result, 0.0001);

    result = distribution.cumulativeProbability(2);
    assertEquals(0.242938348, result, 0.0001);
    
    // Add more test cases to cover all scenarios
  }
  
  @Test
  public void testInverseCumulativeProbability() throws MathException {
    double result = distribution.inverseCumulativeProbability(0);
    assertEquals(0.0, result, 0.0001);
    
    result = distribution.inverseCumulativeProbability(1);
    assertEquals(Double.POSITIVE_INFINITY, result, 0.0001);
    
    // Add more test cases to cover all scenarios
  }
  
  @Test
  public void testGetDomainLowerBound() {
    double result = distribution.getDomainLowerBound(0.1);
    assertEquals(0.0, result, 0.0001);
    
    result = distribution.getDomainLowerBound(0.5);
    assertEquals(0.0, result, 0.0001);
    
    // Add more test cases to cover all scenarios
  }
  
  @Test
  public void testGetDomainUpperBound() {
    double result = distribution.getDomainUpperBound(0.1);
    assertEquals(Double.MAX_VALUE, result, 0.0001);
    
    result = distribution.getDomainUpperBound(0.5);
    assertEquals(Double.MAX_VALUE, result, 0.0001);
    
    // Add more test cases to cover all scenarios
  }
  
  @Test
  public void testGetInitialDomain() {
    double result = distribution.getInitialDomain(0.1);
    assertEquals(5.0, result, 0.0001);
    
    result = distribution.getInitialDomain(0.5);
    assertEquals(5.0, result, 0.0001);
    
    // Add more test cases to cover all scenarios
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetNumeratorDegreesOfFreedom() {
    distribution.setNumeratorDegreesOfFreedom(0);
  }
  
  @Test
  public void testGetNumeratorDegreesOfFreedom() {
    double result = distribution.getNumeratorDegreesOfFreedom();
    assertEquals(5.0, result, 0.0001);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetDenominatorDegreesOfFreedom() {
    distribution.setDenominatorDegreesOfFreedom(0);
  }
  
  @Test
  public void testGetDenominatorDegreesOfFreedom() {
    double result = distribution.getDenominatorDegreesOfFreedom();
    assertEquals(10.0, result, 0.0001);
  }
}