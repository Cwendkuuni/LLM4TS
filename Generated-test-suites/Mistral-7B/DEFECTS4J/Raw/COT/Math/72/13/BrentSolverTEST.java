package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

/**
 * Test class for BrentSolver
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   */
  @Test
  public void testFindRoot() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return FunctionUtils.sin(x) * Math.exp(-x) - 1.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return (fx * fxPrev > 0) ? ConvergenceChecker.UNDECIDED : (Math.abs(fx) < 1e-6) ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double x1 = 3.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(1.2745421875119823, root, 0.0001);
  }

  /**
   * Test method for {@link BrentSolver#getRoot()}
   */
  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) / x - 1.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return (fx * fxPrev > 0) ? ConvergenceChecker.UNDECIDED : (Math.abs(fx) < 1e-6) ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }
    };
    solver.findRoot(function, checker, 100, 1.0, function.valueAt(1.0), 2.0, function.valueAt(2.0));
    double root = solver.getRoot();
    assertEquals(1.4142135625576755, root, 0.0001);
  }

  /**
   * Test method for error handling in {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   */
  @Test(expected = FunctionException.class)
  public void testFindRootFunctionException() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new ArithmeticException("Division by zero");
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.UNDECIDED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double x1 = 3.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  /**
   * Test method for error handling in {@link BrentSolver#getRoot()}
   */
  @Test(expected = IllegalStateException.class)
  public void testGetRootIllegalStateException() {
    BrentSolver solver = new BrentSolver();
  }

}