import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class UnivariateRealSolverUtilsTest {
    // Test for solve method with default solver and valid inputs
    @Test
    public void testSolveWithDefaultSolverAndValidInputs() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction function = new SimpleFunction();
        double x0 = 1.0;
        double x1 = 2.0;
        double expectedResult = 1.5;
        double result = UnivariateRealSolverUtils.solve(function, x0, x1);
        assertEquals(expectedResult, result, 0.001);
    }

    // Test for solve method with custom solver, valid inputs, and absolute accuracy
    @Test
    public void testSolveWithCustomSolverAndValidInputsAndAbsoluteAccuracy() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction function = new SimpleFunction();
        double x0 = 1.0;
        double x1 = 2.0;
        double absoluteAccuracy = 0.001;
        double expectedResult = 1.5;
        double result = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
        assertEquals(expectedResult, result, absoluteAccuracy);
    }

    // Test for solve method with null function
    @Test(expected = MathRuntimeException.class)
    public void testSolveWithNullFunction() {
        UnivariateRealFunction function = null;
        double x0 = 1.0;
        double x1 = 2.0;
        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    // Test for solve method with invalid bounds
    @Test(expected = MathRuntimeException.class)
    public void testSolveWithInvalidBounds() {
        UnivariateRealFunction function = new SimpleFunction();
        double x0 = 3.0;
        double x1 = 1.0;
        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    // Test for bracket method with valid inputs
    @Test
    public void testBracketWithValidInputs() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction function = new SimpleFunction();
        double initial = 1.5;
        double lowerBound = 0.0;
        double upperBound = 3.0;
        double[] expectedResult = {1.0, 2.0};
        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        assertArrayEquals(expectedResult, result);
    }

    // Test for bracket method with function that is not continuous in the interval
    @Test(expected = ConvergenceException.class)
    public void testBracketWithFunctionNotContinuousInTheInterval() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction function = new DiscontinuousFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 3.0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    // Test for bracket method with function that does not have a root in the interval
    @Test(expected = ConvergenceException.class)
    public void testBracketWithFunctionNoRootInTheInterval() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction function = new NoRootFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 3.0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    // Test for bracket method with null function
    @Test(expected = MathRuntimeException.class)
    public void testBracketWithNullFunction() {
        UnivariateRealFunction function = null;
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 3.0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    // Test for bracket method with invalid bounds
    @Test(expected = MathRuntimeException.class)
    public void testBracketWithInvalidBounds() {
        UnivariateRealFunction function = new SimpleFunction();
        double initial = 1.0;
        double lowerBound = 3.0;
        double upperBound = 1.0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    // Test for midpoint method with valid inputs
    @Test
    public void testMidpointWithValidInputs() {
        double a = 1.0;
        double b = 2.0;
        double expectedResult = 1.5;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expectedResult, result, 0.001);
    }

    // Test for setup method with null function
    @Test(expected = MathRuntimeException.class)
    public void testSetupWithNullFunction() {
        UnivariateRealFunction function = null;
        UnivariateRealSolverUtils.setup(function);
    }

    private static class SimpleFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x * x - 1.0;
        }
    }

    private static class DiscontinuousFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            if (x > 1.0) {
                throw new ConvergenceException("Function is discontinuous at x = " + x);
            }
            return Math.sin(x);
        }
    }

    private static class NoRootFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 4.0;
        }
    }
}