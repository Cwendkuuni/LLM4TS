
package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceTest {
    
    @Test
    public void testBuildDistance_withMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001f);
        assertEquals(2.5f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testBuildDistance_withMeanStdDev() {
        Distance distance = Distance.buildDistance(20.0f, 3.0f);
        
        assertEquals(17, distance.getMin());
        assertEquals(23, distance.getMax());
        assertEquals(20.0f, distance.getMean(), 0.001f);
        assertEquals(3.0f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testBuildDistance_withMinMax() {
        Distance distance = Distance.buildDistance(10, 20);
        
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001f);
        assertEquals(1.666f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_lessThanDelta() {
        Distance distance = Distance.buildDistance(5, 10, 7.0f, 1.0f);
        
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        assertEquals(5, celeraDistance.getMin());
        assertEquals(10, celeraDistance.getMax());
        assertEquals(7.0f, celeraDistance.getMean(), 0.001f);
        assertEquals(1.328f, celeraDistance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_greaterThanDelta() {
        Distance distance = Distance.buildDistance(15, 25, 20.0f, 4.0f);
        
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        assertEquals(15, celeraDistance.getMin());
        assertEquals(25, celeraDistance.getMax());
        assertEquals(20.0f, celeraDistance.getMean(), 0.001f);
        assertEquals(4.0f, celeraDistance.getStdDev(), 0.001f);
    }
}
