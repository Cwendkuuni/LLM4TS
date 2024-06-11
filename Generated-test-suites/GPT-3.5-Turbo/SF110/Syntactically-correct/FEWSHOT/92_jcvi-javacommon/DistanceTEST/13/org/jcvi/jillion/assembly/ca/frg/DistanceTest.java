package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceTest {
    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(2.5f, distance.getStdDev(), 0.001);
        
        Distance distance2 = Distance.buildDistance(10.0f, 2.5f);
        assertEquals(9, distance2.getMin());
        assertEquals(11, distance2.getMax());
        assertEquals(10.0f, distance2.getMean(), 0.001);
        assertEquals(2.5f, distance2.getStdDev(), 0.001);
        
        Distance distance3 = Distance.buildDistance(10, 20);
        assertEquals(10, distance3.getMin());
        assertEquals(20, distance3.getMax());
        assertEquals(15.0f, distance3.getMean(), 0.001);
        assertEquals(1.666f, distance3.getStdDev(), 0.001);
    }
    
    @Test
    public void testCalculateDelta() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        float delta = Distance.calculateDelta(distance.getStdDev());
        assertEquals(7.5f, delta, 0.001);
    }
    
    @Test
    public void testComputeMean() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        float mean = Distance.computeMean(distance.getMin(), distance.getMax());
        assertEquals(15.0f, mean, 0.001);
    }
    
    @Test
    public void testComputeStandardDeviation() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        float stdDev = Distance.computeStandardDeviation(distance.getMin(), distance.getMax());
        assertEquals(1.666f, stdDev, 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(10, transformedDistance.getMin());
        assertEquals(20, transformedDistance.getMax());
        assertEquals(15.0f, transformedDistance.getMean(), 0.001);
        assertEquals(2.16f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        Distance distance2 = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        Distance distance3 = Distance.buildDistance(20, 30, 20.0f, 3.0f);
        
        assertEquals(true, distance1.equals(distance2));
        assertEquals(false, distance1.equals(distance3));
    }
    
    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        assertEquals("15.000, std: 2.500", distance.toString());
    }
}