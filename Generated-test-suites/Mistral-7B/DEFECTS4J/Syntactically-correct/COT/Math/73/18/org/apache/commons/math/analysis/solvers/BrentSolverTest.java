package org.spaceroots.mantissa.roots.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test cases for the BrentSolver class
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   */
  @Test
  public void testFindRoot() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return (fx * fxPrevious > 0) ? ConvergenceChecker.NONE : ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, -1.0, -1.5, -0.5, -0.75);
    assertTrue(result);
    assertEquals(-0.8535398236521353, solver.getRoot(), 1e-6);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with interval containing no root
   */
  @Test
  public void testFindRootNoRoot() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(Math.PI * x);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, 0.5, 1.5, 2.5, 3.5);
    assertFalse(result);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with exception handling
   */
  @Test(expected = FunctionException.class)
  public void testFindRootException() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        if (x > 1.0) {
          throw new FunctionException("Exception message");
        }
        return Math.pow(x, 3) - 2;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, 100, -1.0, -1.5, -0.5, -0.75);
  }

}