package org.apache.commons.math.optimization.univariate.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.InitialGuess;

import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.optimization.univariate.GoalType;

public class BrentOptimizerTest {

    @org.junit.Test
    public void testBrentOptimizer_minimize() throws Exception {
        UnivariateFunction f = new MyFunction();
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair solution = optimizer.optimize(f, GoalType.MINIMIZE, new SimpleBounds(1.0, 4.0), InitialGuess.THE_ORIGIN);
        assertEquals(2.0, solution.getPoint(), 1e-10);
    }

    @org.junit.Test
    public void testBrentOptimizer_maximize() throws Exception {
        UnivariateFunction f = new MyFunction();
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair solution = optimizer.optimize(f, GoalType.MAXIMIZE, new SimpleBounds(1.0, 4.0), InitialGuess.THE_ORIGIN);
        assertEquals(3.0, solution.getPoint(), 1e-10);
    }

    @org.junit.Test
    public void testBrentOptimizer_minimize_boundary() throws Exception {
        UnivariateFunction f = new MyFunction();
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair solution = optimizer.optimize(f, GoalType.MINIMIZE, new SimpleBounds(0.0, 1.0), InitialGuess.THE_ORIGIN);
        assertEquals(0.0, solution.getPoint(), 1e-10);
    }

    @org.junit.Test
    public void testBrentOptimizer_maximize_boundary() throws Exception {
        UnivariateFunction f = new MyFunction();
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair solution = optimizer.optimize(f, GoalType.MAXIMIZE, new SimpleBounds(0.0, 1.0), InitialGuess.THE_ORIGIN);
        assertEquals(1.0, solution.getPoint(), 1e-10);
    }

    @org.junit.Test
    public void testBrentOptimizer_functionEvaluationException() throws Exception {
        UnivariateFunction f = new MyFunctionWithException();
        UnivariateOptimizer optimizer = new BrentOptimizer();
        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, new SimpleBounds(1.0, 4.0), InitialGuess.THE_ORIGIN));
    }

    @org.junit.Test
    public void testBrentOptimizer_maxIterationsExceededException() throws Exception {
        UnivariateFunction f = new MyFunction();
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(1);
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, new SimpleBounds(1.0, 4.0), InitialGuess.THE_ORIGIN));
    }

    @org.junit.Test
    public void testBrentOptimizer_notStrictlyPositiveException() throws Exception {
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(-1);
        assertThrows(NotStrictlyPositiveException.class, optimizer::optimize);
    }

    private static class MyFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x - 2.0, 2.0);
        }
    }

    private static class MyFunctionWithException implements UnivariateFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            if (x == 2.0) {
                throw new FunctionEvaluationException("Exception occurred");
            }
            return Math.pow(x - 2.0, 2.0);
        }
    }

}