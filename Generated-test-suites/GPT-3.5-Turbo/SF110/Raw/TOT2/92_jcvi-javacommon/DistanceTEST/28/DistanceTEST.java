import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistance_withMinMaxMeanAndStdDev() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.0f);
        assertEquals(2.5f, distance.getStdDev(), 0.0f);
    }

    @Test
    public void testBuildDistance_withMeanAndStdDev() {
        Distance distance = Distance.buildDistance(12.0f, 2.0f);
        assertTrue(distance.getMin() <= distance.getMax());
        assertEquals(12.0f, distance.getMean(), 0.0f);
        assertEquals(2.0f, distance.getStdDev(), 0.0f);
    }

    @Test
    public void testBuildDistance_withMinMax() {
        Distance distance = Distance.buildDistance(5, 15);
        assertTrue(distance.getMin() <= distance.getMax());
        assertEquals(10.0f, distance.getMean(), 0.0f);
        assertEquals(1.666f, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_lessThanDelta() {
        Distance inputDistance = new Distance(5, 10, 5.0f, 1.5f);
        Distance newDistance = Distance.transformIntoCeleraAssemblerDistance(inputDistance);
        assertEquals(5.0f, newDistance.getMean(), 0.0f);
        assertEquals(1.015f, newDistance.getStdDev(), 0.0f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_greaterThanDelta() {
        Distance inputDistance = new Distance(10, 20, 15.0f, 2.5f);
        Distance newDistance = Distance.transformIntoCeleraAssemblerDistance(inputDistance);
        assertEquals(15.0f, newDistance.getMean(), 0.0f);
        assertEquals(2.5f, newDistance.getStdDev(), 0.0f);
    }

    @Test
    public void testEquals() {
        Distance distance1 = new Distance(5, 10, 5.0f, 1.5f);
        Distance distance2 = new Distance(5, 10, 5.0f, 1.5f);
        assertEquals(distance1, distance2);
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(5, 10, 5.0f, 1.5f);
        assertEquals("5.000, std: 1.500", distance.toString());
    }

}