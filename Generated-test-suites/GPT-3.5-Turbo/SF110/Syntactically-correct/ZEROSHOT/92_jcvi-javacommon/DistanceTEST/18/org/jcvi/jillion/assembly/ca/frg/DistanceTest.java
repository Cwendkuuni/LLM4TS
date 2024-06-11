import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
    @Test
    public void testBuildDistance_withInt_Params() {
        Distance distance = Distance.buildDistance(1, 10);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withFloat_Params() {
        Distance distance = Distance.buildDistance(5.5f, 1.5f);
        assertEquals(3, distance.getMin());
        assertEquals(8, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMinAndMax_Only() {
        Distance distance = Distance.buildDistance(1, 10);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(1, 10, 5.5f, 1.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(5.5f, transformedDistance.getMean(), 0.001);
        assertEquals((5.5f - 1.015f) / 3.0f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = new Distance(1, 10, 5.5f, 1.5f);
        Distance distance2 = new Distance(1, 10, 5.5f, 1.5f);
        assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(1, 10, 5.5f, 1.5f);
        Distance distance2 = new Distance(1, 10, 5.5f, 1.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testToString() {
        Distance distance = new Distance(1, 10, 5.5f, 1.5f);
        assertEquals("5.500, std: 1.500", distance.toString());
    }
}