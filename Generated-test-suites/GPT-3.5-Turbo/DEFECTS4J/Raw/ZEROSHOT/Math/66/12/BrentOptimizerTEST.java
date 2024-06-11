import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }
    
    @Test
    public void testOptimize_withStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3; // Example function: (x-1)(x-3)
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 4, 1);
        assertEquals(1.0, result, 0.001);
    }
    
    @Test
    public void testOptimize_withoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3; // Example function: (x-1)(x-3)
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 4);
        assertEquals(1.0, result, 0.001);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_negativeRelativeAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3; // Example function: (x-1)(x-3)
            }
        };
        optimizer.setRelativeAccuracy(-0.001);
        optimizer.optimize(f, GoalType.MINIMIZE, 0, 4);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_negativeAbsoluteAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3; // Example function: (x-1)(x-3)
            }
        };
        optimizer.setAbsoluteAccuracy(-0.001);
        optimizer.optimize(f, GoalType.MINIMIZE, 0, 4);
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimize_maxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3; // Example function: (x-1)(x-3)
            }
        };
        optimizer.setMaximalIterationCount(5);
        optimizer.optimize(f, GoalType.MINIMIZE, 0, 4);
    }
    
}