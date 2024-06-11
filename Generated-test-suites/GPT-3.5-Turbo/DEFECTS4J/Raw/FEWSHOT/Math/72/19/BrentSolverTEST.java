import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static junit.framework.TestCase.*;

public class BrentSolverTest {
    
    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        
        // Test case 1: Root found in the given interval
        ComputableFunction function1 = new ComputableFunction() {
            public double valueAt(double x) {
                return x*x - 4;
            }
        };
        ConvergenceChecker checker1 = new ConvergenceChecker() {
            public int converged(double x0, double f0, double x1, double f1) {
                if (Math.abs(f1) <= 1e-6) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(f0) <= 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NOT_CONVERGED;
                }
            }
        };
        boolean result1 = solver.findRoot(function1, checker1, 100, -10, 96, 10, -96);
        assertTrue(result1);
        assertEquals(2.0, solver.getRoot(), 1e-6);
        
        // Test case 2: No root found in the given interval
        ComputableFunction function2 = new ComputableFunction() {
            public double valueAt(double x) {
                return Math.sin(x);
            }
        };
        ConvergenceChecker checker2 = new ConvergenceChecker() {
            public int converged(double x0, double f0, double x1, double f1) {
                if (Math.abs(f1) <= 1e-6) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(f0) <= 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NOT_CONVERGED;
                }
            }
        };
        boolean result2 = solver.findRoot(function2, checker2, 100, -10, 96, 10, -96);
        assertFalse(result2);
        assertEquals(Double.NaN, solver.getRoot());
    }
}