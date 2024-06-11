package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionMock;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    double x0 = 0.0;
    double f0 = 1.0;
    double x1 = 2.0;
    double f1 = -1.0;
    ComputableFunction function = new FunctionMock(0.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xOld, double fxOld) {
        if (Math.abs(fx) < 0.001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fxOld) < 0.001) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    BrentSolver solver = new BrentSolver();

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertTrue(Math.abs(solver.getRoot() - 1.0) < 0.001);

    x0 = 0.0;
    f0 = -1.0;
    x1 = 2.0;
    f1 = 1.0;
    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertTrue(Math.abs(solver.getRoot() - 2.0) < 0.001);

    x0 = 0.0;
    f0 = 1.0;
    x1 = 0.0;
    f1 = -1.0;
    assertFalse(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertNull(solver.getRoot());
  }

}