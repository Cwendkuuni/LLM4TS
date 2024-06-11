package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testSimpleMinimization() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, new SimpleBounds(0, 5), new InitialGuess(2));
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testMaximization() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, new SimpleBounds(0, 5), new InitialGuess(2));
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(0.0, -result.getValue(), 1e-6);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testDivisionByZeroFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.MINIMIZE, new SimpleBounds(0, 1), new InitialGuess(1));
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.optimize(f, GoalType.MINIMIZE, new SimpleBounds(0, Math.PI), new InitialGuess(0));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeStepSize() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.MINIMIZE, new SimpleBounds(0, 1), new InitialGuess(1), new MaxEval(1));
        optimizer.setMaxIterations(1);
        optimizer.optimize(f, GoalType.MINIMIZE, new SimpleBounds(0, 1), new InitialGuess(1), new MaxEval(1));
        optimizer.optimize(f, GoalType.MINIMIZE, new SimpleBounds(0, 1), new InitialGuess(1), new MaxEval(1));
        optimizer.setStepSize(-0.1);
        optimizer.optimize(f, GoalType.MINIMIZE, new SimpleBounds(0, 1), new InitialGuess(1), new MaxEval(1));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeRelativeAccuracy() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.MINIMIZE, new SimpleBounds(0, 1), new InitialGuess(1), new MaxEval(1));
        optimizer.setRelativeAccuracy(-1e-6);
        optimizer.optimize(f, GoalType.MINIMIZE, new SimpleBounds(0, 1), new InitialGuess(1), new MaxEval(1));
    }

    @Test
    public void testDifferentBounds() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.log(1 + x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, new SimpleBounds(-1, 1), new InitialGuess(0));
        assertEquals(-1, result.getPoint(), 1e-6);
        assertEquals(1.621935600262837, result.getValue(), 1e-6);
    }
}