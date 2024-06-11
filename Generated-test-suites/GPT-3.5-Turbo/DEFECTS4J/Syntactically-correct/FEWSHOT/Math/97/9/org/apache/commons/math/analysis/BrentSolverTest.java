import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

    private BrentSolver solver;

    @Before
    public void setUp() {
        solver = new BrentSolver();
    }

    @Test
    public void testFindRootLowConvergence() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x1, double f1, double x2, double f2) {
                return ConvergenceChecker.LOW;
            }
        };

        assertTrue(solver.findRoot(function, checker, 100, 0.0, 0.0, 1.0, 1.0));
        assertEquals(0.0, solver.getRoot(), 0.0);
    }

    @Test
    public void testFindRootHighConvergence() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x1, double f1, double x2, double f2) {
                return ConvergenceChecker.HIGH;
            }
        };

        assertTrue(solver.findRoot(function, checker, 100, 0.0, 0.0, 1.0, 1.0));
        assertEquals(1.0, solver.getRoot(), 0.0);
    }

    @Test
    public void testFindRootNoConvergence() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x1, double f1, double x2, double f2) {
                return ConvergenceChecker.NO_CONVERGENCE;
            }
        };

        assertFalse(solver.findRoot(function, checker, 100, 0.0, 0.0, 1.0, 1.0));
    }

    @Test
    public void testFindRootBisectionMethod() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 1.0;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x1, double f1, double x2, double f2) {
                if (Math.abs(f1) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(f2) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NO_CONVERGENCE;
                }
            }
        };

        assertTrue(solver.findRoot(function, checker, 100, -2.0, 3.0, 2.0, -3.0));
        assertEquals(1.0, solver.getRoot(), 1e-6);
    }

    @Test
    public void testFindRootSecantMethod() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 1.0;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x1, double f1, double x2, double f2) {
                if (Math.abs(f1) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(f2) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NO_CONVERGENCE;
                }
            }
        };

        assertTrue(solver.findRoot(function, checker, 100, -2.0, 3.0, 2.0, -3.0));
        assertEquals(1.0, solver.getRoot(), 1e-6);
    }
}