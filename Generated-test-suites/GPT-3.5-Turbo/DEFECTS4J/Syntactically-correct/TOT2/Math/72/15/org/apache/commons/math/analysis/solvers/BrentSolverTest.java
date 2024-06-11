import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        // Typical use case
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker();
        int maxIter = 100;
        double x0 = 1.0;
        double f0 = -3.0;
        double x1 = 2.0;
        double f1 = 0.0;
        assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    }

    @Test
    public void testZeroIterations() {
        // Edge case: zero iterations allowed
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker();
        int maxIter = 0;
        double x0 = 1.0;
        double f0 = -3.0;
        double x1 = 2.0;
        double f1 = 0.0;
        assertFalse(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    }

    @Test(expected = FunctionException.class)
    public void testFunctionException() throws FunctionException {
        // Error scenario: FunctionException thrown
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                throw new FunctionException("Function not implemented");
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker();
        int maxIter = 100;
        double x0 = 1.0;
        double f0 = -3.0;
        double x1 = 2.0;
        double f1 = 0.0;
        solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    }

    @Test
    public void testGetRoot() {
        // Typical use case for getRoot
        BrentSolver solver = new BrentSolver();
        solver.findRoot(new ComputableFunction() {
            public double valueAt(double x) {
                return Math.sin(x);
            }
        }, new ConvergenceChecker(), 100, 0.0, -1.0, Math.PI, 1.0);
        assertEquals(Math.PI, solver.getRoot(), 0.0001);
    }
}