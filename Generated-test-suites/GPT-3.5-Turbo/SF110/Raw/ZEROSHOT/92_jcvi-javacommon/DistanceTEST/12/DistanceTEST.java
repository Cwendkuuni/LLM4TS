
import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinAndMax() {
        Distance distance = Distance.buildDistance(10, 20);
        Assert.assertEquals(10, distance.getMin());
        Assert.assertEquals(20, distance.getMax());
        Assert.assertEquals(15.0f, distance.getMean(), 0.001);
        Assert.assertEquals(1.666f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(15.0f, 1.666f);
        Assert.assertEquals(12, distance.getMin());
        Assert.assertEquals(18, distance.getMax());
        Assert.assertEquals(15.0f, distance.getMean(), 0.001);
        Assert.assertEquals(1.666f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithTransformIntoCeleraAssemblerDistance() {
        Distance originalDistance = Distance.buildDistance(10, 20);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);
        Assert.assertEquals(originalDistance.getMin(), transformedDistance.getMin());
        Assert.assertEquals(originalDistance.getMax(), transformedDistance.getMax());
        Assert.assertEquals(originalDistance.getMean(), transformedDistance.getMean(), 0.001);
        Assert.assertEquals(1.318f, transformedDistance.getStdDev(), 0.001);
    }

    @Test
    public void testEqualsAndHashCode() {
        Distance distance1 = new Distance(10, 20, 15.0f, 1.666f);
        Distance distance2 = new Distance(10, 20, 15.0f, 1.666f);
        Distance distance3 = new Distance(10, 30, 15.0f, 1.666f);
        Assert.assertEquals(distance1, distance2);
        Assert.assertEquals(distance1.hashCode(), distance2.hashCode());
        Assert.assertNotEquals(distance1, distance3);
        Assert.assertNotEquals(distance1.hashCode(), distance3.hashCode());
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(10, 20, 15.0f, 1.666f);
        Assert.assertEquals("15.000, std: 1.666", distance.toString());
    }

}
