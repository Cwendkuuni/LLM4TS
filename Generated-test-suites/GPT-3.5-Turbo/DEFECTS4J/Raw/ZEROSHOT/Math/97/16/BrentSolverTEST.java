import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        // Test 1
        ComputableFunction function1 = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4.0;
            }
        };
        ConvergenceChecker checker1 = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(xHigh - xLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else {
                    return ConvergenceChecker.CONTINUE;
                }
            }
        };
        double x0 = 0.0;
        double f0 = function1.valueAt(x0);
        double x1 = 3.0;
        double f1 = function1.valueAt(x1);
        
        assertTrue(solver.findRoot(function1, checker1, 100, x0, f0, x1, f1));
        assertEquals(2.0, solver.getRoot(), 1e-6);

        // Test 2
        ComputableFunction function2 = new ComputableFunction() {
            public double valueAt(double x) {
                return Math.exp(x) - 2.0;
            }
        };
        ConvergenceChecker checker2 = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else {
                    return ConvergenceChecker.CONTINUE;
                }
            }
        };
        x0 = 0.0;
        f0 = function2.valueAt(x0);
        x1 = 1.0;
        f1 = function2.valueAt(x1);
        
        assertTrue(solver.findRoot(function2, checker2, 100, x0, f0, x1, f1));
        assertEquals(0.693147, solver.getRoot(), 1e-6);
    }
}