import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testMinimizationWithBoundedInterval() throws Exception {
        UnivariateRealFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double min = optimizer.optimize(function, GoalType.MINIMIZE, 0, 5).getPoint();
        assertEquals(2.5, min, 0.001);
    }

    @Test
    public void testMinimizationWithStartValue() throws Exception {
        UnivariateRealFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double min = optimizer.optimize(function, GoalType.MINIMIZE, -5, 5, InitialGuess.ZERO).getPoint();
        assertEquals(-1.2732406948976168E-16, min, 1e-9);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidAbsoluteAccuracy() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(-1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidRelativeAccuracy() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setRelativeAccuracy(-1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testMaxIterationsExceeded() throws Exception {
        UnivariateRealFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.optimize(function, GoalType.MINIMIZE, 0, 1, 0.5);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededWithDefaultMaxIterations() throws Exception {
        UnivariateRealFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(function, GoalType.MINIMIZE, 0, 1, 0.5);
    }

    @Test
    public void testMinimizationWithDifferentGoalTypes() throws Exception {
        UnivariateRealFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair resultMin = optimizer.optimize(function, GoalType.MINIMIZE, 0, 5);
        PointValuePair resultMax = optimizer.optimize(function, GoalType.MAXIMIZE, 0, 5);

        assertEquals(2.5, resultMin.getPoint(), 0.001);
        assertEquals(-2.5, resultMax.getPoint(), 0.001);
    }

    @Test
    public void testMinimizationWithMaxEval() throws Exception {
        UnivariateRealFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) * Math.sin(Math.PI * x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEval(new MaxEval(5));
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 10);
        assertEquals(-1.5643224561958023, result.getPoint(), 0.01);
    }
}