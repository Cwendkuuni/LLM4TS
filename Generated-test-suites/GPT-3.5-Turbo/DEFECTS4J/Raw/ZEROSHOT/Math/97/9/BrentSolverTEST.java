import org.junit.Test;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (Math.abs(fLow) < 1e-6) ? ConvergenceChecker.LOW :
               (Math.abs(fHigh) < 1e-6) ? ConvergenceChecker.HIGH :
               ConvergenceChecker.NOT_CONVERGED;
      }
    };

    int maxIter = 100;
    double x0 = -10.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRootNoRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (Math.abs(fLow) < 1e-6) ? ConvergenceChecker.LOW :
               (Math.abs(fHigh) < 1e-6) ? ConvergenceChecker.HIGH :
               ConvergenceChecker.NOT_CONVERGED;
      }
    };

    int maxIter = 100;
    double x0 = -10.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);

    assertFalse(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
  }
}