package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }
    
    @Test
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 2 * x + 1;
            }
        };
        double min = 0;
        double max = 10;
        double startValue = 1;
        
        double result = optimizer.optimize(f, GoalType.MINIMIZE, min, max, startValue);
        
        assertEquals(result, -0.5, 0.0001);
    }
    
    @Test
    public void testOptimize_withZeroMaxIterations() throws MaxIterationsExceededException, FunctionEvaluationException {
        optimizer.setMaximalIterationCount(0);
        
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        double min = 0;
        double max = 10;
        
        double result = optimizer.optimize(f, GoalType.MINIMIZE, min, max);
        
        assertEquals(result, 0, 0.0001);
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimize_withUnreachableTolerance() throws MaxIterationsExceededException, FunctionEvaluationException {
        optimizer.setRelativeAccuracy(0);
        
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        double min = 0;
        double max = 10;
        
        optimizer.optimize(f, GoalType.MINIMIZE, min, max);
    }
    
    @Test
    public void testOptimize_withMaxIterationsReached() throws MaxIterationsExceededException, FunctionEvaluationException {
        optimizer.setMaximalIterationCount(5);
        
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        double min = 0;
        double max = 10;
        
        double result = optimizer.optimize(f, GoalType.MINIMIZE, min, max);
        
        assertTrue(result >= min);
        assertTrue(result <= max);
    }
}