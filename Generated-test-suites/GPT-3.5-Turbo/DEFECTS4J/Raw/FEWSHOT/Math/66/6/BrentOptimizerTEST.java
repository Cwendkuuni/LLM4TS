package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BrentOptimizerTest {
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinInvalidEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.localMin(true, null, GoalType.MINIMIZE, 0, 0, 0, -1, 1);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinInvalidT() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.localMin(true, null, GoalType.MINIMIZE, 0, 0, 0, 1, -1);
    }
    
    @Test
    public void testLocalMinGoalTypeMinimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.localMin(true, null, GoalType.MINIMIZE, 0, 1, 2, 0.001, 0.001);
        assertEquals(1.0, result, 0.0001);
    }
    
    @Test
    public void testLocalMinGoalTypeMaximize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.localMin(false, null, GoalType.MAXIMIZE, 0, 1, 2, 0.001, 0.001);
        assertEquals(1.0, result, 0.0001);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.doOptimize();
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeInvalidEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(null, GoalType.MINIMIZE, 0, 1, 2, -1, 1);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeInvalidT() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(null, GoalType.MINIMIZE, 0, 1, 2, 1, -1);
    }
    
    @Test
    public void testOptimizeGoalTypeMinimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(null, GoalType.MINIMIZE, 0, 1, 2, 0.001, 0.001);
        assertEquals(1.0, result, 0.0001);
    }
    
    @Test
    public void testOptimizeGoalTypeMaximize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(null, GoalType.MAXIMIZE, 0, 1, 2, 0.001, 0.001);
        assertEquals(1.0, result, 0.0001);
    }
    
}