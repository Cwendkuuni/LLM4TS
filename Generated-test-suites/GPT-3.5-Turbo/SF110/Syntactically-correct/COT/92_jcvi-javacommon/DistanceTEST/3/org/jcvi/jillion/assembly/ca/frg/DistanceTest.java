
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithIntIntFloatFloat() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.01);
        assertEquals(2.0f, distance.getStdDev(), 0.01);
    }
    
    @Test
    public void testBuildDistanceWithFloatFloat() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        
        assertEquals(3, distance.getMin());
        assertEquals(7, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.01);
        assertEquals(2.0f, distance.getStdDev(), 0.01);
    }
    
    @Test
    public void testBuildDistanceWithIntInt() {
        Distance distance = Distance.buildDistance(1, 10);
        
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.01);
        assertEquals(1.5f, distance.getStdDev(), 0.01);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(1, 10);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        assertEquals(1, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(5.5f, transformedDistance.getMean(), 0.01);
        assertEquals(1.16f, transformedDistance.getStdDev(), 0.01);
        
        distance = Distance.buildDistance(10, 100);
        transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        assertEquals(10, transformedDistance.getMin());
        assertEquals(100, transformedDistance.getMax());
        assertEquals(55.0f, transformedDistance.getMean(), 0.01);
        assertEquals(9.67f, transformedDistance.getStdDev(), 0.01);
    }
    
    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        
        assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        
        assertEquals("5.000, std: 2.000", distance.toString());
    }
}
