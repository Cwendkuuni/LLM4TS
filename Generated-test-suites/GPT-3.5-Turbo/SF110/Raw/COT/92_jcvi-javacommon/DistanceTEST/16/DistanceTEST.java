import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {
   
   // Test case for buildDistance(int min, int max, float mean, float stdDev)
   @Test
   public void testBuildDistanceWithMinMaxMeanStdDev() {
      Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
      
      Assert.assertEquals(10, distance.getMin());
      Assert.assertEquals(20, distance.getMax());
      Assert.assertEquals(15.0f, distance.getMean(), 0.001);
      Assert.assertEquals(2.0f, distance.getStdDev(), 0.001);
   }

   // Test case for buildDistance(float mean, float stdDev)
   @Test
   public void testBuildDistanceWithMeanStdDev() {
      Distance distance = Distance.buildDistance(15.0f, 2.0f);
      
      float delta = Distance.calculateDelta(2.0f);
      int plusDelta = (int)(15.0f + delta);
      int minusDelta = (int)(15.0f - delta);
      
      Assert.assertEquals(minusDelta, distance.getMin());
      Assert.assertEquals(plusDelta, distance.getMax());
      Assert.assertEquals(15.0f, distance.getMean(), 0.001);
      Assert.assertEquals(2.0f, distance.getStdDev(), 0.001);
   }
   
   // Test case for buildDistance(int min, int max)
   @Test
   public void testBuildDistanceWithMinMax() {
      Distance distance = Distance.buildDistance(10, 20);
      
      float mean = Distance.computeMean(10, 20);
      float stdDev = Distance.computeStandardDeviation(10, 20);
      
      Assert.assertEquals(10, distance.getMin());
      Assert.assertEquals(20, distance.getMax());
      Assert.assertEquals(mean, distance.getMean(), 0.001);
      Assert.assertEquals(stdDev, distance.getStdDev(), 0.001);
   }
   
   // Test case for transformIntoCeleraAssemblerDistance(Distance distance)
   @Test
   public void testTransformIntoCeleraAssemblerDistance() {
      Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
      Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
      
      float mean = distance.getMean();
      float stdDev = distance.getStdDev();
  
      if (mean < Distance.calculateDelta(stdDev)) {
         float correctedStdDev = (mean - 1.015f) / 3.0f;
         Assert.assertEquals(correctedStdDev, transformedDistance.getStdDev(), 0.001);
      } else {
         Assert.assertEquals(stdDev, transformedDistance.getStdDev(), 0.001);
      }
   }
   
   // Test case for hashCode()
   @Test
   public void testHashCode() {
      Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
      Distance distance2 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
      
      Assert.assertEquals(distance1.hashCode(), distance2.hashCode());
   }
   
   // Test case for equals(Object obj)
   @Test
   public void testEquals() {
      Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
      Distance distance2 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
      
      Assert.assertTrue(distance1.equals(distance2));
   }
   
   // Test case for toString()
   @Test
   public void testToString() {
      Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
      
      String expected = "15.000, std: 2.000";
      String actual = distance.toString();
      
      Assert.assertEquals(expected, actual);
   }
}