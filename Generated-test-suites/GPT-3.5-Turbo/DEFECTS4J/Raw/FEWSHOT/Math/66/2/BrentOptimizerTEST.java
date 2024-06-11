package org.apache.commons.math.optimization.univariate;

import org.junit.Test;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;

public class BrentOptimizerTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithNegativeEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * 2;
            }
        };
        optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 2, 4, -1, 0.1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithNegativeT() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * 2;
            }
        };
        optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 2, 4, 0.1, -1);
    }
    
    @Test
    public void testLocalMinCase1() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * 2;
            }
        };
        double result = optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 2, 4, 0.1, 0.1);
        assertEquals(0.0, result, 0);
    }

    @Test
    public void testLocalMinCase2() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2) - x - 6;
            }
        };
        double result = optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 2, 4, 0.1, 0.1);
        assertEquals(2.0, result, 0);
    }

    @Test
    public void testOptimizeCase1() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * 2;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 4);
        assertEquals(0.0, result, 0);
    }

    @Test
    public void testOptimizeCase2() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2) - x - 6;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 4);
        assertEquals(2.0, result, 0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeWithMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sinh(x);
            }
        };
        optimizer.setMaximalIterationCount(10);
        optimizer.optimize(f, GoalType.MINIMIZE, 0, 4);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeWithFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        optimizer.optimize(f, GoalType.MINIMIZE, 0, 2);
    }
}