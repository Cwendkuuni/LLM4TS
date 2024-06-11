import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction function = new QuadraticFunction(-1.0, 2.0, 1.0);
        double result = UnivariateRealSolverUtils.solve(function, -2.0, 2.0);
        double expected = -1.0;
        assertEquals(expected, result, 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() throws FunctionEvaluationException {
        UnivariateRealFunction function = new QuadraticFunction(-1.0, 2.0, 1.0);
        UnivariateRealSolverUtils.bracket(function, -2.0, -1.0, 1.0, 1000);
        try {
            UnivariateRealSolverUtils.solve(function, -2.0, 1.0);
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() {
        UnivariateRealFunction function = new FunctionWithException();
        try {
            UnivariateRealSolverUtils.solve(function, -2.0, 1.0);
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testSolveAbsoluteAccuracy() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction function = new QuadraticFunction(-1.0, 2.0, 1.0);
        double result = UnivariateRealSolverUtils.solve(function, -2.0, 2.0, 1e-5);
        double expected = -1.0;
        assertEquals(expected, result, 1e-5);
    }

    @Test
    public void testBracket() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction function = new QuadraticFunction(-1.0, 2.0, 1.0);
        double[] result = UnivariateRealSolverUtils.bracket(function, 0.0, -2.0, 2.0);
        double[] expected = {-1.5, 0.5};
        assertEquals(expected[0], result[0], 0.001);
        assertEquals(expected[1], result[1], 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketConvergenceException() throws FunctionEvaluationException {
        UnivariateRealFunction function = new QuadraticFunction(-1.0, 2.0, 1.0);
        UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0, 1000);
        try {
            UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0, 1);
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    private static class QuadraticFunction implements UnivariateRealFunction {
        double a;
        double b;
        double c;

        public QuadraticFunction(double a, double b, double c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public double value(double x) {
            return a * x * x + b * x + c;
        }

        @Override
        public int compareTo(UnivariateRealFunction other) {
            return 0;
        }
    }

    private static class FunctionWithException implements UnivariateRealFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException("Function evaluation exception");
        }

        @Override
        public int compareTo(UnivariateRealFunction other) {
            return 0;
        }
    }
}