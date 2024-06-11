package org.apache.commons.math.optimization.univariate.test;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.FunctionEvaluationException;
import org.apache.commons.math.exception.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testOptimize_WithStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = Math.PI;
        double startValue = 1.0;
        double optimum = optimizer.optimize(f, goalType, min, max, startValue);

        assertEquals(0.0, Math.sin(optimum), 1e-15);
    }

    @Test
    public void testOptimize_WithoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = Math.PI;
        double optimum = optimizer.optimize(f, goalType, min, max);

        assertEquals(0.0, Math.sin(optimum), 1e-15);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_WithNegativeEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = Math.PI;
        double eps = -1.0;
        double t = 1e-6;
        optimizer.setRelativeAccuracy(eps);
        optimizer.optimize(f, goalType, min, max);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_WithNegativeT() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = Math.PI;
        double eps = 1e-6;
        double t = -1.0;
        optimizer.setAbsoluteAccuracy(t);
        optimizer.optimize(f, goalType, min, max);
    }
}