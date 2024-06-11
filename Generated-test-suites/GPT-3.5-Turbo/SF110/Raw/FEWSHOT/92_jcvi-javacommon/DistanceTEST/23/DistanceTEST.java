package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceTest {
    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
        
        distance = Distance.buildDistance(10, 20);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.6667f, distance.getStdDev(), 0.001);
        
        distance = Distance.buildDistance(15.0f, 2.0f);
        assertEquals(12, distance.getMin());
        assertEquals(18, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(0.3283f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(10, 20, 15.0f, 2.0f);
        distance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(4.328f, distance.getStdDev(), 0.001);
        
        distance = new Distance(10, 20, 14.0f, 1.0f);
        distance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(12, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(14.0f, distance.getMean(), 0.001);
        assertEquals(3.328f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(10, 20, 15.0f, 2.0f);
        Distance distance2 = new Distance(10, 20, 15.0f, 2.0f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = new Distance(10, 20, 15.0f, 2.0f);
        Distance distance2 = new Distance(10, 20, 15.0f, 2.0f);
        assertEquals(true, distance1.equals(distance2));
        
        distance2 = new Distance(10, 20, 15.0f, 3.0f);
        assertEquals(false, distance1.equals(distance2));
    }
    
    @Test
    public void testToString() {
        Distance distance = new Distance(10, 20, 15.0f, 2.0f);
        assertEquals("15.000, std: 2.000", distance.toString());
    }
}