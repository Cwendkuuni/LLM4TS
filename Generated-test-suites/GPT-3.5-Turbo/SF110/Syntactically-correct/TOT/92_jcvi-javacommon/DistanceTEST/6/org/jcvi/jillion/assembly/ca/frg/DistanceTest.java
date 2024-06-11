import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance_WithMinAndMaxAndMeanAndStdDev_CreatesNewDistanceObject() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.0f);
        assertNotNull(distance);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_WithMeanAndStdDev_CreatesNewDistanceObjectWithComputedMinAndMax() {
        Distance distance = Distance.buildDistance(5.5f, 2.0f);
        assertNotNull(distance);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_WithMinAndMax_CreatesNewDistanceObjectWithComputedMeanAndStdDev() {
        Distance distance = Distance.buildDistance(1, 10);
        assertNotNull(distance);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_WithMeanAndStdDevBelowThreshold_ReturnsDistanceWithCorrectedStdDev() {
        Distance distance = new Distance(1, 10, 5.5f, 0.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertNotNull(transformedDistance);
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001);
        assertEquals(0.166f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_WithMeanAndStdDevAboveThreshold_ReturnsSameDistance() {
        Distance distance = new Distance(1, 10, 5.5f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertNotNull(transformedDistance);
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001);
        assertEquals(distance.getStdDev(), transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testHashCode_ReturnsCorrectHashCode() {
        Distance distance = new Distance(1, 10, 5.5f, 2.0f);
        int expectedHashCode = 31 * (31 * (31 * (31 * 1 + 10) + Float.floatToIntBits(5.5f)) + 1) + Float.floatToIntBits(2.0f);
        assertEquals(expectedHashCode, distance.hashCode());
    }
    
    @Test
    public void testEquals_ComparingWithSameObject_ReturnsTrue() {
        Distance distance = new Distance(1, 10, 5.5f, 2.0f);
        assertEquals(distance, distance);
    }
    
    @Test
    public void testEquals_ComparingWithNullObject_ReturnsFalse() {
        Distance distance = new Distance(1, 10, 5.5f, 2.0f);
        assertFalse(distance.equals(null));
    }
    
    @Test
    public void testEquals_ComparingWithDifferentObjectClass_ReturnsFalse() {
        Distance distance = new Distance(1, 10, 5.5f, 2.0f);
        assertFalse(distance.equals("Distance"));
    }
    
    @Test
    public void testEquals_ComparingWithDifferentObject_ReturnsFalse() {
        Distance distance = new Distance(1, 10, 5.5f, 2.0f);
        Distance otherDistance = new Distance(2, 10, 5.5f, 2.0f);
        assertFalse(distance.equals(otherDistance));
    }
    
    @Test
    public void testEquals_ComparingWithEqualObject_ReturnsTrue() {
        Distance distance = new Distance(1, 10, 5.5f, 2.0f);
        Distance equalDistance = new Distance(1, 10, 5.5f, 2.0f);
        assertTrue(distance.equals(equalDistance));
    }
    
    @Test
    public void testToString_ReturnsCorrectStringRepresentation() {
        Distance distance = new Distance(1, 10, 5.5f, 2.0f);
        String expectedString = "5.500, std: 2.000";
        assertEquals(expectedString, distance.toString());
    }
}