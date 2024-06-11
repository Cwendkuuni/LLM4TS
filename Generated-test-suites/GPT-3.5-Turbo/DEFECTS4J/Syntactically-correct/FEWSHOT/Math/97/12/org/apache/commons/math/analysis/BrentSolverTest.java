import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();

    // Test case 1: function with a single root within the interval
    ComputableFunction function1 = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return (x - 1) * (x - 1);
      }
    };
    ConvergenceChecker checker1 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(xHigh - xLow) < 1e-6) {
          return ConvergenceChecker.EQUAL;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
    double x0 = 0;
    double f0 = function1.valueAt(x0);
    double x1 = 2;
    double f1 = function1.valueAt(x1);
    
    try {
      boolean rootFound = solver.findRoot(function1, checker1, 10000, x0, f0, x1, f1);
      assertTrue(rootFound);
      assertEquals(1.0, solver.getRoot(), 1e-6);
    } catch (FunctionException e) {
      fail("An exception should not be thrown");
    }

    // Test case 2: function with no root within the interval
    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x + 1;
      }
    };
    ConvergenceChecker checker2 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(xHigh - xLow) < 1e-6) {
          return ConvergenceChecker.EQUAL;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
    
    x0 = -2;
    f0 = function2.valueAt(x0);
    x1 = 0;
    f1 = function2.valueAt(x1);
    
    try {
      boolean rootFound = solver.findRoot(function2, checker2, 10000, x0, f0, x1, f1);
      assertFalse(rootFound);
    } catch (FunctionException e) {
      fail("An exception should not be thrown");
    }

    // Test case 3: function with multiple roots within the interval
    ComputableFunction function3 = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return (x - 1) * (x + 2) * (x - 4);
      }
    };
    ConvergenceChecker checker3 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(xHigh - xLow) < 1e-6) {
          return ConvergenceChecker.EQUAL;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
    
    x0 = -5;
    f0 = function3.valueAt(x0);
    x1 = 5;
    f1 = function3.valueAt(x1);
    
    try {
      boolean rootFound = solver.findRoot(function3, checker3, 10000, x0, f0, x1, f1);
      assertTrue(rootFound);
      double root = solver.getRoot();
      assertTrue(Math.abs(root - 1) < 1e-6 || Math.abs(root + 2) < 1e-6 || Math.abs(root - 4) < 1e-6);
    } catch (FunctionException e) {
      fail("An exception should not be thrown");
    }
  }
}