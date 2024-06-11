package org.spaceroots.mantissa.roots.test;

import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

  /** Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} */
  @Test
  public void testFindRoot() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return FunctionUtils.sin(x) - x;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return (Math.abs(f) < 0.001) ? ConvergenceChecker.LOW : ConvergenceChecker.UNDECIDED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;

    try {
      solver.findRoot(function, checker, 100, x0, FunctionUtils.sin(x0), x1, FunctionUtils.sin(x1));
      double root = solver.getRoot();
      Assert.assertEquals(Math.PI / 2, root, 0.001);
    } catch (FunctionException e) {
      e.printStackTrace();
      Assert.fail("Exception thrown: " + e.getMessage());
    }
  }

  /** Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with function not converging */
  @Test
  public void testFindRootNotConverging() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(Math.PI / 2) * Math.sin(Math.PI / 2) - x * x;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return ConvergenceChecker.UNDECIDED;
      }
    };

    BrentSolver solver = new BrentSolver();

    try {
      solver.findRoot(function, checker, 100, 0.0, 1.0, 1.0, 0.0);
      Assert.fail("Expected FunctionException to be thrown");
    } catch (FunctionException e) {
      // Expected
    }
  }

  /** Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with interval too small */
  @Test
  public void testFindRootIntervalTooSmall() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 1.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return ConvergenceChecker.UNDECIDED;
      }
    };

    BrentSolver solver = new BrentSolver();

    try {
      solver.findRoot(function, checker, 1, 0.0, 0.0, 0.1, 0.0);
      Assert.fail("Expected FunctionException to be thrown");
    } catch (FunctionException e) {
      // Expected
    }
  }

  /** Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with interval containing no root */
  @Test
  public void testFindRootIntervalNoRoot() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) + 1.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return ConvergenceChecker.UNDECIDED;
      }
    };

    BrentSolver solver = new BrentSolver();

    try {
      solver.findRoot(function, checker, 100, 0.0, 0.0, 1.0, 0.0);
      Assert.assertEquals(Double.NaN, solver.getRoot(), 0.0);
    } catch (FunctionException e) {
      // Ignore
    }
  }

  /** Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with x0 and x1 close */
  @Test
  public void testFindRootX0X1Close() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 1.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return ConvergenceChecker.UNDECIDED;
      }
    };

    BrentSolver solver = new BrentSolver();

    try {
      solver.findRoot(function, checker, 100, 0.99, 0.99, 1.0, 0.0);
      double root = solver.getRoot();
      Assert.assertEquals(-0.999999999999895, root, 0.001);
    } catch (FunctionException e) {
      // Ignore
    }
  }

  /** Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with x0 and x1 far */
  @Test
  public void testFindRootX0X1Far() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - x;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return ConvergenceChecker.UNDECIDED;
      }
    };

    BrentSolver solver = new BrentSolver();

    try {
      solver.findRoot(function, checker, 100, -10.0, 10.0, 0.0, 0.0);
      double root = solver.getRoot();
      Assert.assertEquals(Math.PI / 2, root, 0.001);
    } catch (FunctionException e) {
      // Ignore
    }
  }

  /** Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with function having multiple roots */
  @Test
  public void testFindRootFunctionMultipleRoots() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return (x - 1.0) * (x - 3.0) * (x - 5.0);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return ConvergenceChecker.UNDECIDED;
      }
    };

    BrentSolver solver = new BrentSolver();

    try {
      solver.findRoot(function, checker, 100, 2.0, 4.0, 0.0, 0.0);
      double root = solver.getRoot();
      Assert.assertEquals(3.0, root, 0.001);
    } catch (FunctionException e) {
      // Ignore
    }
  }
}