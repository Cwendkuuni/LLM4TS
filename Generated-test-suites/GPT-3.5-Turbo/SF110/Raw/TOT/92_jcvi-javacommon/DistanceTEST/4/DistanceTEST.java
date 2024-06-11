
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinAndMax() {
        Distance distance = Distance.buildDistance(5, 10);
        assertEquals(5, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(7.5f, distance.getMean(), 0.001);
        assertEquals(0.833f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(7.5f, 0.833f);
        assertEquals(5, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(7.5f, distance.getMean(), 0.001);
        assertEquals(0.833f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev_RoundingError() {
        Distance distance = Distance.buildDistance(4.185f, 0.697f);
        assertEquals(3, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(4.185f, distance.getMean(), 0.001);
        assertEquals(0.697f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_MeanLessThanDelta() {
        Distance distance = Distance.buildDistance(3, 6, 2.5f, 0.417f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(3, celeraDistance.getMin());
        assertEquals(6, celeraDistance.getMax());
        assertEquals(2.5f, celeraDistance.getMean(), 0.001);
        assertEquals(0.495f, celeraDistance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_MeanGreaterThanDelta() {
        Distance distance = Distance.buildDistance(3, 6, 4.5f, 0.75f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(3, celeraDistance.getMin());
        assertEquals(6, celeraDistance.getMax());
        assertEquals(4.5f, celeraDistance.getMean(), 0.001);
        assertEquals(0.75f, celeraDistance.getStdDev(), 0.001);
    }

    @Test
    public void testComputeStandardDeviation() {
        float stdDev = Distance.computeStandardDeviation(3, 15);
        assertEquals(2f, stdDev, 0.001);
    }

    @Test
    public void testComputeMean() {
        float mean = Distance.computeMean(3, 15);
        assertEquals(9f, mean, 0.001);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(5, 10);
        Distance distance2 = Distance.buildDistance(5, 10);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals_SameObject() {
        Distance distance = Distance.buildDistance(5, 10);
        assertTrue(distance.equals(distance));
    }

    @Test
    public void testEquals_DifferentObjects() {
        Distance distance1 = Distance.buildDistance(5, 10);
        Distance distance2 = Distance.buildDistance(5, 10);
        assertTrue(distance1.equals(distance2));
    }

    @Test
    public void testEquals_NullObject() {
        Distance distance = Distance.buildDistance(5, 10);
        assertFalse(distance.equals(null));
    }

    @Test
    public void testEquals_DifferentClassObject() {
        Distance distance = Distance.buildDistance(5, 10);
        assertFalse(distance.equals("Distance"));
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(5, 10);
        assertEquals("7.500, std: 0.833", distance.toString());
    }
}

