package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.test.AbstractMathTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for the Brent optimizer.
 */
public class BrentOptimizerTest extends AbstractMathTestCase {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}.
     */
    @Test
    public void testBrentOptimizerConstructor() {
        final double rel = 1e-6;
        final double abs = 1e-8;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        assertEquals(rel, optimizer.getRelativeThreshold(), 1e-8);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 1e-8);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}.
     */
    @Test
    public void testBrentOptimizerConstructorWithChecker() {
        final double rel = 1e-6;
        final double abs = 1e-8;
        final ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return false;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        assertEquals(rel, optimizer.getRelativeThreshold(), 1e-8);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 1e-8);
        assertFalse(optimizer.isConverged());
    }

    /**
     * Test method for {@link BrentOptimizer#doOptimize()}.
     */
    @Test
    public void testDoOptimize() {
        final double rel = 1e-5;
        final double abs = 1e-8;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        ObjectiveFunction function = f -> Math.pow(Math.E, -f);
        UnivariateOptimizer result = optimizer.optimize(function, -10, 10);

        PointValuePair expected = new PointValuePair(-E.EPSILON.doubleValue(), 0);
        assertEquals(expected, result);
        assertTrue(optimizer.isConverged());
    }

    /**
     * Test method for {@link BrentOptimizer#doOptimize()} with user-defined convergence checking procedure.
     */
    @Test
    public void testDoOptimizeWithChecker() {
        final double rel = 1e-5;
        final double abs = 1e-8;
        final ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return current.getValue() <= previous.getValue();
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        ObjectiveFunction function = f -> Math.pow(Math.E, -f);
        UnivariateOptimizer result = optimizer.optimize(function, -10, 10);

        PointValuePair expected = new PointValuePair(-E.EPSILON.doubleValue(), 0);
        assertEquals(expected, result);
        assertTrue(optimizer.isConverged());
    }

    /**
     * Test method for {@link BrentOptimizer#doOptimize()} with non-converged optimization.
     */
    @Test
    public void testDoOptimizeWithNonConvergedOptimization() {
        final double rel = 1e-5;
        final double abs = 1e-8;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        ObjectiveFunction function = f -> Math.pow(Math.E, -f) + 1;
        UnivariateOptimizer result = optimizer.optimize(function, -10, 10);

        assertFalse(optimizer.isConverged());
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, GoalType)}.
     */
    @Test
    public void testBrentOptimizerConstructorWithGoalType() {
        final double rel = 1e-6;
        final double abs = 1e-8;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, GoalType.MAXIMIZE);
        assertEquals(rel, optimizer.getRelativeThreshold(), 1e-8);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 1e-8);
    }
}