import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
    
    @Test
    public void testBuildDistance_withMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMeanStdDev() {
        Distance distance = Distance.buildDistance(15.0f, 2.0f);
        assertEquals(13, distance.getMin());
        assertEquals(17, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMinMax() {
        Distance distance = Distance.buildDistance(10, 20);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance baseDistance = Distance.buildDistance(30, 40, 35.0f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(baseDistance);
        assertEquals(35, transformedDistance.getMin());
        assertEquals(45, transformedDistance.getMax());
        assertEquals(35.0f, transformedDistance.getMean(), 0.001);
        assertEquals(11.995f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        assertEquals(distance1, distance2);
    }
    
    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        assertEquals("15.000, std: 2.000", distance.toString());
    }
}