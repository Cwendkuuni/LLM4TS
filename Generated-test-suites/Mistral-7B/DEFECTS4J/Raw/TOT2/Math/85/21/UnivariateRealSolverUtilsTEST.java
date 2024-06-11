import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new MockFunction();
        double root = UnivariateRealSolverUtils.solve(function, 0.0, 1.0);
        assertEquals("Expected root to be approximately 0.5", 0.5, root, 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() throws FunctionEvaluationException {
        UnivariateRealFunction function = new MockFunctionWithNoRoots();
        UnivariateRealSolverUtils.solve(function, 0.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() {
        UnivariateRealFunction function = new MockFunctionWithEvaluationException();
        try {
            UnivariateRealSolverUtils.solve(function, 0.0, 1.0);
        } catch (ConvergenceException ce) {
            throw new RuntimeException("Unexpected ConvergenceException", ce);
        }
        fail("Expected FunctionEvaluationException");
    }

    @Test
    public void testSolveIllegalArgumentException() {
        UnivariateRealFunction function = null;
        try {
            UnivariateRealSolverUtils.solve(function, 0.0, 1.0);
        } catch (IllegalArgumentException iae) {
            assertEquals("Expected IllegalArgumentException message",
                    "function is null", iae.getMessage());
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testSolveInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new MockFunction();
        try {
            UnivariateRealSolverUtils.solve(function, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        } catch (IllegalArgumentException iae) {
            assertEquals("Expected IllegalArgumentException message",
                    "initial is not between lowerBound and upperBound", iae.getMessage());
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new MockFunction();
        double[] bracketingValues = UnivariateRealSolverUtils.bracket(function, 0.5, 0.0, 1.0);
        assertEquals("Expected bracketing values to be (0.3, 0.6)", 0.3, bracketingValues[0], 0.001);
        assertEquals("Expected bracketing values to be (0.3, 0.6)", 0.6, bracketingValues[1], 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketConvergenceException() throws FunctionEvaluationException {
        UnivariateRealFunction function = new MockFunctionWithNoRoots();
        UnivariateRealSolverUtils.bracket(function, 0.0, 1.0, 1.0, 10);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracketFunctionEvaluationException() throws FunctionEvaluationException {
        UnivariateRealFunction function = new MockFunctionWithEvaluationException();
        try {
            UnivariateRealSolverUtils.bracket(function, 0.0, 1.0, 1.0, 10);
        } catch (ConvergenceException ce) {
            throw new RuntimeException("Unexpected ConvergenceException", ce);
        }
        fail("Expected FunctionEvaluationException");
    }

    @Test
    public void testBracketIllegalArgumentException() {
        UnivariableRealFunction function = null;
        try {
            UnivariateRealSolverUtils.bracket(function, 0.0, 1.0, 1.0, 10);
        } catch (IllegalArgumentException iae) {
            assertEquals("Expected IllegalArgumentException message",
                    "function is null", iae.getMessage());
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testBracketInvalidParameters() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new MockFunction();
        try {
            UnivariateRealSolverUtils.bracket(function, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 0.0, 10);
        } catch (IllegalArgumentException iae) {
            assertEquals("Expected IllegalArgumentException message",
                    "lower bound is not less than upper bound", iae.getMessage());
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    private static class MockFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x * x - 1.0;
        }
    }

    private static class MockFunctionWithNoRoots implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.sin(Math.PI * x);
        }
    }

    private static class MockFunctionWithEvaluationException implements UnivariateRealFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException();
        }
    }
}