package org.jcvi.jillion.assembly.ca.frg;

import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
    
    @Test
    public void testBuildDistance_withMinAndMax() {
        Distance distance = Distance.buildDistance(10, 20);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.0001);
        assertEquals(1.6667f, distance.getStdDev(), 0.0001);
    }
    
    @Test
    public void testBuildDistance_withMeanAndStdDev() {
        Distance distance = Distance.buildDistance(15.0f, 1.6667f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.0001);
        assertEquals(1.6667f, distance.getStdDev(), 0.0001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(10, 20);
        
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.0001);
        assertEquals((distance.getMean() - 1.015f) / 3.0f, transformedDistance.getStdDev(), 0.0001);
        
        distance = Distance.buildDistance(25.0f, 3.3333f);
        transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.0001);
        assertEquals(distance.getStdDev(), transformedDistance.getStdDev(), 0.0001);
    }
    
    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(10, 20);
        Distance distance2 = Distance.buildDistance(10, 20);
        Distance distance3 = Distance.buildDistance(15, 30);
        
        assertEquals(distance1.hashCode(), distance2.hashCode());
        assertNotEquals(distance1.hashCode(), distance3.hashCode());
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(10, 20);
        Distance distance2 = Distance.buildDistance(10, 20);
        Distance distance3 = Distance.buildDistance(15, 30);
        Distance distance4 = Distance.buildDistance(10, 30);
        
        assertTrue(distance1.equals(distance2));
        assertFalse(distance1.equals(distance3));
        assertFalse(distance1.equals(distance4));
    }
    
    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(10, 20);
        
        String expected = "15.000, std: 1.667";
        assertEquals(expected, distance.toString());
    }
}