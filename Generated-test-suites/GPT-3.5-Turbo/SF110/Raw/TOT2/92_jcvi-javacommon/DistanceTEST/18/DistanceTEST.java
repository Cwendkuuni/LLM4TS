import static org.junit.Assert.*;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance1() {
        Distance dist = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(1, dist.getMin());
        assertEquals(10, dist.getMax());
        assertEquals(5.0f, dist.getMean(), 0.001);
        assertEquals(2.0f, dist.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance2() {
        Distance dist = Distance.buildDistance(5.0f, 1.0f);
        assertEquals(4, dist.getMin());
        assertEquals(6, dist.getMax());
        assertEquals(5.0f, dist.getMean(), 0.001);
        assertEquals(1.0f, dist.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance3() {
        Distance dist = Distance.buildDistance(1, 10);
        assertEquals(1, dist.getMin());
        assertEquals(10, dist.getMax());
        assertEquals(5.5f, dist.getMean(), 0.001);
        assertEquals(1.5f, dist.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance original = Distance.buildDistance(1, 10);
        Distance transformed = Distance.transformIntoCeleraAssemblerDistance(original);
        assertEquals(original.getMin(), transformed.getMin());
        assertEquals(original.getMax(), transformed.getMax());
        assertEquals(original.getMean(), transformed.getMean(), 0.001);
        assertEquals(original.getStdDev(), transformed.getStdDev(), 0.001);
    }

    @Test
    public void testHashCode() {
        Distance dist1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance dist2 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(dist1.hashCode(), dist2.hashCode());
    }

    @Test
    public void testEquals() {
        Distance dist1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance dist2 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertTrue(dist1.equals(dist2));
    }

    @Test
    public void testToString() {
        Distance dist = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals("5.000, std: 2.000", dist.toString());
    }
}