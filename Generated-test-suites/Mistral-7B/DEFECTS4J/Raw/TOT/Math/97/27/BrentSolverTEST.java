package org.spaceroots.mantissa.roots.test;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the BrentSolver class.
 */
public class BrentSolverTest {

  /**
   * Test method for the findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double) method.
   * Positive case: function has a root in the given interval.
   */
  @Test
  public void testFindRootPositive() {
    double x1 = 1.0;
    double x2 = 2.0;
    ComputableFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 1.5, 2) - 0.25;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();
    double root = solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x2, function.valueAt(x2))[0];
    assertEquals("The root was not found.", 1.5, root, 0.001);
  }

  /**
   * Test method for the findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double) method.
   * Edge case: function has a root on the boundary of the interval.
   */
  @Test
  public void testFindRootEdge() {
    double x1 = 0.0;
    double x2 = 1.0;
    ComputableFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(Math.PI * x) - 0.5;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();
    double root = solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x2, function.valueAt(x2))[0];
    assertEquals("The root was not found.", 0.0, root, 0.001);
  }

  /**
   * Test method for the findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double) method.
   * Error case: function has no root in the given interval.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootNoRoot() {
    double x1 = -1.0;
    double x2 = 1.0;
    ComputableFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(Math.PI * x) + 0.5;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x2, function.valueAt(x2));
  }

  /**
   * Test method for the getRoot() method.
   * Positive case: the solver has found a root.
   */
  @Test
  public void testGetRootPositive() {
    double x1 = 1.0;
    double x2 = 2.0;
    ComputableFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 1.5, 2) - 0.25;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();
    double[] root = solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x2, function.valueAt(x2));
    assertEquals("The root was not found.", 1.5, solver.getRoot(), 0.001);
  }

  /**
   * Test method for the getRoot() method.
   * Edge case: the solver has not found a root.
   */
  @Test
  public void testGetRootNoRoot() {
    double x1 = -1.0;
    double x2 = 1.0;
    ComputableFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(Math.PI * x) + 0.5;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x2, function.valueAt(x2));
    assertEquals("The root was found.", Double.NaN, solver.getRoot(), 0.0);
  }

}