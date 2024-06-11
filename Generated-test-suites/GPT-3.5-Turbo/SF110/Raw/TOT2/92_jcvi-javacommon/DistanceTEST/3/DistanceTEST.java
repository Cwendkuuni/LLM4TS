import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanAndStdDev() {
        Distance distance = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(3.0f, 1.5f);

        assertEquals(1.5f, distance.getMean(), 0.001);
        assertEquals(3.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(1, 5);

        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance originalDistance = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);

        // Check if mean < delta * stdDev, correct the stdDev
        assertEquals(1.5f, transformedDistance.getStdDev(), 0.001);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        Distance distance2 = Distance.buildDistance(1, 5, 3.0f, 1.5f);

        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        Distance distance2 = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        Distance distance3 = Distance.buildDistance(2, 6, 4.0f, 2.0f);

        assertTrue(distance1.equals(distance2));
        assertFalse(distance1.equals(distance3));
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(3.0f, 1.5f);

        assertEquals("3.000, std: 1.500", distance.toString());
    }

}