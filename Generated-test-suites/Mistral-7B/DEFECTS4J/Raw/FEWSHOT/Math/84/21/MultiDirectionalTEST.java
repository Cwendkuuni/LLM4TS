package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalDefaultConstructor() {
        MultiDirectional optimizer = new MultiDirectional();
        assertEquals(2.0, optimizer.khi, 0.01);
        assertEquals(0.5, optimizer.gamma, 0.01);
    }

    @Test
    public void testMultiDirectionalCustomConstructor() {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.7);
        assertEquals(3.0, optimizer.khi, 0.01);
        assertEquals(0.7, optimizer.gamma, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiDirectionalNegativeKhi() {
        new MultiDirectional(-1.5, 0.8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiDirectionalNegativeGamma() {
        new MultiDirectional(2.1, -0.4);
    }

    @Test
    public void testIterateSimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.7);
        RealPointValuePair[] original = new RealPointValuePair[4];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 3.5, false);
        original[1] = new RealPointValuePair(new double[]{3.0, 4.0}, 5.6, false);
        original[2] = new RealPointValuePair(new double[]{5.0, 6.0}, 7.8, false);
        original[3] = new RealPointValuePair(new double[]{7.0, 8.0}, 12.0, false);

        Comparator<RealPointValuePair> comparator = (p1, p2) -> Double.compare(p1.getValue(), p2.getValue());

        optimizer.iterateSimplex(comparator);

        assertNotNull(optimizer.getBestPoint());
        assertNotNull(optimizer.getBestValue());
        assertArrayEquals(new double[]{1.0, 2.0}, optimizer.getBestPoint().getPointRef(), 0.01);
        assertEquals(3.5, optimizer.getBestValue(), 0.01);
    }

    @Test
    public void testEvaluateNewSimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.7);

        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 3.5, false);
        original[1] = new RealPointValuePair(new double[]{3.0, 4.0}, 5.6, false);
        original[2] = new RealPointValuePair(new double[]{5.0, 6.0}, 7.8, false);

        Comparator<RealPointValuePair> comparator = (p1, p2) -> Double.compare(p1.getValue(), p2.getValue());

        RealPointValuePair reflected = optimizer.evaluateNewSimplex(original, 1.0, comparator);
        assertNotNull(reflected);
        assertArrayEquals(new double[]{1.2, 2.4}, reflected.getPointRef(), 0.01);
        assertEquals(3.2 * 3.5, reflected.getValue(), 0.01);

        RealPointValuePair expanded = optimizer.evaluateNewSimplex(original, 3.0, comparator);
        assertNotNull(expanded);
        assertTrue(expanded.getValue() < reflected.getValue());

        RealPointValuePair contracted = optimizer.evaluateNewSimplex(original, 0.5, comparator);
        assertNull(contracted);
    }
}