import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

import static org.junit.Assert.*;

public class BrentSolverTest {

    private BrentSolver solver;

    @Before
    public void setUp() {
        solver = new BrentSolver();
    }

    @Test
    public void testFindRoot() {
        // Typical use case - root is found within the given interval
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (fLow == 0) {
                    return LOW;
                } else if (fHigh == 0) {
                    return HIGH;
                } else {
                    return UNCONVERGED;
                }
            }
        };
        int maxIter = 100;
        double x0 = 0;
        double f0 = -4;
        double x1 = 3;
        double f1 = 5;
        try {
            boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
            assertTrue(rootFound);
            assertEquals(2.0, solver.getRoot(), 1e-15);
        } catch (FunctionException e) {
            fail("Exception should not be thrown");
        }

        // Edge case - interval is of size 0
        x0 = 1;
        f0 = -3;
        x1 = 1;
        f1 = -3;
        try {
            boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
            assertTrue(rootFound);
            assertEquals(1.0, solver.getRoot(), 1e-15);
        } catch (FunctionException e) {
            fail("Exception should not be thrown");
        }

        // Error scenario - no root within the given interval
        x0 = 0;
        f0 = -4;
        x1 = 1;
        f1 = -3;
        try {
            boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
            assertFalse(rootFound);
        } catch (FunctionException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetRoot() {
        double expectedRoot = 2.0;
        solver = new BrentSolver();
        assertEquals(expectedRoot, solver.getRoot(), 1e-15);
    }

}