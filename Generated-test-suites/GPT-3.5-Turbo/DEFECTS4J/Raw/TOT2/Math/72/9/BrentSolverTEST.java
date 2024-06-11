import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testConstructor() {
        BrentSolver brentSolver = new BrentSolver();
        assertNotNull(brentSolver);
    }

    @Test
    public void testFindRoot() {
        BrentSolver brentSolver = new BrentSolver();
        ComputableFunction function = new ExampleFunction();
        ConvergenceChecker checker = new ExampleConvergenceChecker();
        int maxIter = 100;
        double x0 = -1.0;
        double f0 = function.valueAt(x0);
        double x1 = 1.0;
        double f1 = function.valueAt(x1);

        try {
            boolean rootFound = brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
            assertTrue(rootFound);
            double root = brentSolver.getRoot();
            assertEquals(0.0, function.valueAt(root), 1e-10);
        } catch (FunctionException e) {
            fail("Exception thrown in findRoot method");
        }
    }

    @Test
    public void testGetRoot() {
        BrentSolver brentSolver = new BrentSolver();
        double root = 1.0;
        brentSolver.root = root;
        assertEquals(root, brentSolver.getRoot(), 0.0);
    }

    // Edge Cases and Exception Handling

    @Test
    public void testFindRootWithInvalidFunction() {
        BrentSolver brentSolver = new BrentSolver();
        ComputableFunction function = null;
        ConvergenceChecker checker = new ExampleConvergenceChecker();
        int maxIter = 100;
        double x0 = -1.0;
        double f0 = 0.0;
        double x1 = 1.0;
        double f1 = 0.0;

        assertThrows(FunctionException.class, () ->
                brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    }

    @Test
    public void testFindRootWithConvergenceFailure() {
        BrentSolver brentSolver = new BrentSolver();
        ComputableFunction function = new ExampleFunction();
        ConvergenceChecker checker = new ExampleConvergenceChecker();
        int maxIter = 1;
        double x0 = -1.0;
        double f0 = function.valueAt(x0);
        double x1 = 1.0;
        double f1 = function.valueAt(x1);

        assertFalse(brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    }

    // Helper classes for testing

    private static class ExampleFunction implements ComputableFunction {
        @Override
        public double valueAt(double x) {
            return x * x - 4;
        }
    }

    private static class ExampleConvergenceChecker implements ConvergenceChecker {
        @Override
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            return 0;
        }
    }
}