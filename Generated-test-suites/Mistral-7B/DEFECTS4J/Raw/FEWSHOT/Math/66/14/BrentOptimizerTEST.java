package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        UnivariateOptimizer optimizer = new BrentOptimizer();
        assertNotNull(optimizer);
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);
    }

    @Test
    public void testOptimizeWithMinimum() throws MaxIterationsExceededException, FunctionEvaluationException {
        DoubleFunction f = x -> Math.pow(x, 2);
        double min = 1.0;
        double startValue = 2.0;
        GoalType goalType = GoalType.MINIMIZE;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return f.apply(x);
            }
        };
        double optimum = optimizer.optimize(function, goalType, min, min + 1.0, startValue).getPoint();
        assertEquals(1.0, optimum, 1e-6);
    }

    @Test
    public void testOptimizeWithMaximum() throws MaxIterationsExceededException, FunctionEvaluationException {
        DoubleFunction f = x -> Math.pow(x, 2);
        double min = 1.0;
        double max = 2.0;
        double startValue = 1.5;
        GoalType goalType = GoalType.MAXIMIZE;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return f.apply(x);
            }
        };
        double optimum = optimizer.optimize(function, goalType, min, max, startValue).getPoint();
        assertEquals(1.25, optimum, 1e-6);
    }

    @Test
    public void testOptimizeWithMinimumOutOfBounds() throws MaxIterationsExceededException, FunctionEvaluationException, NotStrictlyPositiveException {
        DoubleFunction f = x -> Math.pow(x, 2);
        double min = -1.0;
        double max = 1.0;
        double startValue = 0.0;
        GoalType goalType = GoalType.MINIMIZE;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return f.apply(x);
            }
        };
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.optimize(function, goalType, min, max, startValue));
    }

    @Test
    public void testOptimizeWithInfiniteBounds() throws MaxIterationsExceededException, FunctionEvaluationException {
        DoubleFunction f = x -> Math.log(x - 1.0);
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;
        double startValue = 1.0;
        GoalType goalType = GoalType.MINIMIZE;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return f.apply(x);
            }
        };
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(function, goalType, min, max, startValue));
    }

    @Test
    public void testOptimizeWithInvalidStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        DoubleFunction f = x -> Math.pow(x, 2);
        double min = 1.0;
        double max = 2.0;
        double startValue = min;
        GoalType goalType = GoalType.MINIMIZE;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return f.apply(x);
            }
        };
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(function, goalType, min, max, startValue));
    }

    @Test
    public void testOptimizeWithInvalidFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateOptimizer optimizer = new BrentOptimizer() {
            @Override
            protected double doOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
                throw new UnsupportedOperationException();
            }
        };
        DoubleFunction f = x -> { throw new IllegalArgumentException(); };
        double min = 1.0;
        double max = 2.0;
        double startValue = 1.5;
        GoalType goalType = GoalType.MAXIMIZE;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return f.apply(x);
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(function, goalType, min, max, startValue));
    }

    @Test
    public void testOptimizeWithMaxIterationsExceededException() throws MaxIterationsExceededException, FunctionEvaluationException {
        DoubleFunction f = x -> Math.pow(x, 2);
        double min = 1.0;
        double max = 2.0;
        double startValue = 1.5;
        GoalType goalType = GoalType.MAXIMIZE;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return f.apply(x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer() {
            @Override
            protected int getMaximalIterationCount() {
                return 0;
            }
        };
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(function, goalType, min, max, startValue));
    }
}

interface DoubleFunction {
    double apply(double x);
}