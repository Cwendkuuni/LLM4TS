import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

import static org.junit.Assert.*;

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
                return x * x - 4;
            }
        };
        checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return (Math.abs(fLow) < 1e-6) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
            }
        };
    }

    @Test
    public void testFindRoot() throws FunctionException {
        assertTrue(solver.findRoot(function, checker, 100, -10, -100, 10, 100));
        assertEquals(2.0, solver.getRoot(), 1e-6);
    }

}