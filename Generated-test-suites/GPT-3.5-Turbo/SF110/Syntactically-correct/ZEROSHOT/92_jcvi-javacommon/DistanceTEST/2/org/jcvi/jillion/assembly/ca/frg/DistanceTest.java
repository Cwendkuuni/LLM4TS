import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithInts() {
        Distance distance = Distance.buildDistance(1, 5);
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0.001);
        assertEquals(0.666f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithFloats() {
        Distance distance = Distance.buildDistance(2.0f, 0.5f);
        assertEquals(1, distance.getMin());
        assertEquals(3, distance.getMax());
        assertEquals(2.0f, distance.getMean(), 0.001);
        assertEquals(0.15f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMinAndMax() {
        Distance distance = Distance.buildDistance(1, 10);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithLowerMean() {
        Distance originalDistance = Distance.buildDistance(2, 4, 1.5f, 0.5f);
        Distance distance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);
        assertEquals(2, distance.getMin());
        assertEquals(4, distance.getMax());
        assertEquals(1.5f, distance.getMean(), 0.001);
        assertEquals(0.495f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithHigherMean() {
        Distance originalDistance = Distance.buildDistance(2, 4, 4.5f, 0.5f);
        Distance distance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);
        assertEquals(2, distance.getMin());
        assertEquals(4, distance.getMax());
        assertEquals(4.5f, distance.getMean(), 0.001);
        assertEquals(0.5f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 5, 3.0f, 0.666f);
        Distance distance2 = Distance.buildDistance(1, 5, 3.0f, 0.666f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEqualsWithSameObject() {
        Distance distance = Distance.buildDistance(1, 5, 3.0f, 0.666f);
        assertTrue(distance.equals(distance));
    }

    @Test
    public void testEqualsWithNullObject() {
        Distance distance = Distance.buildDistance(1, 5, 3.0f, 0.666f);
        assertFalse(distance.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        Distance distance = Distance.buildDistance(1, 5, 3.0f, 0.666f);
        assertFalse(distance.equals("test"));
    }

    @Test
    public void testEqualsWithDifferentObject() {
        Distance distance1 = Distance.buildDistance(1, 5, 3.0f, 0.666f);
        Distance distance2 = Distance.buildDistance(2, 4, 3.0f, 0.666f);
        assertFalse(distance1.equals(distance2));
    }

    @Test
    public void testEqualsWithSameValues() {
        Distance distance1 = Distance.buildDistance(1, 5, 3.0f, 0.666f);
        Distance distance2 = Distance.buildDistance(1, 5, 3.0f, 0.666f);
        assertTrue(distance1.equals(distance2));
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(1, 5, 3.0f, 0.666f);
        assertEquals("3.000, std: 0.666", distance.toString());
    }
}