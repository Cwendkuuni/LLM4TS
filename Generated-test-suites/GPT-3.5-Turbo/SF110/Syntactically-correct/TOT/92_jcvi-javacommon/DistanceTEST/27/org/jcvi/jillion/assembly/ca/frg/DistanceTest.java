import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        assertEquals(5, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(7.5f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);

        distance = Distance.buildDistance(2.5f, 0.5f);
        assertEquals(2, distance.getMin());
        assertEquals(3, distance.getMax());
        assertEquals(2.5f, distance.getMean(), 0.001);
        assertEquals(0.5f, distance.getStdDev(), 0.001);

        distance = Distance.buildDistance(5, 10);
        assertEquals(5, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(7.5f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(5, 10, 7.5f, 1.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertSame(distance, transformedDistance);

        distance = new Distance(2, 4, 3.0f, 0.9f);
        transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(2, transformedDistance.getMin());
        assertEquals(4, transformedDistance.getMax());
        assertEquals(3.0f, transformedDistance.getMean(), 0.001);
        assertEquals(0.995f, transformedDistance.getStdDev(), 0.001);

        distance = new Distance(1, 8, 4.5f, 1.0f);
        transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(8, transformedDistance.getMax());
        assertEquals(4.5f, transformedDistance.getMean(), 0.001);
        assertEquals(1.5f, transformedDistance.getStdDev(), 0.001);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(5, 10, 7.5f, 1.5f);
        Distance distance2 = new Distance(5, 10, 7.5f, 1.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());

        distance1 = new Distance(2, 4, 3.0f, 0.9f);
        distance2 = new Distance(2, 4, 3.0f, 0.9f);
        assertEquals(distance1.hashCode(), distance2.hashCode());

        distance1 = new Distance(1, 8, 4.5f, 1.0f);
        distance2 = new Distance(1, 8, 4.5f, 1.0f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = new Distance(5, 10, 7.5f, 1.5f);
        Distance distance2 = new Distance(5, 10, 7.5f, 1.5f);
        assertTrue(distance1.equals(distance2));

        distance1 = new Distance(2, 4, 3.0f, 0.9f);
        distance2 = new Distance(2, 4, 3.0f, 0.9f);
        assertTrue(distance1.equals(distance2));

        distance1 = new Distance(1, 8, 4.5f, 1.0f);
        distance2 = new Distance(1, 8, 4.5f, 1.0f);
        assertTrue(distance1.equals(distance2));
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(5, 10, 7.5f, 1.5f);
        assertEquals("7.500, std: 1.500", distance.toString());

        distance = new Distance(2, 4, 3.0f, 0.9f);
        assertEquals("3.000, std: 0.900", distance.toString());

        distance = new Distance(1, 8, 4.5f, 1.0f);
        assertEquals("4.500, std: 1.000", distance.toString());
    }

}