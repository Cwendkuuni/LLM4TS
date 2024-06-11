
import org.junit.*;
import static org.junit.Assert.*;

public class BrentSolverTest {

    // Create instance of BrentSolver
    private BrentSolver solver;

    @Before
    public void setUp() {
        solver = new BrentSolver();
    }

    @Test
    public void testFindRoot() {
        // Create test function that returns x^2 - 4
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };

        // Create checker that checks if a function value is less than a tolerance
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return HIGH;
                } else {
                    return UNDEFINED;
                }
            }
        };

        // Call findRoot method with valid interval and return true
        boolean rootFound = solver.findRoot(function, checker, 1000, -2, -4, 2, 0);
        assertTrue(rootFound);

        // Check if the abscissa of the root is approximated correctly
        assertEquals(2, solver.getRoot(), 1e-6);
    }

    @Test
    public void testFindRootInvalidInterval() {
        // Create test function that returns x^2 - 4
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };

        // Create checker that checks if a function value is less than a tolerance
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return HIGH;
                } else {
                    return UNDEFINED;
                }
            }
        };

        // Call findRoot method with invalid interval and return false
        boolean rootFound = solver.findRoot(function, checker, 1000, 2, 0, -2, -4);
        assertFalse(rootFound);
    }

    @Test
    public void testFindRootInvalidFunction() {
        // Create test function that throws an exception
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                throw new FunctionException("Invalid function");
            }
        };

        // Create checker that checks if a function value is less than a tolerance
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return HIGH;
                } else {
                    return UNDEFINED;
                }
            }
        };

        // Call findRoot method with valid interval and invalid function and expect an exception
        try {
            solver.findRoot(function, checker, 1000, -2, -4, 2, 0);
            fail("Expected FunctionException to be thrown");
        } catch (FunctionException e) {
            assertEquals("Invalid function", e.getMessage());
        }
    }

    @Test
    public void testGetRoot() {
        // Check if the initial root value is NaN
        assertTrue(Double.isNaN(solver.getRoot()));

        // Set root value to 2
        solver.root = 2;

        // Check if the getRoot method returns 2
        assertEquals(2, solver.getRoot(), 1e-6);
    }
}

