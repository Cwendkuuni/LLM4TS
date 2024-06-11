package org.jcvi.jillion.assembly.ca.frg;

import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
    
    @Test
    public void testBuildDistance1() {
        Distance distance = Distance.buildDistance(1, 10, 5.5, 1.5);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5, distance.getMean(), 0);
        assertEquals(1.5, distance.getStdDev(), 0);
    }
    
    @Test
    public void testBuildDistance2() {
        Distance distance = Distance.buildDistance(5, 8);
        assertEquals(5, distance.getMin());
        assertEquals(8, distance.getMax());
        assertEquals(6.5, distance.getMean(), 0);
        assertEquals(0.5, distance.getStdDev(), 0);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance originalDistance = Distance.buildDistance(1, 10, 3.0, 1.0);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);
        assertEquals(originalDistance.getMin(), transformedDistance.getMin());
        assertEquals(originalDistance.getMax(), transformedDistance.getMax());
        assertEquals(originalDistance.getMean(), transformedDistance.getMean(), 0);
        assertEquals(0.995, transformedDistance.getStdDev(), 0);
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(5, 10, 4.5, 2.0);
        Distance distance2 = Distance.buildDistance(5, 10, 4.5, 2.0);
        Distance distance3 = Distance.buildDistance(1, 5, 3.0, 1.5);
        
        assertTrue(distance1.equals(distance2));
        assertFalse(distance1.equals(distance3));
    }
    
    @Test
    public void testHashCode() {
        Distance distance = Distance.buildDistance(5, 10, 4.5, 2.0);
        int expectedHashCode = 1338260;
        assertEquals(expectedHashCode, distance.hashCode());
    }
}