package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceTest {
    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(1, 10);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
        
        distance = Distance.buildDistance(5, 15);
        assertEquals(5, distance.getMin());
        assertEquals(15, distance.getMax());
        assertEquals(10.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
        
        distance = Distance.buildDistance(10, 20);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(5.5f, 1.5f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
        
        distance = Distance.buildDistance(10.0f, 1.666f);
        assertEquals(5, distance.getMin());
        assertEquals(15, distance.getMax());
        assertEquals(10.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
        
        distance = Distance.buildDistance(15.0f, 1.666f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMinAndMax() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 1.5f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
        
        distance = Distance.buildDistance(5, 15, 10.0f, 1.666f);
        assertEquals(5, distance.getMin());
        assertEquals(15, distance.getMax());
        assertEquals(10.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
        
        distance = Distance.buildDistance(10, 20, 15.0f, 1.666f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 1.5f);
        distance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(1.497f, distance.getStdDev(), 0.001);
        
        distance = Distance.buildDistance(5, 15, 10.0f, 1.666f);
        distance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(5, distance.getMin());
        assertEquals(15, distance.getMax());
        assertEquals(10.0f, distance.getMean(), 0.001);
        assertEquals(1.495f, distance.getStdDev(), 0.001);
        
        distance = Distance.buildDistance(10, 20, 15.0f, 1.666f);
        distance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.498f, distance.getStdDev(), 0.001);
    }
}