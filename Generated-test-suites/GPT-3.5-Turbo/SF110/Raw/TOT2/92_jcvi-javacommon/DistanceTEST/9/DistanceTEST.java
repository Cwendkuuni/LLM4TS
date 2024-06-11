import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
    
    @Test
    public void testBuildDistanceWithMinMaxAndMeanAndStdDev() {
        Distance distance = Distance.buildDistance(3, 8, 5.5f, 1.5f);
        assertEquals(3, distance.getMin());
        assertEquals(8, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0);
        assertEquals(1.5f, distance.getStdDev(), 0);
    }
    
    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(10.5f, 2.0f);
        assertEquals(9, distance.getMin());
        assertEquals(12, distance.getMax());
        assertEquals(10.5f, distance.getMean(), 0);
        assertEquals(2.0f, distance.getStdDev(), 0);
    }
    
    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(4, 10);
        assertEquals(4, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(7, distance.getMean(), 0);
        assertEquals(1, distance.getStdDev(), 0);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 1.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(5, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(7.5f, transformedDistance.getMean(), 0);
        assertEquals(1.0f, transformedDistance.getStdDev(), 0);
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(3, 8, 5.5f, 1.5f);
        Distance distance2 = Distance.buildDistance(3, 8, 5.5f, 1.5f);
        assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testHashCode() {
        Distance distance = Distance.buildDistance(3, 8, 5.5f, 1.5f);
        assertEquals(distance.hashCode(), distance.hashCode());
    }
    
    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(3, 8, 5.5f, 1.5f);
        assertEquals("5.500, std: 1.500", distance.toString());
    }
}