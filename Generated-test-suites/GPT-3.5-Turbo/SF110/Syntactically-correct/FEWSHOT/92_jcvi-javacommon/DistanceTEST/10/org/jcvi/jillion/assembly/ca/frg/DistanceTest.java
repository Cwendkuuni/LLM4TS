package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DistanceTest {
    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001f);
        assertEquals(2.5f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(5.5f, 2.5f);
        assertNotNull(distance);
    }
    
    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        assertNotNull(distance);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertNotEquals(distance, transformedDistance);
    }
    
    @Test
    public void testGetMean() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        assertEquals(5.5f, distance.getMean(), 0.001f);
    }
    
    @Test
    public void testGetStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        assertEquals(2.5f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testGetMin() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        assertEquals(1, distance.getMin());
    }
    
    @Test
    public void testGetMax() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        assertEquals(10, distance.getMax());
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