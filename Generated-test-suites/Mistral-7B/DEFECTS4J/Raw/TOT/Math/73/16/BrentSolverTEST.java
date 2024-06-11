package org.spaceroots.mantissa.roots;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        if (Math.abs(fx) < 0.001) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(fx) > 0.01) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };
  }

  @After
  public void tearDown() {
    solver = null;
    checker = null;
  }

  @Test
  public void testFindRootQuadraticFunction() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };

    double x0 = 1.0;
    double x1 = 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    double expectedRoot = 2.0;
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRootLinearFunction() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x - 1.0;
      }
    };

    double x0 = 0.0;
    double x1 = 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    double expectedRoot = 1.0;
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRootFunctionWithNoRoots() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };

    double x0 = 0.0;
    double x1 = Math.PI;

    assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertNotNull(solver.getRoot());
  }

  @Test(expected = FunctionException.class)
  public void testFindRootFunctionWithDivisionByZero() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return 1.0 / (x - 1.0);
      }
    };

    double x0 = 0.0;
    double x1 = 1.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }
}