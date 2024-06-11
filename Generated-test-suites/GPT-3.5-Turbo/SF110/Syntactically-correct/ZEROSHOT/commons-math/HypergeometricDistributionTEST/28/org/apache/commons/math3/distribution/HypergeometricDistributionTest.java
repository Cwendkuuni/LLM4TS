package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {
  
  // Test cumulativeProbability method
  @Test
  public void testCumulativeProbability() {
    HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    
    // Test x < lower domain
    double result1 = hypergeometricDistribution.cumulativeProbability(0);
    assertEquals(0.0, result1, 0.0001);
    
    // Test x > upper domain
    double result2 = hypergeometricDistribution.cumulativeProbability(7);
    assertEquals(1.0, result2, 0.0001);
    
    // Test innerCumulativeProbability
    double result3 = hypergeometricDistribution.cumulativeProbability(4);
    assertEquals(0.9444, result3, 0.0001);
  }
  
  // Test getDomain method
  @Test
  public void testGetDomain() {
    HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    
    int[] result = hypergeometricDistribution.getDomain(10, 5, 3);
    
    assertEquals(1, result[0]);
    assertEquals(3, result[1]);
  }
  
  // Test getLowerDomain method
  @Test
  public void testGetLowerDomain() {
    HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    
    int result = hypergeometricDistribution.getLowerDomain(10, 5, 3);
    
    assertEquals(2, result);
  }
  
  // Test getNumberOfSuccesses method
  @Test
  public void testGetNumberOfSuccesses() {
    HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    
    int result = hypergeometricDistribution.getNumberOfSuccesses();
    
    assertEquals(5, result);
  }
  
  // Test getPopulationSize method
  @Test
  public void testGetPopulationSize() {
    HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    
    int result = hypergeometricDistribution.getPopulationSize();
    
    assertEquals(10, result);
  }
  
  // Test getSampleSize method
  @Test
  public void testGetSampleSize() {
    HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    
    int result = hypergeometricDistribution.getSampleSize();
    
    assertEquals(3, result);
  }
  
  // Test getUpperDomain method
  @Test
  public void testGetUpperDomain() {
    HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    
    int result = hypergeometricDistribution.getUpperDomain(5, 3);
    
    assertEquals(3, result);
  }
  
  // Test probability method
  @Test
  public void testProbability() {
    HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    
    // Test x < lower domain or x > upper domain
    double result1 = hypergeometricDistribution.probability(0);
    assertEquals(0.0, result1, 0.0001);
    
    // Test x within domain
    double result2 = hypergeometricDistribution.probability(2);
    assertEquals(0.2308, result2, 0.0001);
  }
  
  // Test upperCumulativeProbability method
  @Test
  public void testUpperCumulativeProbability() {
    HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    
    // Test x <= lower domain
    double result1 = hypergeometricDistribution.upperCumulativeProbability(1);
    assertEquals(1.0, result1, 0.0001);
    
    // Test x > upper domain
    double result2 = hypergeometricDistribution.upperCumulativeProbability(6);
    assertEquals(0.0, result2, 0.0001);
    
    // Test innerCumulativeProbability
    double result3 = hypergeometricDistribution.upperCumulativeProbability(3);
    assertEquals(0.9444, result3, 0.0001);
  }
  
  // Test innerCumulativeProbability method
  @Test
  public void testInnerCumulativeProbability() {
    HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    
    double result = hypergeometricDistribution.innerCumulativeProbability(3, 4, 1);
    
    assertEquals(0.8416, result, 0.0001);
  }
  
  // Test getNumericalMean method
  @Test
  public void testGetNumericalMean() {
    HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    
    double result = hypergeometricDistribution.getNumericalMean();
    
    assertEquals(1.5, result, 0.0001);
  }
  
  // Test getNumericalVariance method
  @Test
  public void testGetNumericalVariance() {
    HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    
    double result = hypergeometricDistribution.getNumericalVariance();
    
    assertEquals(0.375, result, 0.0001);
  }
  
  // Test calculateNumericalVariance method
  @Test
  public void testCalculateNumericalVariance() {
    HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    
    double result = hypergeometricDistribution.calculateNumericalVariance();
    
    assertEquals(0.375, result, 0.0001);
  }
  
  // Test getSupportLowerBound method
  @Test
  public void testGetSupportLowerBound() {
    HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    
    int result = hypergeometricDistribution.getSupportLowerBound();
    
    assertEquals(3, result);
  }
  
  // Test getSupportUpperBound method
  @Test
  public void testGetSupportUpperBound() {
    HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    
    int result = hypergeometricDistribution.getSupportUpperBound();
    
    assertEquals(3, result);
  }
  
  // Test isSupportConnected method
  @Test
  public void testIsSupportConnected() {
    HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    
    boolean result = hypergeometricDistribution.isSupportConnected();
    
    assertTrue(result);
  }
}