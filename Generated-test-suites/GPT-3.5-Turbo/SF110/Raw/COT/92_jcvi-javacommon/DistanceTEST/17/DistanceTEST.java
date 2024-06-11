
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistance_withMinAndMax() {
        Distance distance = Distance.buildDistance(0, 10);
        assertEquals(0, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance_withMeanAndStdDev() {
        Distance distance = Distance.buildDistance(5.0f, 1.666f);
        assertEquals(2, distance.getMin());
        assertEquals(8, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance_withMeanAndStdDev_lowMean() {
        Distance distance = Distance.buildDistance(2.0f, 1.666f);
        assertEquals(2, distance.getMin());
        assertEquals(2, distance.getMax());
        assertEquals(2.0f, distance.getMean(), 0.001);
        assertEquals(0.333f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_lowMean() {
        Distance distance = new Distance(2, 2, 2.0f, 0.333f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(2, transformedDistance.getMin());
        assertEquals(2, transformedDistance.getMax());
        assertEquals(2.0f, transformedDistance.getMean(), 0.001);
        assertEquals(-0.005f, transformedDistance.getStdDev(), 0.001);
    }

}
