package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceTest {
    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001f);
        assertEquals(2.0f, distance.getStdDev(), 0.001f);

        distance = Distance.buildDistance(5.0f, 2.0f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001f);
        assertEquals(2.0f, distance.getStdDev(), 0.001f);

        distance = Distance.buildDistance(1, 10);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001f);
        assertEquals(1.5f, distance.getStdDev(), 0.001f);

        distance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001f);
        assertEquals(1.49667f, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testComputeStandardDeviation() {
        float stdDev = Distance.computeStandardDeviation(1, 10);
        assertEquals(1.5f, stdDev, 0.001f);

        stdDev = Distance.computeStandardDeviation(10, 20);
        assertEquals(1.66667, stdDev, 0.001f);

        stdDev = Distance.computeStandardDeviation(20, 30);
        assertEquals(1.66667, stdDev, 0.001f);
    }

    @Test
    public void testComputeMean() {
        float mean = Distance.computeMean(1, 10);
        assertEquals(5.5f, mean, 0.001f);

        mean = Distance.computeMean(10, 20);
        assertEquals(15.0f, mean, 0.001f);

        mean = Distance.computeMean(20, 30);
        assertEquals(25.0f, mean, 0.001f);
    }
}