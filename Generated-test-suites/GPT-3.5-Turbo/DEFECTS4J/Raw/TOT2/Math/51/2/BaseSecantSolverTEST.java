import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolveWithDefaultValues() {
        BaseSecantSolver solver = new BaseSecantSolverImpl();
        double result = solver.solve(100, Math::sin, 0.0, 1.5, AllowedSolution.ANY_SIDE);
        assertEquals(1.5707963267948966, result, 1e-6);
    }

    @Test
    public void testSolveWithSpecificValues() {
        BaseSecantSolver solver = new BaseSecantSolverImpl();
        double result = solver.solve(100, Math::cos, -2.0, 0.0, 0.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(-1.5707963267948966, result, 1e-6);
    }

    @Test
    public void testSolveWithInvalidInterval() {
        BaseSecantSolver solver = new BaseSecantSolverImpl();
        try {
            solver.solve(100, Math::exp, 1.0, 0.0, AllowedSolution.ANY_SIDE);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Interval endpoints do not bracket the root", e.getMessage());
        }
    }

    @Test
    public void testSolveWithSmallFunctionValue() {
        BaseSecantSolver solver = new BaseSecantSolverImpl();
        double result = solver.solve(100, Math::tan, 0.1, 1.5, AllowedSolution.BELOW_SIDE);
        assertEquals(0.4636476090008061, result, 1e-6);
    }

    private static class BaseSecantSolverImpl extends BaseSecantSolver {

        public BaseSecantSolverImpl() {
            super(1e-6, Method.REGULA_FALSI);
        }

        @Override
        protected double computeObjectiveValue(double point) {
            return 0.0; // Dummy implementation
        }
    }
}