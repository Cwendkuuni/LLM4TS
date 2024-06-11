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
   public void testDensity() {
      // Test for x < lower bound
      double x = -1.0;
      double expectedDensity = 0.0;
      double actualDensity = distribution.density(x);
      Assert.assertEquals(expectedDensity, actualDensity, 0.0001);
      
      // Test for x > upper bound
      x = 2.0;
      expectedDensity = 0.0;
      actualDensity = distribution.density(x);
      Assert.assertEquals(expectedDensity, actualDensity, 0.0001);
      
      // Test for x within bounds
      x = 0.5;
      expectedDensity = 1.0;
      actualDensity = distribution.density(x);
      Assert.assertEquals(expectedDensity, actualDensity, 0.0001);
   }
   
   @Test
   public void testCumulativeProbability() {
      // Test for x <= lower bound
      double x = -1.0;
      double expectedProbability = 0.0;
      double actualProbability = distribution.cumulativeProbability(x);
      Assert.assertEquals(expectedProbability, actualProbability, 0.0001);
      
      // Test for x >= upper bound
      x = 2.0;
      expectedProbability = 1.0;
      actualProbability = distribution.cumulativeProbability(x);
      Assert.assertEquals(expectedProbability, actualProbability, 0.0001);
      
      // Test for x within bounds
      x = 0.5;
      expectedProbability = 0.5;
      actualProbability = distribution.cumulativeProbability(x);
      Assert.assertEquals(expectedProbability, actualProbability, 0.0001);
   }
   
   @Test
   public void testGetSolverAbsoluteAccuracy() {
      double expectedAccuracy = 1e-9;
      double actualAccuracy = distribution.getSolverAbsoluteAccuracy();
      Assert.assertEquals(expectedAccuracy, actualAccuracy, 0.0001);
   }
   
   @Test
   public void testGetNumericalMean() {
      double expectedMean = 0.5;
      double actualMean = distribution.getNumericalMean();
      Assert.assertEquals(expectedMean, actualMean, 0.0001);
   }
   
   @Test
   public void testGetNumericalVariance() {
      double expectedVariance = 0.08333333333333333;
      double actualVariance = distribution.getNumericalVariance();
      Assert.assertEquals(expectedVariance, actualVariance, 0.0001);
   }
   
   @Test
   public void testGetSupportLowerBound() {
      double expectedLowerBound = 0;
      double actualLowerBound = distribution.getSupportLowerBound();
      Assert.assertEquals(expectedLowerBound, actualLowerBound, 0.0001);
   }
   
   @Test
   public void testGetSupportUpperBound() {
      double expectedUpperBound = 1;
      double actualUpperBound = distribution.getSupportUpperBound();
      Assert.assertEquals(expectedUpperBound, actualUpperBound, 0.0001);
   }
   
   @Test
   public void testIsSupportLowerBoundInclusive() {
      boolean expectedInclusive = true;
      boolean actualInclusive = distribution.isSupportLowerBoundInclusive();
      Assert.assertEquals(expectedInclusive, actualInclusive);
   }
   
   @Test
   public void testIsSupportUpperBoundInclusive() {
      boolean expectedInclusive = false;
      boolean actualInclusive = distribution.isSupportUpperBoundInclusive();
      Assert.assertEquals(expectedInclusive, actualInclusive);
   }
   
   @Test
   public void testIsSupportConnected() {
      boolean expectedConnected = true;
      boolean actualConnected = distribution.isSupportConnected();
      Assert.assertEquals(expectedConnected, actualConnected);
   }
   
   @Test
   public void testSample() {
      // Generate 1000 samples and check if they are between lower and upper bounds
      for (int i = 0; i < 1000; i++) {
         double sample = distribution.sample();
         Assert.assertTrue(sample >= 0 && sample < 1);
      }
   }
}