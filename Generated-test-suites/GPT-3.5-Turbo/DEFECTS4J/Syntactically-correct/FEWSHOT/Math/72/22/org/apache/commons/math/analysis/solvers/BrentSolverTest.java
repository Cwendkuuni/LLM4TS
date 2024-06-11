import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentSolverTest {

    private BrentSolver brentSolver;

    @Before
    public void setUp() {
        brentSolver = new BrentSolver();
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
                if (Math.abs(fLow) < 1e-6) {
                    return LOW;
                }
                if (Math.abs(fHigh) < 1e-6) {
                    return HIGH;
                }
                return NONE;
            }
        };

        assertFalse(brentSolver.findRoot(function, checker, 10, 2.0, 0.0, 4.0, 0.0));
        assertEquals(2.000000015530705, brentSolver.getRoot(), 1e-6);
    }

    @Test
    public void testGetRoot() {
        assertEquals(Double.NaN, brentSolver.getRoot(), 0.0);
    }
}