import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance_minMaxMeanStdDev() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 1.0f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.001);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance_meanStdDev() {
        float mean = 4.7f;
        float stdDev = 1.5f;

        Distance distance = Distance.buildDistance(mean, stdDev);

        float delta = calculateDelta(stdDev);
        int plusDelta = (int)(mean + delta);
        int minusDelta = (int)(mean - delta);
        float expectedMean = (plusDelta > minusDelta) ? plusDelta : minusDelta;
        float expectedStdDev = (plusDelta > minusDelta) ? (plusDelta - mean) / 3.0f : (minusDelta - mean) / 3.0f;

        Assert.assertEquals(expectedMean, distance.getMean(), 0.001);
        Assert.assertEquals(expectedStdDev, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance_minMax() {
        int min = 5;
        int max = 15;

        Distance distance = Distance.buildDistance(min, max);

        float expectedMean = computeMean(min, max);
        float expectedStdDev = computeStandardDeviation(min, max);

        Assert.assertEquals(min, distance.getMin());
        Assert.assertEquals(max, distance.getMax());
        Assert.assertEquals(expectedMean, distance.getMean(), 0.001);
        Assert.assertEquals(expectedStdDev, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_underDelta() {
        int min = 2;
        int max = 10;
        float mean = 3.0f;
        float stdDev = 0.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        Assert.assertEquals(min, transformedDistance.getMin());
        Assert.assertEquals(max, transformedDistance.getMax());
        Assert.assertEquals(mean, transformedDistance.getMean(), 0.001);
        float expectedStdDev = (mean - 1.015f) / 3.0f;
        Assert.assertEquals(expectedStdDev, transformedDistance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_overDelta() {
        int min = 5;
        int max = 15;
        float mean = 7.0f;
        float stdDev = 1.5f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);

        Assert.assertEquals(min, transformedDistance.getMin());
        Assert.assertEquals(max, transformedDistance.getMax());
        Assert.assertEquals(mean, transformedDistance.getMean(), 0.001);
        Assert.assertEquals(stdDev, transformedDistance.getStdDev(), 0.001);
    }

    @Test
    public void testHashCode() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 1.0f;

        Distance distance1 = Distance.buildDistance(min, max, mean, stdDev);
        Distance distance2 = Distance.buildDistance(min, max, mean, stdDev);

        Assert.assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals_sameObject() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 1.0f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        Assert.assertEquals(distance, distance);
    }

    @Test
    public void testEquals_nullObject() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 1.0f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        Assert.assertNotEquals(distance, null);
    }

    @Test
    public void testEquals_differentClassObject() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 1.0f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        Assert.assertNotEquals(distance, "distance");
    }

    @Test
    public void testEquals_equalObjects() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 1.0f;

        Distance distance1 = Distance.buildDistance(min, max, mean, stdDev);
        Distance distance2 = Distance.buildDistance(min, max, mean, stdDev);

        Assert.assertEquals(distance1, distance2);
    }

    @Test
    public void testEquals_notEqualObjects() {
        int min1 = 1;
        int max1 = 10;
        float mean1 = 5.5f;
        float stdDev1 = 1.0f;

        int min2 = 2;
        int max2 = 15;
        float mean2 = 6.0f;
        float stdDev2 = 1.5f;

        Distance distance1 = Distance.buildDistance(min1, max1, mean1, stdDev1);
        Distance distance2 = Distance.buildDistance(min2, max2, mean2, stdDev2);

        Assert.assertNotEquals(distance1, distance2);
    }

    @Test
    public void testToString() {
        int min = 1;
        int max = 10;
        float mean = 5.5f;
        float stdDev = 1.0f;

        Distance distance = Distance.buildDistance(min, max, mean, stdDev);

        String expectedString = String.format("%.3f, std: %.3f", mean, stdDev);

        Assert.assertEquals(expectedString, distance.toString());
    }

    private float calculateDelta(final float stdDev) {
        return 3.0f * stdDev;
    }

    private float computeMean(final int min, final int max) {
        return min / 2.0f + max / 2.0f;
    }

    private float computeStandardDeviation(final int min, final int max) {
        return (max - min) / 6.0f;
    }
}