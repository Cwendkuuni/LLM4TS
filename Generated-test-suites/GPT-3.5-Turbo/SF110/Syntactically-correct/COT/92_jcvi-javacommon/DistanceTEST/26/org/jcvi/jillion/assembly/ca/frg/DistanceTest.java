
import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {
    
    @Test
    public void testBuildDistanceMinMeanStdDev() {
        int min = 5;
        float mean = 10.5f;
        float stdDev = 2.3f;
        
        Distance distance = Distance.buildDistance(min, mean, stdDev);
        
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(mean, distance.getMean(), 0.0);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.0);
    }
    
    @Test
    public void testBuildDistanceMeanStdDev() {
        float mean = 10.5f;
        float stdDev = 2.3f;
        
        Distance distance = Distance.buildDistance(mean, stdDev);
        
        Assert.assertEquals(mean - (3.0f * stdDev), distance.getMin(), 0.0);
        Assert.assertEquals(mean + (3.0f * stdDev), distance.getMax(), 0.0);
        Assert.assertEquals(mean, distance.getMean(), 0.0);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.0);
    }
    
    @Test
    public void testBuildDistanceMinMax() {
        int min = 5;
        int max = 20;
        
        Distance distance = Distance.buildDistance(min, max);
        
        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals((min + max) / 2.0, distance.getMean(), 0.0);
        Assert.assertEquals((max - min) / 6.0, distance.getStdDev(), 0.0);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        int min = 5;
        int max = 20;
        float mean = (min + max) / 2.0f;
        float stdDev = (max - min) / 6.0f;
        
        Distance distance = new Distance(min, max, mean, stdDev);
        
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        Assert.assertEquals(distance.getMin(), transformedDistance.getMin());
        Assert.assertEquals(distance.getMax(), transformedDistance.getMax());
        Assert.assertEquals(distance.getMean(), transformedDistance.getMean(), 0.0);
        Assert.assertEquals((mean - Distance.MAX_ROUNDING_ERROR) / Distance.SIX, transformedDistance.getStdDev(), 0.0);
    }
    
    @Test
    public void testEquals() {
        int min = 5;
        int max = 20;
        float mean = (min + max) / 2.0f;
        float stdDev = (max - min) / 6.0f;
        
        Distance distance1 = new Distance(min, max, mean, stdDev);
        Distance distance2 = new Distance(min, max, mean, stdDev);
        
        Assert.assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testHashCode() {
        int min = 5;
        int max = 20;
        float mean = (min + max) / 2.0f;
        float stdDev = (max - min) / 6.0f;
        
        Distance distance = new Distance(min, max, mean, stdDev);
        
        int expectedHashCode = 31 * (31 * (31 * min + Float.floatToIntBits(mean)) + max) + Float.floatToIntBits(stdDev);
        
        Assert.assertEquals(expectedHashCode, distance.hashCode());
    }
    
    @Test
    public void testToString() {
        int min = 5;
        int max = 20;
        float mean = (min + max) / 2.0f;
        float stdDev = (max - min) / 6.0f;
        
        Distance distance = new Distance(min, max, mean, stdDev);
        
        String expectedString = String.format("%.3f, std: %.3f", mean, stdDev);
        
        Assert.assertEquals(expectedString, distance.toString());
    }
}
