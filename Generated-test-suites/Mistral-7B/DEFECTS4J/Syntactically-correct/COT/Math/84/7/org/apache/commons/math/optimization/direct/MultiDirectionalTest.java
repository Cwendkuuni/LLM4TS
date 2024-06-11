package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalConstructor() {
        MultiDirectional md = new MultiDirectional();
        assertNotNull(md);

        MultiDirectional mdWithParams = new MultiDirectional(3.0, 0.8);
        assertNotNull(mdWithParams);
    }

    @Test
    public void testIterateSimplex() throws Exception {
        MultiDirectional md = new MultiDirectional();

        // Test with default coefficients
        RealPointValuePair[] simplex = new RealPointValuePair[5];
        simplex[0] = new RealPointValuePair(new double[]{0.0, 0.0}, 0.0, false);
        simplex[1] = new RealPointValuePair(new double[]{1.0, 1.0}, 2.0, false);
        simplex[2] = new RealPointValuePair(new double[]{2.0, 0.0}, 4.0, false);
        simplex[3] = new RealPointValuePair(new double[]{0.0, 2.0}, 1.0, false);
        simplex[4] = new RealPointValuePair(new double[]{2.0, 2.0}, 3.0, false);

        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        md.iterateSimplex(comparator);

        PointValuePair best = md.getBestPoint();
        assertEquals(0.0, best.getPoint()[0], 0.001);
        assertEquals(0.0, best.getPoint()[1], 0.001);

        // Test with custom coefficients
        MultiDirectional mdCustom = new MultiDirectional(3.0, 0.8);
        simplex = new RealPointValuePair[5];
        simplex[0] = new RealPointValuePair(new double[]{0.0, 0.0}, 0.0, false);
        simplex[1] = new RealPointValuePair(new double[]{1.0, 1.0}, 2.0, false);
        simplex[2] = new RealPointValuePair(new double[]{2.0, 0.0}, 4.0, false);
        simplex[3] = new RealPointValuePair(new double[]{0.0, 2.0}, 1.0, false);
        simplex[4] = new RealPointValuePair(new double[]{2.0, 2.0}, 3.0, false);

        mdCustom.iterateSimplex(comparator);

        PointValuePair bestCustom = mdCustom.getBestPoint();
        assertTrue(bestCustom.getValue() < 2.0);

        // Test with incorrect number of vertices
        simplex = new RealPointValuePair[4];
        simplex[0] = new RealPointValuePair(new double[]{0.0, 0.0}, 0.0, false);
        simplex[1] = new RealPointValuePoint(new double[]{1.0, 1.0}, 2.0, false);
        simplex[2] = new RealPointValuePair(new double[]{2.0, 0.0}, 4.0, false);
        simplex[3] = new RealPointValuePair(new double[]{0.0, 2.0}, 1.0, false);

        try {
            md.iterateSimplex(comparator);
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // Expected
        }

    }

    @Test
    public void testEvaluateNewSimplex() throws Exception {
        MultiDirectional md = new MultiDirectional();

        RealPointValuePair[] original = new RealPointValuePair[2];
        original[0] = new RealPointValuePair(new double[]{0.0, 0.0}, 0.0, false);
        original[1] = new RealPointValuePair(new double[]{1.0, 1.0}, 2.0, false);

        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        double coeff = 1.5;
        RealPointValuePair reflected = md.evaluateNewSimplex(original, coeff, comparator);

        assertEquals(original[0], reflected);

        coeff = 2.0;
        RealPointValuePair[] reflectedSimplex = new RealPointValuePair[3];
        reflectedSimplex[0] = original[0];
        reflectedSimplex[1] = md.evaluateNewSimplex(original, coeff, comparator);
        reflectedSimplex[2] = md.evaluateNewSimplex(original, coeff, comparator);

        RealPointValuePair expanded = md.evaluateNewSimplex(original, 2.5, comparator);
        assertTrue(reflected.getValue() <= expanded.getValue());

    }

}