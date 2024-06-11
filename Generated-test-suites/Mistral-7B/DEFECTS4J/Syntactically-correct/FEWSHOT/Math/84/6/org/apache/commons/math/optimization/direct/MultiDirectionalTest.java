package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.UnacceptableSolutionException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealConstraint;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.ScalarFunction;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test(expected = NullArgumentException.class)
    public void testNullConstructor() {
        new MultiDirectional(null, 0.5);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullGammaConstructor() {
        new MultiDirectional(2.0, null);
    }

    @Test
    public void testDefaultConstructor() {
        final MultiDirectional optimizer = new MultiDirectional();
        assertEquals(2.0, optimizer.getExpansionCoefficient(), 0.001);
        assertEquals(0.5, optimizer.getContractionCoefficient(), 0.001);
    }

    @Test
    public void testCustomConstructor() {
        final MultiDirectional optimizer = new MultiDirectional(3.0, 0.6);
        assertEquals(3.0, optimizer.getExpansionCoefficient(), 0.001);
        assertEquals(0.6, optimizer.getContractionCoefficient(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeExpansionCoefficient() {
        new MultiDirectional(-1.0, 0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeContractionCoefficient() {
        new MultiDirectional(2.0, -1.0);
    }

    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, MaxEvaluationsExceededException,
            NoBracketingException, OptimizationException, UnacceptableSolutionException {

        final ScalarFunction function = new ScalarFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] - 2.0, 2.0) + Math.pow(x[1] - 3.0, 2.0) + Math.cos(2.0 * Math.PI * x[0]);
            }

            @Override
            public double value(double[] x, double[] gradient, int gradientLength) {
                throw new UnsupportedOperationException();
            }
        };

        final double[] bounds = {-5.0, -5.0, 5.0, 5.0};
        final RealConstraints constraints = new RealConstraints(new RealConstraint[] {
                new RealConstraint(new SimpleBounds(0.0, 5.0))
        });

        final MultiDirectional optimizer = new MultiDirectional();
        optimizer.optimize(function, bounds, GoalType.MINIMIZE, 30, constraints, new MaxEval(100));

        final PointValuePair best = optimizer.getOptimum();
        assertNotNull(best);
        assertEquals(2.0, best.getPoint()[0], 0.01);
        assertEquals(1.5363681645751713, best.getValue(), 0.001);

    }

    @Test
    public void testIterateSimplexWithExplicitComparator() throws FunctionEvaluationException,
            MaxEvaluationsExceededException, NoBracketingException, OptimizationException,
            UnacceptableSolutionException {

        final ScalarFunction function = new ScalarFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] - 2.0, 2.0) + Math.pow(x[1] - 3.0, 2.0) + Math.cos(2.0 * Math.PI * x[0]);
            }

            @Override
            public double value(double[] x, double[] gradient, int gradientLength) {
                throw new UnsupportedOperationException();
            }
        };

        final double[] bounds = {-5.0, -5.0, 5.0, 5.0};
        final Comparator<PointValuePair> comparator = (p1, p2) -> Double.compare(p1.getValue(), p2.getValue());

        final MultiDirectional optimizer = new MultiDirectional();
        optimizer.optimize(function, bounds, GoalType.MINIMIZE, 30, null, new MaxEval(100), comparator);

        final PointValuePair best = optimizer.getOptimum();
        assertNotNull(best);
        assertEquals(2.0, best.getPoint()[0], 0.01);
        assertEquals(1.5363681645751713, best.getValue(), 0.001);

    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {

        final ScalarFunction function = new ScalarFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] - 2.0, 2.0) + Math.pow(x[1] - 3.0, 2.0);
            }

            @Override
            public double value(double[] x, double[] gradient, int gradientLength) {
                throw new UnsupportedOperationException();
            }
        };

        final double[] bounds = {-5.0, -5.0, 5.0, 5.0};
        final Comparator<RealPointValuePair> comparator = (p1, p2) -> Double.compare(p1.getValue(), p2.getValue());

        final MultiDirectional optimizer = new MultiDirectional();
        final RealPointValuePair[] original = new RealPointValuePair[2];
        original[0] = new RealPointValuePair(new double[] {0.0, 0.0}, 0.0, false);
        original[1] = new RealPointValuePair(new double[] {1.0, 1.0}, 4.0, false);
        final RealPointValuePair best = original[0];

        // reflection
        final RealPointValuePair reflected = optimizer.evaluateNewSimplex(original, 1.0, comparator);
        assertEquals(best.getPoint()[0], reflected.getPoint()[0], 0.01);
        assertEquals(best.getPoint()[1], reflected.getPoint()[1], 0.01);
        assertTrue(Math.abs(reflected.getValue() - best.getValue()) < 0.01);

        // expansion
        final RealPointValuePair[] reflectedSimplex = new RealPointValuePair[3];
        reflectedSimplex[0] = best;
        final RealPointValuePair expanded = optimizer.evaluateNewSimplex(original, 2.0, comparator);
        assertEquals(best.getPoint()[0], expanded.getPoint()[0], 0.01);
        assertEquals(best.getPoint()[1], expanded.getPoint()[1], 0.01);
        assertTrue(Math.abs(expanded.getValue() - best.getValue()) < 0.01);

        // contracted
        final RealPointValuePair contracted = optimizer.evaluateNewSimplex(original, 0.5, comparator);
        assertNull(contracted);

    }

}