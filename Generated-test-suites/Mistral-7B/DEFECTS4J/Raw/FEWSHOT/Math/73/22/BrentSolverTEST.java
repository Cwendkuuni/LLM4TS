package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;

public class BrentSolverTest {

  @Test
  public void testBrentSolverSuccess() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        return FunctionUtils.sin(Math.PI * x);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 0;
    double x1 = 1;
    double root = Math.PI / 2;

    assertTrue(solver.findRoot(function, checker, 100, x0, FunctionUtils.sin(Math.PI * x0), x1, FunctionUtils.sin(Math.PI * x1)));
    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testBrentSolverFailure() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Test exception");
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 0;
    double x1 = 1;

    assertFalse(solver.findRoot(function, checker, 100, x0, Double.NaN, x1, Double.NaN));
    assertNull(solver.getRoot());
  }

  @Test
  public void testBrentSolverConvergenceCheck() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        if (Math.abs(fx) < epsilon) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(x - xPrev) < epsilon) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = -2;
    double x1 = 2;

    double root = -2.0;
    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), epsilon);

    root = 2.0;
    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testBrentSolverIntervalTooNarrow() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        return Math.sin(x) - 0.5;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = -Math.PI;
    double x1 = -Math.PI + epsilon;

    assertFalse(solver.findRoot(function, checker, 100, x0, FunctionUtils.sin(x0), x1, FunctionUtils.sin(x1)));
    assertNull(solver.getRoot());
  }

  @Test
  public void testBrentSolverMaxIterations() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        double result = Math.log(1 + x) / x;
        if (result > 1000) {
          throw new FunctionException("Function value too large");
        }
        return result;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 0;
    double x1 = 1;

    assertFalse(solver.findRoot(function, checker, 0, x0, FunctionUtils.log1p(x0) / x0, x1, FunctionUtils.log1p(x1) / x1));
    assertNull(solver.getRoot());
  }

}