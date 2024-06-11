import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BrentSolverTest {
    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x0, double f0, double x1, double f1) {
                if (Math.abs(x1 - x0) < 0.01) {
                    return LOW;
                } else if (Math.abs(f1 - f0) < 0.01) {
                    return HIGH;
                } else {
                    return UNDEFINED;
                }
            }
        };
        double x0 = 0.0;
        double f0 = function.valueAt(x0);
        double x1 = 5.0;
        double f1 = function.valueAt(x1);
        assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
        assertEquals(2.0, solver.getRoot(), 0.01);
    }

    @Test
    public void testFindRootNoRoot() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return x;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x0, double f0, double x1, double f1) {
                if (Math.abs(x1 - x0) < 0.01) {
                    return LOW;
                } else if (Math.abs(f1 - f0) < 0.01) {
                    return HIGH;
                } else {
                    return UNDEFINED;
                }
            }
        };
        double x0 = 0.0;
        double f0 = function.valueAt(x0);
        double x1 = 5.0;
        double f1 = function.valueAt(x1);
        assertFalse(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    }
}