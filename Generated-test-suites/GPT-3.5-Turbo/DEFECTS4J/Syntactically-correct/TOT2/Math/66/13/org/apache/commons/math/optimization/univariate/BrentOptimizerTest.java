import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNotNull(optimizer);
    }

    @Test
    public void testOptimizeWithStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 1, 3, 1.5);
        assertEquals(2.0, result, 1e-10);
    }

    @Test
    public void testOptimizeWithoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 1, 3);
        assertEquals(2.0, result, 1e-10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithNegativeEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        optimizer.optimize(f, GoalType.MINIMIZE, 1, 3, 2, -1, 0.1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithNegativeT() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        optimizer.optimize(f, GoalType.MINIMIZE, 1, 3, 2, 0.1, -1);
    }

    @Test
    public void testLocalMinWithSmallInterval() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = optimizer.localMin(true, f, GoalType.MINIMIZE, 1, 1.5, 1.7, 0.1, 0.01);
        assertEquals(1.0, result, 1e-10);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testLocalMinMaxIterations() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        optimizer.localMin(true, f, GoalType.MINIMIZE, 1, 3, 2, 0.1, 0.01);
    }

}