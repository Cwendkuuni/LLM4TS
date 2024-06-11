import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new FunctionSquareRoot();
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 5.0, GoalType.MINIMIZE);
        double expectedResult = 1.0;
        assertEquals(expectedResult, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMinimizationWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
        UnivariateFunction function = new FunctionSquareRoot();
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 5.0, GoalType.MINIMIZE);
        double expectedResult = 1.0;
        assertEquals(expectedResult, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new FunctionCubic();
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 5.0, GoalType.MAXIMIZE);
        double expectedResult = 2.0;
        assertEquals(expectedResult, result.getPoint(), 1e-6);
        assertEquals(8.0, result.getValue(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInvalidAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerInvalidRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerInvalidMinValue() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new FunctionInvalidMin();
        optimizer.optimize(function, Double.NEGATIVE_INFINITY, 5.0, GoalType.MINIMIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerInvalidMaxValue() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new FunctionInvalidMax();
        optimizer.optimize(function, 0.0, Double.POSITIVE_INFINITY, GoalType.MINIMIZE);
    }

    @Test
    public void testBrentOptimizerMinimizationWithInvalidStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new FunctionSquareRoot();
        UnivariatePointValuePair result = optimizer.optimize(function, Double.NaN, 5.0, GoalType.MINIMIZE);
        assertNull(result);
    }

    @Test
    public void testBrentOptimizerMaximizationWithInvalidStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new FunctionCubic();
        UnivariatePointValuePair result = optimizer.optimize(function, Double.NaN, 5.0, GoalType.MAXIMIZE);
        assertNull(result);
    }

    @Test
    public void testBrentOptimizerMinimizationWithEmptyConstraintSet() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new FunctionSquareRoot();
        Constraint[] constraints = new Constraint[0];
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 5.0, GoalType.MINIMIZE, constraints);
        double expectedResult = 1.0;
        assertEquals(expectedResult, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximizationWithEmptyConstraintSet() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new FunctionCubic();
        Constraint[] constraints = new Constraint[0];
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 5.0, GoalType.MAXIMIZE, constraints);
        double expectedResult = 2.0;
        assertEquals(expectedResult, result.getPoint(), 1e-6);
        assertEquals(8.0, result.getValue(), 1e-6);
    }

    private static class FunctionSquareRoot implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.sqrt(x);
        }
    }

    private static class FunctionCubic implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 3);
        }
    }

    private static class FunctionInvalidMin implements UnivariateFunction {
        @Override
        public double value(double x) {
            throw new IllegalArgumentException("Invalid minimum: " + x);
        }
    }

    private static class FunctionInvalidMax implements UnivariateFunction {
        @Override
        public double value(double x) {
            throw new IllegalArgumentException("Invalid maximum: " + x);
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return (iteration >= 10) || (Math.abs(previous.getValue() - current.getValue()) < 1e-6);
        }
    }
}