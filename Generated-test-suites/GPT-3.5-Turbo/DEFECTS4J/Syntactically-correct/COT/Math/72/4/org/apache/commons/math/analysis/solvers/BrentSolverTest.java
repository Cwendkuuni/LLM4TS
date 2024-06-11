import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        // Test case 1: Basic test case
        // Create a dummy function
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 2;
            }
        };
        // Create a default convergence checker
        ConvergenceChecker checker = new DefaultConvergenceChecker();
        // Set maximum iterations to 100
        int maxIter = 100;
        // Set the interval [1, 2] which is known to contain a root
        double x0 = 1.0;
        double f0 = function.valueAt(x0);
        double x1 = 2.0;
        double f1 = function.valueAt(x1);
        // Create a BrentSolver object
        BrentSolver solver = new BrentSolver();
        // Call the findRoot method
        boolean found = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        // Assert the result
        assertTrue(found);
        // Assert the root value (within a certain tolerance)
        assertEquals(Math.sqrt(2), solver.getRoot(), 1e-6);
    }

    @Test
    public void testFindRootWithInvalidFunction() {
        // Test case 2: Test with an invalid function
        // Create a dummy function that always throws an exception
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                throw new RuntimeException("Invalid function");
            }
        };
        // Create a default convergence checker
        ConvergenceChecker checker = new DefaultConvergenceChecker();
        // Set maximum iterations to 100
        int maxIter = 100;
        // Set the interval [1, 2] which is known to contain a root
        double x0 = 1.0;
        double f0 = function.valueAt(x0);
        double x1 = 2.0;
        double f1 = function.valueAt(x1);
        // Create a BrentSolver object
        BrentSolver solver = new BrentSolver();
        // Call the findRoot method and expect it to throw a FunctionException
        assertThrows(FunctionException.class, () -> {
            solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        });
    }

    @Test
    public void testFindRootWithInvalidInterval() {
        // Test case 3: Test with an invalid interval
        // Create a dummy function
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 2;
            }
        };
        // Create a default convergence checker
        ConvergenceChecker checker = new DefaultConvergenceChecker();
        // Set maximum iterations to 100
        int maxIter = 100;
        // Set the interval [2, 1] which is invalid (should throw an exception)
        double x0 = 2.0;
        double f0 = function.valueAt(x0);
        double x1 = 1.0;
        double f1 = function.valueAt(x1);
        // Create a BrentSolver object
        BrentSolver solver = new BrentSolver();
        // Call the findRoot method and expect it to throw a FunctionException
        assertThrows(FunctionException.class, () -> {
            solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        });
    }
}