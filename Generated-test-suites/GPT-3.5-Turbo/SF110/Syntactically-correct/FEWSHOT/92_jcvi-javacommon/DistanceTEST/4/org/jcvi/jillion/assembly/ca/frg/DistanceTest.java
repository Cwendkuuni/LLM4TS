import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DistanceTest {
  
  @Test
  public void testBuildDistance1() {
    Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
    assertEquals(1, distance.getMin());
    assertEquals(10, distance.getMax());
    assertEquals(5.5f, distance.getMean(), 0.001);
    assertEquals(2.5f, distance.getStdDev(), 0.001);
  }
  
  @Test
  public void testBuildDistance2() {
    Distance distance = Distance.buildDistance(5.5f, 2.5f);
    assertEquals(5, distance.getMin());
    assertEquals(6, distance.getMax());
    assertEquals(5.5f, distance.getMean(), 0.001);
    assertEquals(2.5f, distance.getStdDev(), 0.001);
  }
  
  @Test
  public void testBuildDistance3() {
    Distance distance = Distance.buildDistance(1, 10);
    assertEquals(1, distance.getMin());
    assertEquals(10, distance.getMax());
    assertEquals(5.5f, distance.getMean(), 0.001);
    assertEquals(1.5f, distance.getStdDev(), 0.001);
  }
  
  @Test
  public void testTransformIntoCeleraAssemblerDistance() {
    Distance distance = new Distance(1, 10, 5.5f, 2.5f);
    Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
    assertEquals(1, celeraDistance.getMin());
    assertEquals(10, celeraDistance.getMax());
    assertEquals(5.5f, celeraDistance.getMean(), 0.001);
    assertEquals(1.49833333f, celeraDistance.getStdDev(), 0.001);
  }
  
}