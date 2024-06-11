import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithAllParameters() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Assert.assertEquals(1, distance.getMin());
        Assert.assertEquals(10, distance.getMax());
        Assert.assertEquals(5.0f, distance.getMean(), 0.001);
        Assert.assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        Assert.assertEquals(3, distance.getMin());
        Assert.assertEquals(7, distance.getMax());
        Assert.assertEquals(5.0f, distance.getMean(), 0.001);
        Assert.assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        Assert.assertEquals(1, distance.getMin());
        Assert.assertEquals(10, distance.getMax());
        Assert.assertEquals(5.5f, distance.getMean(), 0.001);
        Assert.assertEquals(1.5f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 4.0f);
        Distance distance2 = Distance.transformIntoCeleraAssemblerDistance(distance1);
        Assert.assertEquals(distance1, distance2);

        Distance distance3 = Distance.buildDistance(1, 2, 1.5f, 0.2f);
        Distance distance4 = Distance.transformIntoCeleraAssemblerDistance(distance3);
        Assert.assertEquals(distance3.getMin(), distance4.getMin());
        Assert.assertEquals(distance3.getMax(), distance4.getMax());
        Assert.assertEquals(distance3.getMean(), distance4.getMean(), 0.001);
        Assert.assertEquals((distance3.getMean() - 1.015f) / 3.0f, distance4.getStdDev(), 0.001);

        Distance distance5 = Distance.buildDistance(1, 2, 2.0f, 0.3f);
        Distance distance6 = Distance.transformIntoCeleraAssemblerDistance(distance5);
        Assert.assertEquals(distance5.getMin(), distance6.getMin());
        Assert.assertEquals(distance5.getMax(), distance6.getMax());
        Assert.assertEquals(distance5.getMean(), distance6.getMean(), 0.001);
        Assert.assertEquals(distance5.getStdDev(), distance6.getStdDev(), 0.001);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Assert.assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Assert.assertEquals(distance1, distance2);
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        String expected = "5.000, std: 2.000";
        Assert.assertEquals(expected, distance.toString());
    }
}