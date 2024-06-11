import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution = solver.solve(100, f, 1, 3);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testSolve_minMaxAreRoots() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 3;
            }
        };
        double solution = solver.solve(100, f, 3, 3);
        assertEquals(3.0, solution, 1e-6);
    }

    @Test
    public void testSolve_solutionIsRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution = solver.solve(100, f, 2, 4);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_internalError() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        solver.solve(100, f, 1, 2);
    }

    @Test
    public void testSolve_maxIterations() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution = solver.solve(1, f, 1, 3);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testSolve_allowedSolutionAnySide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testSolve_allowedSolutionLeftSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testSolve_allowedSolutionRightSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testSolve_allowedSolutionBelowSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testSolve_allowedSolutionAboveSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(2.0, solution, 1e-6);
    }
}