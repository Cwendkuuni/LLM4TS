
import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 5;
        int max = 10;
        float mean = 7.5f;
        float stdDev = 1.25f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.001);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        float mean = 10.5f;
        float stdDev = 2.0f;

        Distance distance = Distance.buildDistance(mean, stdDev);

        int expectedMin = (int)(mean - 3.0f * stdDev);
        int expectedMax = (int)(mean + 3.0f * stdDev);

        Assert.assertEquals(expectedMin, distance.getMin());
        Assert.assertEquals(expectedMax, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.001);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        int min = 1;
        int max = 7;

        Distance distance = Distance.buildDistance(min, max);

        float expectedMean = (min / 2.0f) + (max / 2.0f);
        float expectedStdDev = (max - min) / 6.0f;

        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(expectedMean, distance.getMean(), 0.001);
        Assert.assertEquals(expectedStdDev, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        int min = 4;
        int max = 8;
        float mean = 6.0f;
        float stdDev = 1.0f;

        Distance distance = new Distance(min, max, mean, stdDev);

        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        Assert.assertSame(distance, transformedDistance); // If mean >= delta, the object should remain the same

        mean = 0.9f;
        stdDev = 0.3f;

        distance = new Distance(min, max, mean, stdDev);

        transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        float expectedStdDev = (mean - 1.015f) / 3.0f;

        Assert.assertEquals(min, transformedDistance.getMin());
        Assert.assertEquals(max, transformedDistance.getMax());
        Assert.assertEquals(mean, transformedDistance.getMean(), 0.001);
        Assert.assertEquals(expectedStdDev, transformedDistance.getStdDev(), 0.001);
    }

    @Test
    public void testHashCode() {
        int min = 5;
        int max = 10;
        float mean = 7.5f;
        float stdDev = 1.25f;

        Distance distance1 = new Distance(min, max, mean, stdDev);
        Distance distance2 = new Distance(min, max, mean, stdDev);

        Assert.assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        int min = 5;
        int max = 10;
        float mean = 7.5f;
        float stdDev = 1.25f;

        Distance distance1 = new Distance(min, max, mean, stdDev);
        Distance distance2 = new Distance(min, max, mean, stdDev);
        Distance distance3 = new Distance(min, max + 1, mean, stdDev);

        Assert.assertEquals(distance1, distance2);
        Assert.assertNotEquals(distance1, distance3);
    }

    @Test
    public void testToString() {
        int min = 5;
        int max = 10;
        float mean = 7.5f;
        float stdDev = 1.25f;

        Distance distance = new Distance(min, max, mean, stdDev);

        String expectedToString = String.format("%.3f, std: %.3f", mean, stdDev);

        Assert.assertEquals(expectedToString, distance.toString());
    }
}
