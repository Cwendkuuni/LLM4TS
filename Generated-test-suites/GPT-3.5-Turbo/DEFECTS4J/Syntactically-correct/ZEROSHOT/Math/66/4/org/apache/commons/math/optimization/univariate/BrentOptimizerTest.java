import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BrentOptimizerTest {
    
    @Test
    public void testOptimizeWithStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x - 1, 2);
            }
        };
        
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 2, 1);
        
        assertEquals(1, result, 0);
    }

    @Test
    public void testOptimizeWithoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x - 1, 2);
            }
        };
        
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 2);
        
        assertEquals(1, result, 0);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithNegativeRelativeAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x - 1, 2);
            }
        };
        
        optimizer.setRelativeAccuracy(-0.01);
        optimizer.optimize(function, GoalType.MINIMIZE, 0, 2);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithNegativeAbsoluteAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x - 1, 2);
            }
        };
        
        optimizer.setAbsoluteAccuracy(-0.01);
        optimizer.optimize(function, GoalType.MINIMIZE, 0, 2);
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeWithMaximalIterationCountExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x - 1, 2);
            }
        };
        
        optimizer.setMaximalIterationCount(10);
        optimizer.optimize(function, GoalType.MINIMIZE, 0, 2);
    }
}