import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 9;
            }
        }, -10, 10);
        assertEquals(3, result, 1e-6);
    }

    @Test
    public void testSolve_withAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 9;
            }
        }, -10, 10, AllowedSolution.BELOW_SIDE);
        assertEquals(-3, result, 1e-6);
    }

    @Test
    public void testSolve_multipleRoots() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, -10, 10);
        assertTrue(result == 2 || result == -2);
    }

    @Test
    public void testSolve_incorrectBracketing() {
        try {
            BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
            double result = solver.solve(100, new UnivariateRealFunction() {
                public double value(double x) {
                    return x * x - 9;
                }
            }, 10, -10);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    @Test
    public void testSolve_exactRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        }, 0, 10);
        assertEquals(5, result, 1e-6);
    }
}