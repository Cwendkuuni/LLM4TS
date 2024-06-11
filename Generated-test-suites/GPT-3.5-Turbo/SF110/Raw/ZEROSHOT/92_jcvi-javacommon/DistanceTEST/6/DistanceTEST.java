package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance_intMinIntMaxFloatMeanFloatStdDev() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance_floatMeanFloatStdDev() {
        Distance distance = Distance.buildDistance(15.0f, 2.0f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance_intMinIntMax() {
        Distance distance = Distance.buildDistance(10, 20);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        Distance distance2 = Distance.transformIntoCeleraAssemblerDistance(distance1);
        assertEquals(distance1, distance2);
    }

    @Test
    public void testHashCode() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        int expected = 31 * (31 * (31 * (31 + distance.getMax()) + Float.floatToIntBits(distance.getMean())) + distance.getMin()) + Float.floatToIntBits(distance.getStdDev());
        assertEquals(expected, distance.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        assertEquals(distance1, distance2);
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        String expected = "15.000, std: 2.000";
        assertEquals(expected, distance.toString());
    }
}