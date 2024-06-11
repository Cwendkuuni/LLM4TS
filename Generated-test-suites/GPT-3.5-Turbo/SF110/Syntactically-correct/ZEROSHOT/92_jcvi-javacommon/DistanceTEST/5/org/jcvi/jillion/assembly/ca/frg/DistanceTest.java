import org.junit.*;
import static org.junit.Assert.*;

public class DistanceTest {
    
    @Test
    public void testBuildDistance_withMinAndMaxAndMeanAndStdDev_shouldReturnDistanceObject() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMeanAndStdDev_shouldReturnDistanceObject() {
        Distance distance = Distance.buildDistance(15.0f, 2.0f);
        float delta = Distance.calculateDelta(2.0f);
        int plusDelta = (int)(15.0f + delta);
        int minusDelta = (int)(15.0f - delta);
        assertEquals(Math.min(plusDelta, minusDelta), distance.getMin());
        assertEquals(Math.max(plusDelta, minusDelta), distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMinAndMax_shouldReturnDistanceObjectWithCalculatedMeanAndStdDev() {
        Distance distance = Distance.buildDistance(10, 20);
        float mean = Distance.computeMean(10, 20);
        float stdDev = Distance.computeStandardDeviation(10, 20);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withDistanceBelowDelta_shouldReturnDistanceObjectWithCorrectedStdDev() {
        Distance distance = Distance.buildDistance(10, 20, 5.0f, 1.0f);
        float mean = distance.getMean();
        float stdDev = distance.getStdDev();
        if (mean < Distance.calculateDelta(stdDev)) {
            float correctedStdDev = (mean - 1.015f) / 3.0f;
            Distance expectedDistance = new Distance(distance.getMin(), distance.getMax(), mean, correctedStdDev);
            assertEquals(expectedDistance, Distance.transformIntoCeleraAssemblerDistance(distance));
        }
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withDistanceAboveDelta_shouldReturnSameDistanceObject() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 3.0f);
        assertEquals(distance, Distance.transformIntoCeleraAssemblerDistance(distance));
    }
    
    @Test
    public void testEquals_withSameDistanceObject_shouldReturnTrue() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        Distance distance2 = distance1;
        assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_withDifferentDistanceObjectsWithSameProperties_shouldReturnTrue() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_withDifferentDistanceObjectsWithDifferentProperties_shouldReturnFalse() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(15, 30, 20.0f, 3.0f);
        assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testToString_withNonEmptyDistanceObject_shouldReturnFormattedString() {
        Distance distance = Distance.buildDistance(10, 20, 15.123f, 2.345f);
        assertEquals("15.123, std: 2.345", distance.toString());
    }
    
    @Test
    public void testHashCode_withSameDistanceObjects_shouldReturnSameHashCode() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testHashCode_withDifferentDistanceObjects_shouldReturnDifferentHashCode() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(15, 30, 20.0f, 3.0f);
        assertNotEquals(distance1.hashCode(), distance2.hashCode());
    }
}