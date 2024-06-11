package org.apache.commons.math3.distribution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UniformRealDistributionTest {
  
  private UniformRealDistribution distribution;
  
  @Before
  public void setup() {
    distribution = new UniformRealDistribution();
  }
  
  @Test
  public void testDensity_WhenXisWithinBounds_ExpectDensity() {
    double x = 0.5;
    double density = distribution.density(x);
    Assert.assertEquals(1 / (distribution.getSupportUpperBound() - distribution.getSupportLowerBound()), density, 0.00001);
  }
  
  @Test
  public void testDensity_WhenXisBelowLowerBound_ExpectZeroDensity() {
    double x = distribution.getSupportLowerBound() - 1;
    double density = distribution.density(x);
    Assert.assertEquals(0.0, density, 0.00001);
  }
  
  @Test
  public void testDensity_WhenXisAboveUpperBound_ExpectZeroDensity() {
    double x = distribution.getSupportUpperBound() + 1;
    double density = distribution.density(x);
    Assert.assertEquals(0.0, density, 0.00001);
  }
  
  @Test
  public void testCumulativeProbability_WhenXisEqualToLowerBound_ExpectZeroCumulativeProbability() {
    double x = distribution.getSupportLowerBound();
    double cumulativeProbability = distribution.cumulativeProbability(x);
    Assert.assertEquals(0.0, cumulativeProbability, 0.00001);
  }
  
  @Test
  public void testCumulativeProbability_WhenXisAboveUpperBound_ExpectOneCumulativeProbability() {
    double x = distribution.getSupportUpperBound() + 1;
    double cumulativeProbability = distribution.cumulativeProbability(x);
    Assert.assertEquals(1.0, cumulativeProbability, 0.00001);
  }
  
  @Test
  public void testCumulativeProbability_WhenXisWithinBounds_ExpectCumulativeProbability() {
    double x = 0.25;
    double cumulativeProbability = distribution.cumulativeProbability(x);
    double expectedProbability = (x - distribution.getSupportLowerBound()) / (distribution.getSupportUpperBound() - distribution.getSupportLowerBound());
    Assert.assertEquals(expectedProbability, cumulativeProbability, 0.00001);
  }
  
  @Test
  public void testGetNumericalMean_ExpectMeanEqualToHalfOfSumOfBounds() {
    double mean = distribution.getNumericalMean();
    double expectedMean = 0.5 * (distribution.getSupportLowerBound() + distribution.getSupportUpperBound());
    Assert.assertEquals(expectedMean, mean, 0.00001);
  }
  
  @Test
  public void testGetNumericalVariance_ExpectVarianceEqualToSquareOfSubtractionOfBoundsDividedByTwelve() {
    double variance = distribution.getNumericalVariance();
    double expectedVariance = Math.pow(distribution.getSupportUpperBound() - distribution.getSupportLowerBound(), 2) / 12;
    Assert.assertEquals(expectedVariance, variance, 0.00001);
  }
  
  @Test
  public void testGetSupportLowerBound_ExpectLowerBoundReturned() {
    double lowerBound = distribution.getSupportLowerBound();
    Assert.assertEquals(0, lowerBound, 0.00001);
  }
  
  @Test
  public void testGetSupportUpperBound_ExpectUpperBoundReturned() {
    double upperBound = distribution.getSupportUpperBound();
    Assert.assertEquals(1, upperBound, 0.00001);
  }
  
  @Test
  public void testIsSupportLowerBoundInclusive_ExpectTrue() {
    boolean inclusive = distribution.isSupportLowerBoundInclusive();
    Assert.assertTrue(inclusive);
  }
  
  @Test
  public void testIsSupportUpperBoundInclusive_ExpectFalse() {
    boolean inclusive = distribution.isSupportUpperBoundInclusive();
    Assert.assertFalse(inclusive);
  }
  
  @Test
  public void testIsSupportConnected_ExpectTrue() {
    boolean connected = distribution.isSupportConnected();
    Assert.assertTrue(connected);
  }
  
  @Test
  public void testSample_ExpectSampleWithinBounds() {
    double sample = distribution.sample();
    Assert.assertTrue(sample >= distribution.getSupportLowerBound());
    Assert.assertTrue(sample < distribution.getSupportUpperBound());
  }
  
}