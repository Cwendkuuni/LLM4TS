import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    // Test buildDistance(int min, int max, float mean, float stdDev)
    @Test
    public void testBuildDistance_MinMaxMeanStdDev() {
        int min = -10;
        int max = 10;
        float mean = 0.0f;
        float stdDev = 2.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.001);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001);
    }

    // Test buildDistance(float mean, float stdDev)
    @Test
    public void testBuildDistance_MeanStdDev() {
        float mean = 5.0f;
        float stdDev = 1.0f;

        Distance distance = Distance.buildDistance(mean, stdDev);

        Assert.assertEquals(mean, distance.getMean(), 0.001);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001);
        Assert.assertEquals(mean - stdDev, distance.getMin(), 0.001);
        Assert.assertEquals(mean + stdDev, distance.getMax(), 0.001);
    }

    // Test buildDistance(int min, int max)
    @Test
    public void testBuildDistance_MinMax() {
        int min = 0;
        int max = 100;

        Distance distance = Distance.buildDistance(min, max);

        float expectedMean = (float) (min / 2.0 + max / 2.0);
        float expectedStdDev = (max - min) / 6.0f;

        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(expectedMean, distance.getMean(), 0.001);
        Assert.assertEquals(expectedStdDev, distance.getStdDev(), 0.001);
    }

    // Test transformIntoCeleraAssemblerDistance(Distance distance)
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(0, 10, 5.0f, 1.0f);

        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        Assert.assertEquals(distance.getMin(), transformedDistance.getMin());
        Assert.assertEquals(distance.getMax(), transformedDistance.getMax());
        Assert.assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001);
        Assert.assertEquals((distance.getMean() - Distance.MAX_ROUNDING_ERROR) / Distance.SIX, transformedDistance.getStdDev(), 0.001);

        distance = new Distance(0, 10, 15.0f, 1.0f);

        transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        Assert.assertEquals(distance.getMin(), transformedDistance.getMin());
        Assert.assertEquals(distance.getMax(), transformedDistance.getMax());
        Assert.assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001);
        Assert.assertEquals(distance.getStdDev(), transformedDistance.getStdDev(), 0.001);
    }

    // Test equals(Object obj)
    @Test
    public void testEquals() {
        Distance distance1 = new Distance(0, 10, 5.0f, 1.0f);
        Distance distance2 = new Distance(0, 10, 5.0f, 1.0f);
        Distance distance3 = new Distance(0, 5, 5.0f, 1.0f);

        Assert.assertTrue(distance1.equals(distance2));
        Assert.assertTrue(distance2.equals(distance1));
        Assert.assertFalse(distance1.equals(distance3));
        Assert.assertFalse(distance3.equals(distance1));
    }

    // Test hashCode()
    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(0, 10, 5.0f, 1.0f);
        Distance distance2 = new Distance(0, 10, 5.0f, 1.0f);

        Assert.assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    // Test toString()
    @Test
    public void testToString() {
        Distance distance = new Distance(0, 10, 5.0f, 1.0f);

        String expectedString = "5.000, std: 1.000";

        Assert.assertEquals(expectedString, distance.toString());
    }

    // Additional tests for edge cases and error scenarios can be added here

}