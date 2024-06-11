package org.jcvi.jillion.assembly.ca.frg;

import org.junit.*;
import static org.junit.Assert.*;

public class DistanceTest {

   @Test
   public void testBuildDistance() {
      Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
      assertEquals(10, distance.getMin());
      assertEquals(20, distance.geMax());
      assertEquals(15.0f, distance.getMean(), 0.001);
      assertEquals(2.0f, distance.getStdDev(), 0.001);
   }

   @Test
   public void testBuildDistance2() {
      Distance distance = Distance.buildDistance(15.0f, 2.0f);
      assertEquals(15.0f, distance.getMean(), 0.001);
      assertEquals(2.0f, distance.getStdDev(), 0.001);
      float delta = Distance.calculateDelta(2.0f);
      int plusDelta = (int)(15.0f + delta);
      int minusDelta = (int)(15.0f - delta);
      assertEquals(Math.min(plusDelta, minusDelta), distance.getMin());
      assertEquals(Math.max(plusDelta, minusDelta), distance.getMax());
   }

   @Test
   public void testBuildDistance3() {
      Distance distance = Distance.buildDistance(10, 20);
      assertEquals(10, distance.getMin());
      assertEquals(20, distance.getMax());
      float mean = Distance.computeMean(10, 20);
      float stdDev = Distance.computeStandardDeviation(10, 20);
      assertEquals(mean, distance.getMean(), 0.001);
      assertEquals(stdDev, distance.getStdDev(), 0.001);
   }

   @Test
   public void testTransformIntoCeleraAssemblerDistance() {
      Distance distance = new Distance(10, 20, 15.0f, 2.0f);
      Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
      assertEquals(10, transformedDistance.getMin());
      assertEquals(20, transformedDistance.getMax());
      assertEquals(15.0f, transformedDistance.getMean(), 0.001);
      float correctedStdDev = (15.0f - 1.015f) / 3.0f;
      assertEquals(correctedStdDev, transformedDistance.getStdDev(), 0.001);
      Distance sameDistance = new Distance(10, 20, 15.0f, 2.0f);
      Distance sameTransformedDistance = Distance.transformIntoCeleraAssemblerDistance(sameDistance);
      assertEquals(sameDistance.getMin(), sameTransformedDistance.getMin());
      assertEquals(sameDistance.getMax(), sameTransformedDistance.getMax());
      assertEquals(sameDistance.getMean(), sameTransformedDistance.getMean(), 0.001);
      assertEquals(sameDistance.getStdDev(), sameTransformedDistance.getStdDev(), 0.001);
   }

   @Test
   public void testGetMean() {
      Distance distance = new Distance(10, 20, 15.0f, 2.0f);
      assertEquals(15.0f, distance.getMean(), 0.001);
   }

   @Test
   public void testGetStdDev() {
      Distance distance = new Distance(10, 20, 15.0f, 2.0f);
      assertEquals(2.0f, distance.getStdDev(), 0.001);
   }

   @Test
   public void testGetMin() {
      Distance distance = new Distance(10, 20, 15.0f, 2.0f);
      assertEquals(10, distance.getMin());
   }

   @Test
   public void testGetMax() {
      Distance distance = new Distance(10, 20, 15.0f, 2.0f);
      assertEquals(20, distance.getMax());
   }

   @Test
   public void testHashCode() {
      Distance distance = new Distance(10, 20, 15.0f, 2.0f);
      assertEquals(distance.hashCode(), distance.hashCode());
   }

   @Test
   public void testEquals() {
      Distance distance1 = new Distance(10, 20, 15.0f, 2.0f);
      Distance distance2 = new Distance(10, 20, 15.0f, 2.0f);
      assertTrue(distance1.equals(distance1));
      assertTrue(distance1.equals(distance2));
   }

   @Test
   public void testToString() {
      Distance distance = new Distance(10, 20, 15.0f, 2.0f);
      assertEquals("15.000, std: 2.000", distance.toString());
   }
}