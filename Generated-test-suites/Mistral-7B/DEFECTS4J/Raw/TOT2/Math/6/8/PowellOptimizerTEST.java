package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValueFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.goal.GoalType;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class PowellOptimizerTest {

    @Test
    public void testMinimizationWithDefaults() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.sin(x[1]), 2);
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer();
        GoalType goal = GoalType.MINIMIZE;
        InitialGuess startPoint = new InitialGuess(new double[]{1.0, 1.0});
        MaxEval maxEval = new MaxEval(100);
        PointValuePair solution = optimizer.optimize(f, goal, startPoint, maxEval);
        double[] expectedSolution = new double[]{1.1832781257451334, -1.2017965414322926};
        assertEquals(expectedSolution[0], solution.getPoint()[0], 1e-6);
        assertEquals(expectedSolution[1], solution.getPoint()[1], 1e-6);
        assertEquals(-0.3608354561246154, solution.getValue(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testIllegalArgumentExceptionWithInvalidAbsoluteThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(1.0, -1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIllegalArgumentExceptionWithInvalidRelativeThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(0.5, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithInvalidGoalType() {
        PowellOptimizer optimizer = new PowellOptimizer();
        optimizer.optimize(new MultivariateFunction() {
            public double value(double[] x) {
                return 0;
            }
        }, GoalType.MAXIMIZE, new InitialGuess(new double[]{0.0}), new MaxEval(100));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithNullConvergenceChecker() {
        PowellOptimizer optimizer = new PowellOptimizer();
        optimizer.optimize(new MultivariateFunction() {
            public double value(double[] x) {
                return 0;
            }
        }, GoalType.MINIMIZE, null, new MaxEval(100));
    }

    @Test
    public void testMinimizationWithCustomConvergenceChecker() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.sin(x[1]), 2);
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
        GoalType goal = GoalType.MINIMIZE;
        InitialGuess startPoint = new InitialGuess(new double[]{1.0, 1.0});
        MaxEval maxEval = new MaxEval(100);
        PointValuePair solution = optimizer.optimize(f, goal, startPoint, maxEval);
        double[] expectedSolution = new double[]{1.1832781257451334, -1.2017965414322926};
        assertEquals(expectedSolution[0], solution.getPoint()[0], 1e-6);
        assertEquals(expectedSolution[1], solution.getPoint()[1], 1e-6);
        assertEquals(-0.3608354561246154, solution.getValue(), 1e-6);
    }

    /**
     * Custom convergence checker for testing purposes.
     */
    private static class MyConvergenceChecker
            implements ConvergenceChecker<PointValuePair> {

        @Override
        public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
            double[] currentPoint = current.getPoint();
            double[] previousPoint = previous.getPoint();
            double currentValue = current.getValue();
            double previousValue = previous.getValue();
            return (Math.abs(currentValue - previousValue) < 1e-5);
        }
    }

    @Test
    public void testMinimizationWithCustomBounds() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.sin(x[1]), 2);
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer();
        GoalType goal = GoalType.MINIMIZE;
        InitialGuess startPoint = new InitialGuess(new double[]{1.0, 1.0});
        MaxEval maxEval = new MaxEval(100);
        SimpleBounds bounds = new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0});
        PointValuePair solution = optimizer.optimize(f, goal, startPoint, maxEval, bounds);
        double[] expectedSolution = new double[]{1.1832781257451334, -1.2017965414322926};
        assertEquals(expectedSolution[0], solution.getPoint()[0], 1e-6);
        assertEquals(expectedSolution[1], solution.getPoint()[1], 1e-6);
        assertEquals(-0.3608354561246154, solution.getValue(), 1e-6);
    }

    @Test
    public void testMinimizationWithCustomFunction() {
        PointValueFunction f = new PointValueFunction() {
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i] - 1, 2);
                }
                return sum;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer();
        GoalType goal = GoalType.MINIMIZE;
        InitialGuess startPoint = new InitialGuess(new double[]{2.0, 2.0, 2.0});
        MaxEval maxEval = new MaxEval(100);
        PointValuePair solution = optimizer.optimize(f, goal, startPoint, maxEval);
        double[] expectedSolution = new double[]{1.0, 1.0, 1.0};
        assertEquals(Arrays.toString(expectedSolution), Arrays.toString(solution.getPoint()), 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testMinimizationWithCustomInitialGuessAndFunction() {
        PointValueFunction f = new PointValueFunction() {
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i] - 1, 2);
                }
                return sum;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer();
        GoalType goal = GoalType.MINIMIZE;
        InitialGuess startPoint = new InitialGuess(new double[]{3.0, 3.0, 3.0});
        MaxEval maxEval = new MaxEval(100);
        PointValuePair solution = optimizer.optimize(f, goal, startPoint, maxEval);
        double[] expectedSolution = new double[]{1.0, 1.0, 1.0};
        assertEquals(Arrays.toString(expectedSolution), Arrays.toString(solution.getPoint()), 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }
}