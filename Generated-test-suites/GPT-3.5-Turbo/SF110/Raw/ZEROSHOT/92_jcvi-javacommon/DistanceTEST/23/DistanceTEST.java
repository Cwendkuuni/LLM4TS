import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithFourArguments() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.0f);
        Assert.assertEquals(1, distance.getMin());
        Assert.assertEquals(10, distance.getMax());
        Assert.assertEquals(5.5f, distance.getMean(), 0.001);
        Assert.assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithTwoArguments() {
        Distance distance = Distance.buildDistance(3.5f, 1.5f);
        Assert.assertEquals(2, distance.getMin());
        Assert.assertEquals(5, distance.getMax());
        Assert.assertEquals(3.5f, distance.getMean(), 0.001);
        Assert.assertEquals(1.5f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithTwoArgumentsLessThanDelta() {
        Distance distance = Distance.buildDistance(0.5f, 0.1f);
        Assert.assertEquals(0, distance.getMin());
        Assert.assertEquals(1, distance.getMax());
        Assert.assertEquals(0.5f, distance.getMean(), 0.001);
        Assert.assertEquals((0.5f - 1.015f) / 3.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithTwoArgumentsGreaterThanDelta() {
        Distance distance = Distance.buildDistance(10.0f, 2.0f);
        Assert.assertEquals(8, distance.getMin());
        Assert.assertEquals(12, distance.getMax());
        Assert.assertEquals(10.0f, distance.getMean(), 0.001);
        Assert.assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testComputeStandardDeviation() {
        float stdDev = Distance.computeStandardDeviation(0, 10);
        Assert.assertEquals(10.0f / 6.0f, stdDev, 0.001);
    }

    @Test
    public void testComputeMean() {
        float mean = Distance.computeMean(0, 10);
        Assert.assertEquals(5.0f, mean, 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(5, 15, 10.0f, 1.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(distance.getMin(), transformedDistance.getMin());
        Assert.assertEquals(distance.getMax(), transformedDistance.getMax());
        Assert.assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001);
        Assert.assertEquals((distance.getMean() - 1.015f) / 3.0f, transformedDistance.getStdDev(), 0.001);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 2.0f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.5f, 2.0f);
        Assert.assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 2.0f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.5f, 2.0f);
        Assert.assertEquals(distance1, distance2);
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(5, 15, 10.0f, 1.5f);
        Assert.assertEquals("10.000, std: 1.500", distance.toString());
    }
}