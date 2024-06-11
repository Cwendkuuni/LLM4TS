import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceTest {
    @Test
    public void testBuildDistance1() {
        Distance distance = Distance.buildDistance(2, 10, 6.5f, 1.5f);
        assertEquals(2, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(6.5f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance2() {
        Distance distance = Distance.buildDistance(7.0f, 2.0f);
        assertEquals(4, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(7.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance3() {
        Distance distance = Distance.buildDistance(5, 12);
        assertEquals(5, distance.getMin());
        assertEquals(12, distance.getMax());
        assertEquals(8.5f, distance.getMean(), 0.001);
        assertEquals(1.166f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance1() {
        Distance distance = Distance.buildDistance(3, 8, 6.0f, 1.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(3, transformedDistance.getMin());
        assertEquals(8, transformedDistance.getMax());
        assertEquals(6.0f, transformedDistance.getMean(), 0.001);
        assertEquals(1.0f, transformedDistance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance2() {
        Distance distance = Distance.buildDistance(10, 15, 12.0f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(10, transformedDistance.getMin());
        assertEquals(15, transformedDistance.getMax());
        assertEquals(12.0f, transformedDistance.getMean(), 0.001);
        assertEquals(2.0f, transformedDistance.getStdDev(), 0.001);
    }
}