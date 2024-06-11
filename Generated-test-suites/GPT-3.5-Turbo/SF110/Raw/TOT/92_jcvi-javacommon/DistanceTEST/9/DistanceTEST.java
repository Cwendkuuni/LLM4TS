import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance_withMinAndMax() {
        Distance distance = Distance.buildDistance(10, 20);
        Assert.assertEquals(10, distance.getMin());
        Assert.assertEquals(20, distance.getMax());
        Assert.assertEquals(15.0f, distance.getMean(), 0.01f);
        Assert.assertEquals(1.6667f, distance.getStdDev(), 0.0001f);
    }

    @Test
    public void testBuildDistance_withMeanAndStdDev() {
        Distance distance = Distance.buildDistance(15.0f, 1.6667f);
        Assert.assertEquals(11, distance.getMin());
        Assert.assertEquals(20, distance.getMax());
        Assert.assertEquals(15.0f, distance.getMean(), 0.01f);
        Assert.assertEquals(1.6667f, distance.getStdDev(), 0.0001f);
    }

    @Test
    public void testBuildDistance_withMinMaxMeanAndStdDev() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 1.6667f);
        Assert.assertEquals(10, distance.getMin());
        Assert.assertEquals(20, distance.getMax());
        Assert.assertEquals(15.0f, distance.getMean(), 0.01f);
        Assert.assertEquals(1.6667f, distance.getStdDev(), 0.0001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_withMeanLessThanDelta() {
        Distance distance = new Distance(10, 20, 4.0f, 1.0f);
        distance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(10, distance.getMin());
        Assert.assertEquals(20, distance.getMax());
        Assert.assertEquals(4.0f, distance.getMean(), 0.01f);
        Assert.assertEquals(1.3283f, distance.getStdDev(), 0.0001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_withMeanGreaterThanDelta() {
        Distance distance = new Distance(10, 20, 16.0f, 2.0f);
        distance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(10, distance.getMin());
        Assert.assertEquals(20, distance.getMax());
        Assert.assertEquals(16.0f, distance.getMean(), 0.01f);
        Assert.assertEquals(2.0f, distance.getStdDev(), 0.0001f);
    }

    @Test
    public void testEquals() {
        Distance distance1 = new Distance(10, 20, 15.0f, 1.6667f);
        Distance distance2 = new Distance(10, 20, 15.0f, 1.6667f);
        Assert.assertTrue(distance1.equals(distance2));
        Assert.assertTrue(distance2.equals(distance1));
    }

    @Test
    public void testNotEquals() {
        Distance distance1 = new Distance(10, 20, 15.0f, 1.6667f);
        Distance distance2 = new Distance(10, 30, 15.0f, 1.6667f);
        Distance distance3 = new Distance(10, 20, 16.0f, 1.6667f);
        Distance distance4 = new Distance(10, 20, 15.0f, 1.6666f);
        Assert.assertFalse(distance1.equals(distance2));
        Assert.assertFalse(distance1.equals(distance3));
        Assert.assertFalse(distance1.equals(distance4));
    }

    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(10, 20, 15.0f, 1.6667f);
        Distance distance2 = new Distance(10, 20, 15.0f, 1.6667f);
        Assert.assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(10, 20, 15.0f, 1.6667f);
        Assert.assertEquals("15.000, std: 1.667", distance.toString());
    }
}