import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import org.junit.Test;
import org.junit.Assert;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveWithDefaultSolver() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{1, -3, 2});
        double result = UnivariateRealSolverUtils.solve(function, 0, 3);
        Assert.assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testSolveWithDefaultSolverAndAccuracy() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 4, -5});
        double result = UnivariateRealSolverUtils.solve(function, 1, 3, 1e-6);
        Assert.assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{1, -2, -3});
        double[] result = UnivariateRealSolverUtils.bracket(function, 0, -5, 5, 100);
        Assert.assertArrayEquals(new double[]{-2.0, 0.0}, result, 1e-6);
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(2, 8);
        Assert.assertEquals(5.0, result, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() {
        UnivariateRealSolverUtils.solve(null, 1, 2);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketNoRoot() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{1, 1, 1});
        UnivariateRealSolverUtils.bracket(function, 0, -10, 10, 100);
    }

}