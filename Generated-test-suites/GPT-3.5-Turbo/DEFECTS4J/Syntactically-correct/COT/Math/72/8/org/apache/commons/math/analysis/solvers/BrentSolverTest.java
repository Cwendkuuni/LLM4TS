
import org.junit.Assert;
import org.junit.Test;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    int maxIter = 100;
    double x0 = 0;
    double f0 = -4;
    double x1 = 2;
    double f1 = 0;

    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    Assert.assertTrue(rootFound);
    Assert.assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_NoRootInInterval() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x + 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    int maxIter = 100;
    double x0 = 0;
    double f0 = 4;
    double x1 = 2;
    double f1 = 8;

    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    Assert.assertFalse(rootFound);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionError() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Function error");
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    int maxIter = 100;
    double x0 = 0;
    double f0 = -4;
    double x1 = 2;
    double f1 = 0;

    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
  }

}

