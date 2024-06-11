import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {
    
    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Assert.assertEquals(1, distance.getMin());
        Assert.assertEquals(10, distance.getMax());
        Assert.assertEquals(5.5f, distance.getMean(), 0.01);
        Assert.assertEquals(2.5f, distance.getStdDev(), 0.01);
    }
    
    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(5.5f, 2.5f);
        float delta = 3.0f * 2.5f;
        int plusDelta = (int)(5.5f + delta);
        int minusDelta = (int)(5.5f - delta);
        Assert.assertEquals(Math.min(plusDelta, minusDelta), distance.getMin());
        Assert.assertEquals(Math.max(plusDelta, minusDelta), distance.getMax());
        Assert.assertEquals(5.5f, distance.getMean(), 0.01);
        Assert.assertEquals(2.5f, distance.getStdDev(), 0.01);
    }
    
    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        float mean = (1/2.0f) + (10/2.0f);
        float stdDev = (10 - 1) / 6.0f;
        Assert.assertEquals(1, distance.getMin());
        Assert.assertEquals(10, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.01);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.01);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance initialDistance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(initialDistance);
        if (initialDistance.getMean() < Distance.calculateDelta(initialDistance.getStdDev())) {
            float correctedStdDev = (initialDistance.getMean() - 1.015f) / 3.0f;
            Assert.assertEquals(initialDistance.getMin(), transformedDistance.getMin());
            Assert.assertEquals(initialDistance.getMax(), transformedDistance.getMax());
            Assert.assertEquals(initialDistance.getMean(), transformedDistance.getMean(), 0.01);
            Assert.assertEquals(correctedStdDev, transformedDistance.getStdDev(), 0.01);
        }
        else {
            Assert.assertEquals(initialDistance, transformedDistance);
        }
    }
    
    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Assert.assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Assert.assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        String expected = String.format("%.3f, std: %.3f", distance.getMean(), distance.getStdDev());
        Assert.assertEquals(expected, distance.toString());
    }
}