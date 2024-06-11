import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WithAllowedSolutionAnySide() {
        // Test solve method with allowed solution ANY_SIDE
        BaseSecantSolver solver = new TestSecantSolver();
        double root = solver.solve(100, new TestFunction(), 0, 5, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolve_WithAllowedSolutionLeftSide() {
        // Test solve method with allowed solution LEFT_SIDE
        BaseSecantSolver solver = new TestSecantSolver();
        double root = solver.solve(100, new TestFunction(), 0, 5, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolve_WithAllowedSolutionRightSide() {
        // Test solve method with allowed solution RIGHT_SIDE
        BaseSecantSolver solver = new TestSecantSolver();
        double root = solver.solve(100, new TestFunction(), 0, 5, AllowedSolution.RIGHT_SIDE);
        assertEquals(5.0, root, 1e-6);
    }

    @Test
    public void testSolve_WithAllowedSolutionBelowSide() {
        // Test solve method with allowed solution BELOW_SIDE
        BaseSecantSolver solver = new TestSecantSolver();
        double root = solver.solve(100, new TestFunction(), -5, 5, AllowedSolution.BELOW_SIDE);
        assertEquals(-2.0, root, 1e-6);
    }

    @Test
    public void testSolve_WithAllowedSolutionAboveSide() {
        // Test solve method with allowed solution ABOVE_SIDE
        BaseSecantSolver solver = new TestSecantSolver();
        double root = solver.solve(100, new TestFunction(), -5, 5, AllowedSolution.ABOVE_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

    private static class TestSecantSolver extends BaseSecantSolver {
        public TestSecantSolver() {
            super(1e-6, Method.REGULA_FALSI);
        }

        @Override
        protected double computeObjectiveValue(double x) {
            return x - 2;
        }
    }

    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x - 2;
        }
    }
}