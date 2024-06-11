import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_ExactRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                if (x == 4.0) {
                    return 0.0;
                } else {
                    return x - 4.0; // f(x) = x - 4
                }
            }
        };
        
        double result = solver.solve(100, null, 3.0, 5.0, 0.0);
        
        assertEquals(4.0, result, 1e-6);
    }

    @Test
    public void testSolve_WithinAccuracies() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x * x - 4.0; // f(x) = x^2 - 4
            }
        };
        
        double result = solver.solve(100, null, 1.0, 3.0, 0.0);
        
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_AnySideAllowed() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x - 4.0; // f(x) = x - 4
            }
        };
        
        double result = solver.solve(100, null, 1.0, 3.0, 0.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        
        assertEquals(4.0, result, 1e-6);
    }

    @Test
    public void testSolve_LeftSideAllowed() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x - 4.0; // f(x) = x - 4
            }
        };
        
        double result = solver.solve(100, null, 1.0, 3.0, 0.0, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        
        assertEquals(4.0, result, 1e-6);
    }

    @Test
    public void testSolve_RightSideAllowed() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x - 4.0; // f(x) = x - 4
            }
        };
        
        double result = solver.solve(100, null, 1.0, 3.0, 0.0, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        
        assertEquals(4.0, result, 1e-6);
    }

    @Test
    public void testSolve_BelowSideAllowed() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x - 4.0; // f(x) = x - 4
            }
        };
        
        double result = solver.solve(100, null, 1.0, 3.0, 0.0, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        
        assertEquals(4.0, result, 1e-6);
    }

    @Test
    public void testSolve_AboveSideAllowed() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x - 4.0; // f(x) = x - 4
            }
        };
        
        double result = solver.solve(100, null, 1.0, 3.0, 0.0, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        
        assertEquals(4.0, result, 1e-6);
    }

    @Test
    public void testSolve_MaxIterationsExceeded() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x - 4.0; // f(x) = x - 4
            }
        };
        
        double result = solver.solve(10, null, 1.0, 3.0, 0.0);
        
        assertEquals(Double.NaN, result, 0.0);
    }

    @Test
    public void testSolve_FunctionValueTooSmall() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return FastMath.nextAfter(x - 4.0, 0.0); // f(x) = x - 4
            }
        };
        
        double result = solver.solve(100, null, 1.0, 3.0, 0.0);
        
        assertEquals(4.0, result, 1e-6);
    }

}