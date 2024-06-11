package org.jcvi.jillion.assembly.ca.frg;
import static org.junit.Assert.*;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance1() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance2() {
        Distance distance = Distance.buildDistance(15.0f, 2.0f);
        assertEquals(13, distance.getMin());
        assertEquals(17, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance3() {
        Distance distance = Distance.buildDistance(10, 20);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(10, 20, 3.0f, 1.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(10, transformedDistance.getMin());
        assertEquals(20, transformedDistance.getMax());
        assertEquals(3.0f, transformedDistance.getMean(), 0.001);
        assertEquals(0.328f, transformedDistance.getStdDev(), 0.001);
    }

    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(10, 20);
        Distance distance2 = Distance.buildDistance(10, 20);
        assertEquals(distance1, distance2);
    }

    @Test
    public void testHashCode() {
        Distance distance = Distance.buildDistance(10, 20);
        int hashCode = distance.hashCode();
        assertEquals(2031579995, hashCode);
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(15, 25, 20.0f, 3.0f);
        String expected = "20.000, std: 3.000";
        assertEquals(expected, distance.toString());
    }
}