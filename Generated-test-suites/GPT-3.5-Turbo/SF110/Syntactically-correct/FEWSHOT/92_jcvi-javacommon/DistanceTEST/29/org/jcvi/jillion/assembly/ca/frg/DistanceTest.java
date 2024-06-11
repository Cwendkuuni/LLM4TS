package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceTest {
    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(1, 10);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001f);
        assertEquals(1.5f, distance.getStdDev(), 0.001f);

        distance = Distance.buildDistance(5.5f, 1.5f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001f);
        assertEquals(1.5f, distance.getStdDev(), 0.001f);

        distance = Distance.buildDistance(5, 15, 10.0f, 2.0f);
        assertEquals(5, distance.getMin());
        assertEquals(15, distance.getMax());
        assertEquals(10.0f, distance.getMean(), 0.001f);
        assertEquals(2.0f, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 1.5f);
        distance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001f);
        assertEquals(1.498f, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testComputeMean() {
        float mean = Distance.computeMean(1, 10);
        assertEquals(5.5f, mean, 0.001f);
    }

    @Test
    public void testComputeStandardDeviation() {
        float stdDev = Distance.computeStandardDeviation(1, 10);
        assertEquals(1.5f, stdDev, 0.001f);
    }

    @Test
    public void testCalculateDelta() {
        float delta = Distance.calculateDelta(1.5f);
        assertEquals(4.5f, delta, 0.001f);
    }
}