
package org.jcvi.jillion.assembly.ca.frg;

import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {
    
    @Test
    public void testBuildDistance_withMinAndMax_shouldSetMinMaxMeanAndStdDev() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 1.5f;
        
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.001);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMeanAndStdDev_shouldSetMinMaxMeanAndStdDev() {
        float mean = 5.5f;
        float stdDev = 1.5f;
        
        Distance distance = Distance.buildDistance(mean, stdDev);
        
        float expectedMin = (int)(mean - (3.0f * stdDev));
        float expectedMax = (int)(mean + (3.0f * stdDev));
        
        Assert.assertEquals(expectedMin, distance.getMin(), 0.001);
        Assert.assertEquals(expectedMax, distance.getMax(), 0.001);
        Assert.assertEquals(mean, distance.getMean(), 0.001);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMinAndMax_shouldSetMeanAndStdDev() {
        int min = 1;
        int max = 10;
        
        Distance distance = Distance.buildDistance(min, max);
        
        float expectedMean = (min / 2.0f) + (max / 2.0f);
        float expectedStdDev = (max - min) / 6.0f;
        
        Assert.assertEquals(expectedMean, distance.getMean(), 0.001);
        Assert.assertEquals(expectedStdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withMeanLowerThanDelta_shouldCorrectStdDev() {
        int min = 1;
        int max = 10;
        float mean = 2.0f;
        float stdDev = 1.0f;
        Distance originalDistance = new Distance(min, max, mean, stdDev);
        
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);
        
        float expectedCorrectedStdDev = (mean - 1.015f) / 3.0f;
        
        Assert.assertEquals(stdDev, originalDistance.getStdDev(), 0.001);
        Assert.assertEquals(mean, originalDistance.getMean(), 0.001);
        Assert.assertEquals(expectedCorrectedStdDev, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withMeanHigherThanDelta_shouldNotChangeValues() {
        int min = 1;
        int max = 10;
        float mean = 5.0f;
        float stdDev = 1.0f;
        Distance originalDistance = new Distance(min, max, mean, stdDev);
        
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);
        
        Assert.assertEquals(stdDev, originalDistance.getStdDev(), 0.001);
        Assert.assertEquals(mean, originalDistance.getMean(), 0.001);
        Assert.assertTrue(transformedDistance == originalDistance);
    }
    
    @Test
    public void testHashCode_withEqualDistances_shouldReturnSameHashCode() {
        Distance distance1 = new Distance(1, 10, 5.5f, 1.5f);
        Distance distance2 = new Distance(1, 10, 5.5f, 1.5f);
        
        Assert.assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testHashCode_withDifferentDistances_shouldNotReturnSameHashCode() {
        Distance distance1 = new Distance(1, 10, 5.5f, 1.5f);
        Distance distance2 = new Distance(1, 20, 5.5f, 1.5f);
        
        Assert.assertNotEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testEquals_withSameObject_shouldReturnTrue() {
        Distance distance = new Distance(1, 10, 5.5f, 1.5f);
        
        Assert.assertTrue(distance.equals(distance));
    }
    
    @Test
    public void testEquals_withNullObject_shouldReturnFalse() {
        Distance distance = new Distance(1, 10, 5.5f, 1.5f);
        
        Assert.assertFalse(distance.equals(null));
    }
    
    @Test
    public void testEquals_withDifferentClassObject_shouldReturnFalse() {
        Distance distance = new Distance(1, 10, 5.5f, 1.5f);
        
        Assert.assertFalse(distance.equals("test"));
    }
    
    @Test
    public void testEquals_withDifferentDistances_shouldReturnFalse() {
        Distance distance1 = new Distance(1, 10, 5.5f, 1.5f);
        Distance distance2 = new Distance(1, 20, 5.5f, 1.5f);
        
        Assert.assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_withEqualDistances_shouldReturnTrue() {
        Distance distance1 = new Distance(1, 10, 5.5f, 1.5f);
        Distance distance2 = new Distance(1, 10, 5.5f, 1.5f);
        
        Assert.assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testToString_shouldReturnMeanAndStdDev() {
        Distance distance = new Distance(1, 10, 5.5f, 1.5f);
        
        String expected = "5.500, std: 1.500";
        
        Assert.assertEquals(expected, distance.toString());
    }
}
