package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/** Test case for BrentSolver class. */
public class BrentSolverTest {

  /** Function used for testing. */
  private static class TestFunction implements ComputableFunction {
    @Override
    public double valueAt(double x) {
      return Math.pow(x, 3) - 2;
    }
  }

  /** Checker used for testing. */
  private static class TestChecker implements ConvergenceChecker {
    @Override
    public int converged(double x, double fx, double xPrev, double fxPrev) {
      if (Math.abs(fx) < 0.0001) {
        return ConvergenceChecker.LOW;
      } else if (Math.abs(fxPrev) < 0.0001) {
        return ConvergenceChecker.HIGH;
      } else {
        return ConvergenceChecker.NONE;
      }
    }
  }

  /** Test finding a root in a given interval. */
  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    MockFunction function = new MockFunction();
    TestChecker checker = new TestChecker();

    double x0 = 1.0;
    double x1 = 2.0;
    double expectedRoot = 1.3320671785336525; // root of x^3 - 2

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(expectedRoot, solver.getRoot(), 0.0001);
  }

  /** Test handling maximal number of iterations. */
  @Test(expected = FunctionException.class)
  public void testMaxIterations() {
    BrentSolver solver = new BrentSolver();
    MockFunction function = new MockFunction();
    TestChecker checker = new TestChecker();

    double x0 = 1.0;
    double x1 = 2.0;

    solver.findRoot(function, checker, 0, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }
}