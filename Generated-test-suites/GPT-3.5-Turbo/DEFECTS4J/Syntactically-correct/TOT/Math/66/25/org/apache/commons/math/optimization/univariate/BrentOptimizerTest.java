import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;

import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {
    
    @Test
    public void testOptimize_StartValueProvided() throws MaxIterationsExceededException, FunctionEvaluationException {
        // given
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3; // f(x) = x^2 - 4x + 3
            }
        };
        double expectedOptimum = 2.0; // the function reaches its minimum at x = 2
        
        // when
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 5, 2.5);
        
        // then
        assertEquals(expectedOptimum, result, 1e-10);
    }
    
    @Test
    public void testOptimize_StartValueNotProvided() throws MaxIterationsExceededException, FunctionEvaluationException {
        // given
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3; // f(x) = x^2 - 4x + 3
            }
        };
        double expectedOptimum = 2.0; // the function reaches its minimum at x = 2
        
        // when
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 5);
        
        // then
        assertEquals(expectedOptimum, result, 1e-10);
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimize_MaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        // given
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3; // f(x) = x^2 - 4x + 3
            }
        };
        
        // when
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(10); // set small iteration count to trigger exception
        optimizer.optimize(f, GoalType.MINIMIZE, 0, 5);
        
        // then: MaxIterationsExceededException should be thrown
    }
    
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimize_FunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        // given
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        
        // when
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.MINIMIZE, 0, 5);
        
        // then: FunctionEvaluationException should be thrown
    }
}