import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.distribution.UniformRealDistribution;

public class UniformRealDistributionTest {
   @Test
   public void testDensity() {
      UniformRealDistribution dist = new UniformRealDistribution(0, 1);
      Assert.assertEquals(1.0, dist.density(0.5), 0.0001);
      Assert.assertEquals(0.0, dist.density(1.5), 0.0001);
      Assert.assertEquals(0.0, dist.density(-0.5), 0.0001);
   }

   @Test
   public void testCumulativeProbability() {
      UniformRealDistribution dist = new UniformRealDistribution(0, 1);
      Assert.assertEquals(0.5, dist.cumulativeProbability(0.5), 0.0001);
      Assert.assertEquals(0.0, dist.cumulativeProbability(-0.5), 0.0001);
      Assert.assertEquals(1.0, dist.cumulativeProbability(1.5), 0.0001);
   }

   @Test
   public void testGetNumericalMean() {
      UniformRealDistribution dist = new UniformRealDistribution(0, 1);
      Assert.assertEquals(0.5, dist.getNumericalMean(), 0.0001);
   }

   @Test
   public void testGetNumericalVariance() {
      UniformRealDistribution dist = new UniformRealDistribution(0, 1);
      Assert.assertEquals(0.0833333333, dist.getNumericalVariance(), 0.0001);
   }

   @Test
   public void testGetSupportLowerBound() {
      UniformRealDistribution dist = new UniformRealDistribution(0, 1);
      Assert.assertEquals(0.0, dist.getSupportLowerBound(), 0.0001);
   }

   @Test
   public void testGetSupportUpperBound() {
      UniformRealDistribution dist = new UniformRealDistribution(0, 1);
      Assert.assertEquals(1.0, dist.getSupportUpperBound(), 0.0001);
   }

   @Test
   public void testIsSupportLowerBoundInclusive() {
      UniformRealDistribution dist = new UniformRealDistribution(0, 1);
      Assert.assertEquals(true, dist.isSupportLowerBoundInclusive());
   }

   @Test
   public void testIsSupportUpperBoundInclusive() {
      UniformRealDistribution dist = new UniformRealDistribution(0, 1);
      Assert.assertEquals(false, dist.isSupportUpperBoundInclusive());
   }

   @Test
   public void testIsSupportConnected() {
      UniformRealDistribution dist = new UniformRealDistribution(0, 1);
      Assert.assertEquals(true, dist.isSupportConnected());
   }

   @Test
   public void testSample() {
      UniformRealDistribution dist = new UniformRealDistribution(0, 1);
      double sample = dist.sample();
      Assert.assertTrue(sample >= 0 && sample < 1);
   }
}