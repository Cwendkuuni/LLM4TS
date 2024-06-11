import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertNotNull(distance);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(6.0f, 1.0f);
        assertNotNull(distance);
        assertEquals(3, distance.getMin());
        assertEquals(9, distance.getMax());
        assertEquals(6.0f, distance.getMean(), 0.001);
        assertEquals(1.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(10, 20);
        assertNotNull(distance);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanLessThanStdDev() {
        Distance inputDistance = new Distance(10, 20, 5.0f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(inputDistance);
        assertNotNull(transformedDistance);
        assertEquals(10, transformedDistance.getMin());
        assertEquals(20, transformedDistance.getMax());
        assertEquals(5.0f, transformedDistance.getMean(), 0.001);
        assertEquals(1.161666f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanGreaterThanStdDev() {
        Distance inputDistance = new Distance(5, 10, 20.0f, 4.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(inputDistance);
        assertNotNull(transformedDistance);
        assertEquals(5, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(20.0f, transformedDistance.getMean(), 0.001);
        assertEquals(4.0f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(10, 20, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(10, 20, 5.0f, 2.0f);
        Distance distance3 = Distance.buildDistance(5, 15, 3.0f, 1.0f);
        
        assertTrue(distance1.equals(distance2));
        assertTrue(distance2.equals(distance1));
        assertFalse(distance1.equals(distance3));
    }
    
    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(10, 20, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(5, 10, 15.0f, 3.0f);
        
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(10, 20, 5.0f, 2.0f);
        String expectedString = "5.000, std: 2.000";
        
        assertEquals(expectedString, distance.toString());
    }

}