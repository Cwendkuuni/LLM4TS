package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalDefaultConstructor() {
        MultiDirectional md = new MultiDirectional();
        assertEquals(2.0, md.khi, 0.001);
        assertEquals(0.5, md.gamma, 0.001);
    }

    @Test
    public void testMultiDirectionalCustomConstructor() {
        MultiDirectional md = new MultiDirectional(3.0, 0.7);
        assertEquals(3.0, md.khi, 0.001);
        assertEquals(0.7, md.gamma, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiDirectionalNegativeKhi() {
        new MultiDirectional(-1.0, 0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiDirectionalNegativeGamma() {
        new MultiDirectional(2.0, -0.5);
    }

    @Test
    public void testIterateSimplex() throws Exception {
        MultiDirectional md = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        double[] p0 = {1.0, 0.0};
        RealPointValuePair p0Rp = new RealPointValuePair(p0, 1.0, false);
        RealPointValuePair[] simplex = {p0Rp};

        md.iterateSimplex(comparator);

        double[] r0 = {0.5, 1.5};
        RealPointValuePair r0Rp = new RealPointValuePair(r0, 1.5, false);
        double[] reflected = md.reflect(simplex, 1.0, comparator);
        assertEquals(r0Rp.getPointRef(), reflected, 0.001);

        double[] e0 = {-0.5, 2.5};
        RealPointValuePair e0Rp = new RealPointValuePair(e0, 2.5, false);
        RealPointValuePair[] reflectedSimplex = new RealPointValuePair[simplex.length];
        System.arraycopy(simplex, 0, reflectedSimplex, 0, simplex.length);
        RealPointValuePair expanded = md.expand(reflectedSimplex, md.khi, comparator);
        assertEquals(e0Rp.getPointRef(), expanded.getPointRef(), 0.001);

        double[] c0 = {-1.0, 0.0};
        RealPointValuePair c0Rp = new RealPointValuePair(c0, 0.0, false);
        RealPointValuePair contracted = md.contract(simplex, md.gamma, comparator);
        assertEquals(c0Rp.getPointRef(), contracted.getPointRef(), 0.001);

        // check convergence
        assertTrue(md.isConverged());
    }

    @Test
    public void testEvaluateNewSimplex() throws Exception {
        MultiDirectional md = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        double[] p0 = {1.0, 0.0};
        RealPointValuePair p0Rp = new RealPointValuePair(p0, 1.0, false);
        RealPointValuePair[] simplex = {p0Rp};

        RealPointValuePair result = md.evaluateNewSimplex(simplex, 1.0, comparator);

        double[] r0 = {0.5, 1.5};
        RealPointValuePair r0Rp = new RealPointValuePair(r0, 1.5, false);
        assertEquals(r0Rp, result);
    }

    @Test
    public void testReflect() {
        MultiDirectional md = new MultiDirectional();
        double[] p0 = {1.0, 0.0};
        double[] r0 = {0.5, 1.5};
        RealPointValuePair p0Rp = new RealPointValuePair(p0, 1.0, false);
        RealPointValuePair r0Rp = new RealPointValuePair(r0, 1.5, false);

        double[] reflected = md.reflect(new RealPointValuePair[] {p0Rp}, 1.0, null);
        assertEquals(r0Rp.getPointRef(), reflected, 0.001);
    }

    @Test
    public void testExpand() {
        MultiDirectional md = new MultiDirectional();
        double[] p0 = {1.0, 0.0};
        double[] e0 = {-0.5, 2.5};
        RealPointValuePair p0Rp = new RealPointValuePair(p0, 1.0, false);
        RealPointValuePair e0Rp = new RealPointValuePair(e0, 2.5, false);

        RealPointValuePair[] simplex = {p0Rp};
        RealPointValuePair expanded = md.expand(simplex, md.khi, null);
        assertEquals(e0Rp.getPointRef(), expanded.getPointRef(), 0.001);
    }

    @Test
    public void testContract() {
        MultiDirectional md = new MultiDirectional();
        double[] p0 = {1.0, 0.0};
        double[] c0 = {-1.0, 0.0};
        RealPointValuePair p0Rp = new RealPointValuePair(p0, 1.0, false);
        RealPointValuePair c0Rp = new RealPointValuePair(c0, 0.0, false);

        RealPointValuePair contracted = md.contract(new RealPointValuePair[] {p0Rp}, md.gamma, null);
        assertEquals(c0Rp.getPointRef(), contracted.getPointRef(), 0.001);
    }

    @Test
    public void testIsConverged() {
        MultiDirectional md = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        double[] p0 = {1.0, 0.0};
        RealPointValuePair p0Rp = new RealPointValuePair(p0, 1.0, false);
        RealPointValuePair[] simplex = {p0Rp};

        // not converged
        md.iterateSimplex(comparator);
        assertFalse(md.isConverged());

        // converged
        md.reflect(simplex, 1.0, comparator);
        md.expand(simplex, md.khi, comparator);
        md.contract(simplex, md.gamma, comparator);
        assertTrue(md.isConverged());
    }

}