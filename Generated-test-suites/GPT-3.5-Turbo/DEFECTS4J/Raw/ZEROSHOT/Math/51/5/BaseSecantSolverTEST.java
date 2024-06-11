import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Test
    public void testSolve_ForAnySideSolution() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolve_ForLeftSideSolution() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5, BaseSecantSolver.AllowedSolution.LEFT_SIDE);

        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolve_ForRightSideSolution() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolve_ForBelowSideSolution() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5, BaseSecantSolver.AllowedSolution.BELOW_SIDE);

        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolve_ForAboveSideSolution() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);

        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolve_WithMaxEval() {
        double result = solver.solve(10, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5);

        assertEquals(2.0, result, 0.0001);
    }
}