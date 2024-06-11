
package org.jcvi.jillion.assembly.ca.frg;

import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 2.0f;
        
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.001);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMeanStdDev() {
        float mean = 5.5f;
        float stdDev = 2.0f;
        
        Distance distance = Distance.buildDistance(mean, stdDev);
        
        Assert.assertEquals(mean - Distance.calculateDelta(stdDev), distance.getMin(), 0.001);
        Assert.assertEquals(mean + Distance.calculateDelta(stdDev), distance.getMax(), 0.001);
        Assert.assertEquals(mean, distance.getMean(), 0.001);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMinMax() {
        int min = 1;
        int max = 10;
        
        Distance distance = Distance.buildDistance(min, max);
        
        float mean = (float) (min / 2.0 + max / 2.0);
        float stdDev = (max - min) / 6.0f;
        
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.001);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(1, 10, 5.5f, 2.0f);
        
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        float correctedStdDev = (distance.getMean() - Distance.MAX_ROUNDING_ERROR) / Distance.SIX;
        Assert.assertEquals(distance.getMin(), transformedDistance.getMin());
        Assert.assertEquals(distance.getMax(), transformedDistance.getMax());
        Assert.assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001);
        Assert.assertEquals(correctedStdDev, transformedDistance.getStdDev(), 0.001);
        
        distance = new Distance(20, 30, 25.0f, 2.0f);
        
        transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        Assert.assertEquals(distance.getMin(), transformedDistance.getMin());
        Assert.assertEquals(distance.getMax(), transformedDistance.getMax());
        Assert.assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001);
        Assert.assertEquals(distance.getStdDev(), transformedDistance.getStdDev(), 0.001);
    }
}
