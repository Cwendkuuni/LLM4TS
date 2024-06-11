import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
    
    private static final double epsilon = Math.pow(2.0, -52);

    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < epsilon) {
                    return ConvergenceChecker.LOW;
                }
                if (Math.abs(fHigh) < epsilon) {
                    return ConvergenceChecker.HIGH;
                }
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };
        int maxIter = 100;
        double x0 = -10;
        double f0 = function.valueAt(x0);
        double x1 = 10;
        double f1 = function.valueAt(x1);
        
        boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        double root = solver.getRoot();
        
        Assert.assertTrue(rootFound);
        Assert.assertEquals(2, root, epsilon);
    }
  
    // Additional test cases for potential edge cases and exceptions
    @Test
    public void testFindRoot_withZeroMaxIter() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < epsilon) {
                    return ConvergenceChecker.LOW;
                }
                if (Math.abs(fHigh) < epsilon) {
                    return ConvergenceChecker.HIGH;
                }
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };
        int maxIter = 0;
        double x0 = -10;
        double f0 = function.valueAt(x0);
        double x1 = 10;
        double f1 = function.valueAt(x1);
        
        boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        double root = solver.getRoot();
        
        Assert.assertFalse(rootFound);
        Assert.assertTrue(Double.isNaN(root));
    }
  
    @Test
    public void testFindRoot_withInvalidInterval() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < epsilon) {
                    return ConvergenceChecker.LOW;
                }
                if (Math.abs(fHigh) < epsilon) {
                    return ConvergenceChecker.HIGH;
                }
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };
        int maxIter = 100;
        double x0 = 10;
        double f0 = function.valueAt(x0);
        double x1 = 5;
        double f1 = function.valueAt(x1);
        
        boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        double root = solver.getRoot();
        
        Assert.assertFalse(rootFound);
        Assert.assertTrue(Double.isNaN(root));
    }
  
    @Test(expected = FunctionException.class)
    public void testFindRoot_withExceptionInFunction() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                throw new FunctionException();
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < epsilon) {
                    return ConvergenceChecker.LOW;
                }
                if (Math.abs(fHigh) < epsilon) {
                    return ConvergenceChecker.HIGH;
                }
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };
        int maxIter = 100;
        double x0 = -10;
        double f0 = function.valueAt(x0);
        double x1 = 10;
        double f1 = function.valueAt(x1);
        
        boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    }
  
}