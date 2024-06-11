import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    // Test case 1: Function has a root in the interval [1.0, 2.0]
    ComputableFunction function1 = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x - 1.5;
      }
    };
    ConvergenceChecker checker1 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) throws FunctionException {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        }
        if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        }
        return NONE;
      }
    };
    double x01 = 1.0, f01 = function1.valueAt(x01);
    double x11 = 2.0, f11 = function1.valueAt(x11);
    assertTrue(solver.findRoot(function1, checker1, 100, x01, f01, x11, f11));
    assertEquals(1.5, solver.getRoot(), 1e-6);

    // Test case 2: Function has no root in the interval [1.0, 2.0]
    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x + 1.0;
      }
    };
    ConvergenceChecker checker2 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) throws FunctionException {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        }
        if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        }
        return NONE;
      }
    };
    double x02 = 1.0, f02 = function2.valueAt(x02);
    double x12 = 2.0, f12 = function2.valueAt(x12);
    assertFalse(solver.findRoot(function2, checker2, 100, x02, f02, x12, f12));
  }

  @Test
  public void testGetRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x - 1.5;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) throws FunctionException {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        }
        if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        }
        return NONE;
      }
    };
    double x0 = 1.0, f0 = function.valueAt(x0);
    double x1 = 2.0, f1 = function.valueAt(x1);
    solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertEquals(1.5, solver.getRoot(), 1e-6);
  }

}