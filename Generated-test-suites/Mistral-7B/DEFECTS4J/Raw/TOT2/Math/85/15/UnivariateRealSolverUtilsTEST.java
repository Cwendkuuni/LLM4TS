import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Assert;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    @Test
    public void testSolveWithValidInput() {
        UnivariateRealFunction testFunction = new TestFunction();
        double x0 = 1.0;
        double x1 = 2.0;
        double expectedResult = 1.5;

        try {
            double result = UnivariateRealSolverUtils.solve(testFunction, x0, x1);
            Assert.assertEquals(expectedResult, result, 0.001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateRealFunction testFunction = new TestFunction();
        double x0 = 1.0;
        double x1 = 2.0;
        double absoluteAccuracy = 0.001;
        double expectedResult = 1.5;

        try {
            double result = UnivariateRealSolverUtils.solve(testFunction, x0, x1, absoluteAccuracy);
            Assert.assertEquals(expectedResult, result, absoluteAccuracy);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithConvergenceException() {
        UnivariateRealFunction testFunction = new TestFunction();
        double x0 = 1.0;
        double x1 = 1.0;

        try {
            UnivariateRealSolverUtils.solve(testFunction, x0, x1);
            Assert.fail("ConvergenceException expected but not thrown");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithFunctionEvaluationException() {
        UnivariateRealFunction testFunction = new FunctionWithException();
        double x0 = 1.0;
        double x1 = 2.0;

        try {
            UnivariateRealSolverUtils.solve(testFunction, x0, x1);
            Assert.fail("FunctionEvaluationException expected but not thrown");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testBracketWithValidInput() {
        UnivariateRealFunction testFunction = new TestFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        try {
            double[] result = UnivariateRealSolverUtils.bracket(testFunction, initial, lowerBound, upperBound);
            double[] expectedResult = new double[]{0.5, 1.5};
            Assert.assertArrayEquals(expectedResult, result, 0.001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithConvergenceException() {
        UnivariateRealFunction testFunction = new TestFunction();
        double initial = 1.0;
        double lowerBound = 1.0;
        double upperBound = 1.0;

        try {
            UnivariateRealSolverUtils.bracket(testFunction, initial, lowerBound, upperBound);
            Assert.fail("ConvergenceException expected but not thrown");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracketWithFunctionEvaluationException() {
        UnivariateRealFunction testFunction = new FunctionWithException();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        try {
            UnivariateRealSolverUtils.bracket(testFunction, initial, lowerBound, upperBound);
            Assert.fail("FunctionEvaluationException expected but not thrown");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }
}

class TestFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.pow(x, 2) - 1.0;
    }
}

class FunctionWithException implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        if (x < 0.0) {
            throw new FunctionEvaluationException("Function cannot be evaluated for negative x");
        }
        return Math.sqrt(x);
    }
}