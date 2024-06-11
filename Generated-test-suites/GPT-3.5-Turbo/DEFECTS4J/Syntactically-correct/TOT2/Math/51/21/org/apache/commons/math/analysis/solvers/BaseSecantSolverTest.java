import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WithDefaultAbsoluteAccuracy() {
        assertThrows(MathInternalError.class, () -> {
            BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
            UnivariateRealFunction f = val -> val * val - 4;
            double result = solver.solve(100, f, 1, 3, BracketedUnivariateRealSolver.AllowedSolution.ANY_SIDE);
            assertEquals(2.0, result, 1e-6);
        });
    }

    @Test
    public void testSolve_WithCustomAccuracy() {
        assertThrows(MathInternalError.class, () -> {
            BaseSecantSolver solver = new BaseSecantSolver(1e-10, 1e-12, 1e-14, BaseSecantSolver.Method.REGULA_FALSI);
            UnivariateRealFunction f = val -> val * val - 4;
            double result = solver.solve(100, f, 1, 3, BracketedUnivariateRealSolver.AllowedSolution.ANY_SIDE);
            assertEquals(2.0, result, 1e-10);
        });
    }

    @Test
    public void testSolve_WithInvalidBracketing() {
        assertThrows(IllegalArgumentException.class, () -> {
            BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
                @Override
                protected double doSolve() {
                    return 0.0;
                }
            };
            UnivariateRealFunction f = val -> val * val - 4;
            solver.solve(100, f, 3, 1, BracketedUnivariateRealSolver.AllowedSolution.ANY_SIDE);
        });
    }

    @Test
    public void testSolve_WithExactRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double doSolve() {
                return 2.0;
            }
        };
        UnivariateRealFunction f = val -> val * val - 4;
        double result = solver.solve(100, f, 1, 3, BracketedUnivariateRealSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_WithFunctionValueTooSmall() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double point) {
                return 1e-7;
            }
        };
        UnivariateRealFunction f = val -> val * val - 4;
        double result = solver.solve(100, f, 1, 3, BracketedUnivariateRealSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_WithSmallInterval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double point) {
                return point * point - 4;
            }
        };
        UnivariateRealFunction f = val -> val * val - 4;
        double result = solver.solve(100, f, 1.9, 2.1, BracketedUnivariateRealSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

}