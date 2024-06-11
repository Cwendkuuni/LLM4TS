import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinAndMax() {
        Distance distance = Distance.buildDistance(10, 20);
        Assert.assertEquals(10, distance.getMin());
        Assert.assertEquals(20, distance.getMax());
        Assert.assertEquals(15.0f, distance.getMean(), 0);
        Assert.assertEquals(1.6666666f, distance.getStdDev(), 0.00001f);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(15.0f, 1.666666f);
        Assert.assertEquals(10, distance.getMin());
        Assert.assertEquals(20, distance.getMax());
        Assert.assertEquals(15.0f, distance.getMean(), 0);
        Assert.assertEquals(1.6666666f, distance.getStdDev(), 0.00001f);
    }

    @Test
    public void testBuildDistanceWithAllParameters() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 1.6666666f);
        Assert.assertEquals(10, distance.getMin());
        Assert.assertEquals(20, distance.getMax());
        Assert.assertEquals(15.0f, distance.getMean(), 0);
        Assert.assertEquals(1.6666666f, distance.getStdDev(), 0.00001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanBelowDelta() {
        Distance distance = Distance.buildDistance(5, 15);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(5, transformedDistance.getMin());
        Assert.assertEquals(15, transformedDistance.getMax());
        Assert.assertEquals(10.0f, transformedDistance.getMean(), 0);
        Assert.assertEquals(1.661667f, transformedDistance.getStdDev(), 0.00001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanEqualDelta() {
        Distance distance = Distance.buildDistance(3, 9);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(3, transformedDistance.getMin());
        Assert.assertEquals(9, transformedDistance.getMax());
        Assert.assertEquals(6.0f, transformedDistance.getMean(), 0);
        Assert.assertEquals(0.995f, transformedDistance.getStdDev(), 0.00001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanAboveDelta() {
        Distance distance = Distance.buildDistance(2, 4);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(2, transformedDistance.getMin());
        Assert.assertEquals(4, transformedDistance.getMax());
        Assert.assertEquals(3.0f, transformedDistance.getMean(), 0);
        Assert.assertEquals(0.985f, transformedDistance.getStdDev(), 0.00001f);
    }
}