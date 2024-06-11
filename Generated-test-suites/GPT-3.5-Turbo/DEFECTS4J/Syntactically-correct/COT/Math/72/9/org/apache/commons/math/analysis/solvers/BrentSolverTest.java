import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

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
                } else if (Math.abs(fHigh) < 1e-6) {
                    return HIGH;
                } else {
                    return NONE;
                }
            }
        };
        int maxIter = 100;
        double x0 = 0;
        double f0 = function.valueAt(x0);
        double x1 = 5;
        double f1 = function.valueAt(x1);

        BrentSolver solver = new BrentSolver();
        boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        Assert.assertTrue(result);
        Assert.assertEquals(2.0, solver.getRoot(), 1e-6);
    }

    // Additional test cases for potential edge cases and exceptions:

    @Test
    public void testFindRootWithSameFunctionValues() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - x - 6;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return HIGH;
                } else {
                    return NONE;
                }
            }
        };
        int maxIter = 100;
        double x0 = 2;
        double f0 = function.valueAt(x0);
        double x1 = 3;
        double f1 = function.valueAt(x1);

        BrentSolver solver = new BrentSolver();
        boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        Assert.assertTrue(result);
        Assert.assertEquals(2.0, solver.getRoot(), 1e-6);
    }

    @Test(expected = FunctionException.class)
    public void testFindRootWithException() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                throw new FunctionException("Function evaluation failed");
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return HIGH;
                } else {
                    return NONE;
                }
            }
        };
        int maxIter = 100;
        double x0 = 0;
        double f0 = function.valueAt(x0);
        double x1 = 5;
        double f1 = function.valueAt(x1);

        BrentSolver solver = new BrentSolver();
        solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    }
}