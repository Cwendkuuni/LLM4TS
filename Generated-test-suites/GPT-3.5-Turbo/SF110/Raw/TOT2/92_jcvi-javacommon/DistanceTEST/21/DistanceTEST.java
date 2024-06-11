import static org.junit.Assert.*;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance_withMinAndMax() {
        Distance result = Distance.buildDistance(3, 7);
        assertEquals(3, result.getMin());
        assertEquals(7, result.getMax());
        assertEquals(5.0, result.getMean(), 0.001);
        assertEquals(0.666, result.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance_withMeanAndStdDev() {
        Distance result = Distance.buildDistance(5.0f, 2.0f);
        assertEquals(3, result.getMin());
        assertEquals(7, result.getMax());
        assertEquals(5.0, result.getMean(), 0.001);
        assertEquals(2.0, result.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance input = new Distance(3, 7, 5.0f, 2.0f);
        Distance result = Distance.transformIntoCeleraAssemblerDistance(input);
        assertEquals(input, result);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(3, 7, 5.0f, 2.0f);
        Distance distance2 = new Distance(3, 7, 5.0f, 2.0f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = new Distance(3, 7, 5.0f, 2.0f);
        Distance distance2 = new Distance(3, 7, 5.0f, 2.0f);
        assertTrue(distance1.equals(distance2));
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(3, 7, 5.0f, 2.0f);
        assertEquals("5.000, std: 2.000", distance.toString());
    }
}