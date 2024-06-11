import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    // Test 1: simple linear function with root at x = 0
    ComputableFunction function1 = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x;
      }
    };

    ConvergenceChecker checker1 = new ConvergenceChecker() {
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

    assertTrue(solver.findRoot(function1, checker1, 100, -1, -1, 1, 1));
    assertEquals(0.0, solver.getRoot(), 1e-6);


    // Test 2: quadratic function with roots at x=-1 and x=1
    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 1;
      }
    };

    ConvergenceChecker checker2 = new ConvergenceChecker() {
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

    assertTrue(solver.findRoot(function2, checker2, 100, -2, 3, -2, 3));
    assertEquals(-1.0, solver.getRoot(), 1e-6);
    assertTrue(solver.findRoot(function2, checker2, 100, -2, 3, 0, -1));
    assertEquals(1.0, solver.getRoot(), 1e-6);


    // Test 3: trigonometric function with root at x = pi/2
    ComputableFunction function3 = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return Math.sin(x) - 1;
      }
    };

    ConvergenceChecker checker3 = new ConvergenceChecker() {
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

    assertTrue(solver.findRoot(function3, checker3, 100, 1.0, -1, 2.0, 1));
    assertEquals(Math.PI/2, solver.getRoot(), 1e-6);

  }
}