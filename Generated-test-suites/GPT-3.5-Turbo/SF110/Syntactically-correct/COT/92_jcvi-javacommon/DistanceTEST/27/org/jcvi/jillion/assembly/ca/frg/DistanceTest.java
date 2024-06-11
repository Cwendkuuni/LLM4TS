
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
    
    @Test
    public void testBuildDistanceWithMinAndMax() {
        Distance distance = Distance.buildDistance(10, 20);
        
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.66667f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(15.0f, 1.66667f);
        
        assertEquals(12, distance.getMin());
        assertEquals(18, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.66667f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMeanAndStdDevEdgeCase() {
        Distance distance = Distance.buildDistance(1.015f, 0.0f);
        
        assertEquals(1, distance.getMin());
        assertEquals(1, distance.getMax());
        assertEquals(1.015f, distance.getMean(), 0.001);
        assertEquals(0.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(10, 20);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001);
        assertEquals(distance.getStdDev(), transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistanceEdgeCase() {
        Distance distance = Distance.buildDistance(2.045f, 0.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(0.015f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test(expected = ArithmeticException.class)
    public void testComputeStandardDeviationWithInvalidInput() {
        Distance.computeStandardDeviation(10, 5);
    }
    
    @Test
    public void testHashCode() {
        Distance distance = Distance.buildDistance(10, 20);
        
        assertEquals(38701218, distance.hashCode());
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(10, 20);
        Distance distance2 = Distance.buildDistance(10, 20);
        Distance distance3 = Distance.buildDistance(20, 30);
        
        assertTrue(distance1.equals(distance2));
        assertFalse(distance1.equals(distance3));
    }
    
    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(10, 20);
        
        assertEquals("15.000, std: 1.667", distance.toString());
    }
    
}
