package org.spaceroots.mantissa.roots;

import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.CommonFunctions;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for {@link BrentSolver}
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having a simple root
   */
  @Test
  public void testFindRootSimple() {
    CommonFunctions fun = new CommonFunctions();
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    try {
      solver.findRoot(fun::sin, checker, 100, x0, sin(x0), x1, sin(x1));
      Assert.assertEquals(root, solver.getRoot(), epsilon);
    } catch (FunctionException e) {
      Assert.fail("findRoot should not throw an exception for this test case");
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having a multiple root
   */
  @Test
  public void testFindRootMultiple() {
    CommonFunctions fun = new CommonFunctions();
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = 0.0;
    double x1 = 0.0;
    double root = 0.0;

    try {
      solver.findRoot(fun::exp, checker, 100, x0, exp(x0), x1, exp(x1));
      Assert.assertEquals(root, solver.getRoot(), epsilon);
    } catch (FunctionException e) {
      Assert.fail("findRoot should not throw an exception for this test case");
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having no root in the given interval
   */
  @Test(expected = FunctionException.class)
  public void testFindRootNoRoot() {
    CommonFunctions fun = new CommonFunctions();
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = 1.0;
    double x1 = 0.0;
    double root = 2.0;

    try {
      solver.findRoot(fun::sqrt, checker, 100, x0, Math.pow(x0, 2), x1, Math.pow(x1, 2));
      Assert.fail("findRoot should have thrown an exception for this test case");
    } catch (FunctionException e) {
      // expected
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having a root at the boundary of the interval
   */
  @Test
  public void testFindRootRootAtBoundary() {
    CommonFunctions fun = new CommonFunctions();
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = 0.0;
    double x1 = 0.0;
    double root = 0.0;

    try {
      solver.findRoot(fun::abs, checker, 100, x0, Math.abs(x0), x1, Math.abs(x1));
      Assert.assertEquals(root, solver.getRoot(), epsilon);
    } catch (FunctionException e) {
      Assert.fail("findRoot should not throw an exception for this test case");
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having a root very close to a boundary of the interval
   */
  @Test
  public void testFindRootRootNearBoundary() {
    CommonFunctions fun = new CommonFunctions();
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = 0.0;
    double x1 = 1.0e-8;
    double root = 0.0;

    try {
      solver.findRoot(fun::abs, checker, 100, x0, Math.abs(x0), x1, Math.abs(x1));
      Assert.assertEquals(root, solver.getRoot(), epsilon);
    } catch (FunctionException e) {
      Assert.fail("findRoot should not throw an exception for this test case");
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having very large values at the end of the interval
   */
  @Test
  public void testFindRootLargeValues() {
    CommonFunctions fun = new CommonFunctions();
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = 0.0;
    double x1 = 1000.0;
    double root = 0.0;

    try {
      solver.findRoot(fun::exp, checker, 100, x0, Math.exp(x0), x1, Math.exp(x1));
      Assert.assertEquals(root, solver.getRoot(), epsilon);
    } catch (FunctionException e) {
      Assert.fail("findRoot should not throw an exception for this test case");
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having a very large root
   */
  @Test
  public void testFindRootLargeRoot() {
    CommonFunctions fun = new CommonFunctions();
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = 0.0;
    double x1 = 1.0e10;
    double root = 1.0e10;

    try {
      solver.findRoot(fun::pow, checker, 100, x0, Math.pow(x0, 2), x1, Math.pow(x1, 2));
      Assert.assertEquals(root, solver.getRoot(), epsilon);
    } catch (FunctionException e) {
      Assert.fail("findRoot should not throw an exception for this test case");
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having oscillating values in the interval
   */
  @Test
  public void testFindRootOscillating() {
    CommonFunctions fun = new CommonFunctions();
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = 0.0;
    double x1 = Math.PI;
    double root = Math.PI / 2.0;

    try {
      solver.findRoot(fun::sin, checker, 100, x0, Math.sin(x0), x1, Math.sin(x1));
      Assert.assertEquals(root, solver.getRoot(), epsilon);
    } catch (FunctionException e) {
      Assert.fail("findRoot should not throw an exception for this test case");
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having very small values in the interval
   */
  @Test
  public void testFindRootSmallValues() {
    CommonFunctions fun = new CommonFunctions();
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = 0.0;
    double x1 = 1.0e-8;
    double root = 1.0e-8;

    try {
      solver.findRoot(fun::sin, checker, 100, x0, Math.sin(x0), x1, Math.sin(x1));
      Assert.assertEquals(root, solver.getRoot(), epsilon);
    } catch (FunctionException e) {
      Assert.fail("findRoot should not throw an exception for this test case");
    }
  }
}