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
    public void testFindRoot_SimpleFunction_ReturnsTrue() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x - 5;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xMin, double fMin, double xMax, double fMax) {
                return 0;
            }
        };
        int maxIter = 100;
        double x0 = 0;
        double f0 = function.valueAt(x0);
        double x1 = 10;
        double f1 = function.valueAt(x1);

        assertTrue(brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertEquals(5.0, brentSolver.getRoot(), 1e-9);
    }

    @Test
    public void testFindRoot_NoRootInInterval_ReturnsFalse() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xMin, double fMin, double xMax, double fMax) {
                return 0;
            }
        };
        int maxIter = 100;
        double x0 = 0;
        double f0 = function.valueAt(x0);
        double x1 = 1;
        double f1 = function.valueAt(x1);

        assertFalse(brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    }

    @Test(expected = FunctionException.class)
    public void testFindRoot_FunctionThrowsException_ThrowsFunctionException() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                throw new FunctionException("Function error");
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xMin, double fMin, double xMax, double fMax) {
                return 0;
            }
        };
        int maxIter = 100;
        double x0 = 0;
        double f0 = function.valueAt(x0);
        double x1 = 1;
        double f1 = function.valueAt(x1);

        brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    }

    @Test
    public void testFindRoot_HighAccuracy_ReturnsTrue() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xMin, double fMin, double xMax, double fMax) {
                return 0;
            }
        };
        int maxIter = 100;
        double x0 = 0;
        double f0 = function.valueAt(x0);
        double x1 = 10;
        double f1 = function.valueAt(x1);

        assertTrue(brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertEquals(2.0, brentSolver.getRoot(), 1e-9);
    }

}