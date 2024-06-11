import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10);
        assertEquals(0, result, 1E-10);

        result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
        assertEquals(-10, result, 1E-10);

        result = optimizer.optimize(f, GoalType.MINIMIZE, -10, -1);
        assertEquals(-10, result, 1E-10);

        result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 0);
        assertEquals(0, result, 1E-10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithInvalidEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        optimizer.optimize(f, GoalType.MINIMIZE, 0, 10, 1E-15, -1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithInvalidT() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        optimizer.optimize(f, GoalType.MINIMIZE, 0, 10, -1, 1E-15);
    }

    @Test
    public void testLocalMin() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };

        double result = optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 5, 10, 1E-10, 1E-15);
        assertEquals(0, result, 1E-10);

        result = optimizer.localMin(false, f, GoalType.MAXIMIZE, 0, 5, 10, 1E-10, 1E-15);
        assertEquals(10, result, 1E-10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithInvalidEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 5, 10, -1, 1E-15);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithInvalidT() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 5, 10, 1E-10, -1);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testLocalMinWithMaxIterations() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 5, 10, 1E-10, 1E-15);
    }

    @Test
    public void testDoOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        try {
            optimizer.doOptimize();
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}