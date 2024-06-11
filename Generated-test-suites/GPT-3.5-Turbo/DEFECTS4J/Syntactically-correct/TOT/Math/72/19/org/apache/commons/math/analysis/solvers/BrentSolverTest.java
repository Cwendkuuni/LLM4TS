import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

import static org.junit.Assert.*;

public class BrentSolverTest {

    private BrentSolver brentSolver;

    @Before
    public void setUp() {
        brentSolver = new BrentSolver();
    }

    @Test
    public void testFindRoot_WithValidInputs_RootFound() throws FunctionException {
        // Arrange
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double v) throws FunctionException {
                return Math.pow(v, 2) - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double v, double v1, double v2, double v3) {
                return CONVERGENCE;
            }
        };
        int maxIter = 100;
        double x0 = 1.0;
        double f0 = Math.pow(x0, 2) - 4;
        double x1 = 4.0;
        double f1 = Math.pow(x1, 2) - 4;

        // Act
        boolean result = brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        // Assert
        assertTrue(result);
        assertEquals(2.0, brentSolver.getRoot(), 0.0001);
    }

    @Test
    public void testFindRoot_WithInvalidInterval_RootNotFound() throws FunctionException {
        // Arrange
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double v) throws FunctionException {
                return Math.pow(v, 2) - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double v, double v1, double v2, double v3) {
                return CONVERGENCE;
            }
        };
        int maxIter = 100;
        double x0 = 3.0;
        double f0 = Math.pow(x0, 2) - 4;
        double x1 = 4.0;
        double f1 = Math.pow(x1, 2) - 4;

        // Act
        boolean result = brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        // Assert
        assertFalse(result);
    }

    @Test(expected = FunctionException.class)
    public void testFindRoot_WithFunctionException_ExceptionThrown() throws FunctionException {
        // Arrange
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double v) throws FunctionException {
                throw new FunctionException();
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double v, double v1, double v2, double v3) {
                return CONVERGENCE;
            }
        };
        int maxIter = 100;
        double x0 = 1.0;
        double f0 = Math.pow(x0, 2) - 4;
        double x1 = 4.0;
        double f1 = Math.pow(x1, 2) - 4;

        // Act
        brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        // Assert: Exception is expected to be thrown
    }
}