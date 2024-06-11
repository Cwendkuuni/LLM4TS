import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {
    
    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(1, 10);
        Assert.assertEquals(1, distance.getMin());
        Assert.assertEquals(10, distance.getMax());
        Assert.assertEquals(5.5f, distance.getMean(), 0.001f);
        Assert.assertEquals(1.5f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(5.5f, 1.5f);
        Assert.assertEquals(2, distance.getMin());
        Assert.assertEquals(9, distance.getMax());
        Assert.assertEquals(5.5f, distance.getMean(), 0.001f);
        Assert.assertEquals(1.5f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testBuildDistanceWithMinAndMax() {
        Distance distance = Distance.buildDistance(2, 9);
        Assert.assertEquals(2, distance.getMin());
        Assert.assertEquals(9, distance.getMax());
        Assert.assertEquals(5.5f, distance.getMean(), 0.001f);
        Assert.assertEquals(1.1666667f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(2, 9);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(distance.getMin(), transformedDistance.getMin());
        Assert.assertEquals(distance.getMax(), transformedDistance.getMax());
        Assert.assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001f);
        Assert.assertEquals(0.51166666f, transformedDistance.getStdDev(), 0.001f);
        
        distance = Distance.buildDistance(0, 5);
        transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(distance.getMin(), transformedDistance.getMin());
        Assert.assertEquals(distance.getMax(), transformedDistance.getMax());
        Assert.assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001f);
        Assert.assertEquals(-0.17166667f, transformedDistance.getStdDev(), 0.001f);
        
        distance = Distance.buildDistance(10, 15);
        transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(distance.getMin(), transformedDistance.getMin());
        Assert.assertEquals(distance.getMax(), transformedDistance.getMax());
        Assert.assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001f);
        Assert.assertEquals(0.83666664f, transformedDistance.getStdDev(), 0.001f);
    }
}