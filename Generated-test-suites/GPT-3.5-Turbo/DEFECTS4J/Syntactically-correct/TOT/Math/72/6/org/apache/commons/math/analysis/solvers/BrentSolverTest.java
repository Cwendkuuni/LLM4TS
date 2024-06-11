import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;

import static org.junit.Assert.*;

public class BrentSolverTest {

    private BrentSolver solver;

    @Before
    public void setUp() {
        solver = new BrentSolver();
    }

    @Test
    public void testFindRoot_WithValidInputs_ShouldReturnTrue() throws FunctionException {
        // Arrange
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 4;
            }
        };

        double x0 = -2;
        double f0 = function.valueAt(x0);
        double x1 = 2;
        double f1 = function.valueAt(x1);

        // Act
        boolean result = solver.findRoot(function, new ConvergenceChecker(), 100, x0, f0, x1, f1);

        // Assert
        assertTrue(result);
        assertEquals(2.0, solver.getRoot(), 0.0001);
    }

    @Test
    public void testFindRoot_WithInvalidInterval_ShouldReturnFalse() throws FunctionException {
        // Arrange
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 4;
            }
        };

        double x0 = 2;
        double f0 = function.valueAt(x0);
        double x1 = 3;
        double f1 = function.valueAt(x1);

        // Act
        boolean result = solver.findRoot(function, new ConvergenceChecker(), 100, x0, f0, x1, f1);

        // Assert
        assertFalse(result);
        assertEquals(Double.NaN, solver.getRoot(), 0.0001);
    }

    @Test(expected = FunctionException.class)
    public void testFindRoot_WithInvalidFunction_ShouldThrowException() throws FunctionException {
        // Arrange
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                throw new FunctionException("Invalid function");
            }
        };

        double x0 = -2;
        double f0 = function.valueAt(x0);
        double x1 = 2;
        double f1 = function.valueAt(x1);

        // Act
        solver.findRoot(function, new ConvergenceChecker(), 100, x0, f0, x1, f1);

        // Assert (Exception expected)
    }

    @Test(timeout = 100)
    public void testFindRoot_WithLargeMaxIterations_ShouldCompleteWithinTimeout() throws FunctionException {
        // Arrange
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 4;
            }
        };

        double x0 = -2;
        double f0 = function.valueAt(x0);
        double x1 = 2;
        double f1 = function.valueAt(x1);

        // Act
        boolean result = solver.findRoot(function, new ConvergenceChecker(), 1000000, x0, f0, x1, f1);

        // Assert
        assertTrue(result);
        assertEquals(2.0, solver.getRoot(), 0.0001);
    }

}