package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.scalar.FunctionException;

public class BrentSolverTest {

  @Test
  public void testBrentSolver_findRoot_converged() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 1;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int convergenceTest(double x, double fx, double xPrev, double fxPrev) {
        double tol = 1e-8;
        return Math.abs(fx) < tol || Math.abs(x - xPrev) < tol
            ? ConvergenceChecker.CONVERGED
            : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = Math.pow(x0, 3) - 1;
    double x1 = 2.0;
    double f1 = Math.pow(x1, 3) - 1;

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertEquals(x1, solver.getRoot(), 1e-8);
  }

  @Test
  public void testBrentSolver_findRoot_low() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - 0.5;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int convergenceTest(double x, double fx, double xPrev, double fxPrev) {
        double tol = 1e-6;
        return Math.abs(fx) < tol ? ConvergenceChecker.LOW : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double f0 = Math.sin(x0) - 0.5;
    double x1 = Math.PI / 2;
    double f1 = Math.sin(x1) - 0.5;

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertEquals(x0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testBrentSolver_findRoot_high() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - 0.5;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int convergenceTest(double x, double fx, double xPrev, double fxPrev) {
        double tol = 1e-6;
        return Math.abs(fx) > tol ? ConvergenceChecker.HIGH : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double f0 = Math.sin(x0) - 0.5;
    double x1 = Math.PI / 2;
    double f1 = Math.sin(x1) - 0.5;

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertEquals(x1, solver.getRoot(), 1e-6);
  }

  @Test
  public void testBrentSolver_findRoot_notConverged() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int convergenceTest(double x, double fx, double xPrev, double fxPrev) {
        double tol = 1e-6;
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = Math.pow(x0, 2) - 4;
    double x1 = 2.0;
    double f1 = Math.pow(x1, 2) - 4;

    assertFalse(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertNull(solver.getRoot());
  }

}