
package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        Distance distance1 = Distance.buildDistance(10, 20, 15f, 2f);
        assertEquals(10, distance1.getMin());
        assertEquals(20, distance1.getMax());
        assertEquals(15f, distance1.getMean(), 0.001);
        assertEquals(2f, distance1.getStdDev(), 0.001);

        Distance distance2 = Distance.buildDistance(10f, 2f);
        assertEquals(8, distance2.getMin());
        assertEquals(12, distance2.getMax());
        assertEquals(10f, distance2.getMean(), 0.001);
        assertEquals(2f, distance2.getStdDev(), 0.001);

        Distance distance3 = Distance.buildDistance(10, 20);
        assertEquals(10, distance3.getMin());
        assertEquals(20, distance3.getMax());
        assertEquals(15f, distance3.getMean(), 0.001);
        assertEquals(1.666f, distance3.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance1 = Distance.buildDistance(10, 20, 15f, 2f);
        Distance celeraDistance1 = Distance.transformIntoCeleraAssemblerDistance(distance1);
        assertEquals(distance1.getMin(), celeraDistance1.getMin());
        assertEquals(distance1.getMax(), celeraDistance1.getMax());
        assertEquals(distance1.getMean(), celeraDistance1.getMean(), 0.001);
        assertEquals((distance1.getMean() - 1.015f) / 3.0f, celeraDistance1.getStdDev(), 0.001);

        Distance distance2 = Distance.buildDistance(10, 20, 20f, 2f);
        Distance celeraDistance2 = Distance.transformIntoCeleraAssemblerDistance(distance2);
        assertEquals(distance2.getMin(), celeraDistance2.getMin());
        assertEquals(distance2.getMax(), celeraDistance2.getMax());
        assertEquals(distance2.getMean(), celeraDistance2.getMean(), 0.001);
        assertEquals(distance2.getStdDev(), celeraDistance2.getStdDev(), 0.001);
    }

}
