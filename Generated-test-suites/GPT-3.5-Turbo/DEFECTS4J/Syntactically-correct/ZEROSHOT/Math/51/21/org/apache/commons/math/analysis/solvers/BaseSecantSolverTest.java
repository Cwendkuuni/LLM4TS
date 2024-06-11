import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Test
    public void testSolve_AnySide() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolve_LeftSide() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);

        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolve_RightSide() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolve_BelowSide() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);

        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolve_AboveSide() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);

        assertEquals(2.0, result, 0.0001);
    }
}