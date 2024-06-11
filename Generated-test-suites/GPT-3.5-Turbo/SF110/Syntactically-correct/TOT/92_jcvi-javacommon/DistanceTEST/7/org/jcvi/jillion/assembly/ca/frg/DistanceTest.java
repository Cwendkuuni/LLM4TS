import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @Test
    public void testBuildDistance_withMin_Max_Mean_StdDev_ReturnsCorrectDistanceObject() {
        int min = 0;
        int max = 100;
        float mean = 50.0f;
        float stdDev = 10.0f;
        
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.001);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMean_StdDev_ReturnsCorrectDistanceObject() {
        float mean = 25.0f;
        float stdDev = 5.0f;
        
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
    public void testBuildDistance_withMin_Max_ReturnsCorrectDistanceObject() {
        int min = 0;
        int max = 100;
        
        Distance distance = Distance.buildDistance(min, max);
        
        float mean = Distance.computeMean(min, max);
        float stdDev = Distance.computeStandardDeviation(min, max);
        
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.001);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withDistanceBelowDelta_ReturnsCorrectDistanceObject() {
        float mean = 25.0f;
        float stdDev = 5.0f;
        
        Distance distance = new Distance(0, 100, mean, stdDev);
        
        float delta = Distance.calculateDelta(stdDev);
        float correctedStdDev = (mean - Distance.MAX_ROUNDING_ERROR) / SIX;
        
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        Assert.assertEquals(distance.getMin(), transformedDistance.getMin());
        Assert.assertEquals(distance.getMax(), transformedDistance.getMax());
        Assert.assertEquals(mean, transformedDistance.getMean(), 0.001);
        Assert.assertEquals(correctedStdDev, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withDistanceAboveDelta_ReturnsSameDistanceObject() {
        float mean = 25.0f;
        float stdDev = 5.0f;
        
        Distance distance = new Distance(200, 300, mean, stdDev);
        
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        Assert.assertEquals(distance, transformedDistance);
    }
    
    @Test
    public void testEquals_withEqualDistanceObjects_ReturnsTrue() {
        Distance distance1 = new Distance(0, 100, 50.0f, 10.0f);
        Distance distance2 = new Distance(0, 100, 50.0f, 10.0f);
        
        boolean result = distance1.equals(distance2);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEquals_withDifferentDistanceObjects_ReturnsFalse() {
        Distance distance1 = new Distance(0, 100, 50.0f, 10.0f);
        Distance distance2 = new Distance(100, 200, 75.0f, 5.0f);
        
        boolean result = distance1.equals(distance2);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testHashCode_withEqualDistanceObjects_ReturnsSameHashCode() {
        Distance distance1 = new Distance(0, 100, 50.0f, 10.0f);
        Distance distance2 = new Distance(0, 100, 50.0f, 10.0f);
        
        int hashCode1 = distance1.hashCode();
        int hashCode2 = distance2.hashCode();
        
        Assert.assertEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testHashCode_withDifferentDistanceObjects_ReturnsDifferentHashCode() {
        Distance distance1 = new Distance(0, 100, 50.0f, 10.0f);
        Distance distance2 = new Distance(100, 200, 75.0f, 5.0f);
        
        int hashCode1 = distance1.hashCode();
        int hashCode2 = distance2.hashCode();
        
        Assert.assertNotEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testToString_ReturnsFormattedString() {
        Distance distance = new Distance(0, 100, 50.0f, 10.0f);
        String expectedString = "50.000, std: 10.000";
        
        String result = distance.toString();
        
        Assert.assertEquals(expectedString, result);
    }
}