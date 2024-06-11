package org.jcvi.jillion.assembly.ca.frg;

import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
  
  @Test
  public void testBuildDistance1() {
    Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.5f);
    assertEquals(1, distance.getMin());
    assertEquals(10, distance.getMax());
    assertEquals(5.0f, distance.getMean(), 0.0);
    assertEquals(2.5f, distance.getStdDev(), 0.0);
  }
  
  @Test
  public void testBuildDistance2() {
    Distance distance = Distance.buildDistance(5.0f, 2.5f);
    assertEquals(2, distance.getMin());
    assertEquals(8, distance.getMax());
    assertEquals(5.0f, distance.getMean(), 0.0);
    assertEquals(2.5f, distance.getStdDev(), 0.0);
  }
  
  @Test
  public void testBuildDistance3() {
    Distance distance = Distance.buildDistance(1, 10);
    assertEquals(1, distance.getMin());
    assertEquals(10, distance.getMax());
    assertEquals(5.5f, distance.getMean(), 0.0);
    assertEquals(1.5f, distance.getStdDev(), 0.0);
  }
  
  @Test
  public void testTransformIntoCeleraAssemblerDistance() {
    Distance originalDistance = Distance.buildDistance(1, 10, 2.0f, 1.0f);
    Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);
    assertEquals(originalDistance, transformedDistance);
    
    Distance specialDistance = Distance.buildDistance(1, 10, 1.0f, 0.33f);
    transformedDistance = Distance.transformIntoCeleraAssemblerDistance(specialDistance);
    assertNotEquals(specialDistance,transformedDistance);
  }
  
  @Test
  public void testHashCode() {
    Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.5f);
    Distance distance2 = Distance.buildDistance(1, 10, 5.0f, 2.5f);
    assertEquals(distance1.hashCode(), distance2.hashCode());
  }
  
  @Test
  public void testEquals() {
    Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.5f);
    Distance distance2 = Distance.buildDistance(1, 10, 5.0f, 2.5f);
    assertTrue(distance1.equals(distance2));
  }
  
  @Test
  public void testToString() {
    Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.5f);
    assertEquals("5.000, std: 2.500", distance.toString());
  }
  
}