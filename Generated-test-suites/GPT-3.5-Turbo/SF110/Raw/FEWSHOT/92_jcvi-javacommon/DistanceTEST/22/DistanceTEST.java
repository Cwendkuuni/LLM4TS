package org.jcvi.jillion.assembly.ca.frg;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DistanceTest {
    @Test
    public void testBuildDistanceWithMinMaxMeanAndStdDev() {
        Distance distance = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0.0001);
        assertEquals(1.5f, distance.getStdDev(), 0.0001);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(3.0f, 1.5f);
        assertEquals(2, distance.getMin());
        assertEquals(4, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0.0001);
        assertEquals(1.5f, distance.getStdDev(), 0.0001);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(1, 5);
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0.0001);
        assertEquals(0.6667f, distance.getStdDev(), 0.0001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceForMeanLessThanCalculateDelta() {
        Distance distance = new Distance(1, 5, 2.5f, 0.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(5, transformedDistance.getMax());
        assertEquals(2.5f, transformedDistance.getMean(), 0.0001);
        assertEquals(0.495f, transformedDistance.getStdDev(), 0.0001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceForMeanGreaterThanCalculateDelta() {
        Distance distance = new Distance(1, 5, 4.0f, 0.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(5, transformedDistance.getMax());
        assertEquals(4.0f, transformedDistance.getMean(), 0.0001);
        assertEquals(0.5f, transformedDistance.getStdDev(), 0.0001);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(1, 5, 3.0f, 1.5f);
        Distance distance2 = new Distance(1, 5, 3.0f, 1.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = new Distance(1, 5, 3.0f, 1.5f);
        Distance distance2 = new Distance(1, 5, 3.0f, 1.5f);
        assertEquals(distance1, distance2);
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(1, 5, 3.0f, 1.5f);
        assertEquals("3.0, std: 1.5", distance.toString());
    }
}