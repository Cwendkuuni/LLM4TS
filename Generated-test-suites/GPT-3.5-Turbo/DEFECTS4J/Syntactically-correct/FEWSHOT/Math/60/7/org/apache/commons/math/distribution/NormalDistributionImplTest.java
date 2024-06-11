package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
  
  private NormalDistributionImpl normalDistribution;
  
  @Before
  public void setUp() {
    normalDistribution = new NormalDistributionImpl(0,1);
  }
  
  @Test
  public void testGetMean() {
    assertEquals(0.0, normalDistribution.getMean(), 0.0);
  }
  
  @Test
  public void testGetStandardDeviation() {
    assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0);
  }
  
  @Test
  public void testDensity() {
    assertEquals(0.398942280401433, normalDistribution.density(0), 1e-9);
  }
  
  @Test
  public void testCumulativeProbability() {
    try {
      normalDistribution.cumulativeProbability(0);
    } catch (MathException e) {
      fail("An exception should not be thrown");
    }
  }
  
  @Test(expected = MathException.class)
  public void testCumulativeProbabilityWithException() throws MathException {
    normalDistribution.cumulativeProbability(Double.MAX_VALUE);
  }
  
  @Test
  public void testInverseCumulativeProbabilityWithMin() {
    try {
      double result = normalDistribution.inverseCumulativeProbability(0);
      assertEquals(Double.NEGATIVE_INFINITY, result, 0.0);
    } catch (MathException e) {
      fail("An exception should not be thrown");
    }
  }
  
  @Test
  public void testInverseCumulativeProbabilityWithMax() {
    try {
      double result = normalDistribution.inverseCumulativeProbability(1);
      assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
    } catch (MathException e) {
      fail("An exception should not be thrown");
    }
  }
  
  @Test
  public void testSample() {
    try {
      double result = normalDistribution.sample();
      assertTrue(result >= Double.NEGATIVE_INFINITY);
      assertTrue(result <= Double.POSITIVE_INFINITY);
    } catch (MathException e) {
      fail("An exception should not be thrown");
    }
  }
}