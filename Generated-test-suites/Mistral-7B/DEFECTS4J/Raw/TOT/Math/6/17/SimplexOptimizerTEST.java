package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.simple.*;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.point.NonNegativePointValuePair;
import org.apache.commons.math3.optim.point.PointValues;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;

/**
 * Tests for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    /** A simple objective function. */
    static class SimpleObjective implements MultivariateFunction {
        public double value(double[] x) {
            double x0 = x[0];
            double x1 = x[1];
            return x0 * x0 + x1 * x1;
        }

        public int getNumberOfVariables() {
            return 2;
        }
    }

    /** A simple constraint. */
    static class SimpleConstraint implements LinearConstraint {
        public double[] coefficients() {
            return new double[] { 1.0, 1.0 };
        }

        public double constant() {
            return 1.0;
        }

        public int getNumberOfConstraints() {
            return 1;
        }
    }

    /** A simple constraint vector. */
    static class SimpleConstraintVector implements PointValues {
        public double value(int i) {
            return i < 0 || i >= 3 ? Double.NaN : 0.0;
        }

        public int getDimension() {
            return 3;
        }
    }

    /** A simple optimizer. */
    static class SimpleOptimizer extends SimplexOptimizer {
        public SimpleOptimizer() {
            super(new SimpleValueChecker(1e-6, 1e-6));
        }

        public PointValuePair optimize(int maxIterations,
                                        MultivariateFunction objective,
                                        GoalType goalType,
                                        OptimizationData... data) {
            SimpleObjective func = new SimpleObjective();
            return super.optimize(maxIterations, func, goalType, data);
        }
    }

    /** A test method for {@link SimplexOptimizer} with a simple objective function. */
    @Test
    public void testSimpleObjective() {
        SimpleOptimizer optimizer = new SimpleOptimizer();
        SimpleObjective f = new SimpleObjective();
        PointValuePair result;

        // Test optimization with no constraint.
        result = optimizer.optimize(100, f, GoalType.MINIMIZE, null);
        double expected = 1.0 / Math.sqrt(2.0);
        assertEquals("Minimization of a simple function should find the expected minimum.",
                     expected, result.getValue(), 1e-6);
        assertArrayEquals("Minimization of a simple function should find the expected minimum point.",
                         new double[] { expected, expected },
                         result.getPoint(), 1e-6);

        // Test optimization with a simple constraint.
        PointValuePair constraint = new NonNegativePointValuePair(new double[] { 0.0, 0.0, 0.0 });
        result = optimizer.optimize(100, f, GoalType.MINIMIZE, constraint);
        expected = 0.0;
        assertEquals("Minimization of a simple function with a simple constraint should find the expected minimum.",
                     expected, result.getValue(), 1e-6);
        assertArrayEquals("Minimization of a simple function with a simple constraint should find the expected minimum point.",
                         new double[] { 0.0, 0.0 },
                         result.getPoint(), 1e-6);

        // Test optimization with an infeasible constraint.
        LinearConstraint infeasibleConstraint = new SimpleConstraint();
        try {
            result = optimizer.optimize(100, f, GoalType.MINIMIZE, infeasibleConstraint);
            fail("Minimization of a simple function with an infeasible constraint should throw an exception.");
        } catch (IllegalArgumentException e) {
            // Expected result.
        }
    }

    /** A test method for {@link SimplexOptimizer} with a simple objective function and a simple linear constraint. */
    @Test
    public void testSimpleObjectiveAndConstraint() {
        SimpleOptimizer optimizer = new SimpleOptimizer();
        SimpleObjective f = new SimpleObjective();
        LinearObjectiveFunction constraintFunction
            = new LinearObjectiveFunction(new double[] { 1.0, 1.0 }, 1.0);
        PointValuePair constraint = new NonNegativePointValuePair(new double[] { 0.0, 0.0, 0.0 });
        PointValuePair result;

        // Test optimization with a simple constraint.
        result = optimizer.optimize(100, f, GoalType.MINIMIZE, constraintFunction, constraint);
        double expected = 1.0 / Math.sqrt(2.0);
        assertEquals("Minimization of a simple function with a simple constraint should find the expected minimum.",
                     expected, result.getValue(), 1e-6);
        assertArrayEquals("Minimization of a simple function with a simple constraint should find the expected minimum point.",
                         new double[] { expected, expected },
                         result.getPoint(), 1e-6);

        // Test optimization with an infeasible constraint.
        LinearConstraint infeasibleConstraint = new LinearConstraint(new double[] { 1.0, 1.0 }, 0.0);
        try {
            result = optimizer.optimize(100, f, GoalType.MINIMIZE, constraintFunction, infeasibleConstraint);
            fail("Minimization of a simple function with an infeasible constraint should throw an exception.");
        } catch (IllegalArgumentException e) {
            // Expected result.
        }
    }

    /** A test method for {@link SimplexOptimizer} with a non-null constraint vector. */
    @Test
    public void testSimpleObjectiveAndConstraintVector() {
        SimpleOptimizer optimizer = new SimpleOptimizer();
        SimpleObjective f = new SimpleObjective();
        SimpleConstraintVector constraintVector = new SimpleConstraintVector();
        PointValuePair result;

        // Test optimization with a non-null constraint vector.
        result = optimizer.optimize(100, f, GoalType.MINIMIZE, null, constraintVector);
        // The optimization should still work, but the constraint vector is ignored.
        double expected = 1.0 / Math.sqrt(2.0);
        assertEquals("Minimization of a simple function with a non-null constraint vector should find the expected minimum.",
                     expected, result.getValue(), 1e-6);
        assertArrayEquals("Minimization of a simple function with a non-null constraint vector should find the expected minimum point.",
                         new double[] { expected, expected },
                         result.getPoint(), 1e-6);

        // Test optimization with an infeasible constraint vector.
        SimpleConstraintVector infeasibleConstraintVector = new SimpleConstraintVector();
        infeasibleConstraintVector.setValue(1, Double.POSITIVE_INFINITY);
        try {
            result = optimizer.optimize(100, f, GoalType.MINIMIZE, null, infeasibleConstraintVector);
            fail("Minimization of a simple function with an infeasible constraint vector should throw an exception.");
        } catch (IllegalArgumentException e) {
            // Expected result.
        }
    }
}