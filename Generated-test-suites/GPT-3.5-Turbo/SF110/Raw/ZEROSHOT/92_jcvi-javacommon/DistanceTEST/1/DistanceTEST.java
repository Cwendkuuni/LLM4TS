import org.junit.Test;

public class DistanceTest {

  @Test
  public void testBuildDistanceIntIntFloatFloat() {
    Distance d = Distance.buildDistance(1, 10, 5.0f, 2.0f);
    assertEquals(1, d.getMin());
    assertEquals(10, d.getMax());
    assertEquals(5.0f, d.getMean(), 0.001);
    assertEquals(2.0f, d.getStdDev(), 0.001);
  }

  @Test
  public void testBuildDistanceFloatFloat() {
    Distance d = Distance.buildDistance(5.0f, 2.0f);
    assertEquals(2.0f, d.getMean(), 0.001);
    assertEquals(5.0f, d.getStdDev(), 0.001);
  }

  @Test
  public void testBuildDistanceIntInt() {
    Distance d = Distance.buildDistance(1, 10);
    assertEquals(1, d.getMin());
    assertEquals(10, d.getMax());
    assertEquals(5.5f, d.getMean(), 0.001);
    assertEquals(1.5f, d.getStdDev(), 0.001);
  }

  @Test
  public void testTransformIntoCeleraAssemblerDistance() {
    Distance d = Distance.buildDistance(1, 10, 5.0f, 2.0f);
    Distance transformed = Distance.transformIntoCeleraAssemblerDistance(d);
    assertEquals(d, transformed);

    d = Distance.buildDistance(1, 10, 1.2f, 0.4f);
    transformed = Distance.transformIntoCeleraAssemblerDistance(d);
    assertEquals(d, transformed);

    d = Distance.buildDistance(1, 10, 1.0f, 0.3f);
    transformed = Distance.transformIntoCeleraAssemblerDistance(d);
    assertEquals(1.0f, transformed.getMean(), 0.001);
    assertEquals(0.005f, transformed.getStdDev(), 0.001);
  }

  @Test
  public void testGetMean() {
    Distance d = Distance.buildDistance(1, 10, 5.0f, 2.0f);
    assertEquals(5.0f, d.getMean(), 0.001);
  }

  @Test
  public void testGetStdDev() {
    Distance d = Distance.buildDistance(1, 10, 5.0f, 2.0f);
    assertEquals(2.0f, d.getStdDev(), 0.001);
  }

  @Test
  public void testGetMin() {
    Distance d = Distance.buildDistance(1, 10, 5.0f, 2.0f);
    assertEquals(1, d.getMin());
  }

  @Test
  public void testGetMax() {
    Distance d = Distance.buildDistance(1, 10, 5.0f, 2.0f);
    assertEquals(10, d.getMax());
  }

  @Test
  public void testHashCode() {
    Distance d1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
    Distance d2 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
    assertEquals(d1.hashCode(), d2.hashCode());
  }

  @Test
  public void testEquals() {
    Distance d1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
    Distance d2 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
    assertEquals(d1, d2);
  }

  @Test
  public void testToString() {
    Distance d = Distance.buildDistance(1, 10, 5.0f, 2.0f);
    assertEquals("5.000, std: 2.000", d.toString());
  }

}