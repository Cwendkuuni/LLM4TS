import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {
    
    @Test
    public void testBuildDistance_withMinAndMaxAndMeanAndStdDev_shouldReturnDistanceObject() {
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
    public void testBuildDistance_withMeanAndStdDev_shouldReturnDistanceObjectWithCalculatedMinMax() {
        float mean = 5.5f;
        float stdDev = 1.5f;
        
        Distance distance = Distance.buildDistance(mean, stdDev);
        
        float delta = Distance.calculateDelta(stdDev);
        int plusDelta = (int)(mean + delta);
        int minusDelta = (int)(mean - delta);
        
        Assert.assertEquals(Math.min(plusDelta, minusDelta), distance.getMin());
        Assert.assertEquals(Math.max(plusDelta, minusDelta), distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.001);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMinAndMax_shouldReturnDistanceObjectWithCalculatedMeanAndStdDev() {
        int min = 1;
        int max = 10;
        
        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);
        
        Distance distance = Distance.buildDistance(min, max);
        
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.001);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withDistanceBelowDelta_shouldReturnNewDistanceWithCorrectedStdDev() {
        float mean = 1.0f;
        float stdDev = 0.5f;
        Distance distance = new Distance(1, 10, mean, stdDev);
        
        float correctedStdDev = (mean - 1.015f) / 3.0f;
        Distance expectedDistance = new Distance(1, 10, mean, correctedStdDev);
        
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        Assert.assertEquals(expectedDistance, transformedDistance);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withDistanceAboveDelta_shouldReturnSameDistanceObject() {
        float mean = 10.0f;
        float stdDev = 2.0f;
        Distance distance = new Distance(1, 10, mean, stdDev);
        
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        Assert.assertEquals(distance, transformedDistance);
    }
    
    @Test
    public void testHashCode_shouldReturnHashCode() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 1.5f;
        
        Distance distance = new Distance(min, max, mean, stdDev);
        
        int expectedHashCode = 31 * (31 * (31 * (31 * 1 + max) + Float.floatToIntBits(mean)) + min) + Float.floatToIntBits(stdDev);
        
        Assert.assertEquals(expectedHashCode, distance.hashCode());
    }
    
    @Test
    public void testEquals_withSameObject_shouldReturnTrue() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 1.5f;
        
        Distance distance = new Distance(min, max, mean, stdDev);
        
        boolean result = distance.equals(distance);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEquals_withNullObject_shouldReturnFalse() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 1.5f;
        
        Distance distance = new Distance(min, max, mean, stdDev);
        
        boolean result = distance.equals(null);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_withDifferentObjectType_shouldReturnFalse() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 1.5f;
        
        Distance distance = new Distance(min, max, mean, stdDev);
        
        boolean result = distance.equals("test");
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_withDifferentDistanceObject_shouldReturnFalse() {
        int min1 = 1;
        int max1 = 10;
        float mean1 = 5.5f;
        float stdDev1 = 1.5f;
        Distance distance1 = new Distance(min1, max1, mean1, stdDev1);
        
        int min2 = 2;
        int max2 = 11;
        float mean2 = 6.5f;
        float stdDev2 = 2.5f;
        Distance distance2 = new Distance(min2, max2, mean2, stdDev2);
        
        boolean result = distance1.equals(distance2);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_withSameDistanceObject_shouldReturnTrue() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 1.5f;
        
        Distance distance1 = new Distance(min, max, mean, stdDev);
        Distance distance2 = new Distance(min, max, mean, stdDev);
        
        boolean result = distance1.equals(distance2);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testToString_shouldReturnFormattedString() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 1.5f;
        
        Distance distance = new Distance(min, max, mean, stdDev);
        
        String expectedString = String.format("%.3f, std: %.3f", mean, stdDev);
        
        Assert.assertEquals(expectedString, distance.toString());
    }
}