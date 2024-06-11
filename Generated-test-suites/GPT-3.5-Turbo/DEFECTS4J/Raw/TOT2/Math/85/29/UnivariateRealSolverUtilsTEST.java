import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1, -3, 2});
        double result = UnivariateRealSolverUtils.solve(f, 0, 3);
        assertEquals(1.0, result, 1e-15);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1, -3, 2});
        double result = UnivariateRealSolverUtils.solve(f, 0, 3, 1e-10);
        assertEquals(1.0, result, 1e-10);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1, -3, 2});
        double[] result = UnivariateRealSolverUtils.bracket(f, 1.0, -5, 5);
        assertEquals(-2.0, result[0], 1e-15);
        assertEquals(4.0, result[1], 1e-15);
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(-2.0, 4.0);
        assertEquals(1.0, result, 1e-15);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() {
        UnivariateRealSolverUtils.solve(null, 0, 3);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithoutRoot() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1, 1});
        UnivariateRealSolverUtils.bracket(f, 0, -5, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidBounds() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1, -3, 2});
        UnivariateRealSolverUtils.bracket(f, 0, 5, -5);
    }
}