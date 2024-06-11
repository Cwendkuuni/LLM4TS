import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinAndMax() {
        Distance distance = Distance.buildDistance(10, 20);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(15.0f, 1.666f);
        assertEquals(13, distance.getMin());
        assertEquals(17, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDevOutOfRange() {
        Distance distance = Distance.buildDistance(13.0f, 1.5f);
        assertEquals(10, distance.getMin());
        assertEquals(16, distance.getMax());
        assertEquals(13.0f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanAndStdDevLessThanDelta() {
        Distance distance = new Distance(10, 20, 5.0f, 1.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(10, transformedDistance.getMin());
        assertEquals(20, transformedDistance.getMax());
        assertEquals(5.0f, transformedDistance.getMean(), 0.001);
        assertEquals(-0.378f, transformedDistance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanAndStdDevGreaterThanDelta() {
        Distance distance = new Distance(10, 20, 15.0f, 1.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(10, transformedDistance.getMin());
        assertEquals(20, transformedDistance.getMax());
        assertEquals(15.0f, transformedDistance.getMean(), 0.001);
        assertEquals(1.0f, transformedDistance.getStdDev(), 0.001);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(10, 20, 15.0f, 1.0f);
        Distance distance2 = new Distance(10, 20, 15.0f, 1.0f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEqualsWithEqualObjects() {
        Distance distance1 = new Distance(10, 20, 15.0f, 1.0f);
        Distance distance2 = new Distance(10, 20, 15.0f, 1.0f);
        assertEquals(distance1, distance2);
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        Distance distance1 = new Distance(10, 20, 15.0f, 1.0f);
        Distance distance2 = new Distance(10, 21, 15.0f, 1.0f);
        assertNotEquals(distance1, distance2);
    }

    @Test
    public void testEqualsWithNullObject() {
        Distance distance = new Distance(10, 20, 15.0f, 1.0f);
        assertNotEquals(distance, null);
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(10, 20, 15.0f, 1.0f);
        assertEquals("15.000, std: 1.000", distance.toString());
    }

}