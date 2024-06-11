import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    // Test case 1
    ComputableFunction function1 = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };
    ConvergenceChecker checker1 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-5) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-5) {
          return HIGH;
        } else {
          return CONTINUE;
        }
      }
    };

    boolean rootFound1 = solver.findRoot(function1, checker1, 100, 0, -4, 2, 0);
    assertTrue(rootFound1);
    assertEquals(2, solver.getRoot(), 1e-5);

    // Test case 2
    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.pow(x, 3) - 10;
      }
    };
    ConvergenceChecker checker2 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-5) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-5) {
          return HIGH;
        } else {
          return CONTINUE;
        }
      }
    };

    boolean rootFound2 = solver.findRoot(function2, checker2, 100, 1, -9, 2, -2);
    assertTrue(rootFound2);
    assertEquals(2, solver.getRoot(), 1e-5);
  }

  @Test
  public void testGetRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-5) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-5) {
          return HIGH;
        } else {
          return CONTINUE;
        }
      }
    };

    boolean rootFound = solver.findRoot(function, checker, 100, 0, -4, 2, 0);
    assertTrue(rootFound);
    assertEquals(2, solver.getRoot(), 1e-5);
  }
}