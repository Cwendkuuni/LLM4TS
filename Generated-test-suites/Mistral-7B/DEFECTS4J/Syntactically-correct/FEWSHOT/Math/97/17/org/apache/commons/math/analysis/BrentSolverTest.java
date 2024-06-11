package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_converged_low() throws Exception {
    ComputableFunction function = new MockComputableFunction(1.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xLow, double fLow) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;
    double expectedRoot = root;

    assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_converged_high() throws Exception {
    ComputableFunction function = new MockComputableFunction(-1.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xLow, double fLow) {
        return ConvergenceChecker.HIGH;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 0.5;
    double expectedRoot = root;

    assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_converged() throws Exception {
    ComputableFunction function = new MockComputableFunction(0.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xLow, double fLow) {
        return ConvergenceChecker.CONVERGED;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;
    double expectedRoot = root;

    assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_noConvergence() throws Exception {
    ComputableFunction function = new MockComputableFunction(Math.sin(Math.PI));
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xLow, double fLow) {
        return ConvergenceChecker.NO_CONVERGENCE;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double x1 = 2.0;
    double expectedRoot = 1.1111111111111111; // approximated value of sin(PI)

    assertFalse(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertNull(solver.getRoot());
  }
}

class MockComputableFunction implements ComputableFunction {
  private double value;

  public MockComputableFunction(double value) {
    this.value = value;
  }

  @Override
  public double valueAt(double x) {
    return value;
  }
}

interface ConvergenceChecker {
  int converged(double x, double f, double xLow, double fLow);

  static final int LOW = 0;
  static final int HIGH = 1;
  static final int NO_CONVERGENCE = 2;
  static final int CONVERGED = 3;
}