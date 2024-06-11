import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeDefinition;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        // Test with default constructor
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);

        // Test with custom constructor
        BrentOptimizer optimizerCustom = new BrentOptimizer(0.1, 0.01, new MyConvergenceChecker());

        // Test minimize function
        UnivariateOptimizer result = optimizer.optimize(new MyFunction(), new InitialGuess(1.0), GoalType.MINIMIZE);
        PointValuePair expectedResult = new PointValuePair(1.5, 2.25);
        assertEquals(expectedResult.getPoint(), result.getPoint(), 0.001);
        assertEquals(expectedResult.getValue(), result.getValue(), 0.001);

        // Test maximize function
        result = optimizer.optimize(new MyFunction(), new InitialGuess(1.0), GoalType.MAXIMIZE);
        PointValuePair expectedResultMax = new PointValuePair(2.5, 1.25);
        assertEquals(expectedResultMax.getPoint(), result.getPoint(), 0.001);
        assertEquals(expectedResultMax.getValue(), result.getValue(), 0.001);

        // Test with custom convergence checker
        result = optimizerCustom.optimize(new MyFunction(), new InitialGuess(1.0), GoalType.MINIMIZE);
        assertEquals(expectedResult.getPoint(), result.getPoint(), 0.001);
        assertEquals(expectedResult.getValue(), result.getValue(), 0.001);

        // Test with null function
        try {
            optimizer.optimize(null, new InitialGuess(1.0), GoalType.MINIMIZE);
            fail("Expected IllegalStateException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with null initial guess
        try {
            optimizer.optimize(new MyFunction(), null, GoalType.MINIMIZE);
            fail("Expected IllegalStateException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with null goal type
        try {
            optimizer.optimize(new MyFunction(), new InitialGuess(1.0), null);
            fail("Expected IllegalStateException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test negative relative threshold
        try {
            new BrentOptimizer(-0.1, 0.01);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        // Test negative absolute threshold
        try {
            new BrentOptimizer(0.1, -0.01);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test zero absolute threshold
        try {
            new BrentOptimizer(0.1, 0.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test zero relative threshold
        try {
            new BrentOptimizer(0.0, 0.01);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        // Test convergence checking
        optimizerCustom = new BrentOptimizer(0.1, 0.01, new MyConvergenceCheckerWithError());
        result = optimizerCustom.optimize(new MyFunctionWithError(), new InitialGuess(1.0), GoalType.MINIMIZE);
        assertEquals(expectedResult.getPoint(), result.getPoint(), 0.001);
        assertEquals(expectedResult.getValue(), result.getValue(), 0.001);
    }

    private static class MyFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x * x;
        }
    }

    private static class MyFunctionWithError implements UnivariateFunction {
        @Override
        public double value(double x) {
            if (x > 2.0) {
                throw new ArithmeticException("Divide by zero");
            }
            return x * x;
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            return previous == null || (FastMath.abs(previous.getValue() - current.getValue()) < 0.01);
        }
    }

    private static class MyConvergenceCheckerWithError implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            return previous == null || (FastMath.abs(previous.getValue() - current.getValue()) < 0.01 || iteration > 100);
        }
    }
}