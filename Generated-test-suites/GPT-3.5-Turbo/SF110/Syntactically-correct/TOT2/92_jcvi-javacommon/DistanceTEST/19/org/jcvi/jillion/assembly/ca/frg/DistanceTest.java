package org.jcvi.jillion.assembly.ca.frg;
import static org.junit.Assert.*;
import org.junit.Test;

public class DistanceTest {
    
    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 1.5f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(5.0f, 1.5f);
        assertEquals(3.0f, distance.getMin());
        assertEquals(7.0f, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance originalDistance = Distance.buildDistance(1, 10, 5.0f, 1.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(5.0f, transformedDistance.getMean(), 0.001);
        assertEquals(1.0f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 1.5f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.0f, 1.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 1.5f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.0f, 1.5f);
        assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 1.5f);
        assertEquals("5.000, std: 1.500", distance.toString());
    }
}