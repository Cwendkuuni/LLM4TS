import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.NonConvergentIterationException;
import org.apache.commons.math3.optim.UnboundedException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimum() throws MaxIterationsExceededException, UnboundedException, NonConvergentIterationException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, 0.0);

        assertEquals(-2.0, result.getPoint(), 0.01);
        assertEquals(0.0, result.getValue(), 0.01);
    }

    @Test
    public void testBrentOptimizerMaximum() throws MaxIterationsExceededException, UnboundedException, NonConvergentIterationException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(-x - 2, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, -5.0, 5.0, 0.0);

        assertEquals(2.0, result.getPoint(), 0.01);
        assertEquals(0.0, result.getValue(), 0.01);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBrentOptimizerMaxIterationsExceeded() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(x) + 1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(1);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBrentOptimizerFunctionEvaluationException() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Error evaluating function");
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInvalidParameters() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(-1);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
    }
}