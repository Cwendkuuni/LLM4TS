import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Assert;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveDefault() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction testFunction = new TestFunction();
        double[] bracketingValues = new double[]{1.0, 2.0, 3.0};
        double expectedResult = 2.0;
        double result = UnivariateRealSolverUtils.solve(testFunction, bracketingValues[0], bracketingValues[1]);
        Assert.assertEquals("solve method returned unexpected value", expectedResult, result, 0.001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws FunctionEvaluationException {
        UnivariateRealFunction testFunction = new FunctionWithEvaluationException();
        double[] bracketingValues = new double[]{1.0, 2.0, 3.0};
        UnivariateRealSolverUtils.solve(testFunction, bracketingValues[0], bracketingValues[1]);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() throws ConvergenceException {
        UnivariateRealFunction testFunction = new FunctionWithConvergenceException();
        double[] bracketingValues = new double[]{1.0, 2.0, 3.0};
        UnivariateRealSolverUtils.solve(testFunction, bracketingValues[0], bracketingValues[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveIllegalArgumentException() {
        UnivariateRealFunction testFunction = null;
        double[] bracketingValues = new double[]{1.0, 2.0, 3.0};
        try {
            UnivariateRealSolverUtils.solve(testFunction, bracketingValues[0], bracketingValues[1]);
        } catch (FunctionEvaluationException e) {
            // expected FunctionEvaluationException, not IllegalArgumentException
        } finally {
            Assert.assertNotNull("testFunction should not be null", testFunction);
            Assert.assertFalse("bracketingValues should not be null or empty", bracketingValues == null || bracketingValues.length < 3);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidBracketingValues() {
        UnivariateRealFunction testFunction = new TestFunction();
        double[] bracketingValues = new double[]{3.0, 2.0, 1.0};
        try {
            UnivariateRealSolverUtils.solve(testFunction, bracketingValues[0], bracketingValues[1]);
        } catch (FunctionEvaluationException e) {
            // expected FunctionEvaluationException, not IllegalArgumentException
        } finally {
            Assert.assertEquals("bracketingValues[0] should be less than bracketingValues[1]", bracketingValues[0], bracketingValues[1], 0.001);
        }
    }
}

private static class TestFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.sin(x) - Math.cos(x);
    }
}

private static class FunctionWithEvaluationException implements UnivariateRealFunction {
    @Override
    public double value(double x) throws FunctionEvaluationException {
        throw new FunctionEvaluationException("Test Evaluation Exception");
    }
}

private static class FunctionWithConvergenceException implements UnivariateRealFunction {
    @Override
    public double value(double x) throws ConvergenceException {
        throw new ConvergenceException("Test Convergence Exception");
    }
}