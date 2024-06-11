import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    // Test case for constructor with absolute accuracy
    @Test
    public void testConstructorAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
    }

    // Test case for constructor with absolute and relative accuracy
    @Test
    public void testConstructorRelativeAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-12, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(1e-6, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-12, solver.getAbsoluteAccuracy(), 0.0);
    }

    // Test case for constructor with relative, absolute, and function value accuracies
    @Test
    public void testConstructorFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-12, 1e-8, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(1e-6, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-12, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(1e-8, solver.getFunctionValueAccuracy(), 0.0);
    }

    // Test case for solve method with allowed solution specified
    @Test
    public void testSolveWithAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double v) {
                return v * v - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, f, 1.0, 3.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

    // Test case for solve method with default allowed solution
    @Test
    public void testSolveDefaultAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double v) {
                return v - 3;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, f, 2.0, 4.0);
        assertEquals(3.0, root, 1e-6);
    }

    // Test case for solve method handling MathInternalError
    @Test(expected = MathInternalError.class)
    public void testSolveMathInternalError() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double v) {
                return v * v;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double doSolve() {
                throw new MathInternalError();
            }
        };
        solver.solve(100, f, 1.0, 3.0);
    }

}