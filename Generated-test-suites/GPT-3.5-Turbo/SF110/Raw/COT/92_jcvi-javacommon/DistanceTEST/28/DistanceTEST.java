
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(2, 10, 6.0f, 3.0f);
        assertEquals(2, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(6.0f, distance.getMean(), 0.001);
        assertEquals(3.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(6.0f, 3.0f);
        assertEquals(1, distance.getMin());
        assertEquals(11, distance.getMax());
        assertEquals(6.0f, distance.getMean(), 0.001);
        assertEquals(3.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMinAndMax() {
        Distance distance = Distance.buildDistance(2, 10);
        assertEquals(2, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(6.0f, distance.getMean(), 0.001);
        assertEquals(1.333f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(2, 10, 6.0f, 3.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(2, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(6.0f, transformedDistance.getMean(), 0.001);
        assertEquals(1.0f, transformedDistance.getStdDev(), 0.001);
        
        distance = Distance.buildDistance(10, 20, 11.0f, 5.0f);
        transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(10, transformedDistance.getMin());
        assertEquals(20, transformedDistance.getMax());
        assertEquals(11.0f, transformedDistance.getMean(), 0.001);
        assertEquals(5.0f, transformedDistance.getStdDev(), 0.001);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(2, 10, 6.0f, 3.0f);
        Distance distance2 = Distance.buildDistance(2, 10, 6.0f, 3.0f);
        Distance distance3 = Distance.buildDistance(10, 20, 11.0f, 5.0f);
        
        assertEquals(distance1.hashCode(), distance2.hashCode());
        assertNotEquals(distance1.hashCode(), distance3.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(2, 10, 6.0f, 3.0f);
        Distance distance2 = Distance.buildDistance(2, 10, 6.0f, 3.0f);
        Distance distance3 = Distance.buildDistance(10, 20, 11.0f, 5.0f);
        
        assertTrue(distance1.equals(distance2));
        assertFalse(distance1.equals(distance3));
    }

    @Test
    public void testToString() {
        Distance distance1 = Distance.buildDistance(2, 10, 6.0f, 3.0f);
        assertEquals("6.000, std: 3.000", distance1.toString());
        
        Distance distance2 = Distance.buildDistance(10, 20, 11.0f, 5.0f);
        assertEquals("11.000, std: 5.000", distance2.toString());
    }

}
