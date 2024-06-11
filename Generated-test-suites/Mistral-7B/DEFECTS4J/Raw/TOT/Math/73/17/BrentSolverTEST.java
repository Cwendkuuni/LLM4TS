package org.spaceroots.mantissa.roots;

import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

/**
 * Tests for {@link BrentSolver} class
 */
public class BrentSolverTest {

  @Test
  public void testFindRoot_SimpleFunction() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return (fx > 0) ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double expectedRoot = 2.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertEquals(expectedRoot, solver.getRoot(), 1e-6);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_Exception() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("test exception");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.CONVERGED;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    solver.findRoot(function, checker, 100, x0, FunctionUtils.ZERO, x1, FunctionUtils.ZERO);
  }

  @Test
  public void testFindRoot_FunctionWithRootAtBoundary() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(Math.PI - x, 2) - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return (fx > 0) ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 3.14159265; // pi
    double x1 = 3.14159266; // pi + eps
    double expectedRoot = 3.14159265;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertEquals(expectedRoot, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_FunctionWithRootInInterval() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * Math.sin(x);
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return (fx > 0) ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.5;
    double x1 = 2.5;
    double expectedRoot = 1.74532925;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertEquals(expectedRoot, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_FunctionWithRootFarFromInterval() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 5, 2) - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.CONVERGED;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    solver.findRoot(function, checker, 100, x0, FunctionUtils.ZERO, x1, FunctionUtils.ZERO);
    Assert.assertEquals(Double.NaN, solver.getRoot());
  }

}