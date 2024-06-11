package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceTest {
    @Test
    public void testBuildDistance_withMinMaxMeanStdDev() {
        int min = 10;
        int max = 20;
        float mean = 15.0f;
        float stdDev = 2.5f;
        
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMinMax() {
        int min = 10;
        int max = 20;
        
        Distance distance = Distance.buildDistance(min, max);
        float expectedMean = (min / 2.0f) + (max / 2.0f);
        float expectedStdDev = (max - min) / 6.0f;
        
        assertEquals(min, distance.getMin());
        assertEquals(max, distance.getMax());
        assertEquals(expectedMean, distance.getMean(), 0.001);
        assertEquals(expectedStdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMeanStdDev() {
        float mean = 15.0f;
        float stdDev = 2.5f;
        
        float delta = 3.0f * stdDev;
        int plusDelta = (int)(mean + delta);
        int minusDelta = (int)(mean - delta);
        int expectedMin = Math.min(plusDelta, minusDelta);
        int expectedMax = Math.max(plusDelta, minusDelta);
        
        Distance distance = Distance.buildDistance(mean, stdDev);
        
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testComputeMean() {
        int min = 10;
        int max = 20;
        float expectedMean = (min / 2.0f) + (max / 2.0f);
        
        float mean = Distance.computeMean(min, max);
        
        assertEquals(expectedMean, mean, 0.001);
    }
    
    @Test
    public void testComputeStandardDeviation() {
        int min = 10;
        int max = 20;
        float expectedStdDev = (max - min) / 6.0f;
        
        float stdDev = Distance.computeStandardDeviation(min, max);
        
        assertEquals(expectedStdDev, stdDev, 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_whenMeanLessThanDelta() {
        float mean = 10.0f;
        float stdDev = 3.0f;
        float correctedStdDev = (mean - Distance.MAX_ROUNDING_ERROR) / 3.0f;
        
        Distance distance = new Distance(5, 15, mean, stdDev);
        
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001);
        assertEquals(correctedStdDev, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_whenMeanGreaterThanDelta() {
        float mean = 20.0f;
        float stdDev = 3.0f;
        
        Distance distance = new Distance(15, 25, mean, stdDev);
        
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001);
        assertEquals(distance.getStdDev(), transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testHashCode() {
        int min = 10;
        int max = 20;
        float mean = 15.0f;
        float stdDev = 2.5f;
        
        Distance distance = new Distance(min, max, mean, stdDev);
        
        assertEquals(1492243, distance.hashCode());
    }
    
    @Test
    public void testEquals() {
        int min = 10;
        int max = 20;
        float mean = 15.0f;
        float stdDev = 2.5f;
        
        Distance distance1 = new Distance(min, max, mean, stdDev);
        Distance distance2 = new Distance(min, max, mean, stdDev);
        
        assertEquals(true, distance1.equals(distance2));
    }
    
    @Test
    public void testToString() {
        float mean = 15.0f;
        float stdDev = 2.5f;
        
        Distance distance = new Distance(10, 20, mean, stdDev);
        
        assertEquals("15.000, std: 2.500", distance.toString());
    }
}