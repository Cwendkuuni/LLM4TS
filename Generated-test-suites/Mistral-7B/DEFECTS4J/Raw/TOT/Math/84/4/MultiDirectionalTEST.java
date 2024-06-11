package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for MultiDirectional optimizer.
 */
public class MultiDirectionalTest {

    /**
     * Test for MultiDirectional optimizer with default coefficients.
     */
    @Test
    public void testDefaultCoefficients() {
        MultiDirectional optimizer = new MultiDirectional();
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        RealPointValuePair[] initialSolutions = new RealPointValuePair[] {
            new RealPointValuePair(new double[] { 0.0 }, 1.0),
            new RealPointValuePointValuePair(new double[] { Math.PI / 4.0 }, 0.5),
            new RealPointValuePair(new double[] { Math.PI / 2.0 }, 1.0),
            new RealPointValuePair(new double[] { 3.0 * Math.PI / 4.0 }, 1.0),
            new RealPointValuePair(new double[] { Math.PI }, 1.0)
        };

        OptimizationProblem problem = new OptimizationProblem(function, initialSolutions);
        optimizer.optimize(problem);

        PointValuePair bestSolution = optimizer.getBestSolution();
        assertEquals(Math.PI / 2.0, bestSolution.getPoint()[0], 1e-6);
        assertEquals(0.25, bestSolution.getValue(), 1e-6);
    }

    /**
     * Test for MultiDirectional optimizer with custom coefficients.
     */
    @Test
    public void testCustomCoefficients() {
        MultiDirectional optimizer = new MultiDirectional(1.5, 0.7);
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.abs(Math.pow(Math.sin(x), 2)) + Math.abs(Math.cos(x) - 0.5);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        RealPointValuePair[] initialSolutions = new RealPointValuePair[] {
            new RealPointValuePair(new double[] { 0.0 }, Double.POSITIVE_INFINITY),
            new RealPointValuePair(new double[] { -Math.PI / 2.0 }, Double.POSITIVE_INFINITY),
            new RealPointValuePair(new double[] { Math.PI / 4.0 }, Double.POSITIVE_INFINITY),
            new RealPointValuePair(new double[] { Math.PI }, Double.POSITIVE_INFINITY),
            new RealPointValuePair(new double[] { 3.0 * Math.PI / 2.0 }, Double.POSITIVE_INFINITY)
        };

        OptimizationProblem problem = new OptimizationProblem(function, initialSolutions);
        try {
            optimizer.optimize(problem);
            fail("Expected OptimizationException due to exceeded maximum function evaluations");
        } catch (OptimizationException e) {
            // Expected
        }

        assertEquals(MaxEvaluationsExceededException.class, e.getCause().getClass());

        // Check that optimizer can find solution with smaller number of iterations
        optimizer = new MultiDirectional(1.2, 0.8);
        optimizer.optimize(problem);

        PointValuePair bestSolution = optimizer.getBestSolution();
        assertEquals(-Math.PI / 2.0, bestSolution.getPoint()[0], 1e-6);
        assertEquals(1.5, bestSolution.getValue(), 1e-6);
    }

    /**
     * Test for MultiDirectional optimizer with invalid coefficients.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCoefficients() {
        MultiDirectional optimizer = new MultiDirectional(-1.0, 0.5);
    }

    /**
     * Test for MultiDirectional optimizer with non-monotonic function.
     */
    @Test
    public void testNonMonotonicFunction() {
        MultiDirectional optimizer = new MultiDirectional();
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                if (x < 0.5) {
                    return Math.pow(Math.sin(x), 2);
                } else {
                    return -Math.pow(Math.sin(x), 2);
                }
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        RealPointValuePair[] initialSolutions = new RealPointValuePair[] {
            new RealPointValuePair(new double[] { 0.0 }, 1.0),
            new RealPointValuePointValuePair(new double[] { 0.25 }, 0.0),
            new RealPointValuePair(new double[] { 0.5 }, Double.NaN),
            new RealPointValuePair(new double[] { 0.75 }, 1.0),
            new RealPointValuePair(new double[] { 1.0 }, 1.0)
        };

        OptimizationProblem problem = new OptimizationProblem(function, initialSolutions);

        optimizer.optimize(problem);

        PointValuePair bestSolution = optimizer.getBestSolution();
        assertEquals(0.25, bestSolution.getPoint()[0], 1e-6);
        assertEquals(0.0, bestSolution.getValue(), 1e-6);

        RealPointValuePair worstSolution = optimizer.getWorstSolution();
        assertEquals(1.0, worstSolution.getPoint()[0], 1e-6);
        assertEquals(1.0, worstSolution.getValue(), 1e-6);
    }

    /**
     * Test for MultiDirectional optimizer with non-differentiable function.
     */
    @Test
    public void testNonDifferentiableFunction() {
        MultiDirectional optimizer = new MultiDirectional();
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.log(Math.abs(x));
                } else {
                    throw new UnsupportedOperationException("Cannot compute derivative for x = " + x);
                }
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }

            @Override
            public double derivative(double x) {
                if (x > 0) {
                    return 1.0 / x;
                } else {
                    throw new UnsupportedOperationException("Cannot compute derivative for x = " + x);
                }
            }
        };

        RealPointValuePair[] initialSolutions = new RealPointValuePair[] {
            new RealPointValuePair(new double[] { 0.0 }, Double.POSITIVE_INFINITY),
            new RealPointValuePair(new double[] { -1.0 }, Double.POSITIVE_INFINITY),
            new RealPointValuePair(new double[] { 1.0 }, Double.POSITIVE_INFINITY),
            new RealPointValuePair(new double[] { 2.0 }, Double.POSITIVE_INFINITY),
            new RealPointValuePair(new double[] { 3.0 }, Double.POSITIVE_INFINITY)
        };

        OptimizationProblem problem = new OptimizationProblem(function, initialSolutions);
        try {
            optimizer.optimize(problem);
            fail("Expected NoBracketException due to non-differentiable objective function");
        } catch (NoBracketException e) {
            // Expected
        }

        // Test that optimizer can find solution with differentiable function
        ObjectiveFunction differentiableFunction = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.log(Math.abs(x)) + Math.pow(Math.abs(x) - 1.0, 2);
                } else {
                    throw new UnsupportedOperationException("Cannot compute derivative for x = " + x);
                }
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }

            @Override
            public double derivative(double x) {
                if (x > 0) {
                    return (1.0 / x) - 2.0 * (Math.abs(x) - 1.0);
                } else {
                    throw new UnsupportedOperationException("Cannot compute derivative for x = " + x);
                }
            }
        };

        OptimizationProblem problemDifferentiable = new OptimizationProblem(differentiableFunction, initialSolutions);
        optimizer.optimize(problemDifferentiable);

        PointValuePair bestSolution = optimizer.getBestSolution();
        assertEquals(1.5, bestSolution.getPoint()[0], 1e-6);
        assertEquals(1.25, bestSolution.getValue(), 1e-6);
    }

}