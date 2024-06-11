import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testOptimize_startValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x; // Dummy function for testing
            }
        };
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        double expected = 0.0; // Minimum value of x^2 is 0
        double result = optimizer.optimize(f, GoalType.MINIMIZE, min, max, startValue);
        assertEquals(expected, result, 1E-10);
    }

    @Test
    public void testOptimize_noStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x; // Dummy function for testing
            }
        };
        double min = 0.0;
        double max = 10.0;
        double expected = 0.0; // Minimum value of x^2 is 0
        double result = optimizer.optimize(f, GoalType.MINIMIZE, min, max);
        assertEquals(expected, result, 1E-10);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimize_maxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x; // Dummy function for testing
            }
        };
        double min = 0.0;
        double max = 10.0;
        double startValue = 11.0;
        optimizer.optimize(f, GoalType.MINIMIZE, min, max, startValue);
    }

}