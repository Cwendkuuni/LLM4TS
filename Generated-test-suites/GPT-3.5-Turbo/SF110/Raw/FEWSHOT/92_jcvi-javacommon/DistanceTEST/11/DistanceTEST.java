package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceTest {
    
    @Test
    public void testBuildDistance_firstConstructor() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001f);
        assertEquals(2.0f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testBuildDistance_secondConstructor() {
        Distance distance = Distance.buildDistance(15.0f, 2.0f);
        assertEquals(15.0f, distance.getMean(), 0.001f);
        assertEquals(2.0f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testBuildDistance_thirdConstructor() {
        Distance distance = Distance.buildDistance(10, 20);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001f);
        assertEquals(1.6666666f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_meanLessThanDelta() {
        Distance originalDistance = Distance.buildDistance(10, 20, 5.0f, 3.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);
        assertEquals(originalDistance, transformedDistance);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_meanGreaterThanDelta() {
        Distance originalDistance = Distance.buildDistance(10, 20, 15.0f, 3.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);
        Distance expectedDistance = Distance.buildDistance(10, 20, 15.0f, 4.6616666f);
        assertEquals(expectedDistance, transformedDistance);
    }
}