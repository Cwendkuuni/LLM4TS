import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

    private BrentSolver solver;

    @Before
    public void setup() {
        solver = new BrentSolver();
    }

    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (fLow * fHigh < 0) {
                    return HIGH;
                } else if (Math.abs(fLow) < Double.MIN_VALUE) {
                    return LOW;
                } else {
                    return NO_CONVERGENCE;
                }
            }
        };

        assertTrue(solver.findRoot(function, checker, 100, 0, -4, 3, 5));
        assertEquals(2.0, solver.getRoot(), 0.0001);

        assertFalse(solver.findRoot(function, checker, 100, 1, -3, 2, 6));

        assertTrue(solver.findRoot(function, checker, 100, -2, 0, 4, 0));
        assertEquals(0.0, solver.getRoot(), 0.0001);
    }
}