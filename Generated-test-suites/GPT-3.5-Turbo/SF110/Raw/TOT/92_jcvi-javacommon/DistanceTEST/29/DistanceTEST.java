package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.*;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithFourParameters() {
        Distance distance = Distance.buildDistance(2, 8, 5.0f, 1.5f);
        assertEquals(2, distance.getMin());
        assertEquals(8, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithTwoParameters() {
        Distance distance = Distance.buildDistance(6.5f, 1.8f);
        float expectedMean = (6.5f - 3 * 1.8f);
        float expectedStdDev = 1.8f;
        assertTrue(distance.getMin() <= distance.getMax());
        assertTrue(distance.getMin() <= expectedMean && expectedMean <= distance.getMax());
        assertEquals(expectedMean, distance.getMean(), 0.001);
        assertEquals(expectedStdDev, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistanceWithTwoParametersMaxGreaterThanMin() {
        Distance distance = Distance.buildDistance(10, 5);
        float expectedMean = (10 / 2.0f) + (5 / 2.0f);
        float expectedStdDev = (5 - 10) / 6.0f;
        assertEquals(5, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(expectedMean, distance.getMean(), 0.001);
        assertEquals(expectedStdDev, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanLessThanDelta() {
        Distance distance = new Distance(2, 8, 5.0f, 1.5f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        float expectedStdDev = (5.0f - 1.015f) / 3.0f;
        assertEquals(distance.getMin(), celeraDistance.getMin());
        assertEquals(distance.getMax(), celeraDistance.getMax());
        assertEquals(distance.getMean(), celeraDistance.getMean(), 0.001);
        assertEquals(expectedStdDev, celeraDistance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanGreaterThanDelta() {
        Distance distance = new Distance(5, 10, 8.0f, 1.5f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance.getMin(), celeraDistance.getMin());
        assertEquals(distance.getMax(), celeraDistance.getMax());
        assertEquals(distance.getMean(), celeraDistance.getMean(), 0.001);
        assertEquals(distance.getStdDev(), celeraDistance.getStdDev(), 0.001);
    }

}