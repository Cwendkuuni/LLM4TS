package org.jcvi.jillion.assembly.ca.frg;
import static org.junit.Assert.*;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(3.0f, 1.5f);
        assertEquals(1, distance.getMin());
        assertEquals(4, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(5, 15);
        assertEquals(5, distance.getMin());
        assertEquals(15, distance.getMax());
        assertEquals(10.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(4, 12, 8.0f, 2.0f);
        Distance newDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(4, newDistance.getMin());
        assertEquals(12, newDistance.getMax());
        assertEquals(8.0f, newDistance.getMean(), 0.001);
        assertEquals(2.328f, newDistance.getStdDev(), 0.001);
    }

    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(2, 8, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(2, 8, 5.0f, 2.0f);
        assertTrue(distance1.equals(distance2));
    }

    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(2, 8, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(2, 8, 5.0f, 2.0f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

}