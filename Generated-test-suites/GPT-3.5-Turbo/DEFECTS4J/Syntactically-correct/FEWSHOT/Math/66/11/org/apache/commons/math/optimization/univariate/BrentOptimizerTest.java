package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class BrentOptimizerTest {
    
    @Test
    public void testDoOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.doOptimize();
        assertEquals(0, result, 0);
    }
    
    @Test
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 10;
        double startValue = 0;
        double result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(0, result, 0);
    }
    
    @Test
    public void testOptimize2() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 10;
        double result = optimizer.optimize(f, goalType, min, max);
        assertEquals(0, result, 0);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMin() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double lo = -10;
        double mid = 0;
        double hi = 10;
        double eps = -1;
        double t = 1;
        optimizer.localMin(true, f, goalType, lo, mid, hi, eps, t);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMin2() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double lo = -10;
        double mid = 0;
        double hi = 10;
        double eps = 1;
        double t = -1;
        optimizer.localMin(true, f, goalType, lo, mid, hi, eps, t);
    }
}