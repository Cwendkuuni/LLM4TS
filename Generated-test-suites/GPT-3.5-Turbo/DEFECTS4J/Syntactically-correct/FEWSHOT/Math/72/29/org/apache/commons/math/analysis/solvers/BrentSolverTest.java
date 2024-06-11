import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (fLow >= 0 && fHigh <= 0) {
                    return ROOT_FOUND;
                } else if (fLow < 0 && fHigh > 0) {
                    return NOT_CONVERGED;
                } else {
                    return OUT_OF_RANGE;
                }
            }
        };

        boolean rootFound = solver.findRoot(function, checker, 100, 0.0, -4.0, 5.0, 9.0);
        assertTrue(rootFound);
        assertEquals(2.0, solver.getRoot(), 0.00001);
    }

    @Test
    public void testFindRootWithNoRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x + 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (fLow >= 0 && fHigh <= 0) {
                    return ROOT_FOUND;
                } else if (fLow < 0 && fHigh > 0) {
                    return NOT_CONVERGED;
                } else {
                    return OUT_OF_RANGE;
                }
            }
        };

        boolean rootFound = solver.findRoot(function, checker, 100, 0.0, 4.0, 5.0, 9.0);
        assertFalse(rootFound);
    }
}