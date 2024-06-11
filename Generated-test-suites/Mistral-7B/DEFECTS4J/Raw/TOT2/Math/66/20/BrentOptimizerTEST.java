import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMin() throws NotStrictlyPositiveException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
        assertEquals(-3.141592653589793, result.getPoint(), 1e-6);
        assertEquals(1.536258152349826e-7, result.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerMax() throws NotStrictlyPositiveException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, -5.0, 5.0, 0.0);
        assertEquals(3.141592653589793, result.getPoint(), 1e-6);
        assertEquals(-1.536258152349826e-7, result.getValue(), 1e-7);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBrentOptimizerMaxIterationsExceeded() throws NotStrictlyPositiveException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(0);
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNotStrictlyPositive() throws NotStrictlyPositiveException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(-1);
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
    }

    @Test
    public void testBrentOptimizerConvergence() throws NotStrictlyPositiveException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(2);
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
        // Since the optimization has a convergence rate slower than the number of iterations, the result should be close to the minimum but not equal to it.
        assertTrue(Math.abs(result.getPoint() - (-3.141592653589793)) < 1.0);
        assertTrue(Math.abs(result.getValue() - (1.536258152349826e-7)) < 1e-5);
    }
}