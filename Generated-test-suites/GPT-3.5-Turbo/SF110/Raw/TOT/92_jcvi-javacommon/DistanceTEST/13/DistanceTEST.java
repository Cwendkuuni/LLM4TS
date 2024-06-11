import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistance_withMinAndMax_returnsDistanceObject() {
        Distance distance = Distance.buildDistance(1, 10);
        assertNotNull(distance);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
    }
    
    @Test
    public void testBuildDistance_withMeanAndStdDev_returnsDistanceObject() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        assertNotNull(distance);
        assertEquals(4.0f, distance.getMin(), 0.001);
        assertEquals(6.0f, distance.getMax(), 0.001);
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMinMaxMeanAndStdDev_returnsDistanceObject() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertNotNull(distance);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withMeanLessThanDelta_returnsCorrectedStdDev() {
        Distance distance = new Distance(1, 10, 0.5f, 0.2f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertNotNull(transformedDistance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(0.5f, transformedDistance.getMean(), 0.001);
        assertEquals(-0.171f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withMeanGreaterThanDelta_returnsOriginalDistance() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertNotNull(transformedDistance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(5.0f, transformedDistance.getMean(), 0.001);
        assertEquals(2.0f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testGetMean_returnsMeanValue() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        assertEquals(5.0f, distance.getMean(), 0.001);
    }
    
    @Test
    public void testGetStdDev_returnsStdDevValue() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testGetMin_returnsMinValue() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        assertEquals(1, distance.getMin());
    }
    
    @Test
    public void testGetMax_returnsMaxValue() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        assertEquals(10, distance.getMax());
    }
    
    @Test
    public void testEquals_returnsTrueForSameObject() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        assertTrue(distance.equals(distance));
    }
    
    @Test
    public void testEquals_returnsFalseForNullObject() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        assertFalse(distance.equals(null));
    }
    
    @Test
    public void testEquals_returnsFalseForDifferentClassObject() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        assertFalse(distance.equals("not a Distance object"));
    }
    
    @Test
    public void testEquals_returnsFalseForDifferentMaxValue() {
        Distance distance1 = new Distance(1, 10, 5.0f, 2.0f);
        Distance distance2 = new Distance(1, 20, 5.0f, 2.0f);
        assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_returnsFalseForDifferentMeanValue() {
        Distance distance1 = new Distance(1, 10, 5.0f, 2.0f);
        Distance distance2 = new Distance(1, 10, 6.0f, 2.0f);
        assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_returnsFalseForDifferentMinValue() {
        Distance distance1 = new Distance(1, 10, 5.0f, 2.0f);
        Distance distance2 = new Distance(2, 10, 5.0f, 2.0f);
        assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_returnsFalseForDifferentStdDevValue() {
        Distance distance1 = new Distance(1, 10, 5.0f, 2.0f);
        Distance distance2 = new Distance(1, 10, 5.0f, 3.0f);
        assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testHashCode_returnsHashCodeValue() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        assertEquals(-1718573087, distance.hashCode());
    }
    
    @Test
    public void testToString_returnsFormattedString() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        assertEquals("5.000, std: 2.000", distance.toString());
    }
}