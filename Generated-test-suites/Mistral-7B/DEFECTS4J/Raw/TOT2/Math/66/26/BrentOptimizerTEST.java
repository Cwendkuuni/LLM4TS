import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeWeightedSum;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMinimize;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMaximize;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testMinimization() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x * x + 1;
            }
        };

        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, new GoalTypeMinimize(), -5.0, 5.0, 0.0);

        assertEquals(1.0, result.getPoint(), 1e-6);
        assertEquals(-2.0, result.getValue(), 1e-6);
    }

    @Test
    public void testMaximization() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) + 2 * x * x + 1;
            }
        };

        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, new GoalTypeMaximize(), -5.0, 5.0, 0.0);

        assertEquals(3.0, result.getPoint(), 1e-6);
        assertEquals(10.0, result.getValue(), 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.optimize(function, new GoalTypeMinimize(), 0.0, 10.0, 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new ArithmeticException("Division by zero");
            }
        };

        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(function, new GoalTypeMinimize(), 0.0, 1.0, 0.0);
    }

    @Test
    public void testWeightedSumGoalType() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = new GoalTypeWeightedSum(0.5, new GoalTypeMinimize(), new GoalTypeMaximize());
        PointValuePair result = optimizer.optimize(function, goalType, -5.0, 5.0, 0.0);

        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }
}