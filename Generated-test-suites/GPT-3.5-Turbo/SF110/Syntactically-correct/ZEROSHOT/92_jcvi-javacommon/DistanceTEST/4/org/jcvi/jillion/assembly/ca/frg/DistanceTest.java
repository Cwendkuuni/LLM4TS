import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 2.5f);
        
        assertEquals(5, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(7.5f, distance.getMean(), 0.001);
        assertEquals(2.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(7.5f, 2.5f);
        
        assertEquals(5, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(7.5f, distance.getMean(), 0.001);
        assertEquals(2.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(5, 10);
        
        assertEquals(5, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(7.5f, distance.getMean(), 0.001);
        assertEquals(1.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testComputeStandardDeviation() {
        float stdDev = Distance.computeStandardDeviation(5, 10);
        assertEquals(0.833f, stdDev, 0.001);
    }
    
    @Test
    public void testComputeMean() {
        float mean = Distance.computeMean(5, 10);
        assertEquals(7.5f, mean, 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 2.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        assertEquals(5, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(7.5f, transformedDistance.getMean(), 0.001);
        assertEquals(2.495f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(5, 10, 7.5f, 2.5f);
        Distance distance2 = Distance.buildDistance(5, 10, 7.5f, 2.5f);
        
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(5, 10, 7.5f, 2.5f);
        Distance distance2 = Distance.buildDistance(5, 10, 7.5f, 2.5f);
        
        assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 2.5f);
        String expected = "7.500, std: 2.500";
        
        assertEquals(expected, distance.toString());
    }
}