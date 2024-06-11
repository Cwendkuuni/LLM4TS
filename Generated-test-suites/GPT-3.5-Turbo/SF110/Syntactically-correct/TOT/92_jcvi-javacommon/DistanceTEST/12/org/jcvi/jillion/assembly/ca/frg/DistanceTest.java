package org.jcvi.jillion.assembly.ca.frg;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
    
    private Distance distance;
    
    @Before
    public void setup() {
        distance = Distance.buildDistance(2, 10, 6.0f, 1.0f);
    }
    
    @Test
    public void testBuildDistance_withMin_Max_Mean_STDDev() {
        Distance newDistance = Distance.buildDistance(1, 20, 10.0f, 2.0f);
        assertEquals(1, newDistance.getMin());
        assertEquals(20, newDistance.getMax());
        assertEquals(10.0f, newDistance.getMean(), 0.001);
        assertEquals(2.0f, newDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMean_STDDev() {
        Distance newDistance = Distance.buildDistance(5.0f, 0.5f);
        assertEquals(4, newDistance.getMin());
        assertEquals(6, newDistance.getMax());
        assertEquals(5.0f, newDistance.getMean(), 0.001);
        assertEquals(0.5f, newDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMin_Max() {
        Distance newDistance = Distance.buildDistance(5, 10);
        assertEquals(5, newDistance.getMin());
        assertEquals(10, newDistance.getMax());
        assertEquals(7.5f, newDistance.getMean(), 0.001);
        assertEquals(0.833f, newDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withMeanSTDDev_lessThanDelta() {
        Distance sourceDistance = Distance.buildDistance(5.0f, 0.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(sourceDistance);
        assertEquals(4, transformedDistance.getMin());
        assertEquals(6, transformedDistance.getMax());
        assertEquals(5.0f, transformedDistance.getMean(), 0.001);
        assertEquals(1.495f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withMeanSTDDev_greaterThanDelta() {
        Distance sourceDistance = Distance.buildDistance(10.0f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(sourceDistance);
        assertEquals(10, transformedDistance.getMin());
        assertEquals(20, transformedDistance.getMax());
        assertEquals(10.0f, transformedDistance.getMean(), 0.001);
        assertEquals(2.0f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testHashCode() {
        int expectedHashCode = 961;
        assertEquals(expectedHashCode, distance.hashCode());
    }
    
    @Test
    public void testEquals_withEqualObject() {
        Distance equalDistance = Distance.buildDistance(2, 10, 6.0f, 1.0f);
        assertTrue(distance.equals(equalDistance));
    }
    
    @Test
    public void testEquals_withDifferentMinMax() {
        Distance differentDistance1 = Distance.buildDistance(0, 10, 6.0f, 1.0f);
        assertFalse(distance.equals(differentDistance1));
        
        Distance differentDistance2 = Distance.buildDistance(2, 20, 6.0f, 1.0f);
        assertFalse(distance.equals(differentDistance2));
    }
    
    @Test
    public void testEquals_withDifferentMean() {
        Distance differentDistance = Distance.buildDistance(2, 10, 8.0f, 1.0f);
        assertFalse(distance.equals(differentDistance));
    }
    
    @Test
    public void testEquals_withDifferentStdDev() {
        Distance differentDistance = Distance.buildDistance(2, 10, 6.0f, 2.0f);
        assertFalse(distance.equals(differentDistance));
    }
    
    @Test
    public void testToString() {
        String expectedString = "6.000, std: 1.000";
        assertEquals(expectedString, distance.toString());
    }
}