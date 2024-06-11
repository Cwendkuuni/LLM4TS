import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Assert;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve_FunctionWithRoot() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = f -> Math.pow(f, 2) - 1;
        double x0 = 1.0;
        double x1 = 0.0;
        double expectedResult = 0.0;

        double result = UnivariateRealSolverUtils.solve(function, x0, x1);

        Assert.assertEquals(expectedResult, result, 1e-6);
    }

    @Test
    public void testSolve_FunctionWithoutRoot() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = f -> Math.pow(f, 2) + 1;
        double x0 = 1.0;
        double x1 = 0.0;

        try {
            UnivariateRealSolverUtils.solve(function, x0, x1);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    @Test
    public void testSolve_FunctionWithRoot_DifferentSolver() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = f -> Math.pow(f, 2) - 1;
        double x0 = 1.0;
        double x1 = 0.0;

        UnivariateRealSolver solver = UnivariateRealSolverFactory.newBrentSolver();

        double result = UnivariateRealSolverUtils.solve(function, x0, x1, solver);

        double expectedResult = 0.0;

        Assert.assertEquals(expectedResult, result, 1e-6);
    }

    @Test
    public void testSolve_FunctionWithoutRoot_DifferentSolver() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = f -> Math.pow(f, 2) + 1;
        double x0 = 1.0;
        double x1 = 0.0;

        UnivariateRealSolver solver = UnivariateRealSolverFactory.newBrentSolver();

        try {
            UnivariateRealSolverUtils.solve(function, x0, x1, solver);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    @Test
    public void testBracket_FunctionWithRoot() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = f -> Math.pow(f, 2) - 1;
        double initial = 1.5;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        double[] expectedResult = {1.0, 2.0};

        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);

        Assert.assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testBracket_FunctionWithoutRoot() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = f -> Math.pow(f, 2) + 1;
        double initial = 1.5;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        try {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    @Test
    public void testBracket_FunctionWithRoot_DifferentMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = f -> Math.pow(f, 2) - 1;
        double initial = 1.5;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maxIterations = 20;

        double[] expectedResult = {1.0, 2.0};

        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maxIterations);

        Assert.assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testBracket_FunctionWithoutRoot_DifferentMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = f -> Math.pow(f, 2) + 1;
        double initial = 1.5;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maxIterations = 20;

        try {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maxIterations);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }
}