package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceTest {
    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.0001f);
        assertEquals(2.5f, distance.getStdDev(), 0.0001f);
        
        distance = Distance.buildDistance(5.5f, 2.5f);
        assertEquals(2, distance.getMin());
        assertEquals(9, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.0001f);
        assertEquals(2.5f, distance.getStdDev(), 0.0001f);
        
        distance = Distance.buildDistance(1, 10);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.0001f);
        assertEquals(1.5f, distance.getStdDev(), 0.0001f);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(1, celeraDistance.getMin());
        assertEquals(10, celeraDistance.getMax());
        assertEquals(5.5f, celeraDistance.getMean(), 0.0001f);
        assertEquals(1.496f, celeraDistance.getStdDev(), 0.0001f);
        
        distance = Distance.buildDistance(10, 20, 15.5f, 2.5f);
        celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(10, celeraDistance.getMin());
        assertEquals(20, celeraDistance.getMax());
        assertEquals(15.5f, celeraDistance.getMean(), 0.0001f);
        assertEquals(2.5f, celeraDistance.getStdDev(), 0.0001f);
    }
    
    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        assertEquals(distance1, distance2);
    }
    
    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        assertEquals("5.500, std: 2.500", distance.toString());
    }
}