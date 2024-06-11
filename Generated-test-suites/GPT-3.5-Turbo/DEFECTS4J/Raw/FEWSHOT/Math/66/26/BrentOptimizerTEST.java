import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.optimization.univariate.AbstractUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.GoalType;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    @Test
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - x - 1;
            }
        };
        
        double tol = AbstractUnivariateRealOptimizer.DEFAULT_ABSOLUTE_ACCURACY;
        
        // Test minimizing the function
        double min = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 2.0);
        assertEquals(1.0, min, tol);
        
        // Test maximizing the function
        double max = optimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 2.0);
        assertEquals(0.0, max, tol);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidEpsilon() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - x - 1;
            }
        };
        
        double tol = -1.0; // Invalid epsilon value
        
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 2.0, tol);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidTolerance() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - x - 1;
            }
        };
        
        double tol = -1.0; // Invalid tolerance value
        
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 2.0, tol);
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1); // Set maximum iteration count to 1
        
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - x - 1;
            }
        };
        
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 2.0);
    }
}