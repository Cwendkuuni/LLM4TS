import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistance_withMinAndMax() {
        Distance distance = Distance.buildDistance(1, 10);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.01);
        assertEquals(1.5f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistance_withMeanAndStdDev() {
        Distance distance = Distance.buildDistance(5.5f, 1.5f);
        assertEquals(3, distance.getMin());
        assertEquals(8, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.01);
        assertEquals(1.5f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistance_withMinMaxAndMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 1.5f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.01);
        assertEquals(1.5f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_withMeanAndStdDevLessThanDelta() {
        Distance inputDistance = Distance.buildDistance(1, 10, 1.0f, 0.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(inputDistance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(1.0f, transformedDistance.getMean(), 0.01);
        assertEquals(-0.005f / 3.0f, transformedDistance.getStdDev(), 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_withMeanAndStdDevGreaterThanDelta() {
        Distance inputDistance = Distance.buildDistance(1, 10, 5.5f, 1.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(inputDistance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(5.5f, transformedDistance.getMean(), 0.01);
        assertEquals(1.5f, transformedDistance.getStdDev(), 0.01);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 1.5f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.5f, 1.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals_withEqualObjects() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 1.5f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.5f, 1.5f);
        assertTrue(distance1.equals(distance2));
    }

    @Test
    public void testEquals_withDifferentObjects() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 1.5f);
        Distance distance2 = Distance.buildDistance(1, 20, 5.5f, 1.5f);
        assertFalse(distance1.equals(distance2));
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 1.5f);
        assertEquals("5.500, std: 1.500", distance.toString());
    }
}