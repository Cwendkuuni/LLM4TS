import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
    
    @Test
    public void testBuildDistance1() {
        Distance distance = Distance.buildDistance(2, 8, 5.0f, 1.0f);
        assertEquals(2, distance.getMin());
        assertEquals(8, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.0f);
        assertEquals(1.0f, distance.getStdDev(), 0.0f);
    }
    
    @Test
    public void testBuildDistance2() {
        Distance distance = Distance.buildDistance(3.5f, 1.5f);
        assertEquals(2, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.5f, distance.getMean(), 0.0f);
        assertEquals(1.5f, distance.getStdDev(), 0.0f);
    }
    
    @Test
    public void testBuildDistance3() {
        Distance distance = Distance.buildDistance(10, 20);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.0f);
        assertEquals(1.6666666f, distance.getStdDev(), 0.0f);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance1() {
        Distance distance = new Distance(5, 10, 7.0f, 0.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.0f);
        assertEquals(0.0f, transformedDistance.getStdDev(), 0.0f);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance2() {
        Distance distance = new Distance(2, 5, 3.0f, 0.1f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.0f);
        assertEquals(distance.getStdDev(), transformedDistance.getStdDev(), 0.0f);
    }
    
    @Test
    public void testHashCodeAndEquals() {
        Distance distance1 = Distance.buildDistance(2, 8, 5.0f, 1.0f);
        Distance distance2 = Distance.buildDistance(2, 8, 5.0f, 1.0f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
        assertEquals(distance1, distance2);
    }
    
    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(2, 8, 5.0f, 1.0f);
        assertEquals("5.000, std: 1.000", distance.toString());
    }
}