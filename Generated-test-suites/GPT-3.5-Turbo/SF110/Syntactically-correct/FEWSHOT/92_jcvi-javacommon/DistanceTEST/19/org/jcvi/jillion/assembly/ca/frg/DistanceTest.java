
package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;
import org.junit.Test;

public class DistanceTest {
  
    @Test
    public void testBuildDistanceForIntFloatFloat() {
        Distance distance = Distance.buildDistance(1, 10, 4.5f, 2.5f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(4.5f, distance.getMean(), 0);
        assertEquals(2.5f, distance.getStdDev(), 0);
    }
    
    @Test
    public void testBuildDistanceForFloatFloat() {
        Distance distance = Distance.buildDistance(4.5f, 2.5f);
        float delta = Distance.calculateDelta(2.5f);
        int plusDelta = (int)(4.5f + delta);
        int minusDelta = (int)(4.5f - delta);
        assertEquals(Math.min(plusDelta, minusDelta), distance.getMin());
        assertEquals(Math.max(plusDelta, minusDelta), distance.getMax());
        assertEquals(4.5f, distance.getMean(), 0);
        assertEquals(2.5f, distance.getStdDev(), 0);
    }
    
    @Test
    public void testBuildDistanceForIntInt() {
        Distance distance = Distance.buildDistance(1, 10);
        float mean = Distance.computeMean(1, 10);
        float stdDev = Distance.computeStandardDeviation(1, 10);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(mean, distance.getMean(), 0);
        assertEquals(stdDev, distance.getStdDev(), 0);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance original = Distance.buildDistance(1, 10, 4.5f, 2.5f);
        Distance transformed = Distance.transformIntoCeleraAssemblerDistance(original);
        float mean = original.getMean();
        float stdDev = original.getStdDev();
        if (mean < Distance.calculateDelta(stdDev)) {
            float correctedStdDev = (mean - Distance.MAX_ROUNDING_ERROR) / SIX;
            assertEquals(original.getMin(), transformed.getMin());
            assertEquals(original.getMax(), transformed.getMax());
            assertEquals(mean, transformed.getMean(), 0);
            assertEquals(correctedStdDev, transformed.getStdDev(), 0);
        } else {
            assertEquals(original, transformed);
        }
    }
    
    @Test
    public void testGetMean() {
        Distance distance = Distance.buildDistance(1, 10, 4.5f, 2.5f);
        assertEquals(4.5f, distance.getMean(), 0);
    }
    
    @Test
    public void testGetStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 4.5f, 2.5f);
        assertEquals(2.5f, distance.getStdDev(), 0);
    }
    
    @Test
    public void testGetMin() {
        Distance distance = Distance.buildDistance(1, 10, 4.5f, 2.5f);
        assertEquals(1, distance.getMin());
    }
    
    @Test
    public void testGetMax() {
        Distance distance = Distance.buildDistance(1, 10, 4.5f, 2.5f);
        assertEquals(10, distance.getMax());
    }
    
    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 10, 4.5f, 2.5f);
        Distance distance2 = Distance.buildDistance(1, 10, 4.5f, 2.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 10, 4.5f, 2.5f);
        Distance distance2 = Distance.buildDistance(1, 10, 4.5f, 2.5f);
        assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(1, 10, 4.5f, 2.5f);
        assertEquals("4.500, std: 2.500", distance.toString());
    }
}
