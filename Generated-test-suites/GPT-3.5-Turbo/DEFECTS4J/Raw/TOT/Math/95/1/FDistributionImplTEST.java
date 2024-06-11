import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FDistributionImplTest {
  
  private FDistributionImpl fDistribution;

  @Before
  public void setUp() {
    fDistribution = new FDistributionImpl(2, 2);
  }
  
  @Test
  public void testCumulativeProbability() {
    // Typical use case: x = 2.0
    double result = fDistribution.cumulativeProbability(2.0);
    assertEquals(0.7310585786300049, result, 0.0001);
    
    // Edge case: x = 0.0
    result = fDistribution.cumulativeProbability(0.0);
    assertEquals(0.0, result, 0.0001);
    
    // Error scenario: negative value of x
    try {
      result = fDistribution.cumulativeProbability(-1.0);
      fail("Expected MathException not thrown");
    } catch (MathException e) {
      // expected
    }
  }
  
  @Test
  public void testInverseCumulativeProbability() {
    // Typical use case: p = 0.5
    double result = fDistribution.inverseCumulativeProbability(0.5);
    assertEquals(1.0, result, 0.0001);
    
    // Edge case: p = 0.0
    result = fDistribution.inverseCumulativeProbability(0.0);
    assertEquals(0.0, result, 0.0001);
    
    // Edge case: p = 1.0
    result = fDistribution.inverseCumulativeProbability(1.0);
    assertEquals(Double.POSITIVE_INFINITY, result, 0.0001);
    
    // Error scenario: invalid value of p
    try {
      result = fDistribution.inverseCumulativeProbability(1.5);
      fail("Expected MathException not thrown");
    } catch (MathException e) {
      // expected
    }
  }
  
  @Test
  public void testGetDomainLowerBound() {
    // Typical use case: p = 0.5
    double result = fDistribution.getDomainLowerBound(0.5);
    assertEquals(0.0, result, 0.0001);
    
    // Edge case: p = 0.0
    result = fDistribution.getDomainLowerBound(0.0);
    assertEquals(0.0, result, 0.0001);
  }
  
  @Test
  public void testGetDomainUpperBound() {
    // Typical use case: p = 0.5
    double result = fDistribution.getDomainUpperBound(0.5);
    assertEquals(Double.MAX_VALUE, result, 0.0001);
    
    // Edge case: p = 0.0
    result = fDistribution.getDomainLowerBound(0.0);
    assertEquals(Double.MAX_VALUE, result, 0.0001);
  }
  
  @Test
  public void testGetInitialDomain() {
    // Typical use case: p = 0.5
    double result = fDistribution.getInitialDomain(0.5);
    assertEquals(1.0, result, 0.0001);
  }
  
  @Test
  public void testSetNumeratorDegreesOfFreedom() {
    // Typical use case: valid degrees of freedom
    fDistribution.setNumeratorDegreesOfFreedom(5);
    assertEquals(5.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    
    // Error scenario: invalid degrees of freedom
    try {
      fDistribution.setNumeratorDegreesOfFreedom(0);
      fail("Expected IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
  
  @Test
  public void testSetDenominatorDegreesOfFreedom() {
    // Typical use case: valid degrees of freedom
    fDistribution.setDenominatorDegreesOfFreedom(5);
    assertEquals(5.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    
    // Error scenario: invalid degrees of freedom
    try {
      fDistribution.setDenominatorDegreesOfFreedom(0);
      fail("Expected IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
}