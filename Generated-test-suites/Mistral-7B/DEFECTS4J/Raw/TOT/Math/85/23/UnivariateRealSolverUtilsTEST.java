import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.solver.BrentSolver;
import org.apache.commons.math3.solver.UnivariateRealSolver;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve_simpleCase() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new MyUnivariateRealFunction();
        double x0 = 1.0;
        double x1 = 2.0;
        double expected = 1.5;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(expected, result, 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_failToConverge() throws FunctionEvaluationException {
        UnivariateRealFunction f = new MyUnivariateRealFunction();
        double x0 = 1.0;
        double x1 = 1000.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_functionEvalException() throws FunctionEvaluationException {
        UnivariateRealFunction f = new MyBrokenFunction();
        double x0 = 1.0;
        double x1 = 2.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test
    public void testSolve_absoluteAccuracy() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new MyUnivariateRealFunction();
        double x0 = 1.0;
        double x1 = 2.0;
        double absoluteAccuracy = 0.01;
        double expected = 1.51;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        assertEquals(expected, result, absoluteAccuracy);
    }

    @Test
    public void testBracket_simpleCase() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new MyUnivariateRealFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 3.0;
        double[] expected = {1.0, 1.5};
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        assertEquals(expected[0], result[0], 0.001);
        assertEquals(expected[1], result[1], 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracket_failToConverge() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new MyUnivariateRealFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 1000.0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test
    public void testBracket_maxIterations() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new MyUnivariateRealFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 3.0;
        int maxIterations = 5;
        double[] expected = {1.0, 1.2};
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maxIterations);
        assertEquals(expected[0], result[0], 0.001);
        assertEquals(expected[1], result[1], 0.001);
    }

    private static class MyUnivariateRealFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 1.0;
        }
    }

    private static class MyBrokenFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            throw new FunctionEvaluationException("Broken!");
        }
    }
}