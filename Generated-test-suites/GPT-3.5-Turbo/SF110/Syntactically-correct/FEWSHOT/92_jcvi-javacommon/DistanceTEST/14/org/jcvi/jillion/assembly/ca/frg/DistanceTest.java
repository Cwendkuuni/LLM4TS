package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceTest {
    @Test
    public void testBuildDistance_minMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(2.5f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance_meanStdDev() {
        Distance distance = Distance.buildDistance(10.0f, 2.5f);
        float delta = 3.0f * 2.5f;
        int plusDelta = (int)(10.0f + delta);
        int minusDelta = (int)(10.0f - delta);
        assertEquals(Math.min(plusDelta, minusDelta), distance.getMin());
        assertEquals(Math.max(plusDelta, minusDelta), distance.getMax());
        assertEquals(10.0f, distance.getMean(), 0.001);
        assertEquals(2.5f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance_minMax() {
        Distance distance = Distance.buildDistance(10, 20);
        float mean = (10 / 2.0f) + (20 / 2.0f);
        float stdDev = (20 - 10) / 6.0f;
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_meanLessThanDelta() {
        Distance distance = new Distance(10, 20, 9.0f, 2.0f);
        float correctedStdDev = (9.0f - 1.015f) / 3.0f;
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(10, transformedDistance.getMin());
        assertEquals(20, transformedDistance.getMax());
        assertEquals(9.0f, transformedDistance.getMean(), 0.001);
        assertEquals(correctedStdDev, transformedDistance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_meanGreaterThanEqualDelta() {
        Distance distance = new Distance(10, 20, 15.0f, 2.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(10, transformedDistance.getMin());
        assertEquals(20, transformedDistance.getMax());
        assertEquals(15.0f, transformedDistance.getMean(), 0.001);
        assertEquals(2.5f, transformedDistance.getStdDev(), 0.001);
    }

    @Test
    public void testGetMean() {
        Distance distance = new Distance(10, 20, 15.0f, 2.5f);
        assertEquals(15.0f, distance.getMean(), 0.001);
    }

    @Test
    public void testGetStdDev() {
        Distance distance = new Distance(10, 20, 15.0f, 2.5f);
        assertEquals(2.5f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testGetMin() {
        Distance distance = new Distance(10, 20, 15.0f, 2.5f);
        assertEquals(10, distance.getMin());
    }

    @Test
    public void testGetMax() {
        Distance distance = new Distance(10, 20, 15.0f, 2.5f);
        assertEquals(20, distance.getMax());
    }

    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(10, 20, 15.0f, 2.5f);
        Distance distance2 = new Distance(10, 20, 15.0f, 2.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals_sameObj() {
        Distance distance = new Distance(10, 20, 15.0f, 2.5f);
        assertEquals(true, distance.equals(distance));
    }

    @Test
    public void testEquals_nullObj() {
        Distance distance = new Distance(10, 20, 15.0f, 2.5f);
        assertEquals(false, distance.equals(null));
    }

    @Test
    public void testEquals_diffClassObj() {
        Distance distance = new Distance(10, 20, 15.0f, 2.5f);
        assertEquals(false, distance.equals(new Object()));
    }

    @Test
    public void testEquals_samePropertiesObj() {
        Distance distance1 = new Distance(10, 20, 15.0f, 2.5f);
        Distance distance2 = new Distance(10, 20, 15.0f, 2.5f);
        assertEquals(true, distance1.equals(distance2));
    }

    @Test
    public void testEquals_diffMinObj() {
        Distance distance1 = new Distance(10, 20, 15.0f, 2.5f);
        Distance distance2 = new Distance(5, 20, 15.0f, 2.5f);
        assertEquals(false, distance1.equals(distance2));
    }

    @Test
    public void testEquals_diffMaxObj() {
        Distance distance1 = new Distance(10, 20, 15.0f, 2.5f);
        Distance distance2 = new Distance(10, 25, 15.0f, 2.5f);
        assertEquals(false, distance1.equals(distance2));
    }

    @Test
    public void testEquals_diffMeanObj() {
        Distance distance1 = new Distance(10, 20, 15.0f, 2.5f);
        Distance distance2 = new Distance(10, 20, 10.0f, 2.5f);
        assertEquals(false, distance1.equals(distance2));
    }

    @Test
    public void testEquals_diffStdDevObj() {
        Distance distance1 = new Distance(10, 20, 15.0f, 2.5f);
        Distance distance2 = new Distance(10, 20, 15.0f, 3.0f);
        assertEquals(false, distance1.equals(distance2));
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(10, 20, 15.0f, 2.5f);
        assertEquals("15.000, std: 2.500", distance.toString());
    }
}