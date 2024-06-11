import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;

public class BrentSolverTest {

    private BrentSolver solver;

    @Test
    public void testFindRoot() throws FunctionException {
        solver = new BrentSolver();

        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 1;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xl, double fl, double xu, double fu) {
                if (xl * xu < 0) {
                    return ConvergenceChecker.LOW;
                } else if (fl * fu < 0) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        double x0 = -2;
        double f0 = function.valueAt(x0);
        double x1 = 2;
        double f1 = function.valueAt(x1);

        assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
        assertEquals(1.0, solver.getRoot(), 1e-6);
    }

    @Test
    public void testGetRoot() {
        solver = new BrentSolver();
        solver.root = 2.0;
        assertEquals(2.0, solver.getRoot(), 0.0);
    }
}