package org.apache.commons.math3.optimization.univariate;

import junit.framework.TestCase;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBoundingMethod;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunterionTest;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;

/**
 * Test cases for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest extends TestCase {

    /**
     * Test case for the optimization of a quadratic function with a
     * known minimum.
     */
    public void testQuadraticFunctionMinimum() {
        double a = 1.0;
        double b = 2.0;
        double c = -3.0;

        UnivariateFunction function = new LinearObjectiveFunction(new double[]{-a, b, c});
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);

        UnivariateOptimizer result = optimizer.optimize(function, new AroundMinimumInitializer(0.5));
        assertEquals(result.getPoint().getX(), -0.5, 1e-6);
        assertEquals(result.getValue(), -2.25, 1e-6);
    }

    /**
     * Test case for the optimization of a quadratic function with
     * a known maximum.
     */
    public void testQuadraticFunctionMaximum() {
        double a = 1.0;
        double b = 2.0;
        double c = 3.0;

        UnivariateFunction function = new LinearObjectiveFunction(new double[]{-a, b, -c});
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);

        UnivariateOptimizer result = optimizer.optimize(function, new AroundMinimumInitializer(0.5));
        assertEquals(result.getPoint().getX(), 0.5, 1e-6);
        assertEquals(result.getValue(), 2.25, 1e-6);
    }

    /**
     * Test case for the optimization of a linear function.
     */
    public void testLinearFunction() {
        double a = 1.0;
        double b = 2.0;
        double c = 3.0;

        UnivariateFunction function = new LinearObjectiveFunction(new double[]{a, b, -(a + b)});
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);

        UnivariateOptimizer result = optimizer.optimize(function, new AroundMinimumInitializer(0.5));
        assertEquals(result.getPoint().getX(), 0.5, 1e-6);
        assertEquals(result.getValue(), -2.0, 1e-6);
    }

    /**
     * Test case for the optimization of a non-convex function.
     */
    public void testNonConvexFunction() {
        UnivariateFunction function = new LinearObjectiveFunction(new double[]{0.0, 0.0, 4.0 * Math.exp(-Math.pow(Math.abs(x), 0.25))});
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);

        UnivariateOptimizer result = optimizer.optimize(function, new AroundMinimumInitializer(0.0));
        assertEquals(result.getPoint().getX(), 0.0, 1e-6);
        assertFalse(result.isSatisfied());

        result = optimizer.optimize(function, new AroundMinimumInitializer(2.0));
        assertEquals(result.getPoint().getX(), 0.666_666_667_222_222_222_222_222_222, 1e-6);
        assertTrue(result.isSatisfied());
    }

    /**
     * Test case for the optimization of a function with bounds.
     */
    public void testFunctionWithBounds() {
        double a = 1.0;
        double b = 2.0;
        double c = 3.0;
        double d = 4.0;
        double e = 5.0;

        UnivariateFunction function = new LinearObjectiveFunction(new double[]{a, b, -(Math.pow(x, 2) + c * x + d)});

        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
        PointValuePair result = optimizer.optimize(function,
                new AroundMinimumInitializer(2.5),
                new SimpleBoundingMethod(-5.0, 5.0)
        );
        assertEquals(result.getPoint().getX(), -1.0, 1e-6);
        assertEquals(result.getValue(), Math.pow(1.0, 2) + c * (-1.0) + d, 1e-6);
    }

    /**
     * Test case for the optimization of a function with linear constraints.
     */
    public void testFunctionWithLinearConstraints() {
        double a = 1.0;
        double b = 2.0;
        double c = 3.0;
        double d = 4.0;

        UnivariateFunction function = new LinearObjectiveFunction(new double[]{-a, b, Math.pow(x, 2) + c * x + d});

        LinearConstraint constraint1 = new LinearConstraint(0.0, 1.0, 1.0, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(0.0, 1.0, 0.0, 1.0);
        LinearConstraint constraint3 = new LinearConstraint(0.0, 1.0, -1.0, 1.0);

        LinearConstraintSet constraintSet = new LinearConstraintSet();
        constraintSet.add(constraint1);
        constraintSet.add(constraint2);
        constraintSet.add(constraint3);

        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
        PointValuePair result = optimizer.optimize(function, new AroundMinimumInitializer(2.5), constraintSet);
        assertEquals(result.getPoint().getX(), -1.0, 1e-6);
        assertEquals(result.getValue(), Math.pow(1.0, 2) + c * (-1.0) + d, 1e-6);
    }

    /**
     * Test case for the optimization with a non-converging function.
     */
    public void testFunctionWithNonConvergingValues() {
        UnivariateFunction function = new LinearObjectiveFunction(new double[]{-1.0, 1.0, Math.pow(Math.abs(x), 0.5)});
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);

        UnivariateOptimizer result = optimizer.optimize(function, new AroundMinimumInitializer(0.0));
        assertNull(result);
    }
}