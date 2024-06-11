import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class UniformRealDistributionTest {

   @Test
   public void testDensity_typicalCase() {
      UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
      double result = distribution.density(0.5);
      Assert.assertEquals(1.0, result, 0.0001);
   }

   @Test
   public void testDensity_lowerBound() {
      UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
      double result = distribution.density(0.0);
      Assert.assertEquals(1.0, result, 0.0001);
   }

   @Test
   public void testDensity_upperBound() {
      UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
      double result = distribution.density(1.0);
      Assert.assertEquals(1.0, result, 0.0001);
   }

   @Test
   public void testDensity_outsideBounds() {
      UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
      double result = distribution.density(-1.0);
      Assert.assertEquals(0.0, result, 0.0001);
   }

   @Test
   public void testCumulativeProbability_typicalCase() {
      UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
      double result = distribution.cumulativeProbability(0.5);
      Assert.assertEquals(0.5, result, 0.0001);
   }

   @Test
   public void testCumulativeProbability_lowerBound() {
      UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
      double result = distribution.cumulativeProbability(0.0);
      Assert.assertEquals(0.0, result, 0.0001);
   }

   @Test
   public void testCumulativeProbability_upperBound() {
      UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
      double result = distribution.cumulativeProbability(1.0);
      Assert.assertEquals(1.0, result, 0.0001);
   }

   @Test
   public void testCumulativeProbability_outsideBounds() {
      UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
      double result = distribution.cumulativeProbability(-1.0);
      Assert.assertEquals(0.0, result, 0.0001);
   }

   @Test
   public void testGetNumericalMean_typicalCase() {
      UniformRealDistribution distribution = new UniformRealDistribution(0, 3);
      double result = distribution.getNumericalMean();
      Assert.assertEquals(1.5, result, 0.0001);
   }

   @Test(expected = NumberIsTooLargeException.class)
   public void testConstructor_upperBoundSmallerThanLowerBound() {
      new UniformRealDistribution(3, 0);
   }

   @Test
   public void testGetSupportLowerBound() {
      UniformRealDistribution distribution = new UniformRealDistribution(-1, 1);
      double result = distribution.getSupportLowerBound();
      Assert.assertEquals(-1.0, result, 0.0001);
   }

   @Test
   public void testGetSupportUpperBound() {
      UniformRealDistribution distribution = new UniformRealDistribution(-1, 1);
      double result = distribution.getSupportUpperBound();
      Assert.assertEquals(1.0, result, 0.0001);
   }

   @Test
   public void testIsSupportLowerBoundInclusive() {
      UniformRealDistribution distribution = new UniformRealDistribution(-1, 1);
      boolean result = distribution.isSupportLowerBoundInclusive();
      Assert.assertTrue(result);
   }

   @Test
   public void testIsSupportUpperBoundInclusive_neFalse() {
      UniformRealDistribution distribution = new UniformRealDistribution(-1, 1);
      boolean result = distribution.isSupportUpperBoundInclusive();
      Assert.assertFalse(result);
   }

   @Test
   public void testIsSupportConnected() {
      UniformRealDistribution distribution = new UniformRealDistribution(-1, 1);
      boolean result = distribution.isSupportConnected();
      Assert.assertTrue(result);
   }

   @Test
   public void testSample_typicalCase() {
      UniformRealDistribution distribution = new UniformRealDistribution(-1, 1);
      double result = distribution.sample();
      Assert.assertTrue(result >= -1.0 && result <= 1.0);
   }

}