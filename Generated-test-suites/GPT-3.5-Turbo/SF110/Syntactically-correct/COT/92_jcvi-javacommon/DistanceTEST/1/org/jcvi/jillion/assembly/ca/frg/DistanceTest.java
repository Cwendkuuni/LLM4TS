
package org.jcvi.jillion.assembly.ca.frg;

import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {
    
    @Test
    public void testBuildDistance1() {
        int min = 0;
        int max = 10;
        float mean = 5.0f;
        float stdDev = 1.0f;
        
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.0001);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.0001);
    }
    
    @Test
    public void testBuildDistance2() {
        float mean = 5.0f;
        float stdDev = 1.0f;
        
        Distance distance = Distance.buildDistance(mean, stdDev);
        
        Assert.assertTrue(distance.getMin() <= distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.0001);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.0001);
    }
    
    @Test
    public void testBuildDistance3() {
        int min = 0;
        int max = 10;
        
        Distance distance = Distance.buildDistance(min, max);
        
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals((min + max) / 2.0f, distance.getMean(), 0.0001);
        Assert.assertEquals((max - min) / 6.0f, distance.getStdDev(), 0.0001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance1() {
        int min = 0;
        int max = 10;
        float mean = 5.0f;
        float stdDev = 1.0f;
        
        Distance distance = new Distance(min, max, mean, stdDev);
        
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        Assert.assertEquals(min, celeraDistance.getMin());
        Assert.assertEquals(max, celeraDistance.getMax());
        Assert.assertEquals(mean, celeraDistance.getMean(), 0.0001);
        Assert.assertEquals((mean - Distance.MAX_ROUNDING_ERROR) / Distance.CA_GATEKEEPER_STDDEV_LIMIT, celeraDistance.getStdDev(), 0.0001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance2() {
        int min = 0;
        int max = 10;
        float mean = 3.0f;
        float stdDev = 1.0f;
        
        Distance distance = new Distance(min, max, mean, stdDev);
        
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        Assert.assertEquals(min, celeraDistance.getMin());
        Assert.assertEquals(max, celeraDistance.getMax());
        Assert.assertEquals(mean, celeraDistance.getMean(), 0.0001);
        Assert.assertEquals(stdDev, celeraDistance.getStdDev(), 0.0001);
    }
    
}
