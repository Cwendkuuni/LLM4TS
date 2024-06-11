import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return ROOT_FOUND;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return ROOT_FOUND;
                } else {
                    return CONTINUE;
                }
            }
        };
        double x0 = -10.0;
        double f0 = function.valueAt(x0);
        double x1 = 10.0;
        double f1 = function.valueAt(x1);
        int maxIter = 100;
        boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        assertTrue(rootFound);
        assertEquals(2.0, solver.getRoot(), 1e-6);
    }

    // TODO: Add more test cases to cover different scenarios

}