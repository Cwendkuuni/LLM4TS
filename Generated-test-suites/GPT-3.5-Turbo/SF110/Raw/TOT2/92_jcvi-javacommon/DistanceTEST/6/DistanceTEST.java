import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    // Test case for buildDistance(int min, int max, float mean, float stdDev)
    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.0f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    // Test case for buildDistance(float mean, float stdDev)
    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(5.5f, 2.0f);
        assertTrue(distance.getMin() <= distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    // Test case for transformIntoCeleraAssemblerDistance(Distance distance)
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance originalDistance = Distance.buildDistance(1, 10, 5.5f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);
        assertTrue(transformedDistance.getMean() >= transformedDistance.getStdDev());
    }

    // Additional test cases for edge cases and exceptions can be added here

}