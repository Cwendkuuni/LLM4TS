import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {
    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };

        double result = solver.solve(100, f, 0, 4);
        assertEquals(result, 2.0, 1e-6);
    }

    @Test
    public void testSolve_minAndMaxAreRoots() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };

        double result = solver.solve(100, f, 2, 2);
        assertEquals(result, 2.0, 1e-6);
    }

    @Test
    public void testSolve_noSolutionFound() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };

        double result = solver.solve(5, f, 3, 4);

        assertTrue(Double.isNaN(result));
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_earlyExit() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };

        solver.solve(5, f, 0, 2);
    }
}