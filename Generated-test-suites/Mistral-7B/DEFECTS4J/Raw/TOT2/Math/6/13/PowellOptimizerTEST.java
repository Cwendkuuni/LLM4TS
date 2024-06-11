package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraints;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBounds;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizerResult;
import org.junit.Test;

/**
 * Tests for {@link PowellOptimizer} class.
 */
public class PowellOptimizerTest {

    /**
     * Test optimization of a quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        final double[] startPoint = {1.0, 1.0};
        final MultivariateFunction function = new PolynomialFunction(2, 2,
                new double[][]{{-1.0, -1.0}, {1.0, 1.0}, {-1.0, 1.0}, {1.0, -1.0}},
                new double[]{3.0, 3.0, 3.0, 3.0});
        final PowellOptimizer optimizer = new PowellOptimizer(1e-7, 1e-8);
        final PointValuePair solution = optimizer.optimize(function, startPoint);
        final double[] expectedSolution = {-0.5, -0.5};
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(expectedSolution[0], solution.getPoint()[0], 1e-7);
        assertEquals(expectedSolution[1], solution.getPoint()[1], 1e-7);
        assertEquals(function.value(expectedSolution), solution.getValue(), 1e-8);
    }

    /**
     * Test optimization of a function with multiple local minima.
     */
    @Test
    public void testFunctionWithMultipleLocalMinima() {
        final double[] startPoint = {1.0};
        final MultivariateFunction function = new PolynomialFunction(1, 1,
                new double[][]{{-2.0, -1.0}, {-1.0, -1.0}}, new double[]{4.0, 4.0});
        final PowellOptimizer optimizer = new PowellOptimizer(1e-7, 1e-8);
        final PointValuePair solution = optimizer.optimize(function, startPoint);
        final double expectedSolution = 0.5;
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(expectedSolution, solution.getPoint()[0], 1e-7);
        assertEquals(function.value(expectedSolution), solution.getValue(), 1e-8);
    }

    /**
     * Test optimization of a function with a boundary constraint.
     */
    @Test
    public void testFunctionWithBoundaryConstraint() {
        final double[] startPoint = {1.0, 1.0};
        final MultivariateFunction function = new PolynomialFunction(2, 2,
                new double[][]{{-1.0, -1.0}, {1.0, 1.0}, {-1.0, 1.0}, {1.0, -1.0}},
                new double[]{3.0, 3.0, 3.0, 3.0});
        final LinearConstraints constraints = new LinearConstraints(new double[][]{
                {0.0, 1.0}, {1.0, 0.0}}, new double[]{-1.0, -1.0});
        final PowellOptimizer optimizer = new PowellOptimizer(1e-7, 1e-8);
        try {
            optimizer.optimize(function, startPoint, constraints);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    /**
     * Test optimization of a function without finding a solution.
     */
    @Test
    public void testFunctionWithoutSolution() {
        final double[] startPoint = {1.0};
        final UnivariateFunction function = new PolynomialFunction(1, 1,
                new double[]{{-1.0}, {1.0}}, new double[]{0.0});
        final PowellOptimizer optimizer = new PowellOptimizer(1e-7, 1e-8);
        final PointValuePairCollection results = optimizer.optimize(function, startPoint,
                new MaxEval(1000));
        assertEquals(0, results.size());
    }

    /**
     * Test optimization of a function with an exception in the objective
     * function.
     */
    @Test(expected = RuntimeException.class)
    public void testFunctionWithExceptionInObjectiveFunction() {
        final double[] startPoint = {1.0};
        final MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                throw new RuntimeException("Test exception");
            }

            public int getNumberOfVariables() {
                return 1;
            }
        };
        final PowellOptimizer optimizer = new PowellOptimizer(1e-7, 1e-8);
        optimizer.optimize(function, startPoint);
    }

    /**
     * Test optimization of a function with an invalid goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFunctionWithInvalidGoalType() {
        final double[] startPoint = {1.0};
        final MultivariateFunction function = new PolynomialFunction(1, 1,
                new double[][]{{-1.0, -1.0}, {1.0, 1.0}}, new double[]{3.0, 3.0});
        final PowellOptimizer optimizer = new PowellOptimizer(1e-7, 1e-8);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.optimize(function, startPoint);
    }

    /**
     * Test optimization of a function with an invalid starting point.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFunctionWithInvalidStartingPoint() {
        final MultivariateFunction function = new PolynomialFunction(2, 2,
                new double[][]{{-1.0, -1.0}, {1.0, 1.0}, {-1.0, 1.0}, {1.0, -1.0}},
                new double[]{3.0, 3.0, 3.0, 3.0});
        final PowellOptimizer optimizer = new PowellOptimizer(1e-7, 1e-8);
        optimizer.optimize(function, null);
    }

    /**
     * Test optimization of a function with a null convergence checker.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFunctionWithNullConvergenceChecker() {
        final double[] startPoint = {1.0};
        final MultivariateFunction function = new PolynomialFunction(1, 1,
                new double[][]{{-1.0, -1.0}, {1.0, 1.0}}, new double[]{3.0, 3.0});
        final PowellOptimizer optimizer = new PowellOptimizer(1e-7, 1e-8, null);
        optimizer.optimize(function, startPoint);
    }

    /**
     * Test optimization of a function with a custom convergence checker.
     */
    @Test
    public void testFunctionWithCustomConvergenceChecker() {
        final double[] startPoint = {1.0};
        final MultivariateFunction function = new PolynomialFunction(1, 1,
                new double[][]{{-1.0, -1.0}, {1.0, 1.0}}, new double[]{3.0, 3.0});
        final PowellOptimizer optimizer = new PowellOptimizer(1e-7, 1e-8,
                new TestCustomConvergenceChecker());
        final UnivariateOptimizerResult result = optimizer.optimize(function, startPoint);
        final double expectedSolution = 1.5;
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(expectedSolution, result.getPoint()[0], 1e-7);
        assertEquals(function.value(expectedSolution), result.getValue(), 1e-8);
    }

    /**
     * Test optimization of a function with custom start point bounds.
     */
    @Test
    public void testFunctionWithCustomStartPointBounds() {
        final double[] startPoint = {1.0};
        final MultivariateFunction function = new PolynomialFunction(1, 1,
                new double[][]{{-1.0, -1.0}, {1.0, 1.0}}, new double[]{3.0, 3.0});
        final SimpleBounds bounds = new SimpleBounds(0.0, 2.0);
        final PowellOptimizer optimizer = new PowellOptimizer(1e-7, 1e-8, bounds);
        final PointValuePair solution = optimizer.optimize(function, startPoint);
        final double expectedSolution = 1.0;
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(expectedSolution, solution.getPoint()[0], 1e-7);
        assertEquals(function.value(expectedSolution), solution.getValue(), 1e-8);
    }

}

class TestCustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {

    @Override
    public boolean converged(int iteration, PointValuePair previous,
            PointValuePair current) {
        final double error = Math.abs(current.getValue() - previous.getValue());
        return error < 1e-12;
    }
}