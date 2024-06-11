import static org.junit.Assert.*;
import org.junit.Test;

public class DistanceTest {
    
    @Test
    public void testBuildDistanceWithMinAndMax() {
        Distance distance = Distance.buildDistance(10, 20);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(15.0f, 1.666f);
        assertEquals(11, distance.getMin());
        assertEquals(19, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMeanAndStdDev_RoundingError() {
        Distance distance = Distance.buildDistance(15.0f, 1.645f);
        assertEquals(11, distance.getMin());
        assertEquals(18, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.645f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_MeanLessThanDelta() {
        Distance distance = Distance.buildDistance(10, 20);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(10, transformedDistance.getMin());
        assertEquals(20, transformedDistance.getMax());
        assertEquals(15.0f, transformedDistance.getMean(), 0.001);
        assertEquals(1.792f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_MeanGreaterThanOrEqualToDelta() {
        Distance distance = Distance.buildDistance(50, 60);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(50, transformedDistance.getMin());
        assertEquals(60, transformedDistance.getMax());
        assertEquals(55.0f, transformedDistance.getMean(), 0.001);
        assertEquals(1.666f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = new Distance(10, 20, 15.0f, 1.666f);
        Distance distance2 = new Distance(10, 20, 15.0f, 1.666f);
        assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testHashCode() {
        Distance distance = new Distance(10, 20, 15.0f, 1.666f);
        assertEquals(50624320, distance.hashCode());
    }
    
    @Test
    public void testToString() {
        Distance distance = new Distance(10, 20, 15.0f, 1.666f);
        assertEquals("15.000, std: 1.666", distance.toString());
    }
}