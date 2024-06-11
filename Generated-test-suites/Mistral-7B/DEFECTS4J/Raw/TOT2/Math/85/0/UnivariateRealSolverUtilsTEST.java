import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.solvers.UnivariateRealSolver;
import org.apache.commons.math3.solvers.UnivariateRealSolverFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveWithDefaultSolver() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new SimpleFunction();
        double x0 = 1.0;
        double x1 = 2.0;
        double expectedResult = 1.5;
        double actualResult = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(expectedResult, actualResult, 1e-6);
    }

    @Test
    public void testSolveWithDefaultSolverAndAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new SimpleFunction();
        double x0 = 1.0;
        double x1 = 2.0;
        double absoluteAccuracy = 1e-8;
        double expectedResult = 1.5;
        double actualResult = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        assertEquals(expectedResult, actualResult, absoluteAccuracy);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithDefaultSolverThrowsConvergenceException() throws FunctionEvaluationException {
        UnivariateRealFunction f = new SimpleFunctionForConvergenceException();
        double x0 = 1.0;
        double x1 = 2.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithFunctionEvaluationException() {
        UnivariateRealFunction f = new FunctionWithEvaluationException();
        double x0 = 1.0;
        double x1 = 2.0;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithIllegalArgumentException() {
        UnivariateRealFunction f = null;
        double x0 = 1.0;
        double x1 = 2.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x1));
    }

    @Test
    public void testBracketMethodWithValidInput() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new SimpleFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        double[] expectedResult = {0.5, 1.0};
        double[] actualResult = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        assertEquals(expectedResult[0], actualResult[0], 1e-6);
        assertEquals(expectedResult[1], actualResult[1], 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketMethodWithConvergenceException() throws FunctionEvaluationException {
        UnivariateRealFunction f = new SimpleFunctionForConvergenceException();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracketMethodWithFunctionEvaluationException() {
        UnivariateRealFunction f = new FunctionWithEvaluationException();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketMethodWithIllegalArgumentException() {
        UnivariateRealFunction f = null;
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound));
    }

    @Test
    public void testBracketMethodWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new SimpleFunction();
        double initial = 1.0;
        double lowerBound = 2.0;
        double upperBound = 1.0;
        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testBracketMethodWithMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new SimpleFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 0;
        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testMidpointMethod() {
        double a = 2.0;
        double b = 3.0;
        double expectedResult = 2.5;
        double actualResult = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expectedResult, actualResult, 1e-6);
    }

    @Test
    public void testSetupMethod() throws FunctionEvaluationException {
        UnivariateRealFunction f = null;
        try {
            UnivariateRealSolverUtils.setup(f);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private static class SimpleFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 1.0;
        }
    }

    private static class SimpleFunctionForConvergenceException implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            throw new ConvergenceException();
        }
    }

    private static class FunctionWithEvaluationException implements UnivariateRealFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException();
        }
    }
}