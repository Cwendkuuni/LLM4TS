import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5);
        assertEquals(2, result, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2, result, 1e-6);
    }

    @Test
    public void testSolveWithStartValue() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5, 1);
        assertEquals(2, result, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSolutionAndStartValue() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2, result, 1e-6);
    }

    @Test
    public void testSolveWithMaxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(10, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5);
        assertNotEquals(2, result, 1e-6);
    }

    @Test
    public void testSolveWithNoRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x + 4;
            }
        }, 0, 5);
        assertEquals(Double.NaN, result, 1e-6);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-2, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10);
        assertEquals(2, result, 1e-2);
    }

    @Test
    public void testSolveWithRelativeAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-2, 1e-4, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10);
        assertEquals(2, result, 1e-4);
    }

    @Test
    public void testSolveWithFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-4, 1e-2, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10);
        assertEquals(2, result, 1e-2);
    }

    @Test
    public void testSolveEdgeCases() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-2, BaseSecantSolver.Method.REGULA_FALSI);
        double result1 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        }, 0, 10);
        double result2 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return -x;
            }
        }, 0, 10);
        double result3 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        }, 0, 10);
        assertEquals(0, result1, 1e-2);
        assertEquals(0, result2, 1e-2);
        assertEquals(0, result3, 1e-2);
    }

    @Test
    public void testSolveWithExceptions() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = 0;
        try {
            result = solver.solve(100, null, 0, 5);
        } catch (NullPointerException e) {
            assertNull(null, f);
        }
    }
}