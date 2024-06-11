import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DistanceTest {

    @Test
    public void testBuildDistance_withMinAndMax() {
        Distance distance = Distance.buildDistance(1, 10);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMeanAndStdDev() {
        Distance distance = Distance.buildDistance(5.5f, 1.5f);
        assertEquals(4, distance.getMin());
        assertEquals(7, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMeanAndStdDev_belowDelta() {
        Distance distance = Distance.buildDistance(3.5f, 0.8f);
        assertEquals(3, distance.getMin());
        assertEquals(4, distance.getMax());
        assertEquals(3.5f, distance.getMean(), 0.001);
        assertEquals(0.828f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_belowDelta() {
        Distance distance = new Distance(3, 5, 3.5f, 0.8f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(3, transformedDistance.getMin());
        assertEquals(5, transformedDistance.getMax());
        assertEquals(3.5f, transformedDistance.getMean(), 0.001);
        assertEquals(0.828f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_aboveDelta() {
        Distance distance = new Distance(3, 5, 10, 1);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(3, transformedDistance.getMin());
        assertEquals(5, transformedDistance.getMax());
        assertEquals(10, transformedDistance.getMean(), 0.001);
        assertEquals(1, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testHashCode_equalsObjects() {
        Distance distance1 = new Distance(3, 5, 10, 1);
        Distance distance2 = new Distance(3, 5, 10, 1);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testHashCode_notEqualsObjects() {
        Distance distance1 = new Distance(3, 5, 10, 1);
        Distance distance2 = new Distance(3, 6, 10, 1);
        assertNotEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testEquals_equalsObjects() {
        Distance distance1 = new Distance(3, 5, 10, 1);
        Distance distance2 = new Distance(3, 5, 10, 1);
        assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_notEqualsObjects() {
        Distance distance1 = new Distance(3, 5, 10, 1);
        Distance distance2 = new Distance(3, 6, 10, 1);
        assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testToString() {
        Distance distance = new Distance(3, 5, 10, 1);
        assertEquals("10.000, std: 1.000", distance.toString());
    }
}