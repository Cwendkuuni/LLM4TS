import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            double result = optimizer.doOptimize();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Exception expected
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        double expected = 2.0;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 2.0);
            assertEquals(expected, result, 0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
            assertTrue(result >= -10 && result <= 10);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithInvalidEps() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        optimizer.localMin(true, f, GoalType.MINIMIZE, -5, 0, 5, -1, 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithInvalidT() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        optimizer.localMin(true, f, GoalType.MINIMIZE, -5, 0, 5, 0.01, -1);
    }

    @Test
    public void testLocalMinWithMinimizeGoalType() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        double result = optimizer.localMin(true, f, GoalType.MINIMIZE, -5, 0, 5, 0.01, 0.001);
        assertTrue(result >= -5 && result <= 5);
    }

    @Test
    public void testLocalMinWithMaximizeGoalType() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        double result = optimizer.localMin(false, f, GoalType.MAXIMIZE, -5, 0, 5, 0.01, 0.001);
        assertTrue(result >= -5 && result <= 5);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testLocalMinWithMaxIterationsExceeded() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        optimizer.maximalIterationCount = 1;
        optimizer.localMin(true, f, GoalType.MINIMIZE, -5, 0, 5, 0.01, 0.001);
    }

}