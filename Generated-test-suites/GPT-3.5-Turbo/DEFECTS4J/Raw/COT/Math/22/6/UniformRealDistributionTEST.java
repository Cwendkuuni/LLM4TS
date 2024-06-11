import static org.junit.Assert.*;
import org.junit.Test;

public class UniformRealDistributionTest {

   @Test
   public void testDefaultConstructor() {
      UniformRealDistribution distribution = new UniformRealDistribution();
      
      assertEquals(0.0, distribution.getSupportLowerBound(), 0.0001);
      assertEquals(1.0, distribution.getSupportUpperBound(), 0.0001);
   }
   
   @Test
   public void testConstructorWithBounds() {
      UniformRealDistribution distribution = new UniformRealDistribution(2.0, 5.0);
      
      assertEquals(2.0, distribution.getSupportLowerBound(), 0.0001);
      assertEquals(5.0, distribution.getSupportUpperBound(), 0.0001);
   }
   
   @Test
   public void testConstructorWithBoundsAndAccuracy() {
      UniformRealDistribution distribution = new UniformRealDistribution(2.0, 5.0, 0.00001);
      
      assertEquals(2.0, distribution.getSupportLowerBound(), 0.0001);
      assertEquals(5.0, distribution.getSupportUpperBound(), 0.0001);
      assertEquals(0.00001, distribution.getSolverAbsoluteAccuracy(), 0.0001);
   }
   
   @Test(expected = NumberIsTooLargeException.class)
   public void testConstructorWithInvalidBounds() {
      UniformRealDistribution distribution = new UniformRealDistribution(5.0, 2.0);
   }
   
   @Test
   public void testDensityInsideBounds() {
      UniformRealDistribution distribution = new UniformRealDistribution(2.0, 5.0);
      
      assertEquals(0.3333, distribution.density(3.0), 0.0001);
   }
   
   @Test
   public void testDensityOutsideBounds() {
      UniformRealDistribution distribution = new UniformRealDistribution(2.0, 5.0);
      
      assertEquals(0.0, distribution.density(1.0), 0.0001);
      assertEquals(0.0, distribution.density(6.0), 0.0001);
   }
   
   @Test
   public void testCumulativeProbabilityInsideBounds() {
      UniformRealDistribution distribution = new UniformRealDistribution(2.0, 5.0);
      
      assertEquals(0.25, distribution.cumulativeProbability(3.0), 0.0001);
   }
   
   @Test
   public void testCumulativeProbabilityOutsideBounds() {
      UniformRealDistribution distribution = new UniformRealDistribution(2.0, 5.0);
      
      assertEquals(0.0, distribution.cumulativeProbability(1.0), 0.0001);
      assertEquals(1.0, distribution.cumulativeProbability(6.0), 0.0001);
   }
   
   @Test
   public void testNumericalMean() {
      UniformRealDistribution distribution = new UniformRealDistribution(2.0, 5.0);
      
      assertEquals(3.5, distribution.getNumericalMean(), 0.0001);
   }
   
   @Test
   public void testNumericalVariance() {
      UniformRealDistribution distribution = new UniformRealDistribution(2.0, 5.0);
      
      assertEquals(0.5833, distribution.getNumericalVariance(), 0.0001);
   }
   
   @Test
   public void testSupportLowerBound() {
      UniformRealDistribution distribution = new UniformRealDistribution(2.0, 5.0);
      
      assertEquals(2.0, distribution.getSupportLowerBound(), 0.0001);
   }
   
   @Test
   public void testSupportUpperBound() {
      UniformRealDistribution distribution = new UniformRealDistribution(2.0, 5.0);
      
      assertEquals(5.0, distribution.getSupportUpperBound(), 0.0001);
   }
   
   @Test
   public void testSupportLowerBoundInclusive() {
      UniformRealDistribution distribution = new UniformRealDistribution(2.0, 5.0);
      
      assertTrue(distribution.isSupportLowerBoundInclusive());
   }
   
   @Test
   public void testSupportUpperBoundInclusive() {
      UniformRealDistribution distribution = new UniformRealDistribution(2.0, 5.0);
      
      assertFalse(distribution.isSupportUpperBoundInclusive());
   }
   
   @Test
   public void testSupportConnected() {
      UniformRealDistribution distribution = new UniformRealDistribution(2.0, 5.0);
      
      assertTrue(distribution.isSupportConnected());
   }
   
   @Test
   public void testSample() {
      UniformRealDistribution distribution = new UniformRealDistribution(2.0, 5.0);
      
      double sample = distribution.sample();
      assertTrue(sample >= 2.0 && sample < 5.0);
   }
}