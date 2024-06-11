package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoRealSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.FunctionEvaluation;
import org.apache.commons.math3.optim.nonlinear.MaxEval;
import org.apache.commons.math3.optim.nonlinear.convergence.AbstractStoppingCondition;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceCheckerFactory;
import org.apache.commons.math3.optim.nonlinear.functions.ChainsawFunction;
import org.apache.commons.math3.optim.nonlinear.functions.RossenbrookFunction;
import org.apache.commons.math3.optim.nonlinear.functions.SineFunction;
import org.junit.Test;

/**
 * Test class for {@link MultiDirectional} class.
 */
public class MultiDirectionalTest {

    /**
     * Test method for {@link MultiDirectional#MultiDirectional()} constructor.
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiDirectionalDefaultConstructor() throws Exception {
        new MultiDirectional();
    }

    /**
     * Test method for {@link MultiDirectional#MultiDirectional(double, double)} constructor.
     * @throws Exception if an error occurs
     */
    @Test
    public void testMultiDirectionalCustomConstructor() throws Exception {
        final MultiDirectional optimizer = new MultiDirectional(3.0, 0.5);
    }

    /**
     * Test method for {@link MultiDirectional#iterateSimplex(Comparator)} method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testIterateSimplex() throws Exception {
        final MultiDirectional optimizer = new MultiDirectional(1.5, 0.8);
        final Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        final SimpleBounds bounds = new SimpleBounds(new double[] {-10.0, -10.0}, new double[] {10.0, 10.0});
        optimizer.optimize(new ChainsawFunction(), bounds, comparator);

        final PointValuePair best = optimizer.getBestPoint();
        assertEquals(1.0, best.getPoint()[0], 1e-12);
        assertEquals(-3.1313771826024676E-8, best.getValue(), 1e-12);
    }

    /**
     * Test method for {@link MultiDirectional#iterateSimplex(Comparator)} method with {@link MaxEval} stopping condition.
     * @throws Exception if an error occurs
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testIterateSimplexMaxEvalException() throws Exception {
        final MultiDirectional optimizer = new MultiDirectional(1.5, 0.8);
        final Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        final SimpleBounds bounds = new SimpleBounds(new double[] {-10.0, -10.0}, new double[] {10.0, 10.0});
        final MaxEval maxEval = new MaxEval(10);
        optimizer.setStoppingCondition(maxEval);
        optimizer.optimize(new SineFunction(), bounds, comparator);
    }

    /**
     * Test method for {@link MultiDirectional#iterateSimplex(Comparator)} method with {@link ConvergenceChecker} stopping condition.
     * @throws Exception if an error occurs
     */
    @Test
    public void testIterateSimplexConvergenceChecker() throws Exception {
        final MultiDirectional optimizer = new MultiDirectional(1.5, 0.8);
        final Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        final SimpleBounds bounds = new SimpleBounds(new double[] {-10.0, -10.0}, new double[] {10.0, 10.0});
        final ConvergenceChecker stoppingCondition =
                ConvergenceCheckerFactory.createRelativetolerance(1e-6);
        optimizer.setStoppingCondition(stoppingCondition);
        optimizer.optimize(new RossenbrookFunction(), bounds, comparator);

        final PointValuePair best = optimizer.getBestPoint();
        assertEquals(2.0, best.getPoint()[0], 1e-12);
        assertEquals(2.1384948964865365E-6, best.getValue(), 1e-12);
    }

    /**
     * Test method for {@link MultiDirectional#iterateSimplex(Comparator)} method with {@link AbstractStoppingCondition} stopping condition.
     * @throws Exception if an error occurs
     */
    @Test
    public void testIterateSimplexAbstractStoppingCondition() throws Exception {
        final MultiDirectional optimizer = new MultiDirectional(1.5, 0.8);
        final Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        final SimpleBounds bounds = new SimpleBounds(new double[] {-10.0, -10.0}, new double[] {10.0, 10.0});
        final AbstractStoppingCondition stoppingCondition =
                ConvergenceCheckerFactory.createIterationLimit(100);
        optimizer.setStoppingCondition(stoppingCondition);
        optimizer.optimize(new SineFunction(), bounds, comparator);

        final PointValuePair best = optimizer.getBestPoint();
        assertEquals(0.0, best.getPoint()[0], 1e-12);
        assertEquals(0.0, best.getValue(), 1e-12);
        assertTrue(optimizer.isDone());
    }

    /**
     * Test method for {@link MultiDirectional#evaluateNewSimplex(RealPointValuePair[], double, Comparator)} method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testEvaluateNewSimplex() throws Exception {
        final double[] xSmallest = new double[] {1.0, 1.0};
        final MultiDirectional optimizer = new MultiDirectional(1.5, 0.8);
        final Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        final RealPointValuePair[] original = new RealPointValuePair[3];
        for (int i = 0; i < 3; ++i) {
            final double[] x = new double[] {i + 1.0, i + 1.0};
            original[i] = new RealPointValuePair(x, FunctionEvaluation.NAN, false);
        }

        final RealPointValuePair reflected = optimizer.evaluateNewSimplex(original, 1.0, comparator);
        assertEquals(1.0, reflected.getValue(), 1e-12);
        assertFalse(reflected.isSolution());

        final RealPointValuePair expanded = optimizer.evaluateNewSimplex(original, 2.0, comparator);
        assertEquals(1.0, expanded.getValue(), 1e-12);
        assertFalse(expanded.isSolution());

        final RealPointValuePair contracted = optimizer.evaluateNewSimplex(original, 0.5, comparator);
        assertEquals(1.5, contracted.getValue(), 1e-12);
        assertTrue(contracted.isSolution());
    }

}