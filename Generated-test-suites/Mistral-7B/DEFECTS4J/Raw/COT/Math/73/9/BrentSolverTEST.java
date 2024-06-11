package org.spaceroots.mantissa.roots.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for BrentSolver.
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}.
   */
  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 2.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        if (Math.abs(fx) < Double.MIN_VALUE) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(x - xPrevious) < 0.00001) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };

    double a = 1.0;
    double b = 2.0;
    double c = 1.5;
    double xRoot = 1.4142135625;

    try {
      assertTrue(solver.findRoot(function, checker, 100, a, function.valueAt(a), b, function.valueAt(b)));
      double root = solver.getRoot();
      assertEquals(xRoot, root, 0.00001);
    } catch (FunctionException e) {
      fail("Unexpected exception thrown: " + e.getMessage());
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with function that has no root.
   */
  @Test
  public void testFindRootNoRoot() {
    BrentSolver solver = new BrentSolver();
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 3.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        if (Math.abs(fx) < Double.MIN_VALUE) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(x - xPrevious) < 0.00001) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };

    double a = 1.0;
    double b = 2.0;
    double c = 1.5;

    try {
      assertFalse(solver.findRoot(function, checker, 100, a, function.valueAt(a), b, function.valueAt(b)));
    } catch (FunctionException e) {
      fail("Unexpected exception thrown: " + e.getMessage());
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with function that has multiple roots.
   */
  @Test
  public void testFindRootMultipleRoots() {
    BrentSolver solver = new BrentSolver();
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return (x - 1.0) * (x - 2.0) * (x - 3.0);
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        if (Math.abs(fx) < Double.MIN_VALUE) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(x - xPrevious) < 0.00001) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };

    double a = 1.0;
    double b = 3.0;
    double c = 2.0;
    double xRoot1 = 1.0;
    double xRoot2 = 2.0;
    double xRoot3 = 3.0;

    try {
      assertTrue(solver.findRoot(function, checker, 100, a, function.valueAt(a), b, function.valueAt(b)));
      double root = solver.getRoot();
      // The BrentSolver will return only one root. We'll check
      // the other roots with an additional test.
      assertEquals(xRoot1, root, 0.001);

      // Test for other roots
      solver.reset();
      assertTrue(solver.findRoot(function, checker, 100, b, function.valueAt(b), a, function.valueAt(a)));
      root = solver.getRoot();
      assertEquals(xRoot2, root, 0.001);

      solver.reset();
      assertTrue(solver.findRoot(function, checker, 100, c, function.valueAt(c), b, function.valueAt(b)));
      root = solver.getRoot();
      assertEquals(xRoot3, root, 0.001);
    } catch (FunctionException e) {
      fail("Unexpected exception thrown: " + e.getMessage());
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with function that has roots very close.
   */
  @Test
  public void testFindRootCloseRoots() {
    BrentSolver solver = new BrentSolver();
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3.0) - 2.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        if (Math.abs(fx) < Double.MIN_VALUE) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(x - xPrevious) < 0.00001) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };

    double a = 1.0;
    double b = 1.1;
    double c = 1.1;
    double xRoot1 = 1.0;
    double xRoot2 = 1.1;

    try {
      assertTrue(solver.findRoot(function, checker, 100, a, function.valueAt(a), b, function.valueAt(b)));
      double root = solver.getRoot();
      // The BrentSolver will return only one root. We'll check
      // the other root with an additional test.
      assertEquals(xRoot1, root, 0.001);

      solver.reset();
      assertTrue(solver.findRoot(function, checker, 100, b, function.valueAt(b), c, function.valueAt(c)));
      root = solver.getRoot();
      assertEquals(xRoot2, root, 0.001);
    } catch (FunctionException e) {
      fail("Unexpected exception thrown: " + e.getMessage());
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with exception handling.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootException() {
    BrentSolver solver = new BrentSolver();
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        throw new RuntimeException("Exception thrown in function.");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    double a = 1.0;
    double b = 2.0;
    double c = 1.5;

    solver.findRoot(function, checker, 100, a, function.valueAt(a), b, function.valueAt(b));
  }

}