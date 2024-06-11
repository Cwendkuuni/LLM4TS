package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance1() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.01);
        assertEquals(2.0f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistance2() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.01);
        assertEquals(2.0f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistance3() {
        Distance distance = Distance.buildDistance(1, 10);
        assertEquals("The minimum value should be equal to the mean divided by 2", 5.0f, distance.getMean(), 0.01);
        assertEquals("The maximum value should be equal to the mean divided by 2", 5.0f, distance.getMean(), 0.01);
        assertEquals("The standard deviation should be equal to the difference between the maximum and minimum divided by 6", 0.0f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance1() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(5.0f, transformedDistance.getMean(), 0.01);
        assertEquals("The stdDev should be corrected when mean < calculateDelta(stdDev)", (5.0f - 1.015f) / 3.0f, transformedDistance.getStdDev(), 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance2() {
        Distance distance = new Distance(1, 10, 5.0f, 5.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(5.0f, transformedDistance.getMean(), 0.01);
        assertEquals("The stdDev should remain the same when mean >= calculateDelta(stdDev)", 5.0f, transformedDistance.getStdDev(), 0.01);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(1, 10, 5.0f, 2.0f);
        Distance distance2 = new Distance(1, 10, 5.0f, 2.0f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = new Distance(1, 10, 5.0f, 2.0f);
        Distance distance2 = new Distance(1, 10, 5.0f, 2.0f);
        assertEquals(distance1, distance2);
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        assertEquals("5.000, std: 2.000", distance.toString());
    }
}