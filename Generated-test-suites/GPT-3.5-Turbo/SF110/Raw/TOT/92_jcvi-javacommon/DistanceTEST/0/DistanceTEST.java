package org.jcvi.jillion.assembly.ca.frg;

import org.junit.*;
import static org.junit.Assert.*;

public class DistanceTest {
    
    @Test
    public void testBuildDistance() {
        // Typical use case
        Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 2.0f);
        assertEquals(1, distance1.getMin());
        assertEquals(10, distance1.getMax());
        assertEquals(5.5f, distance1.getMean(), 0.001);
        assertEquals(2.0f, distance1.getStdDev(), 0.001);
        
        // Edge case
        Distance distance2 = Distance.buildDistance(1, 1, 1.0f, 0.0f);
        assertEquals(1, distance2.getMin());
        assertEquals(1, distance2.getMax());
        assertEquals(1.0f, distance2.getMean(), 0.001);
        assertEquals(0.0f, distance2.getStdDev(), 0.001);
        
        // Error scenario
        Distance distance3 = Distance.buildDistance(10, 1, 5.5f, 2.0f);
        assertEquals(1, distance3.getMin());
        assertEquals(10, distance3.getMax());
        assertEquals(5.5f, distance3.getMean(), 0.001);
        assertEquals(2.0f, distance3.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_2() {
        // Typical use case
        Distance distance1 = Distance.buildDistance(5.5f, 2.0f);
        assertEquals(3, distance1.getMin());
        assertEquals(8, distance1.getMax());
        assertEquals(5.5f, distance1.getMean(), 0.001);
        assertEquals(2.0f, distance1.getStdDev(), 0.001);
        
        // Edge case
        Distance distance2 = Distance.buildDistance(1.0f, 0.0f);
        assertEquals(1, distance2.getMin());
        assertEquals(1, distance2.getMax());
        assertEquals(1.0f, distance2.getMean(), 0.001);
        assertEquals(0.0f, distance2.getStdDev(), 0.001);
        
        // Error scenario
        Distance distance3 = Distance.buildDistance(2.0f, 1.0f);
        assertEquals(1, distance3.getMin());
        assertEquals(4, distance3.getMax());
        assertEquals(2.0f, distance3.getMean(), 0.001);
        assertEquals(1.0f, distance3.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_3() {
        // Typical use case
        Distance distance1 = Distance.buildDistance(1, 10);
        assertEquals(1, distance1.getMin());
        assertEquals(10, distance1.getMax());
        assertEquals(5.5f, distance1.getMean(), 0.001);
        assertEquals(1.5f, distance1.getStdDev(), 0.001);
        
        // Edge case
        Distance distance2 = Distance.buildDistance(1, 1);
        assertEquals(1, distance2.getMin());
        assertEquals(1, distance2.getMax());
        assertEquals(1.0f, distance2.getMean(), 0.001);
        assertEquals(0.0f, distance2.getStdDev(), 0.001);
        
        // Error scenario
        Distance distance3 = Distance.buildDistance(10, 1);
        assertEquals(1, distance3.getMin());
        assertEquals(10, distance3.getMax());
        assertEquals(5.5f, distance3.getMean(), 0.001);
        assertEquals(1.5f, distance3.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        // Typical use case
        Distance distance1 = new Distance(1, 10, 5.5f, 2.0f);
        Distance transformed1 = Distance.transformIntoCeleraAssemblerDistance(distance1);
        assertEquals(1, transformed1.getMin());
        assertEquals(10, transformed1.getMax());
        assertEquals(5.5f, transformed1.getMean(), 0.001);
        assertEquals(1.495f, transformed1.getStdDev(), 0.001);
        
        // Edge case
        Distance distance2 = new Distance(1, 1, 1.0f, 0.0f);
        Distance transformed2 = Distance.transformIntoCeleraAssemblerDistance(distance2);
        assertEquals(1, transformed2.getMin());
        assertEquals(1, transformed2.getMax());
        assertEquals(1.0f, transformed2.getMean(), 0.001);
        assertEquals(0.0f, transformed2.getStdDev(), 0.001);
        
        // Error scenario
        Distance distance3 = new Distance(10, 1, 5.5f, 2.0f);
        Distance transformed3 = Distance.transformIntoCeleraAssemblerDistance(distance3);
        assertEquals(10, transformed3.getMin());
        assertEquals(1, transformed3.getMax());
        assertEquals(5.5f, transformed3.getMean(), 0.001);
        assertEquals(2.0f, transformed3.getStdDev(), 0.001);
    }
    
    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(1, 10, 5.5f, 2.0f);
        Distance distance2 = new Distance(1, 10, 5.5f, 2.0f);
        
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = new Distance(1, 10, 5.5f, 2.0f);
        Distance distance2 = new Distance(1, 10, 5.5f, 2.0f);
        Distance distance3 = new Distance(2, 8, 4.8f, 1.5f);
        
        assertTrue(distance1.equals(distance2));
        assertFalse(distance1.equals(distance3));
    }
    
    @Test
    public void testToString() {
        Distance distance1 = new Distance(1, 10, 5.5f, 2.0f);
        String expected = "5.500, std: 2.000";
        
        assertEquals(expected, distance1.toString());
    }
}