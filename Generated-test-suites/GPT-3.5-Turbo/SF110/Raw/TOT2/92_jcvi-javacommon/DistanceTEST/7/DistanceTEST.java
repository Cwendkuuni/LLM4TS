import static org.junit.Assert.*;
import org.junit.Test;

public class DistanceTest {
    
    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        assertEquals(5, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(7.5f, distance.getMean(), 0.01);
        assertEquals(1.5f, distance.getStdDev(), 0.01);
    }
    
    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(6.5f, 1.2f);
        assertEquals(5, distance.getMin());
        assertEquals(8, distance.getMax());
        assertEquals(6.5f, distance.getMean(), 0.01);
        assertEquals(1.2f, distance.getStdDev(), 0.01);
    }
    
    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(2, 8);
        assertEquals(2, distance.getMin());
        assertEquals(8, distance.getMax());
        assertEquals(5, distance.getMean(), 0.01);
        assertEquals(1, distance.getStdDev(), 0.01);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(3, 7, 5, 1);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(3, transformedDistance.getMin());
        assertEquals(7, transformedDistance.getMax());
        assertEquals(5, transformedDistance.getMean(), 0.01);
        assertEquals(1.0125f, transformedDistance.getStdDev(), 0.01);
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = new Distance(2, 5, 3.5f, 1.0f);
        Distance distance2 = new Distance(2, 5, 3.5f, 1.0f);
        assertEquals(distance1, distance2);
    }
    
    @Test
    public void testHashCode() {
        Distance distance = new Distance(1, 6, 3.5f, 1.5f);
        int expectedHashCode = 130517;
        assertEquals(expectedHashCode, distance.hashCode());
    }
    
    @Test
    public void testToString() {
        Distance distance = new Distance(4, 8, 6, 1.2f);
        String expectedString = "6.000, std: 1.200";
        assertEquals(expectedString, distance.toString());
    }
}