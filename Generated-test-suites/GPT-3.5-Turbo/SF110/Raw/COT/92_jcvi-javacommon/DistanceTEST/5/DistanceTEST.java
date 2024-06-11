
import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.5f);
        Assert.assertEquals(1, distance.getMin());
        Assert.assertEquals(10, distance.getMax());
        Assert.assertEquals(5.0f, distance.getMean(), 0.001);
        Assert.assertEquals(2.5f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(5.0f, 2.5f);
        float expectedMin = (int) (5.0f - 7.5f);
        float expectedMax = (int) (5.0f + 7.5f);
        Assert.assertEquals(expectedMin, distance.getMin(), 0.001);
        Assert.assertEquals(expectedMax, distance.getMax(), 0.001);
        Assert.assertEquals(5.0f, distance.getMean(), 0.001);
        Assert.assertEquals(2.5f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        float expectedMean = (1 / 2.0f) + (10 / 2.0f);
        float expectedStdDev = (10 - 1) / 6.0f;
        Assert.assertEquals(1, distance.getMin());
        Assert.assertEquals(10, distance.getMax());
        Assert.assertEquals(expectedMean, distance.getMean(), 0.001);
        Assert.assertEquals(expectedStdDev, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceSmallerMean() {
        Distance distance = Distance.buildDistance(4, 7, 3.0f, 1.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(4, transformedDistance.getMin());
        Assert.assertEquals(7, transformedDistance.getMax());
        Assert.assertEquals(3.0f, transformedDistance.getMean(), 0.001);
        Assert.assertEquals(0.995f, transformedDistance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceLargerMean() {
        Distance distance = Distance.buildDistance(10, 15, 12.0f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(10, transformedDistance.getMin());
        Assert.assertEquals(15, transformedDistance.getMax());
        Assert.assertEquals(12.0f, transformedDistance.getMean(), 0.001);
        Assert.assertEquals(2.0f, transformedDistance.getStdDev(), 0.001);
    }
}
