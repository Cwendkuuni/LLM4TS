import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.PrecisionException;
import org.junit.Test;

public class BaseSecantSolverTest {

    

    @Test
    public void testDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solver);
    }

    @Test
    public void testConstructorWithRelativeAndAbsoluteAccuracyAndMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-6, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver);
    }

    @Test
    public void testConstructorWithRelativeAndAbsoluteAccuracyAndFunctionValueAccuracyAndMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-6, 1e-7, BaseSecantSolver.Method.PEGASUS);
        assertNotNull(solver);
    }

    @Test
    public void testSolveMethodWithDefaultParameters() throws ConvergenceException, MaxEvalException, PrecisionException {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0}, 3);
        double root = solver.solve(f, -10.0, 10.0);
        assertEquals(0.0, root, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveMethodWithNullFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateFunction f = null;
        solver.solve(f, -10.0, 10.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveMethodWithNullMin() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, -2.0, 1.0}, 2);
        double min = null;
        solver.solve(f, min, 10.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveMethodWithNullMax() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, -2.0, 1.0}, 2);
        double max = null;
        solver.solve(f, -10.0, max);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveMethodWithFunctionThatHasNoRootInInterval() throws ConvergenceException, MaxEvalException, PrecisionException {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, 1.0, 1.0}, 3);
        double root = solver.solve(f, -10.0, 10.0);
        fail("Solver should not have found a root.");
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveMethodWithFunctionThatHasMultipleRootsInInterval() throws ConvergenceException, MaxEvalException, PrecisionException {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, -2.0, 2.0, 1.0}, 4);
        double root = solver.solve(f, -10.0, 10.0);
        fail("Solver should not have found a unique root.");
    }

    @Test(expected = PrecisionException.class)
    public void testSolveMethodWithFunctionValueAccuracyLowerThanAbsoluteAccuracy() throws ConvergenceException, MaxEvalException, PrecisionException {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-4, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, -2.0, 1.0}, 2);
        solver.solve(f, -10.0, 10.0);
        fail("Solver should have thrown an PrecisionException.");
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveMethodWithMaxEvalExceeded() throws ConvergenceException, MaxEvalException, PrecisionException {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0}, 3);
        solver.setMaxIterations(0);
        double root = solver.solve(f, -10.0, 10.0);
        fail("Solver should not have found a root.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveMethodWithInvalidAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0}, 3);
        try {
            solver.solve(f, -10.0, 10.0, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
            fail("Solver should have thrown an IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSolveMethodWithAllowedSolutionLeftSide() throws ConvergenceException, MaxEvalException, PrecisionException {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0}, 3);
        double root = solver.solve(f, -10.0, 10.0, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolveMethodWithAllowedSolutionRightSide() throws ConvergenceException, MaxEvalException, PrecisionException {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, -4.0, 0.0, 1.0}, 3);
        double root = solver.solve(f, -10.0, 10.0, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolveMethodWithAllowedSolutionBelowSide() throws ConvergenceException, MaxEvalException, PrecisionException {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0}, 3);
        double root = solver.solve(f, -10.0, 10.0, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolveMethodWithAllowedSolutionAboveSide() throws ConvergenceException, MaxEvalException, PrecisionException {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, -4.0, 0.0, 1.0}, 3);
        double root = solver.solve(f, -10.0, 10.0, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolveMethodWithAllowedSolutionAnySide() throws ConvergenceException, MaxEvalException, PrecisionException {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 0.0, 1.0}, 3);
        double root = solver.solve(f, -10.0, 10.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-6);
    }

    
}