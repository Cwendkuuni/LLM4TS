package org.spaceroots.mantissa.roots;

import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRootSimpleFunction() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 2;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    double x0 = 1.0;
    double x1 = 2.0;
    double root = -1.0;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotEquals(Double.NaN, solver.getRoot());
    Assert.assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRootFunctionWithRootInMiddle() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 1;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    double x0 = 0.5;
    double x1 = 1.5;
    double root = 0.0;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotEquals(Double.NaN, solver.getRoot());
    Assert.assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootFunctionWithNoRootInInterval() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) + 1;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    double x0 = 0.5;
    double x1 = 1.5;

    Assert.assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

  @Test
  public void testFindRootFunctionWithDivergingInterval() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(Math.PI / x);
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    double x0 = 1.0;
    double x1 = 0.1;

    Assert.assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

  @Test
  public void testFindRootFunctionWithFunctionException() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("FunctionException message");
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    double x0 = 1.0;
    double x1 = 2.0;

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    } catch (FunctionException e) {
      // expected
    }
  }

  @Test
  public void testFindRootFunctionWithZeroFunction() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return 0.0;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    double x0 = 1.0;
    double x1 = 2.0;

    Assert.assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

}