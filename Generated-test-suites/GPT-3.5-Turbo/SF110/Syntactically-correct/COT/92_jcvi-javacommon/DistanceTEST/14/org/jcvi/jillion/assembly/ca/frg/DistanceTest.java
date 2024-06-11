import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistance_minMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 1.2f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001f);
        assertEquals(1.2f, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistance_meanStdDev() {
        Distance distance = Distance.buildDistance(5.5f, 1.2f);
        float delta = Distance.calculateDelta(1.2f);
        int plusDelta = (int) (5.5f + delta);
        int minusDelta = (int) (5.5f - delta);
        assertEquals(Math.min(plusDelta, minusDelta), distance.getMin());
        assertEquals(Math.max(plusDelta, minusDelta), distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001f);
        assertEquals(1.2f, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistance_minMax() {
        Distance distance = Distance.buildDistance(1, 10);
        float mean = Distance.computeMean(1, 10);
        float stdDev = Distance.computeStandardDeviation(1, 10);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001f);
        assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_meanLessThanDelta() {
        Distance distance = Distance.transformIntoCeleraAssemblerDistance(new Distance(2, 8, 1.0f, 0.5f));
        float correctedStdDev = (1.0f - Distance.MAX_ROUNDING_ERROR) / 3.0f;
        assertEquals(2, distance.getMin());
        assertEquals(8, distance.getMax());
        assertEquals(1.0f, distance.getMean(), 0.001f);
        assertEquals(correctedStdDev, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_meanGreaterThanDelta() {
        Distance originalDistance = new Distance(2, 8, 6.0f, 0.5f);
        Distance distance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);
        assertEquals(originalDistance, distance);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(1, 10, 5.5f, 1.2f);
        Distance distance2 = new Distance(1, 10, 5.5f, 1.2f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals_sameObject() {
        Distance distance = new Distance(1, 10, 5.5f, 1.2f);
        assertTrue(distance.equals(distance));
    }

    @Test
    public void testEquals_differentObject() {
        Distance distance1 = new Distance(1, 10, 5.5f, 1.2f);
        Distance distance2 = new Distance(1, 10, 5.5f, 1.2f);
        assertTrue(distance1.equals(distance2));
    }

    @Test
    public void testEquals_differentMin() {
        Distance distance1 = new Distance(1, 10, 5.5f, 1.2f);
        Distance distance2 = new Distance(2, 10, 5.5f, 1.2f);
        assertFalse(distance1.equals(distance2));
    }

    @Test
    public void testEquals_differentMax() {
        Distance distance1 = new Distance(1, 10, 5.5f, 1.2f);
        Distance distance2 = new Distance(1, 11, 5.5f, 1.2f);
        assertFalse(distance1.equals(distance2));
    }

    @Test
    public void testEquals_differentMean() {
        Distance distance1 = new Distance(1, 10, 5.5f, 1.2f);
        Distance distance2 = new Distance(1, 10, 6.0f, 1.2f);
        assertFalse(distance1.equals(distance2));
    }

    @Test
    public void testEquals_differentStdDev() {
        Distance distance1 = new Distance(1, 10, 5.5f, 1.2f);
        Distance distance2 = new Distance(1, 10, 5.5f, 1.5f);
        assertFalse(distance1.equals(distance2));
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(1, 10, 5.5f, 1.2f);
        assertEquals("5.500, std: 1.200", distance.toString());
    }
}