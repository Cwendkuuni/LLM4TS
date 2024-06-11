import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
    
    @Test
    public void testBuildDistance_withMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(10, 20, 15.5f, 3.0f);
        
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.5f, distance.getMean(), 0.001);
        assertEquals(3.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMeanStdDev() {
        Distance distance = Distance.buildDistance(15.5f, 3.0f);
        float expectedDelta = Distance.calculateDelta(3.0f);
        float expectedPlusDelta = 15.5f + expectedDelta;
        float expectedMinusDelta = 15.5f - expectedDelta;

        assertEquals((int) Math.min(expectedPlusDelta, expectedMinusDelta), distance.getMin());
        assertEquals((int) Math.max(expectedPlusDelta, expectedMinusDelta), distance.getMax());
        assertEquals(15.5f, distance.getMean(), 0.001);
        assertEquals(3.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMinMax() {
        Distance distance = Distance.buildDistance(10, 20);
        float expectedMean = Distance.computeMean(10, 20);
        float expectedStdDev = Distance.computeStandardDeviation(10, 20);

        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(expectedMean, distance.getMean(), 0.001);
        assertEquals(expectedStdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_lessThanDelta() {
        Distance originalDistance = new Distance(10, 20, 19.5f, 2.9f);

        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);

        assertEquals(originalDistance.getMin(), transformedDistance.getMin());
        assertEquals(originalDistance.getMax(), transformedDistance.getMax());
        assertEquals(originalDistance.getMean(), transformedDistance.getMean(), 0.001);
        assertEquals((originalDistance.getMean() - 1.015f) / 3.0f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_greaterThanDelta() {
        Distance originalDistance = new Distance(10, 20, 21.5f, 4.0f);

        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);

        assertEquals(originalDistance.getMin(), transformedDistance.getMin());
        assertEquals(originalDistance.getMax(), transformedDistance.getMax());
        assertEquals(originalDistance.getMean(), transformedDistance.getMean(), 0.001);
        assertEquals(originalDistance.getStdDev(), transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testHashCode_equalObjects() {
        Distance distance1 = new Distance(10, 20, 15.5f, 3.0f);
        Distance distance2 = new Distance(10, 20, 15.5f, 3.0f);

        assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testHashCode_differentObjects() {
        Distance distance1 = new Distance(10, 20, 15.5f, 3.0f);
        Distance distance2 = new Distance(20, 30, 20.0f, 4.0f);

        assertNotEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testEquals_sameObjects() {
        Distance distance = new Distance(10, 20, 15.5f, 3.0f);

        assertTrue(distance.equals(distance));
    }
    
    @Test
    public void testEquals_equalObjects() {
        Distance distance1 = new Distance(10, 20, 15.5f, 3.0f);
        Distance distance2 = new Distance(10, 20, 15.5f, 3.0f);

        assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_differentObjects() {
        Distance distance1 = new Distance(10, 20, 15.5f, 3.0f);
        Distance distance2 = new Distance(20, 30, 20.0f, 4.0f);

        assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testToString() {
        Distance distance = new Distance(10, 20, 15.5f, 3.0f);

        assertEquals("15.500, std: 3.000", distance.toString());
    }
}