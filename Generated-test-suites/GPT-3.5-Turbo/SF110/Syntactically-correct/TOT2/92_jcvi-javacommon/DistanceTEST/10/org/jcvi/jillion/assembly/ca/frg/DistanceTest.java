package org.jcvi.jillion.assembly.ca.frg;
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistance_minMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance_meanStdDev() {
        Distance distance = Distance.buildDistance(15.0f, 2.0f);
        assertEquals(13, distance.getMin());
        assertEquals(17, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance_minMax() {
        Distance distance = Distance.buildDistance(10, 20);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance inputDistance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        Distance outputDistance = Distance.transformIntoCeleraAssemblerDistance(inputDistance);
        assertEquals(10, outputDistance.getMin());
        assertEquals(20, outputDistance.getMax());
        assertEquals(15.0f, outputDistance.getMean(), 0.001);
        assertEquals(4.995f, outputDistance.getStdDev(), 0.001);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        assertTrue(distance1.equals(distance2));
    }
}