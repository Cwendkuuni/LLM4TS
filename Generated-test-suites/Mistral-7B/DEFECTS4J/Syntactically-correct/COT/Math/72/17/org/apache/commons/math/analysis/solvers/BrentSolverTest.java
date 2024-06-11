package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.function.FunctionUtils;
import org.apache.commons.math3.function.UnivariateFunction;
import org.apache.commons.math3.util.Precision;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @org.junit.Test
  public void testFindRoot_functionWithOneRoot() throws Exception {
    ComputableFunction function = new ComputableUnivariateFunction(x -> x * x - 2);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return (Precision.abs(fx) < 0.01) ? ConvergenceChecker.LOW : ConvergenceChecker.CONTINUE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)).getRoot();
    assertEquals("root should be close to -1", -1.0, root, 0.01);
  }

  @org.junit.Test(expected = FunctionException.class)
  public void testFindRoot_functionWithNoRoot() throws Exception {
    ComputableFunction function = new ComputableUnivariateFunction(x -> x * x + 1);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.CONTINUE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @org.junit.Test(expected = FunctionException.class)
  public void testFindRoot_functionWithMultipleRoots() throws Exception {
    ComputableFunction function = new ComputableUnivariateFunction(x -> Math.pow(x, 3) - 3 * Math.pow(x, 2) + 2);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.CONTINUE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = -1.0;
    double x1 = 0.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @org.junit.Test
  public void testFindRoot_functionWithIntervalContainingRoot() throws Exception {
    ComputableFunction function = new ComputableUnivariateFunction(x -> Math.sin(x) - 0.5);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return (Precision.abs(fx) < 0.01) ? ConvergenceChecker.LOW : ConvergenceChecker.CONTINUE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    double root = solver.getRoot();
    assertTrue("root should be between x0 and x1", x0 < root && root < x1);
  }

  @org.junit.Test
  public void testFindRoot_functionWithIntervalNotContainingRoot() throws Exception {
    ComputableFunction function = new ComputableUnivariateFunction(x -> Math.sin(x) - 0.5);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.CONTINUE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double x1 = 1.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals("root not found", false, solver.isRootFound());
  }

  @org.junit.Test
  public void testFindRoot_functionWithIntervalTooSmall() throws Exception {
    ComputableFunction function = new ComputableUnivariateFunction(x -> Math.sin(x) - 0.5);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.CONTINUE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double x1 = 0.1;
    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Should throw an exception for too small interval");
    } catch (FunctionException ex) {
      // expected
    }
  }

  @org.junit.Test
  public void testFindRoot_functionWithLargeInterval() throws Exception {
    ComputableFunction function = new ComputableUnivariateFunction(x -> Math.sin(x) - 0.5);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.CONTINUE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double x1 = 10.0;
    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Should throw an exception for too large interval");
    } catch (FunctionException ex) {
      // expected
    }
  }

  @org.junit.Test(expected = ConvergenceException.class)
  public void testFindRoot_functionWithDivergingSequence() throws Exception {
    ComputableFunction function = new ComputableUnivariateFunction(x -> x * x);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.CONTINUE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @org.junit.Test
  public void testFindRoot_functionWithLargeInput() throws Exception {
    ComputableFunction function = new ComputableUnivariateFunction(x -> Math.exp(-Math.pow(x, 2.0)));
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.CONTINUE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = Double.MAX_VALUE;
    double x1 = Double.MAX_VALUE + 1.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @org.junit.Test
  public void testGetRoot() throws Exception {
    ComputableFunction function = new ComputableUnivariateFunction(x -> x * x - 2);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    double root = solver.getRoot();
    assertEquals("root should be close to -1", -1.0, root, 0.01);
  }

  static class ComputableUnivariateFunction implements ComputableFunction {

    private final UnivariateFunction function;

    public ComputableUnivariateFunction(UnivariateFunction function) {
      this.function = function;
    }

    @Override
    public double valueAt(double x) {
      return FunctionUtils.value(function, x);
    }
  }
}