import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
    
    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.01);
        assertEquals(2.0f, distance.getStdDev(), 0.01);
    }
    
    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        float expectedDelta = Distance.calculateDelta(2.0f);
        int expectedPlusDelta = (int) (5.0f + expectedDelta);
        int expectedMinusDelta = (int) (5.0f - expectedDelta);
        assertEquals(expectedMinusDelta, distance.getMin());
        assertEquals(expectedPlusDelta, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.01);
        assertEquals(2.0f, distance.getStdDev(), 0.01);
    }
    
    @Test
    public void testCalculateDelta() {
        float stdDev = 2.0f;
        float delta = Distance.calculateDelta(stdDev);
        float expectedDelta = 3.0f * stdDev;
        assertEquals(expectedDelta, delta, 0.01);
    }
    
    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        float expectedMean = Distance.computeMean(1, 10);
        float expectedStdDev = Distance.computeStandardDeviation(1, 10);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(expectedMean, distance.getMean(), 0.01);
        assertEquals(expectedStdDev, distance.getStdDev(), 0.01);
    }
    
    @Test
    public void testComputeStandardDeviation() {
        int min = 1;
        int max = 10;
        float stdDev = Distance.computeStandardDeviation(min, max);
        float expectedStdDev = (max - min) / 6.0f;
        assertEquals(expectedStdDev, stdDev, 0.01);
    }
    
    @Test
    public void testComputeMean() {
        int min = 1;
        int max = 10;
        float mean = Distance.computeMean(min, max);
        float expectedMean = min / 2.0f + max / 2.0f;
        assertEquals(expectedMean, mean, 0.01);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        float mean = distance.getMean();
        float stdDev = distance.getStdDev();
        if (mean < Distance.calculateDelta(stdDev)) {
            float correctedStdDev = (mean - 1.015f) / 3.0f;
            Distance expectedDistance = new Distance(distance.getMin(), distance.getMax(), mean, correctedStdDev);
            assertEquals(expectedDistance, transformedDistance);
        } else {
            assertEquals(distance, transformedDistance);
        }
    }
    
    @Test
    public void testGetMean() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(5.0f, distance.getMean(), 0.01);
    }
    
    @Test
    public void testGetStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(2.0f, distance.getStdDev(), 0.01);
    }
    
    @Test
    public void testGetMin() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(1, distance.getMin());
    }
    
    @Test
    public void testGetMax() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(10, distance.getMax());
    }
    
    @Test
    public void testHashCode() {
        int min = 1;
        int max = 10;
        float mean = 5.0f;
        float stdDev = 2.0f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        int expectedHashCode = 31 * (31 * (31 * min + Float.floatToIntBits(mean)) + max) + Float.floatToIntBits(stdDev);
        assertEquals(expectedHashCode, distance.hashCode());
    }
    
    @Test
    public void testEquals() {
        int min = 1;
        int max = 10;
        float mean = 5.0f;
        float stdDev = 2.0f;
        Distance distance1 = Distance.buildDistance(min, max, mean, stdDev);
        Distance distance2 = Distance.buildDistance(min, max, mean, stdDev);
        assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testToString() {
        int min = 1;
        int max = 10;
        float mean = 5.0f;
        float stdDev = 2.0f;
        Distance distance = Distance.buildDistance(min, max, mean, stdDev);
        String expectedString = String.format("%.3f, std: %.3f", mean, stdDev);
        assertEquals(expectedString, distance.toString());
    }
}