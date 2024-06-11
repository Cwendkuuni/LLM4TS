import org.apache.commons.math3.distribution.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

   @Test
   public void testDensity() {
      FDistribution dist = new FDistribution(2, 3);
      assertEquals(0.1055728, dist.density(0.5), 0.000001);
   }

   @Test
   public void testCumulativeProbability() {
      FDistribution dist = new FDistribution(2, 3);
      assertEquals(0.32726992, dist.cumulativeProbability(0.5), 0.000001);
   }

   @Test
   public void testGetNumeratorDegreesOfFreedom() {
      FDistribution dist = new FDistribution(2, 3);
      assertEquals(2, dist.getNumeratorDegreesOfFreedom(), 0);
   }

   @Test
   public void testGetDenominatorDegreesOfFreedom() {
      FDistribution dist = new FDistribution(2, 3);
      assertEquals(3, dist.getDenominatorDegreesOfFreedom(), 0);
   }

   @Test
   public void testGetNumericalMean() {
      FDistribution dist = new FDistribution(2, 3);
      assertEquals(1.5, dist.getNumericalMean(), 0);
   }

   @Test
   public void testGetNumericalVariance() {
      FDistribution dist = new FDistribution(2, 3);
      assertEquals(6, dist.getNumericalVariance(), 0);
   }

   @Test
   public void testGetSupportLowerBound() {
      FDistribution dist = new FDistribution(2, 3);
      assertEquals(0, dist.getSupportLowerBound(), 0);
   }

   @Test
   public void testGetSupportUpperBound() {
      FDistribution dist = new FDistribution(2, 3);
      assertEquals(Double.POSITIVE_INFINITY, dist.getSupportUpperBound(), 0);
   }

   @Test
   public void testIsSupportLowerBoundInclusive() {
      FDistribution dist = new FDistribution(2, 3);
      assertTrue(dist.isSupportLowerBoundInclusive());
   }

   @Test
   public void testIsSupportUpperBoundInclusive() {
      FDistribution dist = new FDistribution(2, 3);
      assertFalse(dist.isSupportUpperBoundInclusive());
   }

   @Test
   public void testIsSupportConnected() {
      FDistribution dist = new FDistribution(2, 3);
      assertTrue(dist.isSupportConnected());
   }

}