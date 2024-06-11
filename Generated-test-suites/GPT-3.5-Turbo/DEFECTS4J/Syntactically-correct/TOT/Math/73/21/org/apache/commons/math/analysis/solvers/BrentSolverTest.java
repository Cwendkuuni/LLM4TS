import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot_withValidInterval_ShouldReturnTrue() throws FunctionException {
        // Arrange
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                // Return the value of the function at x
                return x - 2;
            }
        };
        ConvergenceChecker checker = new DummyConvergenceChecker();
        int maxIter = 100;
        double x0 = 0.0;
        double f0 = function.valueAt(x0);
        double x1 = 4.0;
        double f1 = function.valueAt(x1);

        // Act
        boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        double root = solver.getRoot();

        // Assert
        assertTrue(rootFound);
        assertEquals(2.0, root, 0.0001);
    }

    @Test
    public void testFindRoot_withInvalidInterval_ShouldReturnFalse() throws FunctionException {
        // Arrange
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                // Return the value of the function at x
                return x - 2;
            }
        };
        ConvergenceChecker checker = new DummyConvergenceChecker();
        int maxIter = 100;
        double x0 = 0.0;
        double f0 = function.valueAt(x0);
        double x1 = 1.0;
        double f1 = function.valueAt(x1);

        // Act
        boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        double root = solver.getRoot();

        // Assert
        assertFalse(rootFound);
        assertEquals(Double.NaN, root, 0.0001);
    }

    @Test
    public void testFindRoot_withInvalidFunction_ShouldThrowFunctionException() throws FunctionException {
        // Arrange
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                if (x > 0) {
                    throw new FunctionException("Invalid function value at x > 0");
                }
                return x - 2;
            }
        };
        ConvergenceChecker checker = new DummyConvergenceChecker();
        int maxIter = 100;
        double x0 = -2.0;
        double f0 = function.valueAt(x0);
        double x1 = 4.0;
        double f1 = function.valueAt(x1);

        // Act & Assert
        try {
            solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
            fail("Expected FunctionException to be thrown");
        } catch (FunctionException e) {
            assertEquals("Invalid function value at x > 0", e.getMessage());
        }
    }

    private static class DummyConvergenceChecker implements ConvergenceChecker {
        @Override
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            // Dummy implementation always returns 0, indicating no convergence
            return 0;
        }
    }
}