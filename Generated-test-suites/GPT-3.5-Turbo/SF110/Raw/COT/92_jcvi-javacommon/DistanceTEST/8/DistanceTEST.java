import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        float delta = 3.0f * 2.0f;
        int plusDelta = (int)(5.0f + delta);
        int minusDelta = (int)(5.0f - delta);
        assertEquals(Math.min(plusDelta, minusDelta), distance.getMin());
        assertEquals(Math.max(plusDelta, minusDelta), distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        float mean = (1 / 2.0f) + (10 / 2.0f);
        float stdDev = (10 - 1) / 6.0f;
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        Distance transformed = Distance.transformIntoCeleraAssemblerDistance(distance);
        float mean = distance.getMean();
        float stdDev = distance.getStdDev();
        if (mean < 3.0f * stdDev) {
            float correctedStdDev = (mean - 1.015f) / 3.0f;
            assertEquals(distance.getMin(), transformed.getMin());
            assertEquals(distance.getMax(), transformed.getMax());
            assertEquals(mean, transformed.getMean(), 0.001);
            assertEquals(correctedStdDev, transformed.getStdDev(), 0.001);
        } else {
            assertEquals(distance, transformed);
        }
    }

    @Test
    public void testHashCode() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        int hashCode = 31 * (31 * (31 * (31 * 1 + 10) + Float.floatToIntBits(5.0f)) + 1) + Float.floatToIntBits(2.0f);
        assertEquals(hashCode, distance.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertTrue(distance1.equals(distance2));
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        String expected = String.format("%.3f, std: %.3f", 5.0f, 2.0f);
        assertEquals(expected, distance.toString());
    }
}



// Add the test cases here

