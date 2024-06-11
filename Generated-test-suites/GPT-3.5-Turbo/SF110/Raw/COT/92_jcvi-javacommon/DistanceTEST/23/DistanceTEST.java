
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 5, 3.5f, 0.5f);
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.5f, distance.getMean(), 0.001);
        assertEquals(0.5f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(2.0f, 0.75f);
        assertEquals(1, distance.getMin());
        assertEquals(3, distance.getMax());
        assertEquals(2.0f, distance.getMean(), 0.001);
        assertEquals(0.75f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(2, 4);
        assertEquals(2, distance.getMin());
        assertEquals(4, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0.001);
        assertEquals(0.333f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(1, 5, 3.5f, 0.5f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance.getMin(), celeraDistance.getMin());
        assertEquals(distance.getMax(), celeraDistance.getMax());
        assertEquals(distance.getMean(), celeraDistance.getMean(), 0.001);
        assertEquals((distance.getMean() - 1.015f) / 3.0f, celeraDistance.getStdDev(), 0.001);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 5, 3.5f, 0.5f);
        Distance distance2 = Distance.buildDistance(1, 5, 3.5f, 0.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 5, 3.5f, 0.5f);
        Distance distance2 = Distance.buildDistance(1, 5, 3.5f, 0.5f);
        assertEquals(distance1, distance2);
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(1, 5, 3.5f, 0.5f);
        assertEquals("3.500, std: 0.500", distance.toString());
    }
}

