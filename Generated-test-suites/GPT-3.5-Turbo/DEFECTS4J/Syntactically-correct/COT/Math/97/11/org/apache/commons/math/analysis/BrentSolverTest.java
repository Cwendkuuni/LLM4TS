
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();

        // Test case 1: Basic functionality test
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-12) {
                    return LOW;
                } else if (Math.abs(fHigh) < 1e-12) {
                    return HIGH;
                } else {
                    return NOT_CONVERGED;
                }
            }
        };
        double x0 = -5;
        double f0 = function.valueAt(x0);
        double x1 = 5;
        double f1 = function.valueAt(x1);
        boolean result = solver.findRoot(function, checker, 1000, x0, f0, x1, f1);
        double root = solver.getRoot();
        assertEquals(true, result);
        assertEquals(2.0, root, 1e-12);

        // Additional test cases for edge cases and exception handling scenarios

        // Test case 2: Interval with no root
        x0 = -1;
        f0 = function.valueAt(x0);
        x1 = 1;
        f1 = function.valueAt(x1);
        result = solver.findRoot(function, checker, 1000, x0, f0, x1, f1);
        root = solver.getRoot();
        assertEquals(false, result);

        // Test case 3: Interval with multiple roots
        function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return (x - 2) * (x - 3) * (x - 4);
            }
        };
        x0 = 1;
        f0 = function.valueAt(x0);
        x1 = 5;
        f1 = function.valueAt(x1);
        result = solver.findRoot(function, checker, 1000, x0, f0, x1, f1);
        root = solver.getRoot();
        assertEquals(false, result);
    }

    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();
        double root = 2.0;
        solver.root = root;
        assertEquals(root, solver.getRoot(), 1e-12);
    }

}
