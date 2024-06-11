
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev_ReturnsNewDistanceObject() {
        Distance distance = Distance.buildDistance(1, 5, 3.5f, 1.5f);
        assertNotNull(distance);
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.5f, distance.getMean(), 0);
        assertEquals(1.5f, distance.getStdDev(), 0);
    }

    @Test
    public void testBuildDistanceWithMeanStdDev_ReturnsNewDistanceObject() {
        Distance distance = Distance.buildDistance(3.5f, 1.5f);
        assertNotNull(distance);
        assertEquals(2, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.5f, distance.getMean(), 0);
        assertEquals(1.5f, distance.getStdDev(), 0);
    }

    @Test
    public void testBuildDistanceWithMinMax_ReturnsNewDistanceObjectWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(1, 5);
        assertNotNull(distance);
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0);
        assertEquals(0.666f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_WhenMeanLessThanDelta_ReturnsDistanceWithCorrectedStdDev() {
        Distance distance = Distance.buildDistance(1, 5, 3.5f, 1.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(1.5f, transformedDistance.getStdDev(), 0);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_WhenMeanGreaterThanDelta_ReturnsSameDistance() {
        Distance distance = Distance.buildDistance(10, 15, 12.5f, 1.5f);
        assertSame(distance, Distance.transformIntoCeleraAssemblerDistance(distance));
    }
    
}
