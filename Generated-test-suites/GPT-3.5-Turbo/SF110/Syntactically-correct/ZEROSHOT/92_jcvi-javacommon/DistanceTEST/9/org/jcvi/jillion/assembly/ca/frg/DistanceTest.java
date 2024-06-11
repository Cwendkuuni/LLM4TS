package org.jcvi.jillion.assembly.ca.frg;

import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithIntegers() {
        Distance distance = Distance.buildDistance(1, 10);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.01);
        assertEquals(1.5f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceWithFloats() {
        Distance distance = Distance.buildDistance(5.5f, 1.5f);
        assertEquals(3, distance.getMin());
        assertEquals(8, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.01);
        assertEquals(1.5f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.01);
        assertEquals(1.5f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(5.5f, 1.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.01);
        assertEquals(0.495f, transformedDistance.getStdDev(), 0.01);
    }
}