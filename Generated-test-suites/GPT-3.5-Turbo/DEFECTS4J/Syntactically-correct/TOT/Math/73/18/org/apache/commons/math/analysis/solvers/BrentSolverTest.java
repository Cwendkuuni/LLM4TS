import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

    private BrentSolver solver;

    @Before
    public void setup() {
        solver = new BrentSolver();
    }

    @Test
    public void testFindRoot_withValidInterval_returnsTrueAndComputesRoot() throws FunctionException {
        // Arrange
        TestFunction function = new TestFunction();
        TestConvergenceChecker checker = new TestConvergenceChecker();
        double x0 = -100;
        double f0 = function.valueAt(x0);
        double x1 = 100;
        double f1 = function.valueAt(x1);

        // Act
        boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

        // Assert
        assertTrue(result);
        assertEquals(0.0, solver.getRoot(), 1e-6);
    }

    @Test
    public void testFindRoot_withInvalidInterval_returnsFalseAndDoesNotComputeRoot() throws FunctionException {
        // Arrange
        TestFunction function = new TestFunction();
        TestConvergenceChecker checker = new TestConvergenceChecker();
        double x0 = -100;
        double f0 = function.valueAt(x0);
        double x1 = -50;
        double f1 = function.valueAt(x1);

        // Act
        boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

        // Assert
        assertFalse(result);
        assertEquals(Double.NaN, solver.getRoot(), 1e-6);
    }

    @Test
    public void testFindRoot_withMaxIterationsExceeded_returnsFalseAndDoesNotComputeRoot() throws FunctionException {
        // Arrange
        TestFunction function = new TestFunction();
        TestConvergenceChecker checker = new TestConvergenceChecker();
        double x0 = -100;
        double f0 = function.valueAt(x0);
        double x1 = 100;
        double f1 = function.valueAt(x1);

        // Act
        boolean result = solver.findRoot(function, checker, 1, x0, f0, x1, f1);

        // Assert
        assertFalse(result);
        assertEquals(Double.NaN, solver.getRoot(), 1e-6);
    }

    // Additional test cases for various scenarios, including edge cases and error scenarios...
    // ...
    // ...

}

class TestFunction implements ComputableFunction {
    public double valueAt(double x) {
        return x * x;
    }
}

class TestConvergenceChecker implements ConvergenceChecker {
    public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow <= 0 && fHigh > 0) {
            return ConvergenceChecker.LOW;
        }
        if (fLow > 0 && fHigh <= 0) {
            return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
    }
}