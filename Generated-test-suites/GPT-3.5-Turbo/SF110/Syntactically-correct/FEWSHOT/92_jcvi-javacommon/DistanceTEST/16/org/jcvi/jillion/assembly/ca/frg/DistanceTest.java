package org.jcvi.jillion.assembly.ca.frg;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DistanceTest {
    
    private Distance distance;
    
    @Before
    public void setUp() {
        distance = Distance.buildDistance(10, 20, 15.5f, 2.5f);
    }
    
    @Test
    public void testBuildDistanceWithMinAndMax() {
        Distance d = Distance.buildDistance(10, 20);
        assertEquals(10, d.getMin());
        assertEquals(20, d.getMax());
        assertEquals(15.0f, d.getMean(), 0.001);
        assertEquals(1.666f, d.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance d = Distance.buildDistance(15.5f, 2.5f);
        assertEquals(10, d.getMin());
        assertEquals(20, d.getMax());
        assertEquals(15.5f, d.getMean(), 0.001);
        assertEquals(2.5f, d.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithOnlyMeanAndStdDev() {
        Distance d = Distance.buildDistance(15.5f, 2.5f);
        assertEquals(13, d.getMin());
        assertEquals(18, d.getMax());
        assertEquals(15.5f, d.getMean(), 0.001);
        assertEquals(2.5f, d.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(15, transformedDistance.getMin());
        assertEquals(20, transformedDistance.getMax());
        assertEquals(15.5f, transformedDistance.getMean(), 0.001);
        assertEquals(1.838f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testGetMean() {
        assertEquals(15.5f, distance.getMean(), 0.001);
    }
    
    @Test
    public void testGetStdDev() {
        assertEquals(2.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testGetMin() {
        assertEquals(10, distance.getMin());
    }
    
    @Test
    public void testGetMax() {
        assertEquals(20, distance.getMax());
    }
    
    @Test
    public void testHashCode() {
        int expectedHashCode = 961983 + Float.floatToIntBits(15.5f) + 10 + Float.floatToIntBits(2.5f);
        assertEquals(expectedHashCode, distance.hashCode());
    }
    
    @Test
    public void testEquals() {
        Distance d = Distance.buildDistance(10, 20, 15.5f, 2.5f);
        assertTrue(distance.equals(d));
    }
    
    @Test
    public void testToString() {
        assertEquals("15.500, std: 2.500", distance.toString());
    }
}