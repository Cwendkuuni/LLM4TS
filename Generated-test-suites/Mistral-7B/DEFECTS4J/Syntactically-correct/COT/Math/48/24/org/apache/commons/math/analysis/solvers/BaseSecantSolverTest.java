import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;

public class BaseSecantSolverTest {

    

    @Test
    public void testConstructor() {
        // Test default constructor
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());

        // Test constructor with relative and absolute accuracy
        solver = new BaseSecantSolver(1e-4, 1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(1e-4, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());

        // Test constructor with relative, absolute, and function value accuracy
        solver = new BaseSecantSolver(1e-4, 1e-6, 1e-8, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(1e-4, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(1e-8, solver.getFunctionValueAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidMethod() {
        new BaseSecantSolver(1.0, BaseSecantSolver.Method.INVALID);
    }

    @Test
    public void testSolveWithDefaultFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 3);
        double x = solver.solve(100, function, -5.0, 5.0, 0.0);
        assertEquals(1.0, x, 1e-6);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveWithNoBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{1.0, 0.0}, 1);
        solver.solve(100, function, -5.0, 5.0, 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithConvergenceException() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{0.0, 1.0, 1.0}, 3);
        solver.solve(100, function, -5.0, 5.0, 0.0);
    }

    @Test(expected = MaxEvalException.class)
    public void testSolveWithMaxEvalException() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{1.0, 0.0}, 1);
        solver.setMaxEval(0);
        solver.solve(100, function, -5.0, 5.0, 0.0);
    }

    @Test
    public void testSolveWithAllowedSolutionLeftSide() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS, AllowedSolution.LEFT_SIDE);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 3);
        double x = solver.solve(100, function, -5.0, 5.0, 0.0);
        assertEquals(1.0, x, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSolutionRightSide() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS, AllowedSolution.RIGHT_SIDE);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 3);
        double x = solver.solve(100, function, -5.0, 5.0, 2.0);
        assertEquals(-1.0, x, 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithNumberIsTooSmallException() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{1.0, 0.0}, 1);
        solver.setFunctionValueAccuracy(1.0);
        solver.solve(100, function, -5.0, 5.0, 0.0);
    }

    
}