import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;

public class BrentSolverTest {

    private BrentSolver brentSolver;

    @Before
    public void setUp() {
        brentSolver = new BrentSolver();
    }

    @Test
    public void testFindRoot_withValidData_shouldReturnTrue() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (fLow < 0 && fHigh > 0) {
                    return ConvergenceChecker.LOW;
                } else if (fLow > 0 && fHigh < 0) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };
        int maxIter = 100;
        double x0 = 0;
        double f0 = -4;
        double x1 = 3;
        double f1 = 5;

        boolean rootFound = brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        assertTrue(rootFound);
        assertEquals(2.0, brentSolver.getRoot(), 0.001);
    }

    @Test
    public void testFindRoot_withInvalidInterval_shouldReturnFalse() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (fLow < 0 && fHigh > 0) {
                    return ConvergenceChecker.LOW;
                } else if (fLow > 0 && fHigh < 0) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };
        int maxIter = 100;
        double x0 = 0;
        double f0 = -4;
        double x1 = 0;
        double f1 = 5;

        boolean rootFound = brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        assertFalse(rootFound);
    }

    @Test(expected = FunctionException.class)
    public void testFindRoot_withExceptionInFunction_shouldThrowException() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                throw new FunctionException("Error in function");
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (fLow < 0 && fHigh > 0) {
                    return ConvergenceChecker.LOW;
                } else if (fLow > 0 && fHigh < 0) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };
        int maxIter = 100;
        double x0 = 0;
        double f0 = -4;
        double x1 = 3;
        double f1 = 5;

        brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    }

    @Test
    public void testFindRoot_withMaxIterationsExceeded_shouldReturnFalse() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (fLow < 0 && fHigh > 0) {
                    return ConvergenceChecker.LOW;
                } else if (fLow > 0 && fHigh < 0) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };
        int maxIter = 10;
        double x0 = 0;
        double f0 = -4;
        double x1 = 3;
        double f1 = 5;

        boolean rootFound = brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        assertFalse(rootFound);
    }
}