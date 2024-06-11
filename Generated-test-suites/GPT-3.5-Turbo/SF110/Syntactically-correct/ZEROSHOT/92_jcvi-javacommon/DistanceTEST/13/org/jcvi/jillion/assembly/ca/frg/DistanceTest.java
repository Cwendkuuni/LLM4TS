import static org.junit.Assert.*;
import org.junit.Test;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {
    
    @Test
    public void testBuildDistanceWithMinMaxAndMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        float delta = Distance.calculateDelta(2.0f);
        int plusDelta = (int)(5.0f + delta);
        int minusDelta = (int)(5.0f - delta);
        assertEquals(Math.min(plusDelta, minusDelta), distance.getMin());
        assertEquals(Math.max(plusDelta, minusDelta), distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testCalculateDelta() {
        float stdDev = 2.0f;
        float expectedDelta = 6.0f;
        float delta = Distance.calculateDelta(stdDev);
        assertEquals(expectedDelta, delta, 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        float expectedMean = (1 / 2.0f) + (10 / 2.0f);
        float expectedStdDev = (10 - 1) / 6.0f;
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(expectedMean, distance.getMean(), 0.001);
        assertEquals(expectedStdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testComputeStandardDeviation() {
        int min = 1;
        int max = 10;
        float expectedStdDev = (max - min) / 6.0f;
        float stdDev = Distance.computeStandardDeviation(min, max);
        assertEquals(expectedStdDev, stdDev, 0.001);
    }
    
    @Test
    public void testComputeMean() {
        int min = 1;
        int max = 10;
        float expectedMean = min / 2.0f + max / 2.0f;
        float mean = Distance.computeMean(min, max);
        assertEquals(expectedMean, mean, 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        float mean = distance.getMean();
        float stdDev = distance.getStdDev();
        if (mean < Distance.calculateDelta(stdDev)) {
            float correctedStdDev = (mean - 1.015f) / 3.0f;
            distance = new Distance(distance.getMin(), distance.getMax(), mean, correctedStdDev);
        }
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001);
        if (mean < Distance.calculateDelta(stdDev)) {
            float expectedStdDev = (mean - 1.015f) / 3.0f;
            assertEquals(expectedStdDev, transformedDistance.getStdDev(), 0.001);
        } else {
            assertEquals(distance.getStdDev(), transformedDistance.getStdDev(), 0.001);
        }
    }
    
    @Test
    public void testHashCode() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        int expectedHashCode = 31 * (31 * (31 * (31 + 10) + Float.floatToIntBits(5.0f)) + 1) + Float.floatToIntBits(2.0f);
        int hashCode = distance.hashCode();
        assertEquals(expectedHashCode, hashCode);
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = new Distance(1, 10, 5.0f, 2.0f);
        Distance distance2 = new Distance(1, 10, 5.0f, 2.0f);
        assertTrue(distance1.equals(distance2));
        
        distance1 = new Distance(1, 10, 5.0f, 2.0f);
        distance2 = new Distance(2, 10, 5.0f, 2.0f);
        assertFalse(distance1.equals(distance2));
        
        distance1 = new Distance(1, 10, 5.0f, 2.0f);
        distance2 = new Distance(1, 20, 5.0f, 2.0f);
        assertFalse(distance1.equals(distance2));
        
        distance1 = new Distance(1, 10, 5.0f, 2.0f);
        distance2 = new Distance(1, 10, 4.0f, 2.0f);
        assertFalse(distance1.equals(distance2));
        
        distance1 = new Distance(1, 10, 5.0f, 2.0f);
        distance2 = new Distance(1, 10, 5.0f, 1.0f);
        assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testToString() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        String expectedString = "5.000, std: 2.000";
        String toString = distance.toString();
        assertEquals(expectedString, toString);
    }
}