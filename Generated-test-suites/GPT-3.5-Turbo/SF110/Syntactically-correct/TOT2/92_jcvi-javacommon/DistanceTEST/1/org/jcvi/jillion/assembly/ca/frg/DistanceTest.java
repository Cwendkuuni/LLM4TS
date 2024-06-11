package org.jcvi.jillion.assembly.ca.frg;
import static org.junit.Assert.*;
import org.junit.Test;

public class DistanceTest {
    
    @Test
    public void testBuildDistance1() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.0f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.0);
        assertEquals(2.0f, distance.getStdDev(), 0.0);
    }
    
    @Test
    public void testBuildDistance2() {
        Distance distance = Distance.buildDistance(5.5f, 2.0f);
        assertEquals(4, distance.getMin());
        assertEquals(7, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.0);
        assertEquals(2.0f, distance.getStdDev(), 0.0);
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
        Distance originalDistance = Distance.buildDistance(1, 10, 5.5f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(5.5f, transformedDistance.getMean(), 0.0);
        assertEquals(1.495f, transformedDistance.getStdDev(), 0.0);
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 2.0f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.5f, 2.0f);
        assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testHashCode() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.0f);
        int expectedHashCode = 942758996;
        assertEquals(expectedHashCode, distance.hashCode());
    }

}