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
    public void testFindRoot_NormalCase() throws FunctionException {
        double expectedRoot = 2.0;

        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x - 2;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (fLow == 0) {
                    return ConvergenceChecker.LOW;
                } else if (fHigh == 0) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        int maxIter = 100;
        double x0 = 1.0;
        double f0 = -1.0;
        double x1 = 3.0;
        double f1 = 1.0;

        boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        assertTrue(result);
        assertEquals(expectedRoot, solver.getRoot(), 0.00001);
    }

    @Test
    public void testFindRoot_InvalidInterval() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return ConvergenceChecker.NONE;
            }
        };

        int maxIter = 100;
        double x0 = 2.0;
        double f0 = 0.0;
        double x1 = 1.0;
        double f1 = 0.0;

        boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        assertFalse(result);
        assertEquals(Double.NaN, solver.getRoot(), 0.00001);
    }

    @Test
    public void testFindRoot_FunctionException() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                throw new FunctionException("Function evaluation failed");
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return ConvergenceChecker.NONE;
            }
        };

        int maxIter = 100;
        double x0 = 1.0;
        double f0 = -1.0;
        double x1 = 3.0;
        double f1 = 1.0;

        boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        assertFalse(result);
        assertEquals(Double.NaN, solver.getRoot(), 0.00001);
    }

    @Test
    public void testFindRoot_ConvergenceChecker_High() throws FunctionException {
        double expectedRoot = 3.0;

        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x - 3;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (fHigh == 0) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        int maxIter = 100;
        double x0 = 1.0;
        double f0 = -2.0;
        double x1 = 2.0;
        double f1 = -1.0;

        boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        assertTrue(result);
        assertEquals(expectedRoot, solver.getRoot(), 0.00001);
    }

    @Test
    public void testFindRoot_ConvergenceChecker_Low() throws FunctionException {
        double expectedRoot = 1.0;

        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x - 1;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (fLow == 0) {
                    return ConvergenceChecker.LOW;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        int maxIter = 100;
        double x0 = 0.0;
        double f0 = -1.0;
        double x1 = 2.0;
        double f1 = 1.0;

        boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        assertTrue(result);
        assertEquals(expectedRoot, solver.getRoot(), 0.00001);
    }

}