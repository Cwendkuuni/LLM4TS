package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Test;

public class BrentOptimizerTest {
    private double delta = 1e-7;

    @Test
    public void testDoOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.doOptimize();
            fail("MaxIterationsExceededException not thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testOptimizeWithStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();

        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double result = optimizer.optimize(f, GoalType.MINIMIZE, 1.0, 3.0, 2.0);

        assertEquals(2.0, result, delta);
    }

    @Test
    public void testOptimizeWithoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();

        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double result = optimizer.optimize(f, GoalType.MINIMIZE, 1.0, 3.0);

        assertEquals(2.0, result, delta);
    }

    @Test(expected=NotStrictlyPositiveException.class)
    public void testOptimizeWithNegativeRelativeAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setRelativeAccuracy(-1.0);
        optimizer.optimize(null, GoalType.MINIMIZE, 1.0, 3.0);
    }

    @Test(expected=NotStrictlyPositiveException.class)
    public void testOptimizeWithZeroRelativeAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setRelativeAccuracy(0.0);
        optimizer.optimize(null, GoalType.MINIMIZE, 1.0, 3.0);
    }
    
    @Test(expected=NotStrictlyPositiveException.class)
    public void testOptimizeWithNegativeAbsoluteAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(-1.0);
        optimizer.optimize(null, GoalType.MINIMIZE, 1.0, 3.0);
    }

    @Test(expected=NotStrictlyPositiveException.class)
    public void testOptimizeWithZeroAbsoluteAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(0.0);
        optimizer.optimize(null, GoalType.MINIMIZE, 1.0, 3.0);
    }
    
    @Test(expected=MaxIterationsExceededException.class)
    public void testOptimizeWithMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1);
        
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };

        optimizer.optimize(f, GoalType.MINIMIZE, 1.0, 3.0);
    }
}