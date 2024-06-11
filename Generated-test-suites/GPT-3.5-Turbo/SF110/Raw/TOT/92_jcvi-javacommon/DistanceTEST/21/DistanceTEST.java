package org.jcvi.jillion.assembly.ca.frg;

import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistance_withMinAndMax_properlySetsValues() {
        Distance distance = Distance.buildDistance(3, 10);
        
        assertEquals(3, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(6.5f, distance.getMean(), 0.001f);
        assertEquals(1.166f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testBuildDistance_withMinAndMaxEqual_properlySetsValues() {
        Distance distance = Distance.buildDistance(5, 5);
        
        assertEquals(5, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001f);
        assertEquals(0.0f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testBuildDistance_withMeanAndStdDev_properlySetsValues() {
        Distance distance = Distance.buildDistance(10.5f, 1.5f);
        
        assertEquals(8, distance.getMin());
        assertEquals(13, distance.getMax());
        assertEquals(10.5f, distance.getMean(), 0.001f);
        assertEquals(1.5f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testBuildDistance_withMeanAndStdDevZero_properlySetsValues() {
        Distance distance = Distance.buildDistance(0.0f, 0.0f);
        
        assertEquals(0, distance.getMin());
        assertEquals(0, distance.getMax());
        assertEquals(0.0f, distance.getMean(), 0.001f);
        assertEquals(0.0f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withMeanBelowDelta_returnsDistanceWithCorrectedStdDev() {
        Distance distance = new Distance(3, 8, 2.5f, 0.5f);
        
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        assertEquals(3, celeraDistance.getMin());
        assertEquals(8, celeraDistance.getMax());
        assertEquals(2.5f, celeraDistance.getMean(), 0.001f);
        assertEquals(0.495f, celeraDistance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withMeanAboveDelta_returnsSameDistance() {
        Distance distance = new Distance(3, 8, 5.0f, 0.5f);
        
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        assertEquals(3, celeraDistance.getMin());
        assertEquals(8, celeraDistance.getMax());
        assertEquals(5.0f, celeraDistance.getMean(), 0.001f);
        assertEquals(0.5f, celeraDistance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testHashCode_withSameDistance_returnsSameHashCode() {
        Distance distance1 = new Distance(3, 8, 5.0f, 0.5f);
        Distance distance2 = new Distance(3, 8, 5.0f, 0.5f);
        
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testEquals_withSameDistance_returnsTrue() {
        Distance distance1 = new Distance(3, 8, 5.0f, 0.5f);
        Distance distance2 = new Distance(3, 8, 5.0f, 0.5f);
        
        assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_withDifferentDistance_returnsFalse() {
        Distance distance1 = new Distance(3, 8, 5.0f, 0.5f);
        Distance distance2 = new Distance(4, 8, 5.0f, 0.5f);
        
        assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testToString_returnsStringFormattedWithMeanAndStdDev() {
        Distance distance = new Distance(3, 8, 5.0f, 0.5f);
        
        String expected = "5.000, std: 0.500";
        assertEquals(expected, distance.toString());
    }

}