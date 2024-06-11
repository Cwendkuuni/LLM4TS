import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot_typicalCase() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 16;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 0.01) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < 0.01) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };
        int maxIter = 100;
        double x0 = -10;
        double f0 = function.valueAt(x0);
        double x1 = 10;
        double f1 = function.valueAt(x1);

        assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertEquals(4, solver.getRoot(), 0.01);
    }

    @Test
    public void testFindRoot_edgeCase() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 16;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 0.01) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < 0.01) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };
        int maxIter = 100;
        double x0 = -4;
        double f0 = function.valueAt(x0);
        double x1 = 4;
        double f1 = function.valueAt(x1);

        assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertEquals(4, solver.getRoot(), 0.01);
    }

    @Test
    public void testFindRoot_errorCase() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 16;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 0.01) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < 0.01) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };
        int maxIter = 100;
        double x0 = 2;
        double f0 = function.valueAt(x0);
        double x1 = 3;
        double f1 = function.valueAt(x1);

        assertFalse(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertTrue(Double.isNaN(solver.getRoot()));
    }
}