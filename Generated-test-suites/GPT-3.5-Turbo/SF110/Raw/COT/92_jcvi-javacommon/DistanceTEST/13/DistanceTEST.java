
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

  @Test
  public void testBuildDistanceWithMinAndMax() {
    Distance distance = Distance.buildDistance(2, 10);
    assertEquals(2, distance.getMin());
    assertEquals(10, distance.getMax());
    assertEquals(6.0, distance.getMean(), 0.001);
    assertEquals(1.333, distance.getStdDev(), 0.001);
  }
  
  @Test
  public void testBuildDistanceWithMeanAndStdDev() {
    Distance distance = Distance.buildDistance(3.5f, 0.5f);
    assertEquals(3, distance.getMin());
    assertEquals(4, distance.getMax());
    assertEquals(3.5, distance.getMean(), 0.001);
    assertEquals(0.5, distance.getStdDev(), 0.001);
  }
  
  @Test
  public void testBuildDistanceWithMeanAndStdDevBelowDelta() {
    Distance distance = Distance.buildDistance(1.0f, 0.2f);
    assertEquals(1, distance.getMin());
    assertEquals(1, distance.getMax());
    assertEquals(1.0, distance.getMean(), 0.001);
    assertEquals(0.295, distance.getStdDev(), 0.001);
  }
  
  @Test
  public void testTransformIntoCeleraAssemblerDistanceWithMeanBelowDelta() {
    Distance distance = new Distance(2, 8, 1.0f, 0.295f);
    Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
    assertSame(distance, transformedDistance);
  }
  
  @Test
  public void testTransformIntoCeleraAssemblerDistanceWithMeanAboveDelta() {
    Distance distance = new Distance(2, 10, 5.0f, 0.7f);
    Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
    assertNotSame(distance, transformedDistance);
    assertEquals(distance.getMin(), transformedDistance.getMin());
    assertEquals(distance.getMax(), transformedDistance.getMax());
    assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001);
    assertEquals(distance.getStdDev(), transformedDistance.getStdDev(), 0.001);
  }
  
  @Test
  public void testEqualsWithEqualObjects() {
    Distance distance1 = new Distance(2, 10, 5.0f, 0.7f);
    Distance distance2 = new Distance(2, 10, 5.0f, 0.7f);
    assertEquals(distance1, distance2);
  }
  
  @Test
  public void testEqualsWithUnequalObjects() {
    Distance distance1 = new Distance(2, 10, 5.0f, 0.7f);
    Distance distance2 = new Distance(4, 12, 5.0f, 0.7f);
    assertNotEquals(distance1, distance2);
  }
  
  @Test
  public void testHashCodeWithEqualObjects() {
    Distance distance1 = new Distance(2, 10, 5.0f, 0.7f);
    Distance distance2 = new Distance(2, 10, 5.0f, 0.7f);
    assertEquals(distance1.hashCode(), distance2.hashCode());
  }
  
  @Test
  public void testToString() {
    Distance distance = new Distance(2, 10, 5.0f, 0.7f);
    assertEquals("5.000, std: 0.700", distance.toString());
  }
}
