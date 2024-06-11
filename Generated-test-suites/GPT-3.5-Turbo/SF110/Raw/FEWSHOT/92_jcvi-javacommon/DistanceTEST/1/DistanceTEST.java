package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance_minMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0);
        assertEquals(2.5f, distance.getStdDev(), 0);
    }

    @Test
    public void testBuildDistance_meanStdDev() {
        Distance distance = Distance.buildDistance(5.5f, 2.5f);
        assertEquals(3.0f, distance.getMin(), 0);
        assertEquals(8.0f, distance.getMax(), 0);
        assertEquals(5.5f, distance.getMean(), 0);
        assertEquals(2.5f, distance.getStdDev(), 0);
    }

    @Test
    public void testBuildDistance_minMax() {
        Distance distance = Distance.buildDistance(1, 10);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0);
        assertEquals(1.5f, distance.getStdDev(), 0);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_lessThanDelta() {
        Distance distance = new Distance(1, 10, 3.0f, 0.8f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(3.0f, transformedDistance.getMean(), 0);
        assertEquals(1.015f, transformedDistance.getStdDev(), 0);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_greaterThanDelta() {
        Distance distance = new Distance(1, 10, 6.0f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(6.0f, transformedDistance.getMean(), 0);
        assertEquals(2.0f, transformedDistance.getStdDev(), 0);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(1, 10, 5.5f, 2.5f);
        Distance distance2 = new Distance(1, 10, 5.5f, 2.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals_sameObject() {
        Distance distance = new Distance(1, 10, 5.5f, 2.5f);
        assertEquals(distance, distance);
    }

    @Test
    public void testEquals_nullObject() {
        Distance distance = new Distance(1, 10, 5.5f, 2.5f);
        assertEquals(false, distance.equals(null));
    }

    @Test
    public void testEquals_differentClass() {
        Distance distance = new Distance(1, 10, 5.5f, 2.5f);
        assertEquals(false, distance.equals("notADistanceObject"));
    }

    @Test
    public void testEquals_differentMin() {
        Distance distance1 = new Distance(1, 10, 5.5f, 2.5f);
        Distance distance2 = new Distance(2, 10, 5.5f, 2.5f);
        assertEquals(false, distance1.equals(distance2));
    }

    @Test
    public void testEquals_differentMax() {
        Distance distance1 = new Distance(1, 10, 5.5f, 2.5f);
        Distance distance2 = new Distance(1, 11, 5.5f, 2.5f);
        assertEquals(false, distance1.equals(distance2));
    }

    @Test
    public void testEquals_differentMean() {
        Distance distance1 = new Distance(1, 10, 5.5f, 2.5f);
        Distance distance2 = new Distance(1, 10, 4.5f, 2.5f);
        assertEquals(false, distance1.equals(distance2));
    }

    @Test
    public void testEquals_differentStdDev() {
        Distance distance1 = new Distance(1, 10, 5.5f, 2.5f);
        Distance distance2 = new Distance(1, 10, 5.5f, 2.0f);
        assertEquals(false, distance1.equals(distance2));
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(1, 10, 5.5f, 2.5f);
        assertEquals("5.500, std: 2.500", distance.toString());
    }
}