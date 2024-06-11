import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    private BrentSolver solver;

    @Before
    public void setUp() {
        solver = new BrentSolver();
    }

    @Test
    public void testFindRootWithValidFunctionAndInterval() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x1, double f1, double x2, double f2) {
                return (f2 == 0) ? ConvergenceChecker.HIGH : ConvergenceChecker.NONE;
            }
        };
        double x0 = 1;
        double f0 = -3;
        double x1 = 2;
        double f1 = 0;
        assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
        assertEquals(2.0, solver.getRoot(), 0.0001);
    }

    @Test
    public void testFindRootWithInvalidFunctionAndInterval() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                if (x == 0) {
                    throw new FunctionException("Cannot divide by zero");
                }
                return 1 / x;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x1, double f1, double x2, double f2) {
                return (f2 == 0) ? ConvergenceChecker.HIGH : ConvergenceChecker.NONE;
            }
        };
        double x0 = 1;
        double f0 = 1;
        double x1 = 2;
        double f1 = 0;
        try {
            solver.findRoot(function, checker, 100, x0, f0, x1, f1);
            fail("Expected FunctionException to be thrown");
        } catch (FunctionException e) {
            assertEquals("Cannot divide by zero", e.getMessage());
        }
    }

    @Test
    public void testFindRootWithMaxIterationsExceeded() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return Math.sin(x);
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x1, double f1, double x2, double f2) {
                return (f2 == 0) ? ConvergenceChecker.HIGH : ConvergenceChecker.NONE;
            }
        };
        double x0 = 0;
        double f0 = 0;
        double x1 = Math.PI;
        double f1 = 0;
        assertFalse(solver.findRoot(function, checker, 10, x0, f0, x1, f1));
    }

    @Test
    public void testFindRootWithNoRootInInterval() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x + 1;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x1, double f1, double x2, double f2) {
                return (f2 == 0) ? ConvergenceChecker.HIGH : ConvergenceChecker.NONE;
            }
        };
        double x0 = -2;
        double f0 = 5;
        double x1 = 0;
        double f1 = 1;
        assertFalse(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    }

}