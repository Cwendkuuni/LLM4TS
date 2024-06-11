package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.maximization.MaxEval;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.NonlinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.NonlinearOptimizationResult;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairConsumer;
import org.apache.commons.math3.optim.nonlinear.scalar.simple.RidgedMultivariateFunction;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the Powell optimizer.
 */
public class PowellOptimizerTest {

    /**
     * Test of the constructor.
     */
    @Test
    public void testConstructor() {
        PowellOptimizer powell = new PowellOptimizer(1.0e-6, 1.0e-8);
        assertNotNull(powell);
    }

    /**
     * Test of the optimize method with a simple function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        PowellOptimizer powell = new PowellOptimizer(1.0e-6, 1.0e-8);
        double[] initialPoint = new double[] { 1.0, 1.0 };
        UnivariateFunction function = new RidgedMultivariateFunction(1.0, 2.0, new ExpFunction());
        GoalType goal = GoalType.MINIMIZE;
        NonlinearOptimizationResult result = powell.optimize(function, goal, initialPoint);
        PointValuePair expected = new PointValuePair(new double[] { 1.0, 1.0 }, 3.0);
        assertEquals(expected, result);
    }

    /**
     * Test of the optimize method with a simple function having a convergence checker.
     */
    @Test
    public void testOptimizeSimpleFunctionWithChecker() {
        PowellOptimizer powell = new PowellOptimizer(1.0e-6, 1.0e-8, new CustomChecker(1.0e-3, 1.0e-4));
        double[] initialPoint = new double[] { 1.0, 1.0 };
        UnivariateFunction function = new RidgedMultivariateFunction(1.0, 2.0, new ExpFunction());
        GoalType goal = GoalType.MINIMIZE;
        NonlinearOptimizationResult result = powell.optimize(function, goal, initialPoint);
        PointValuePair expected = new PointValuePair(new double[] { 1.0, 1.0 }, 3.0);
        assertEquals(expected, result);
    }

    /**
     * Test of the optimize method with a simple function and constraints.
     */
    @Test
    public void testOptimizeSimpleFunctionWithConstraints() {
        PowellOptimizer powell = new PowellOptimizer(1.0e-6, 1.0e-8);
        double[] initialPoint = new double[] { 1.0, 1.0 };
        UnivariateFunction function = new RidgedMultivariateFunction(1.0, 2.0, new ExpFunction());
        GoalType goal = GoalType.MINIMIZE;
        LinearConstraint constraint1 = new LinearConstraint(new double[] { 1.0, 1.0 }, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[] { 1.0, 1.0 }, 2.0);
        powell.setConstraints(new NonlinearConstraint[] { constraint1, constraint2 });
        PointValuePairConsumer consumer = new PointValuePairConsumer();
        powell.optimize(function, goal, initialPoint, consumer);
        PointValuePair expected = new PointValuePair(new double[] { 1.0, 1.0 }, 3.0);
        assertEquals(expected, consumer.getLastSolution());
        assertNull(consumer.getException());
    }

    /**
     * Test of the optimize method with a simple function and a convergence checker
     * and constraints.
     */
    @Test
    public void testOptimizeSimpleFunctionWithCheckerAndConstraints() {
        PowellOptimizer powell = new PowellOptimizer(1.0e-6, 1.0e-8, new CustomChecker(1.0e-3, 1.0e-4));
        double[] initialPoint = new double[] { 1.0, 1.0 };
        UnivariateFunction function = new RidgedMultivariateFunction(1.0, 2.0, new ExpFunction());
        GoalType goal = GoalType.MINIMIZE;
        LinearConstraint constraint1 = new LinearConstraint(new double[] { 1.0, 1.0 }, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[] { 1.0, 1.0 }, 2.0);
        powell.setConstraints(new NonlinearConstraint[] { constraint1, constraint2 });
        PointValuePairConsumer consumer = new PointValuePairConsumer();
        powell.optimize(function, goal, initialPoint, consumer);
        PointValuePair expected = new PointValuePair(new double[] { 1.0, 1.0 }, 3.0);
        assertEquals(expected, consumer.getLastSolution());
        assertNull(consumer.getException());
    }

    /**
     * Test of the optimize method with a function having a large number of
     * evaluations and a convergence checker.
     */
    @Test
    @MaxEval(10000)
    public void testOptimizeLargeFunctionWithChecker() {
        PowellOptimizer powell = new PowellOptimizer(1.0e-3, 1.0e-3, new CustomChecker(1.0e-3, 1.0e-3));
        double[] initialPoint = new double[] { 1.0, 1.0 };
        UnivariateFunction function = new RidgedMultivariateFunction(1.0, 2.0, new ExpFunction());
        GoalType goal = GoalType.MINIMIZE;
        NonlinearOptimizationResult result = powell.optimize(function, goal, initialPoint);
        PointValuePair expected = new PointValuePair(new double[] { 1.0, 1.0 }, 3.0);
        assertEquals(expected, result);
    }

    /**
     * Test of the optimize method with a function having a large number of
     * evaluations and constraints.
     */
    @Test
    @MaxEval(10000)
    public void testOptimizeLargeFunctionWithConstraints() {
        PowellOptimizer powell = new PowellOptimizer(1.0e-3, 1.0e-3);
        double[] initialPoint = new double[] { 1.0, 1.0 };
        UnivariateFunction function = new RidgedMultivariateFunction(1.0, 2.0, new ExpFunction());
        GoalType goal = GoalType.MINIMIZE;
        LinearConstraint constraint1 = new LinearConstraint(new double[] { 1.0, 1.0 }, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[] { 1.0, 1.0 }, 2.0);
        powell.setConstraints(new NonlinearConstraint[] { constraint1, constraint2 });
        PointValuePairConsumer consumer = new PointValuePairConsumer();
        powell.optimize(function, goal, initialPoint, consumer);
        PointValuePair expected = new PointValuePair(new double[] { 1.0, 1.0 }, 3.0);
        assertEquals(expected, consumer.getLastSolution());
        assertNull(consumer.getException());
    }

    /**
     * Test of the optimize method with a function having a large number of
     * evaluations, a convergence checker, and constraints.
     */
    @Test
    @MaxEval(10000)
    public void testOptimizeLargeFunctionWithCheckerAndConstraints() {
        PowellOptimizer powell = new PowellOptimizer(1.0e-3, 1.0e-3, new CustomChecker(1.0e-3, 1.0e-3));
        double[] initialPoint = new double[] { 1.0, 1.0 };
        UnivariateFunction function = new RidgedMultivariateFunction(1.0, 2.0, new ExpFunction());
        GoalType goal = GoalType.MINIMIZE;
        LinearConstraint constraint1 = new LinearConstraint(new double[] { 1.0, 1.0 }, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[] { 1.0, 1.0 }, 2.0);
        powell.setConstraints(new NonlinearConstraint[] { constraint1, constraint2 });
        PointValuePairConsumer consumer = new PointValuePairConsumer();
        powell.optimize(function, goal, initialPoint, consumer);
        PointValuePair expected = new PointValuePair(new double[] { 1.0, 1.0 }, 3.0);
        assertEquals(expected, consumer.getLastSolution());
        assertNull(consumer.getException());
    }

    /**
     * Test of the optimize method with a function having illegal arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeIllegalArgument() {
        PowellOptimizer powell = new PowellOptimizer(1.0e-6, 1.0e-8);
        double[] initialPoint = null;
        UnivariateFunction function = new ExpFunction();
        GoalType goal = GoalType.MINIMIZE;
        powell.optimize(function, goal, initialPoint);
    }

    /**
     * Test of the optimize method with a null convergence checker.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullChecker() {
        PowellOptimizer powell = new PowellOptimizer(1.0e-6, 1.0e-8, null);
        double[] initialPoint = new double[] { 1.0, 1.0 };
        UnivariateFunction function = new RidgedMultivariateFunction(1.0, 2.0, new ExpFunction());
        GoalType goal = GoalType.MINIMIZE;
        powell.optimize(function, goal, initialPoint);
    }

    /**
     * Test of the optimize method with a convergence checker that always
     * returns false.
     */
    @Test
    public void testOptimizeAlwaysFalseChecker() {
        PowellOptimizer powell = new PowellOptimizer(1.0e-6, 1.0e-8, new AlwaysFalseChecker());
        double[] initialPoint = new double[] { 1.0, 1.0 };
        UnivariateFunction function = new RidgedMultivariateFunction(1.0, 2.0, new ExpFunction());
        GoalType goal = GoalType.MINIMIZE;
        assertFalse(powell.optimize(function, goal, initialPoint).isConverged());
    }

    /**
     * Test of the optimize method with a convergence checker that always
     * returns true.
     */
    @Test
    public void testOptimizeAlwaysTrueChecker() {
        PowellOptimizer powell = new PowellOptimizer(1.0e-6, 1.0e-8, new AlwaysTrueChecker());
        double[] initialPoint = new double[] { 1.0, 1.0 };
        UnivariateFunction function = new RidgedMultivariateFunction(1.0, 2.0, new ExpFunction());
        GoalType goal = GoalType.MINIMIZE;
        assertTrue(powell.optimize(function, goal, initialPoint).isConverged());
    }

    /**
     * A custom convergence checker that always returns true.
     */
    private static class AlwaysTrueChecker
        implements ConvergenceChecker<PointValuePair> {
        public boolean converged(int iteration, PointValuePair previous,
                                 PointValuePair current) {
            return true;
        }
    }

    /**
     * A custom convergence checker that always returns false.
     */
    private static class AlwaysFalseChecker
        implements ConvergenceChecker<PointValuePair> {
        public boolean converged(int iteration, PointValuePair previous,
                                 PointValuePair current) {
            return false;
        }
    }

    /**
     * A custom convergence checker that checks if the function value is below
     * a specific value.
     */
    private static class CustomChecker
        implements ConvergenceChecker<PointValuePair> {
        private final double relativeTolerance;
        private final double absoluteTolerance;

        public CustomChecker(double relativeTolerance, double absoluteTolerance) {
            this.relativeTolerance = relativeTolerance;
            this.absoluteTolerance = absoluteTolerance;
        }

        public boolean converged(int iteration, PointValuePair previous,
                                 PointValuePair current) {
            double fPrevious = previous.getValue();
            double fCurrent = current.getValue();
            return Math.abs(fCurrent - fPrevious) <= (relativeTolerance * Math.max(Math.abs(fCurrent), Math.abs(fPrevious)) + absoluteTolerance);
        }
    }

    /**
     * A simple function for testing.
     */
    private static class ExpFunction implements UnivariateFunction {
        public double value(double x) {
            return Math.exp(x);
        }
    }
}