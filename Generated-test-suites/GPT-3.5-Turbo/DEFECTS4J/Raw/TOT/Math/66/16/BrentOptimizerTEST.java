import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
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
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Typical Use Case
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, Math.PI);
        assertEquals(0.0, result, 0.001);

        // Typical Use Case, with maximum number of iterations
        f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
        assertEquals(0.0, result, 0.001);

        // Edge Case, smallest possible range
        f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x);
            }
        };
        result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 0);
        assertEquals(1.0, result, 0.001);

        // Error Scenario, function undefined in the given range
        f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1 / x;
            }
        };
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, -1, 1);
            fail("Expected FunctionEvaluationException to be thrown");
        } catch (FunctionEvaluationException e) {
            assertTrue(e.getMessage().contains("Function evaluation error"));
        }
    }
}