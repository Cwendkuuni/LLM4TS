
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 1.0f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001f);
        assertEquals(1.0f, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        float delta = 0.001f;
        assertEquals(3.0f, distance.getMin(), delta);
        assertEquals(7.0f, distance.getMax(), delta);
        assertEquals(5.0f, distance.getMean(), delta);
        assertEquals(2.0f, distance.getStdDev(), delta);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        float delta = 0.001f;
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), delta);
        assertEquals(1.5f, distance.getStdDev(), delta);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(1, 10, 5.0f, 1.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        float delta = 0.001f;
        assertEquals(1, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(5.0f, transformedDistance.getMean(), delta);
        assertEquals(1.0f, transformedDistance.getStdDev(), delta);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(1, 10, 5.0f, 1.0f);
        Distance distance2 = new Distance(1, 10, 5.0f, 1.0f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = new Distance(1, 10, 5.0f, 1.0f);
        Distance distance2 = new Distance(1, 10, 5.0f, 1.0f);
        assertTrue(distance1.equals(distance2));
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(1, 10, 5.0f, 1.0f);
        assertEquals("5.000, std: 1.000", distance.toString());
    }
}

