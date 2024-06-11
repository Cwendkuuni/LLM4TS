package org.jcvi.jillion.assembly.ca.frg;
import static org.junit.Assert.*;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(10, 20, 15.5f, 2.0f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.5f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(30.0f, 3.0f);
        assertEquals(27, distance.getMin());
        assertEquals(33, distance.getMax());
        assertEquals(30.0f, distance.getMean(), 0.001);
        assertEquals(3.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(5, 25);
        assertEquals(5, distance.getMin());
        assertEquals(25, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(3.33f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance originalDistance = Distance.buildDistance(10, 20, 15.5f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);
        assertEquals(originalDistance.getMin(), transformedDistance.getMin());
        assertEquals(originalDistance.getMax(), transformedDistance.getMax());
        assertEquals(15.5f, transformedDistance.getMean(), 0.001);
        assertEquals(4.495f, transformedDistance.getStdDev(), 0.001);
    }

}