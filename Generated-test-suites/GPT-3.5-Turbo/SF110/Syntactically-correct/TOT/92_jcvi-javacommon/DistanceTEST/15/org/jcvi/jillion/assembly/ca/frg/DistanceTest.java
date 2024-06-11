import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinAndMax() {
        Distance distance = Distance.buildDistance(0, 100);
        assertEquals(0, distance.getMin());
        assertEquals(100, distance.getMax());
        assertEquals(50.0f, distance.getMean(), 0.001);
        assertEquals(16.666f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(50.0f, 16.666f);
        assertEquals(0, distance.getMin());
        assertEquals(100, distance.getMax());
        assertEquals(50.0f, distance.getMean(), 0.001);
        assertEquals(16.666f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev_ZeroMean() {
        Distance distance = Distance.buildDistance(0.0f, 16.666f);
        assertEquals(-50, distance.getMin());
        assertEquals(50, distance.getMax());
        assertEquals(0.0f, distance.getMean(), 0.001);
        assertEquals(16.666f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_NoNeedForTransformation() {
        Distance distance = Distance.buildDistance(0, 100);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance, transformedDistance);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_NeedForTransformation() {
        Distance distance = Distance.buildDistance(0.0f, 16.666f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(-49, transformedDistance.getMin());
        assertEquals(1, transformedDistance.getMax());
        assertEquals(0.0f, transformedDistance.getMean(), 0.001);
        assertEquals(-1.005f, transformedDistance.getStdDev(), 0.001);
    }

    @Test
    public void testEquals_SameInstance() {
        Distance distance = Distance.buildDistance(0, 100);
        assertEquals(distance, distance);
    }

    @Test
    public void testEquals_DifferentObjects() {
        Distance distance1 = Distance.buildDistance(0, 100);
        Distance distance2 = Distance.buildDistance(0, 100);
        assertEquals(distance1, distance2);
    }

    @Test
    public void testEquals_DifferentMax() {
        Distance distance1 = Distance.buildDistance(0, 100);
        Distance distance2 = Distance.buildDistance(0, 200);
        assertNotEquals(distance1, distance2);
    }

    @Test
    public void testHashCode_EqualsObjects() {
        Distance distance1 = Distance.buildDistance(0, 100);
        Distance distance2 = Distance.buildDistance(0, 100);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(50.0f, 16.666f);
        String expected = "50.000, std: 16.666";
        assertEquals(expected, distance.toString());
    }
}