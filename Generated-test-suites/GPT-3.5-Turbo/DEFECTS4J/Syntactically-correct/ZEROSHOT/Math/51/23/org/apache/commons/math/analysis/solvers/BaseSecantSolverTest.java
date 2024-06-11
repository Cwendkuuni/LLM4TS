import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_ExactRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return 0.0;
            }
        };
        
        double result = solver.solve(100, null, 0.0, 1.0, 0.5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolve_WithinAccuracies() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * x - 1.0;
            }
        };

        double result = solver.solve(100, null, 0.0, 2.0, 1.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testSolve_InvalidAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * x - 1.0;
            }
        };

        try {
            solver.solve(100, null, 0.0, 2.0, 1.0, null);
            fail("Expected MathInternalError");
        } catch (MathInternalError e) {
            // Pass
        }
    }

    @Test
    public void testSolve_FunctionValueTooSmall() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                if (x < 0.5) {
                    return -1e-12;
                } else {
                    return 1e-12;
                }
            }
        };

        double result = solver.solve(100, null, 0.0, 1.0, 0.5, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(0.0, result, 1e-6);
    }
}