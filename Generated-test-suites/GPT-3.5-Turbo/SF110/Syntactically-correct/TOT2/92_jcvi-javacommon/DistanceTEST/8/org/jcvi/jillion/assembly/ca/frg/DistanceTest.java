package org.jcvi.jillion.assembly.ca.frg;
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 1.5f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.01);
        assertEquals(1.5f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(7.0f, 2.0f);
        assertEquals(5, distance.getMin());
        assertEquals(9, distance.getMax());
        assertEquals(7.0f, distance.getMean(), 0.01);
        assertEquals(2.0f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(3, 8);
        assertEquals(3, distance.getMin());
        assertEquals(8, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.01);
        assertEquals(0.833f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(1, 5, 2.0f, 0.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(5, transformedDistance.getMax());
        assertEquals(2.0f, transformedDistance.getMean(), 0.01);
        assertEquals(0.328f, transformedDistance.getStdDev(), 0.01);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 5, 2.0f, 0.5f);
        Distance distance2 = Distance.buildDistance(1, 5, 2.0f, 0.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 5, 2.0f, 0.5f);
        Distance distance2 = Distance.buildDistance(1, 5, 2.0f, 0.5f);
        assertTrue(distance1.equals(distance2));
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(1, 5, 2.0f, 0.5f);
        assertEquals("2.000, std: 0.500", distance.toString());
    }
}