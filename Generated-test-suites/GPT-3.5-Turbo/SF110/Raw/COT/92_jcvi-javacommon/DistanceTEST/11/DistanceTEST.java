
import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithIntegers() {
        Distance distance = Distance.buildDistance(10, 20);
        Assert.assertEquals(10, distance.getMin());
        Assert.assertEquals(20, distance.getMax());
        Assert.assertEquals(15.0f, distance.getMean(), 0.001);
        Assert.assertEquals(1.666f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithFloats() {
        Distance distance = Distance.buildDistance(15.0f, 1.666f);
        Assert.assertEquals(13, distance.getMin());
        Assert.assertEquals(17, distance.getMax());
        Assert.assertEquals(15.0f, distance.getMean(), 0.001);
        Assert.assertEquals(1.666f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 1.666f);
        Assert.assertEquals(10, distance.getMin());
        Assert.assertEquals(20, distance.getMax());
        Assert.assertEquals(15.0f, distance.getMean(), 0.001);
        Assert.assertEquals(1.666f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_belowDelta() {
        Distance distance = Distance.buildDistance(10, 20);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(10, transformedDistance.getMin());
        Assert.assertEquals(20, transformedDistance.getMax());
        Assert.assertEquals(15.0f, transformedDistance.getMean(), 0.001);
        Assert.assertEquals(0.995f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_aboveDelta() {
        Distance distance = Distance.buildDistance(30, 40);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(30, transformedDistance.getMin());
        Assert.assertEquals(40, transformedDistance.getMax());
        Assert.assertEquals(35.0f, transformedDistance.getMean(), 0.001);
        Assert.assertEquals(2.5f, transformedDistance.getStdDev(), 0.001);
    }
}
