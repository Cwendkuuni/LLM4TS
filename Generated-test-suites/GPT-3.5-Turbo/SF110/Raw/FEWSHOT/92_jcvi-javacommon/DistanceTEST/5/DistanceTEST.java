
package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceTest {
    
    @Test
    public void testBuildDistance_withIntsAndFloats() {
        Distance distance = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withFloats() {
        Distance distance = Distance.buildDistance(3.0f, 1.5f);
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withInts() {
        Distance distance = Distance.buildDistance(1, 5);
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0.001);
        assertEquals(0.666f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(3.0f, 1.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(5, transformedDistance.getMax());
        assertEquals(2.985f, transformedDistance.getMean(), 0.001);
        assertEquals(0.495f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        Distance distance2 = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        Distance distance2 = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        assertEquals(distance1, distance2);
    }
    
    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(3.0f, 1.5f);
        assertEquals("3.000, std: 1.500", distance.toString());
    }
}
