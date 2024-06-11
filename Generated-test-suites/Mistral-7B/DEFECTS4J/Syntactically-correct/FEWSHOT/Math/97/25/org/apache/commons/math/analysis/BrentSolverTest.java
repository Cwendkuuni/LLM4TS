package org.apache.commons.cli.test; // Assuming you want to create test package under org.apache.commons.cli.test

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testBrentSolver_findRoot_ConvergedAtLowerBound() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double fx0 = function.valueAt(x0);
    double x1 = 2.0;
    double fx1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, 10, x0, fx0, x1, fx1));
    assertTrue(Math.abs(solver.getRoot() - 2.0) < 1e-6);
  }

  @Test
  public void testBrentSolver_findRoot_ConvergedAtHigherBound() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.HIGH;
      }
    };

    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double fx0 = function.valueAt(x0);
    double x1 = 2.0;
    double fx1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, 10, x0, fx0, x1, fx1));
    assertTrue(Math.abs(solver.getRoot() - 2.0) < 1e-6);
  }

  @Test
  public void testBrentSolver_findRoot_NoConvergence() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(Math.PI / x);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NO_CONVERGENCE;
      }
    };

    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double fx0 = Math.sin(Math.PI / x0);
    double x1 = 2.0;
    double fx1 = Math.sin(Math.PI / x1);

    assertFalse(solver.findRoot(function, checker, 10, x0, fx0, x1, fx1));
  }
}