package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.NonlinearOptimizer;
import org.apache.commons.math3.optim.PointsToConvergenceException;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNotNull(optimizer);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnoptimizedBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.doOptimize();
    }

    @Test
    public void testBrentOptimizerWithFunction() throws Exception {
        UnivariateFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair solution = optimizer.optimize(f, SimpleGoalType.MINIMIZE, 0.0, 1.0, 0.5);
        assertNotNull(solution);
        assertTrue(solution.getValue() < 0.25);
        assertFalse(Double.isNaN(solution.getValue()));
        assertArrayEquals(new double[]{solution.getPoint()}, new double[]{solution.getPoint()}, 0.0);
    }

    @Test
    public void testBrentOptimizerWithCustomInitialGuess() throws Exception {
        UnivariateFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair solution = optimizer.optimize(f, SimpleGoalType.MINIMIZE, 0.0, 1.0, new InitialGuess(0.75));
        assertNotNull(solution);
        assertTrue(solution.getValue() < 0.25);
        assertFalse(Double.isNaN(solution.getValue()));
        assertArrayEquals(new double[]{solution.getPoint()}, new double[]{solution.getPoint()}, 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBrentOptimizerWithFunctionEvaluationException() throws Exception {
        UnivariateFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) throws Exception {
                if (x == 0.5) {
                    throw new FunctionEvaluationException("Function evaluation failed");
                }
                return x * x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, SimpleGoalType.MINIMIZE, 0.0, 1.0, 0.5);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBrentOptimizerWithMaxIterationsExceededException() throws Exception {
        UnivariateFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(1);
        optimizer.optimize(f, SimpleGoalType.MINIMIZE, 0.0, Math.PI, 0.5);
    }

    @Test(expected = PointsToConvergenceException.class)
    public void testBrentOptimizerWithPointsToConvergenceException() throws Exception {
        UnivariateFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, -Math.pow(x, 2.0));
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(1000);
        PointValuePair solution = optimizer.optimize(f, SimpleGoalType.MINIMIZE, -10.0, 10.0, 0.0);
    }
}