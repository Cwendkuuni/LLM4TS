package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithParameters() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        assertEquals(3.0f, distance.getMin(), 0.001);
        assertEquals(7.0f, distance.getMax(), 0.001);
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testCalculateDelta() {
        float delta = Distance.calculateDelta(2.0f);
        assertEquals(6.0f, delta, 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testComputeStandardDeviation() {
        float stdDev = Distance.computeStandardDeviation(1, 10);
        assertEquals(1.5f, stdDev, 0.001);
    }
    
    @Test
    public void testComputeMean() {
        float mean = Distance.computeMean(1, 10);
        assertEquals(5.5f, mean, 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(5.0f, transformedDistance.getMean(), 0.001);
        assertEquals(1.995f, transformedDistance.getStdDev(), 0.001);
        
        Distance sameDistance = Distance.transformIntoCeleraAssemblerDistance(transformedDistance);
        assertSame(transformedDistance, sameDistance);
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = new Distance(1, 10, 5.0f, 2.0f);
        Distance distance2 = new Distance(1, 10, 5.0f, 2.0f);
        assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testEqualsWithDifferentValues() {
        Distance distance1 = new Distance(1, 10, 5.0f, 2.0f);
        Distance distance2 = new Distance(2, 10, 5.0f, 2.0f);
        assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testToString() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        String expectedString = "5.000, std: 2.000";
        assertEquals(expectedString, distance.toString());
    }
}