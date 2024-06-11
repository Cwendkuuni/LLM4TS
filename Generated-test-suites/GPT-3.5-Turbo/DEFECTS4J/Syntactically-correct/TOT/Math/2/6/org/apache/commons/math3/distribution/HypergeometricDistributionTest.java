import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {
  
  // Test constructor
  
  @Test
  public void testConstructor() {
    try {
      HypergeometricDistribution hd = new HypergeometricDistribution(-1, 2, 3);
      fail("IllegalArgumentException should be thrown for negative population size");
    } catch (NotStrictlyPositiveException e) {
      // Pass
    }
    try {
      HypergeometricDistribution hd = new HypergeometricDistribution(1, -2, 3);
      fail("IllegalArgumentException should be thrown for negative number of successes");
    } catch (NotPositiveException e) {
      // Pass
    }
    try {
      HypergeometricDistribution hd = new HypergeometricDistribution(1, 2, -3);
      fail("IllegalArgumentException should be thrown for negative sample size");
    } catch (NotPositiveException e) {
      // Pass
    }
    try {
      HypergeometricDistribution hd = new HypergeometricDistribution(3, 4, 5);
      fail("IllegalArgumentException should be thrown for number of successes > population size");
    } catch (NumberIsTooLargeException e) {
      // Pass
    }
    try {
      HypergeometricDistribution hd = new HypergeometricDistribution(5, 3, 6);
      fail("IllegalArgumentException should be thrown for sample size > population size");
    } catch (NumberIsTooLargeException e) {
      // Pass
    }
  }
  
  // Test cumulativeProbability method
  
  @Test
  public void testCumulativeProbability() {
    HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 2);
    assertEquals(0, hd.cumulativeProbability(0), 0);
    assertEquals(0.363636, hd.cumulativeProbability(1), 0.000001);
    assertEquals(0.636363, hd.cumulativeProbability(2), 0.000001);
    assertEquals(1, hd.cumulativeProbability(3), 0);
    assertEquals(1, hd.cumulativeProbability(4), 0);
  }
  
  // Test probability method
  
  @Test
  public void testProbability() {
    HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 2);
    assertEquals(1.0/33, hd.probability(0), 0.000001);
    assertEquals(8.0/33, hd.probability(1), 0.000001);
    assertEquals(8.0/33, hd.probability(2), 0.000001);
    assertEquals(8.0/33, hd.probability(3), 0.000001);
    assertEquals(1.0/33, hd.probability(4), 0.000001);
  }
  
  // Test upperCumulativeProbability method
  
  @Test
  public void testUpperCumulativeProbability() {
    HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 2);
    assertEquals(1, hd.upperCumulativeProbability(0), 0);
    assertEquals(1, hd.upperCumulativeProbability(1), 0);
    assertEquals(0.636363, hd.upperCumulativeProbability(2), 0.000001);
    assertEquals(0.363636, hd.upperCumulativeProbability(3), 0.000001);
    assertEquals(0, hd.upperCumulativeProbability(4), 0);
  }
  
  // Test getNumericalMean method
  
  @Test
  public void testGetNumericalMean() {
    HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 2);
    assertEquals(1, hd.getNumericalMean(), 0);
  }
  
  // Test getNumericalVariance method
  
  @Test
  public void testGetNumericalVariance() {
    HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 2);
    assertEquals(1.818182, hd.getNumericalVariance(), 0.000001);
  }
  
  // Test getSupportLowerBound method
  
  @Test
  public void testGetSupportLowerBound() {
    HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 2);
    assertEquals(0, hd.getSupportLowerBound());
  }
  
  // Test getSupportUpperBound method
  
  @Test
  public void testGetSupportUpperBound() {
    HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 2);
    assertEquals(2, hd.getSupportUpperBound());
  }
  
  // Test isSupportConnected method
  
  @Test
  public void testIsSupportConnected() {
    HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 2);
    assertTrue(hd.isSupportConnected());
  }
}