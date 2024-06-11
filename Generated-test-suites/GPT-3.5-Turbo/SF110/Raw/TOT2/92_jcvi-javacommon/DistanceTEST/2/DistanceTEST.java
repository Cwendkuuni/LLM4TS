import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistance_withMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.0001);
        assertEquals(2.0f, distance.getStdDev(), 0.0001);
    }

    @Test
    public void testBuildDistance_withMeanStdDev() {
        Distance distance = Distance.buildDistance(15.0f, 2.0f);
        assertEquals(13, distance.getMin());
        assertEquals(17, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.0001);
        assertEquals(2.0f, distance.getStdDev(), 0.0001);
    }

    @Test
    public void testCalculateDelta() {
        float delta = Distance.calculateDelta(2.0f);
        assertEquals(6.0f, delta, 0.0001);
    }

    @Test
    public void testBuildDistance_withMinMax() {
        Distance distance = Distance.buildDistance(10, 20);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
    }

    @Test
    public void testComputeStandardDeviation() {
        float stdDev = Distance.computeStandardDeviation(10, 20);
        assertEquals(10 / 6.0f, stdDev, 0.0001);
    }

    @Test
    public void testComputeMean() {
        float mean = Distance.computeMean(10, 20);
        assertEquals((10 / 2.0f) + (20 / 2.0f), mean, 0.0001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(5, 25, 15.0f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(5, transformedDistance.getMin());
        assertEquals(25, transformedDistance.getMax());
        assertEquals(15.0f, transformedDistance.getMean(), 0.0001);
        assertEquals(4.28f, transformedDistance.getStdDev(), 0.01);
    }

    @Test
    public void testEquals() {
        Distance distance1 = new Distance(10, 20, 15.0f, 2.0f);
        Distance distance2 = new Distance(10, 20, 15.0f, 2.0f);
        assertTrue(distance1.equals(distance2));
    }

    @Test
    public void testHashCode() {
        Distance distance = new Distance(10, 20, 15.0f, 2.0f);
        assertEquals(-1162613444, distance.hashCode());
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(10, 20, 15.0f, 2.0f);
        assertEquals("15.000, std: 2.000", distance.toString());
    }
}