import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinAndMax() {
        Distance distance = Distance.buildDistance(10, 20);
        assertNotNull(distance);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(15.0f, 1.666f);
        assertNotNull(distance);
        assertEquals(12, distance.getMin());
        assertEquals(18, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMeanAndStdDev_OutOfRange() {
        Distance distance = Distance.buildDistance(5.0f, 1.5f);
        assertNotNull(distance);
        assertEquals(5, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(1.328f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(10, 20, 15.0f, 1.666f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertNotNull(transformedDistance);
        assertEquals(10, transformedDistance.getMin());
        assertEquals(20, transformedDistance.getMax());
        assertEquals(15.0f, transformedDistance.getMean(), 0.001);
        assertEquals(1.666f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_MeanOutOfRange() {
        Distance distance = new Distance(3, 10, 2.0f, 1.1f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertNotNull(transformedDistance);
        assertEquals(3, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(2.0f, transformedDistance.getMean(), 0.001);
        assertEquals(0.628f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(10, 20, 15.0f, 1.666f);
        Distance distance2 = new Distance(10, 20, 15.0f, 1.666f);
        int hashCode1 = distance1.hashCode();
        int hashCode2 = distance2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = new Distance(10, 20, 15.0f, 1.666f);
        Distance distance2 = new Distance(10, 20, 15.0f, 1.666f);
        assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_DifferentMax() {
        Distance distance1 = new Distance(10, 20, 15.0f, 1.666f);
        Distance distance2 = new Distance(5, 20, 15.0f, 1.666f);
        assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_DifferentMean() {
        Distance distance1 = new Distance(10, 20, 15.0f, 1.666f);
        Distance distance2 = new Distance(10, 20, 18.0f, 1.666f);
        assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_DifferentMin() {
        Distance distance1 = new Distance(10, 20, 15.0f, 1.666f);
        Distance distance2 = new Distance(10, 25, 15.0f, 1.666f);
        assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_DifferentStdDev() {
        Distance distance1 = new Distance(10, 20, 15.0f, 1.666f);
        Distance distance2 = new Distance(10, 25, 15.0f, 2.666f);
        assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testToString() {
        Distance distance = new Distance(10, 20, 15.0f, 1.666f);
        String expected = "15.000, std: 1.666";
        assertEquals(expected, distance.toString());
    }
}