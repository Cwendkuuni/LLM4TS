package org.jcvi.jillion.assembly.ca.frg;

import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
    
    @Test
    public void testBuildDistance_withminmaxmeanstdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withmeanstdDev() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        float delta = 3.0f * 2.0f;
        int plusDelta = (int)(5.0f + delta);
        int minusDelta = (int)(5.0f - delta);
        assertEquals(Math.min(plusDelta, minusDelta), distance.getMin());
        assertEquals(Math.max(plusDelta, minusDelta), distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withminmax() {
        Distance distance = Distance.buildDistance(1, 10);
        float mean = (1 / 2.0f) + (10 / 2.0f);
        float stdDev = (10 - 1) / 6.0f;
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        
        float mean = distance.getMean();
        float stdDev = distance.getStdDev();
        if (mean < 3.0f * stdDev) {
            float correctedStdDev = (mean - 1.015f) / 3.0f;
            Distance expectedDistance = new Distance(distance.getMin(), distance.getMax(), mean, correctedStdDev);
            distance = Distance.transformIntoCeleraAssemblerDistance(distance);
            assertEquals(expectedDistance, distance);
        } else {
            Distance expectedDistance = distance;
            distance = Distance.transformIntoCeleraAssemblerDistance(distance);
            assertEquals(expectedDistance, distance);
        }
    }
    
    @Test
    public void testHashCode() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        int expectedHashCode = 31 * 1 + 31 * 10 + Float.floatToIntBits(5.0f) + Float.floatToIntBits(2.0f);
        assertEquals(expectedHashCode, distance.hashCode());
    }
    
    @Test
    public void testEquals_withSameObject() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(distance, distance);
    }
    
    @Test
    public void testEquals_withDifferentObject() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(distance1, distance2);
    }
    
    @Test
    public void testEquals_withNullObject() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertNotEquals(distance, null);
    }
    
    @Test
    public void testEquals_withDifferentMin() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(2, 10, 5.0f, 2.0f);
        assertNotEquals(distance1, distance2);
    }
    
    @Test
    public void testEquals_withDifferentMax() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(1, 11, 5.0f, 2.0f);
        assertNotEquals(distance1, distance2);
    }
    
    @Test
    public void testEquals_withDifferentMean() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(1, 10, 6.0f, 2.0f);
        assertNotEquals(distance1, distance2);
    }
    
    @Test
    public void testEquals_withDifferentStdDev() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.0f, 3.0f);
        assertNotEquals(distance1, distance2);
    }
    
    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        String expectedToString = String.format("%.3f, std: %.3f", 5.0f, 2.0f);
        assertEquals(expectedToString, distance.toString());
    }
    
}