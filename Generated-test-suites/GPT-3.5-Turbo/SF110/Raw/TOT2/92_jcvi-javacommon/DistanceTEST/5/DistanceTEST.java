import static org.junit.Assert.*;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 5, 3.0f, 1.0f);
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0.001);
        assertEquals(1.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceMeanStdDev() {
        Distance distance = Distance.buildDistance(3.0f, 1.0f);
        assertEquals(2, distance.getMin());
        assertEquals(4, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0.001);
        assertEquals(1.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceMinMax() {
        Distance distance = Distance.buildDistance(1, 5);
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0.001);
        assertEquals(0.666f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance initialDistance = new Distance(1, 3, 2.0f, 0.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(initialDistance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(3, transformedDistance.getMax());
        assertEquals(2.0f, transformedDistance.getMean(), 0.001);
        assertEquals(0.328f, transformedDistance.getStdDev(), 0.001);
    }

    @Test
    public void testEquals() {
        Distance distance1 = new Distance(1, 5, 3.0f, 1.0f);
        Distance distance2 = new Distance(1, 5, 3.0f, 1.0f);
        assertEquals(distance1, distance2);
    }

    @Test
    public void testHashCode() {
        Distance distance = new Distance(1, 5, 3.0f, 1.0f);
        assertEquals(1851870998, distance.hashCode());
    }
}