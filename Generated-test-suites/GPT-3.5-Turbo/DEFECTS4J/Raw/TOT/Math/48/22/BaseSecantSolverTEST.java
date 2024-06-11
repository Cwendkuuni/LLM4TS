import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WithFunctionValueBeingExactRoot_ReturnsRoot() {
        // Arrange
        double expected = 3.0;
        BaseSecantSolver solver = new MockSecantSolver(expected, 0.0, BaseSecantSolver.Method.REGULA_FALSI);

        // Act
        double actual = solver.solve(100, new MockUnivariateRealFunction(), 2.0, 4.0, 3.0);

        // Assert
        Assert.assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testSolve_WithToleranceGreaterThanOrEqualToFunctionValueAccuracy_ReturnsApproximation() {
        // Arrange
        double expected = 3.0;
        BaseSecantSolver solver = new MockSecantSolver(3.5, expected, BaseSecantSolver.Method.PEGASUS);

        // Act
        double actual = solver.solve(100, new MockUnivariateRealFunction(), 1.0, 5.0, 3.5);

        // Assert
        Assert.assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testSolve_WithMaxEvalExceeded_ThrowsConvergenceException() {
        // Arrange
        BaseSecantSolver solver = new MockSecantSolver(10.0, 1.0, BaseSecantSolver.Method.ILLINOIS);

        // Act & Assert
        try {
            solver.solve(10, new MockUnivariateRealFunction(), 5.0, 15.0, 10.0);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Success
        }
    }

    private static class MockSecantSolver extends BaseSecantSolver {

        private final double functionValue;
        private final double root;

        public MockSecantSolver(double root, double functionValue, BaseSecantSolver.Method method) {
            super(1e-6, method);
            this.root = root;
            this.functionValue = functionValue;
        }

        @Override
        protected double computeObjectiveValue(double x) {
            return functionValue;
        }
    }

    private static class MockUnivariateRealFunction implements UnivariateRealFunction {

        public double value(double x) {
            return x - 3.0;
        }
    }
}