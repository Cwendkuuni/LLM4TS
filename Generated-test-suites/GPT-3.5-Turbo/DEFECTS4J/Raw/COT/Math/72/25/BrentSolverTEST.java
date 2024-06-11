import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.*;

public class BrentSolverTest {

    private BrentSolver solver;
    private static final double EPSILON = Math.pow(2.0, -52);

    @Before
    public void setUp() {
        solver = new BrentSolver();
    }

    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < EPSILON) {
                    return LOW;
                } else if (Math.abs(fHigh) < EPSILON) {
                    return HIGH;
                } else {
                    return UNCONVERGED;
                }
            }
        };

        double x0 = 0.0;
        double f0 = function.valueAt(x0);
        double x1 = 10.0;
        double f1 = function.valueAt(x1);

        boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

        assertTrue(rootFound);
        assertEquals(2.0, solver.getRoot(), EPSILON);
    }

    @Test
    public void testFindRoot_NoRootInInterval() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < EPSILON) {
                    return LOW;
                } else if (Math.abs(fHigh) < EPSILON) {
                    return HIGH;
                } else {
                    return UNCONVERGED;
                }
            }
        };

        double x0 = 0.0;
        double f0 = function.valueAt(x0);
        double x1 = 10.0;
        double f1 = function.valueAt(x1);

        boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

        assertFalse(rootFound);
    }

    // Add additional test cases here with identified edge cases and exceptions

}