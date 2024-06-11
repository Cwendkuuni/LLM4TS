import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
    
    @Test
    public void testBuildDistance_withMinAndMaxAndMeanAndStdDev_shouldReturnDistanceObject() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        assertNotNull(distance);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001f);
        assertEquals(2.5f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testBuildDistance_withMeanAndStdDev_shouldReturnDistanceObject() {
        Distance distance = Distance.buildDistance(15.0f, 2.5f);
        assertNotNull(distance);
        assertEquals(12, distance.getMin());
        assertEquals(18, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001f);
        assertEquals(2.5f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testBuildDistance_withMinAndMax_shouldReturnDistanceObject() {
        Distance distance = Distance.buildDistance(10, 20);
        assertNotNull(distance);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001f);
        assertEquals(1.666f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withDistanceObject_shouldReturnTransformedDistanceObject() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertNotNull(transformedDistance);
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001f);
        assertEquals(distance.getStdDev(), transformedDistance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withDistanceObjectBelowThreshold_shouldReturnTransformedDistanceObjectWithCorrectedStdDev() {
        Distance distance = Distance.buildDistance(5, 10, 7.0f, 1.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertNotNull(transformedDistance);
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001f);
        assertEquals(-0.005f, transformedDistance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testHashCode_withEqualObjects_shouldReturnSameHashCode() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        Distance distance2 = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testHashCode_withDifferentObjects_shouldReturnDifferentHashCode() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        Distance distance2 = Distance.buildDistance(10, 30, 15.0f, 2.5f);
        assertNotEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testEquals_withEqualObjects_shouldReturnTrue() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        Distance distance2 = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_withDifferentObjects_shouldReturnFalse() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        Distance distance2 = Distance.buildDistance(10, 30, 15.0f, 2.5f);
        assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_withSameObject_shouldReturnTrue() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        assertTrue(distance1.equals(distance1));
    }
    
    @Test
    public void testEquals_withNullObject_shouldReturnFalse() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        assertFalse(distance1.equals(null));
    }
    
    @Test
    public void testEquals_withDifferentClassObject_shouldReturnFalse() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        String distance2 = "Distance";
        assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testToString_withDistanceObject_shouldReturnFormattedString() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        assertEquals("15.000, std: 2.500", distance.toString());
    }
}