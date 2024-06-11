import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolveBracketingInterval() {
        // Test solving in bracketing interval
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-6, 5, 1});
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        Assert.assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testSolveNonBracketingInterval() {
        // Test solving in non-bracketing interval
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1, 2, 1});
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        try {
            solver.solve(100, f, 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            Assert.fail("MathInternalError was expected");
        } catch (MathInternalError e) {
            // Success
        }
    }

    @Test
    public void testSolveExactRoot() {
        // Test solving with exact root
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1, 0, 0});
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(100, f, -5, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        Assert.assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolveUnderApproximation() {
        // Test solving with under-approximation
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1, 0, 0});
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double result = solver.solve(100, f, -5, -3, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        Assert.assertEquals(-5.0, result, 1e-6);
    }

    @Test
    public void testSolveOverApproximation() {
        // Test solving with over-approximation
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1, 0, 0});
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 3, 5, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        Assert.assertEquals(5.0, result, 1e-6);
    }

}