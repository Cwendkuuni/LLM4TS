package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for BrentSolver
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   */
  @Test
  public void testFindRoot() throws Exception {
    // Test case 1: Simple function with a known root
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 1.0, 2.0) - 1.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int convergence(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOWEST;
      }
    };
    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, 100, -1.5, Double.POSITIVE_INFINITY, 1.5, Double.NEGATIVE_INFINITY);
    assertEquals(0.0, solver.getRoot(), 1e-6);

    // Test case 2: Function with no root in the interval
    function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(Math.PI) * (x - 2.0);
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int convergence(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    solver = new BrentSolver();
    solver.findRoot(function, checker, 100, -5.0, 5.0, Double.NaN, Double.NaN);
    assertNull(solver.getRoot());

    // Test case 3: Function with multiple roots
    function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 2.0, 3.0) - 8.0;
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int convergence(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOWEST;
      }
    };
    solver = new BrentSolver();
    solver.findRoot(function, checker, 100, -3.0, 3.0, Double.NaN, Double.NaN);
    assertEquals(1.332057074193683, solver.getRoot(), 1e-6);

    // Test case 4: Function with close roots
    function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 2.0, 3.0) - 1.0;
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int convergence(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    };
    solver = new BrentSolver();
    solver.findRoot(function, checker, 100, -2.1, -1.9, 2.1, 1.9);
    assertEquals(1.9749999999999997, solver.getRoot(), 1e-6);

  }

}