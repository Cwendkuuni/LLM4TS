
package org.jcvi.jillion.assembly.ca.frg;

import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
    
    @Test
    public void testBuildDistanceWithInts() {
        Distance distance = Distance.buildDistance(10, 20);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001f);
        assertEquals(1.666f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testBuildDistanceWithFloats() {
        Distance distance = Distance.buildDistance(12.5f, 1.5f);
        assertEquals(11, distance.getMin());
        assertEquals(14, distance.getMax());
        assertEquals(12.5f, distance.getMean(), 0.001f);
        assertEquals(1.5f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testBuildDistanceWithMinMeanStdDev() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 1.0f);
        assertEquals(5, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(7.5f, distance.getMean(), 0.001f);
        assertEquals(1.0f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testComputeStandardDeviation() {
        float stdDev = Distance.computeStandardDeviation(10, 20);
        assertEquals(1.666f, stdDev, 0.001f);
    }
    
    @Test
    public void testComputeMean() {
        float mean = Distance.computeMean(10, 20);
        assertEquals(15.0f, mean, 0.001f);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(10, 20);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(10, transformedDistance.getMin());
        assertEquals(20, transformedDistance.getMax());
        assertEquals(15.0f, transformedDistance.getMean(), 0.001f);
        assertEquals(1.666f, transformedDistance.getStdDev(), 0.001f);
        
        distance = Distance.buildDistance(5, 10, 7.5f, 1.0f);
        transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(5, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(7.5f, transformedDistance.getMean(), 0.001f);
        assertEquals(0.5f, transformedDistance.getStdDev(), 0.001f);
        
        distance = Distance.buildDistance(2, 7, 4.5f, 1.0f);
        transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(2, transformedDistance.getMin());
        assertEquals(7, transformedDistance.getMax());
        assertEquals(4.5f, transformedDistance.getMean(), 0.001f);
        assertEquals(1.495f, transformedDistance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(10, 20);
        Distance distance2 = Distance.buildDistance(10, 20);
        Distance distance3 = Distance.buildDistance(5, 10, 7.5f, 1.0f);
        
        assertTrue(distance1.equals(distance2));
        assertFalse(distance1.equals(distance3));
    }
    
    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(10, 20);
        assertEquals("15.000, std: 1.666", distance.toString());
    }
}
