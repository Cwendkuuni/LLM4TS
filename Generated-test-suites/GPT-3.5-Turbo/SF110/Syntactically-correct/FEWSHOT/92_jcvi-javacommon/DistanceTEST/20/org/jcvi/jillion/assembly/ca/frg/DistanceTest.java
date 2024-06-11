import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceTest {
    @Test
    public void testBuildDistance() {
        Distance distance1 = Distance.buildDistance(1, 2, 3.0f, 0.5f);
        assertEquals(1, distance1.getMin());
        assertEquals(2, distance1.getMax());
        assertEquals(3.0f, distance1.getMean(), 0.001);
        assertEquals(0.5f, distance1.getStdDev(), 0.001);

        Distance distance2 = Distance.buildDistance(3.0f, 0.5f);
        assertEquals(4, distance2.getMin());
        assertEquals(2, distance2.getMax());
        assertEquals(3.0f, distance2.getMean(), 0.001);
        assertEquals(0.5f, distance2.getStdDev(), 0.001);

        Distance distance3 = Distance.buildDistance(1, 2);
        assertEquals(1, distance3.getMin());
        assertEquals(2, distance3.getMax());
        assertEquals(1.5f, distance3.getMean(), 0.001);
        assertEquals(0.1666f, distance3.getStdDev(), 0.001);
    }

    @Test
    public void testCalculateDelta() {
        Distance distance = Distance.buildDistance(1, 2, 3.0f, 0.5f);
        float delta = distance.calculateDelta(distance.getStdDev());
        assertEquals(1.5f, delta, 0.001);
    }

    @Test
    public void testComputeStandardDeviation() {
        Distance distance = Distance.buildDistance(1, 2, 3.0f, 0.5f);
        float stdDev = distance.computeStandardDeviation(distance.getMin(), distance.getMax());
        assertEquals(0.1666f, stdDev, 0.001);
    }

    @Test
    public void testComputeMean() {
        Distance distance = Distance.buildDistance(1, 2, 3.0f, 0.5f);
        float mean = distance.computeMean(distance.getMin(), distance.getMax());
        assertEquals(1.5f, mean, 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance1 = Distance.buildDistance(1, 2, 3.0f, 0.5f);
        Distance transformedDistance1 = Distance.transformIntoCeleraAssemblerDistance(distance1);
        assertEquals(1, transformedDistance1.getMin());
        assertEquals(2, transformedDistance1.getMax());
        assertEquals(3.0f, transformedDistance1.getMean(), 0.001);
        assertEquals(-0.505f, transformedDistance1.getStdDev(), 0.001);

        Distance distance2 = Distance.buildDistance(3.0f, 2.0f);
        Distance transformedDistance2 = Distance.transformIntoCeleraAssemblerDistance(distance2);
        assertEquals(3, transformedDistance2.getMin());
        assertEquals(2, transformedDistance2.getMax());
        assertEquals(3.0f, transformedDistance2.getMean(), 0.001);
        assertEquals(0.333f, transformedDistance2.getStdDev(), 0.001);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 2, 3.0f, 0.5f);
        Distance distance2 = Distance.buildDistance(1, 2, 3.0f, 0.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 2, 3.0f, 0.5f);
        Distance distance2 = Distance.buildDistance(1, 2, 3.0f, 0.5f);
        assertEquals(distance1, distance2);
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(1, 2, 3.0f, 0.5f);
        assertEquals("3.000, std: 0.500", distance.toString());
    }
}