import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve_Basic() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);
        double x0 = 1.0;
        double x1 = 2.0;
        double expected = -1.0;
        double result = UnivariateRealSolverUtils.solve(function, x0, x1);
        assertEquals(expected, result, 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_ConvergenceException() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);
        double x0 = 1.0;
        double x1 = 1.0;
        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_FunctionEvaluationException() {
        UnivariateRealFunction function = new PolynomialFunction(x -> 1 / x);
        double x0 = 0.0;
        double x1 = 1.0;
        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @Test
    public void testBracket_Basic() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);
        double initial = 1.5;
        double lowerBound = 1.0;
        double upperBound = 2.0;
        double[] expected = {1.0, 2.0};
        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        assertArrayEquals(expected, result, 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracket_ConvergenceException() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);
        double initial = 1.0;
        double lowerBound = 1.0;
        double upperBound = 1.0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracket_FunctionEvaluationException() {
        UnivariateRealFunction function = new PolynomialFunction(x -> 1 / x);
        double initial = 0.5;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test
    public void testMidpoint() {
        double a = 3.5;
        double b = 4.5;
        double expected = 4.0;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expected, result, 0.001);
    }
}