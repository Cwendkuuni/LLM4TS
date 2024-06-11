import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;

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
        if (fLow > 0 && fHigh < 0) {
          return ConvergenceChecker.LOW;
        } else if (fLow < 0 && fHigh > 0) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, 100, -10, -50, 10, 50);
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRoot_NotFound() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow > 0 && fHigh < 0) {
          return ConvergenceChecker.LOW;
        } else if (fLow < 0 && fHigh > 0) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, 5, -10, -50, 10, 50);
    assertFalse(rootFound);
  }

  @Test
  public void testGetRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow > 0 && fHigh < 0) {
          return ConvergenceChecker.LOW;
        } else if (fLow < 0 && fHigh > 0) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, 100, -10, -50, 10, 50);
    assertEquals(2.0, solver.getRoot(), 0.0001);
  }

}