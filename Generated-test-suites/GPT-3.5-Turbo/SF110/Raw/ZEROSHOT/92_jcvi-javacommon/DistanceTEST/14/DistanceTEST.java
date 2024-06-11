import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
    
    @Test
    public void testBuildDistanceWithMinAndMaxAndMeanAndStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        assertEquals(3, distance.getMin());
        assertEquals(7, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMinAndMax() {
        Distance distance = Distance.buildDistance(1, 10);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanLessThanDelta() {
        Distance distance = Distance.buildDistance(3, 7, 2.0f, 0.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        assertEquals(3, transformedDistance.getMin());
        assertEquals(7, transformedDistance.getMax());
        assertEquals(2.0f, transformedDistance.getMean(), 0.001);
        assertEquals(0.3283f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanGreaterThanDelta() {
        Distance distance = Distance.buildDistance(3, 7, 5.0f, 1.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        assertEquals(3, transformedDistance.getMin());
        assertEquals(7, transformedDistance.getMax());
        assertEquals(5.0f, transformedDistance.getMean(), 0.001);
        assertEquals(1.0f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testHashCode() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        int hashCode = distance.hashCode();
        assertEquals(126243415, hashCode);
    }
    
    @Test
    public void testEqualsWithEqualObject() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        boolean equals = distance1.equals(distance2);
        assertTrue(equals);
    }
    
    @Test
    public void testEqualsWithDifferentObject() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Object distance2 = new Object();
        boolean equals = distance1.equals(distance2);
        assertFalse(equals);
    }
    
    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        String toString = distance.toString();
        assertEquals("5.000, std: 2.000", toString);
    }
}