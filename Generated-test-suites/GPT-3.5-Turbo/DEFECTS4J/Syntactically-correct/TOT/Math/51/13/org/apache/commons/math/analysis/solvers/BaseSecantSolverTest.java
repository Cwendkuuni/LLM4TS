import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve_WithMaxEval_ReturnsSolutionWithinAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 2 * x - 1;
            }
        };

        double solution = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(0.5, solution, solver.getAbsoluteAccuracy());
    }

    @Test
    public void testSolve_WhenRootIsLowerBoundary_ReturnsRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };

        double solution = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(0, solution, solver.getAbsoluteAccuracy());
    }

    @Test
    public void testSolve_WhenRootIsUpperBoundary_ReturnsRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 10;
            }
        };

        double solution = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(10, solution, solver.getAbsoluteAccuracy());
    }

    @Test
    public void testSolve_WhenRootIsOutsideBoundaries_ThrowsMathInternalError() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 20;
            }
        };

        try {
            solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        } catch (MathInternalError e) {
            assertTrue(e.getClass().equals(MathInternalError.class));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_WithNegativeMaxEval_ThrowsIllegalArgumentException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };

        solver.solve(-100, f, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    @Test(timeout = 1000)
    public void testSolve_WithMaxEvalTimeout_ReturnsApproximateSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 2 * x - 1;
            }
        };

        double solution = solver.solve(1000000, f, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertTrue(solution >= 0.499999 && solution <= 0.500001);
    }

}