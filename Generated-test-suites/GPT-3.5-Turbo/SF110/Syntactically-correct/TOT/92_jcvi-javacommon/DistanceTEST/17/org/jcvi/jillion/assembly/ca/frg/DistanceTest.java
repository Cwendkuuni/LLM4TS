import org.junit.*;
import static org.junit.Assert.*;

public class DistanceTest {
    
    // Test case for the buildDistance(int min, int max, float mean, float stdDev) method
    
    @Test
    public void testBuildDistanceWithIntegerParameters() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.0);
        assertEquals(2.0f, distance.getStdDev(), 0.0);
    }
    
    // Test case for the buildDistance(float mean, float stdDev) method
    
    @Test
    public void testBuildDistanceWithFloatParameters() {
        Distance distance = Distance.buildDistance(15.0f, 2.0f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.0);
        assertEquals(2.0f, distance.getStdDev(), 0.0);
    }
    
    // Test case for the buildDistance(int min, int max) method
    
    @Test
    public void testBuildDistanceWithIntegerParametersOnly() {
        Distance distance = Distance.buildDistance(10, 20);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.0);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }
    
    // Test case for the transformIntoCeleraAssemblerDistance(Distance distance) method
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(10, transformedDistance.getMin());
        assertEquals(20, transformedDistance.getMax());
        assertEquals(15.0f, transformedDistance.getMean(), 0.0);
        assertEquals(4.995f, transformedDistance.getStdDev(), 0.001);
        
        Distance distance2 = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        Distance transformedDistance2 = Distance.transformIntoCeleraAssemblerDistance(distance2);
        assertEquals(5, transformedDistance2.getMin());
        assertEquals(10, transformedDistance2.getMax());
        assertEquals(7.5f, transformedDistance2.getMean(), 0.0);
        assertEquals(2.160f, transformedDistance2.getStdDev(), 0.001);
    }
    
}