import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    private BrentSolver solver;
    private ComputableFunction function;
    private ConvergenceChecker checker;

    @Before
    public void setup() {
        solver = new BrentSolver();
        function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4.0;
            }
        };
        checker = new ConvergenceChecker() {
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
    }

    @Test
    public void testFindRoot() throws FunctionException {
        assertTrue(solver.findRoot(function, checker, 1000, 1.0, -3.0, 3.0, 7.0));
        assertEquals(2.0, solver.getRoot(), 1e-6);
    }

    @Test
    public void testFindRootNoRootInInterval() throws FunctionException {
        assertFalse(solver.findRoot(function, checker, 1000, 1.0, 7.0, 3.0, 12.0));
    }

    @Test
    public void testFindRootMaxIterationsReached() throws FunctionException {
        assertFalse(solver.findRoot(function, checker, 10, 1.0, -3.0, 3.0, 7.0));
    }

}