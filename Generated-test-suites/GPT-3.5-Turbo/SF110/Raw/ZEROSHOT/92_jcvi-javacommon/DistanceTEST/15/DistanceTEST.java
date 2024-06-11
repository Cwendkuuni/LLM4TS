import static org.junit.Assert.*;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertNotNull(distance);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0);
        assertEquals(2.0f, distance.getStdDev(), 0);
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        assertNotNull(distance);
        assertEquals(2.0f, distance.getStdDev(), 0);
        assertEquals(5.0f, distance.getMean(), 0);
    }

    @Test
    public void testCalculateDelta() {
        float delta = Distance.calculateDelta(2.0f);
        assertEquals(6.0f, delta, 0);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        assertNotNull(distance);
        float expectedMean = 5.5f;
        float expectedStdDev = 1.5f;
        assertEquals(expectedMean, distance.getMean(), 0);
        assertEquals(expectedStdDev, distance.getStdDev(), 0);
    }

    @Test
    public void testComputeStandardDeviation() {
        float stdDev = Distance.computeStandardDeviation(1, 10);
        float expectedStdDev = 1.5f;
        assertEquals(expectedStdDev, stdDev, 0);
    }

    @Test
    public void testComputeMean() {
        float mean = Distance.computeMean(1, 10);
        float expectedMean = 5.5f;
        assertEquals(expectedMean, mean, 0);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertNotNull(transformedDistance);
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0);
        float expectedStdDev = (distance.getMean() - Distance.MAX_ROUNDING_ERROR) / Distance.CA_GATEKEEPER_STDDEV_LIMIT;
        assertEquals(expectedStdDev, transformedDistance.getStdDev(), 0);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertTrue(distance1.equals(distance2));
        assertTrue(distance2.equals(distance1));
    }

    @Test
    public void testToString() {
        float mean = 5.0f;
        float stdDev = 2.0f;
        Distance distance = Distance.buildDistance(1, 10, mean, stdDev);
        String expectedString = String.format("%.3f, std: %.3f", mean, stdDev);
        assertEquals(expectedString, distance.toString());
    }

}