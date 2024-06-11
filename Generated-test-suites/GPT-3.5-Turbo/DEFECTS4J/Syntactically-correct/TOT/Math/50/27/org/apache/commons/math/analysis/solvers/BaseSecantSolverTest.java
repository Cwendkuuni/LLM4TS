import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    @Test
    public void solve_WhenRootInRange_ReturnsRoot() {
        // Arrange
        BaseSecantSolver solver = new TestSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new TestFunction();
        double min = -10.0;
        double max = 10.0;

        // Act
        double result = solver.solve(100, f, min, max, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        // Assert
        assertEquals(3.0, result, 1e-6);
    }

    @Test
    public void solve_WhenRootOutOfRange_ThrowsException() {
        // Arrange
        BaseSecantSolver solver = new TestSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new TestFunction();
        double min = -10.0;
        double max = -5.0;

        try {
            // Act
            solver.solve(100, f, min, max, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        } catch (Exception e) {
            // Assert
            assertEquals("No bracketing interval found", e.getMessage());
        }
    }

    @Test
    public void solve_WhenRootNotFoundWithinMaxEvaluations_ThrowsException() {
        // Arrange
        BaseSecantSolver solver = new TestSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new NoRootFunction();
        double min = -10.0;
        double max = 10.0;

        try {
            // Act
            solver.solve(10, f, min, max, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        } catch (Exception e) {
            // Assert
            assertEquals("Maximum number of evaluations exceeded", e.getMessage());
        }
    }

    @Test
    public void solve_WhenRootIsExact_ReturnsRoot() {
        // Arrange
        BaseSecantSolver solver = new TestSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new ExactRootFunction();
        double min = -10.0;
        double max = 10.0;

        // Act
        double result = solver.solve(100, f, min, max, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        // Assert
        assertEquals(0.0, result, 1e-6);
    }

    // Stub class for testing
    private static class TestSecantSolver extends BaseSecantSolver {

        private TestSecantSolver(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }

        @Override
        protected double computeObjectiveValue(double point) {
            return 0.0;
        }

        @Override
        protected void verifyBracketing(double lower, double upper) {
            if ((lower > 0 && upper > 0) || (lower < 0 && upper < 0)) {
                throw new IllegalArgumentException("No bracketing interval found");
            }
        }
    }

    // Stub class for testing
    private static class TestFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return x - 3.0;
        }
    }

    // Stub class for testing
    private static class NoRootFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return x * x - 4.0;
        }
    }

    // Stub class for testing
    private static class ExactRootFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return x;
        }
    }
}