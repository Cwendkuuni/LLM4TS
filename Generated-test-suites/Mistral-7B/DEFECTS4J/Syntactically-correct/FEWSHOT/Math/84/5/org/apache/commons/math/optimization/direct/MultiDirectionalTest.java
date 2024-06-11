package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimplePointValuePair;
import org.apache.commons.math3.optim.unconstrained.RealPointValuePair;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalOptimizer() {
        MultiDirectional md = new MultiDirectional();

        double[] x = new double[]{1.0, 2.0};
        double f = Math.exp(-3.0) + Math.pow(Math.E, 2.0);
        RealPointValuePair initialPoint = new SimplePointValuePair(x, f);

        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        try {
            md.optimize(x, comparator);
            fail("Expected OptimizationException or FunctionEvaluationException");
        } catch (OptimizationException | FunctionEvaluationException e) {
            // expected
        }

        double[] xBest = new double[]{1.5, 1.5};
        double fBest = Math.exp(-3.0) + Math.pow(Math.E, 1.5);
        RealPointValuePair bestPoint = new SimplePointValuePair(xbest, fBest);

        simplex = new RealPointValuePair[]{initialPoint, bestPoint};

        MultiDirectional md2 = new MultiDirectional(1.5, 0.8);
        RealPointValuePair result = null;
        try {
            result = md2.optimize(x, comparator);
        } catch (OptimizationException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(bestPoint.getPointRef(), result.getPointRef(), 1e-9);
        assertEquals(fBest, result.getValue(), 1e-9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        MultiDirectional md = new MultiDirectional(-1.0, 0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor2() {
        MultiDirectional md = new MultiDirectional(2.0, -0.5);
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        double[] x = new double[]{1.0, 2.0};
        double[] xTransformed = new double[x.length];
        double coeff = 1.5;
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        MultiDirectional md = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[]{new SimplePointValuePair(x, 0.0), new SimplePointValuePair(new double[]{1.1, 2.1}, 1.0)};

        RealPointValuePair result = md.evaluateNewSimplex(original, coeff, comparator);

        for (int i = 0; i < x.length; i++) {
            assertEquals("Transformed point " + i + " is not correct.", x[i] + coeff * (x[i] - xTransformed[i]), result.getPointRef()[i], 1e-9);
        }

        assertEquals("Function value is not correct.", 0.0, result.getValue(), 1e-9);
    }

}