import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;

public class UnivariateRealSolverUtilsTest {

    private class TestFunction implements UnivariateRealFunction {

        public double value(double x) throws FunctionEvaluationException {
            // Define your test function here
            return x * x - 4;
        }
    }

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new TestFunction();
        double x0 = 1.0;
        double x1 = 3.0;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(2.0, result, 0.0001);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_ConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new TestFunction();
        double x0 = 1.0;
        double x1 = 1.5;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_FunctionEvaluationException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = null;
        double x0 = 1.0;
        double x1 = 3.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test
    public void testSolveWithAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new TestFunction();
        double x0 = 1.0;
        double x1 = 3.0;
        double absoluteAccuracy = 0.001;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        assertEquals(2.0, result, 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithAccuracy_ConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new TestFunction();
        double x0 = 1.0;
        double x1 = 1.5;
        double absoluteAccuracy = 0.001;
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithAccuracy_FunctionEvaluationException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = null;
        double x0 = 1.0;
        double x1 = 3.0;
        double absoluteAccuracy = 0.001;
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new TestFunction();
        double initial = 2.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        assertEquals(1.0, result[0], 0.0001);
        assertEquals(3.0, result[1], 0.0001);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracket_ConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new TestFunction();
        double initial = 1.25;
        double lowerBound = 1.0;
        double upperBound = 1.5;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracket_FunctionEvaluationException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = null;
        double initial = 2.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test
    public void testBracketWithMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new TestFunction();
        double initial = 2.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        int maximumIterations = 10;
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
        assertEquals(1.0, result[0], 0.0001);
        assertEquals(3.0, result[1], 0.0001);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithMaxIterations_ConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new TestFunction();
        double initial = 1.25;
        double lowerBound = 1.0;
        double upperBound = 1.5;
        int maximumIterations = 10;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracketWithMaxIterations_FunctionEvaluationException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = null;
        double initial = 2.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        int maximumIterations = 10;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }

    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 3.0;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSetup() {
        try {
            UnivariateRealSolverUtils.setup(new TestFunction());
        } catch (IllegalArgumentException e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetup_IllegalArgumentException() {
        UnivariateRealSolverUtils.setup(null);
    }
}