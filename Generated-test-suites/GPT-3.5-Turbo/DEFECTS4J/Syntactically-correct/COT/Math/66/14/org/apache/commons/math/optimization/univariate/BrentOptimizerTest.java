import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeWithStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x + 2 * x + 1;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 0);
        assertEquals(result, -1, 1e-10);
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x + 2 * x + 1;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
        assertEquals(result, -1, 1e-10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeInvalidEps() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(null, GoalType.MINIMIZE, -10, 10, 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeInvalidT() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(null, GoalType.MINIMIZE, -10, 10, 0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeMaxIterationsExceededException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x + 2 * x + 1;
            }
        };
        optimizer.setMaximalIterationCount(1);
        optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
    }

}