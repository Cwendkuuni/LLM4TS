import static org.junit.Assert.*;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 1.5f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.01);
        assertEquals(1.5f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        assertEquals(3, distance.getMin());
        assertEquals(7, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.01);
        assertEquals(2.0f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(1, 10, 5.5f, 1.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(1, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(5.5f, transformedDistance.getMean(), 0.01);
        assertEquals(1.495f, transformedDistance.getStdDev(), 0.01);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(1, 10, 5.5f, 1.5f);
        Distance distance2 = new Distance(1, 10, 5.5f, 1.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = new Distance(1, 10, 5.5f, 1.5f);
        Distance distance2 = new Distance(1, 10, 5.5f, 1.5f);
        assertTrue(distance1.equals(distance2));
    }

    @Test
    public void testComputeMean() {
        float mean = Distance.computeMean(1, 10);
        assertEquals(5.5f, mean, 0.01);
    }

    @Test
    public void testComputeStandardDeviation() {
        float stdDev = Distance.computeStandardDeviation(1, 10);
        assertEquals(1.5f, stdDev, 0.01);
    }

    // Additional test cases for edge cases and exceptions can be added here
    
}