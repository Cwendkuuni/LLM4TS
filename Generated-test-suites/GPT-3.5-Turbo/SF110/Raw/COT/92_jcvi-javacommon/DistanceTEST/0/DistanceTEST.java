
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance_withMeanAndStdDev() {
        Distance distance = Distance.buildDistance(12.0f, 3.0f);
        float delta = 3.0f * 3.0f;
        int plusDelta = (int)(12.0f + delta);
        int minusDelta = (int)(12.0f - delta);
        assertEquals(Math.min(plusDelta, minusDelta), distance.getMin());
        assertEquals(Math.max(plusDelta, minusDelta), distance.getMax());
        assertEquals(12.0f, distance.getMean(), 0.001);
        assertEquals(3.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance_withMinAndMax() {
        Distance distance = Distance.buildDistance(10, 20);
        float mean = 10 / 2.0f + 20 / 2.0f;
        float stdDev = (20 - 10) / 6.0f;
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(10, 20, 15.0f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(10, transformedDistance.getMin());
        assertEquals(20, transformedDistance.getMax());
        assertEquals(15.0f, transformedDistance.getMean(), 0.001);
        assertEquals((15.0f - 1.015f) / 3.0f, transformedDistance.getStdDev(), 0.001);
    }

}
