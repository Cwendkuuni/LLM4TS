import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 5;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
         if (fLow * fHigh < 0) {
           return ConvergenceChecker.LOW;
         } else if (fLow * fHigh > 0) {
           return ConvergenceChecker.HIGH;
         } else {
           return ConvergenceChecker.NOTHING;
         }
      }
    };
    int maxIter = 1000;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(result);
    assertEquals(2.236, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRootNoRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x + 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
         if (fLow * fHigh < 0) {
           return ConvergenceChecker.LOW;
         } else if (fLow * fHigh > 0) {
           return ConvergenceChecker.HIGH;
         } else {
           return ConvergenceChecker.NOTHING;
         }
      }
    };
    int maxIter = 1000;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertFalse(result);
  }

  // Additional test cases for edge cases and exceptions

  @Test
  public void testFindRootFunctionException() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Function error");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
         if (fLow * fHigh < 0) {
           return ConvergenceChecker.LOW;
         } else if (fLow * fHigh > 0) {
           return ConvergenceChecker.HIGH;
         } else {
           return ConvergenceChecker.NOTHING;
         }
      }
    };
    int maxIter = 1000;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);

    try {
      boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      fail("Expected FunctionException to be thrown");
    } catch (FunctionException e) {
      // Exception correctly thrown
    }
  }

  @Test
  public void testFindRootMaxIterReached() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 5;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
         if (fLow * fHigh < 0) {
           return ConvergenceChecker.LOW;
         } else if (fLow * fHigh > 0) {
           return ConvergenceChecker.HIGH;
         } else {
           return ConvergenceChecker.NOTHING;
         }
      }
    };
    int maxIter = 5;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertFalse(result);
  }

}