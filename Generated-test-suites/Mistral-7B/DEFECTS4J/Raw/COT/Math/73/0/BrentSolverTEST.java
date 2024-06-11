package org.spaceroots.mantissa.roots;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for BrentSolver
 */
public class BrentSolverTest {

  @Test
  public void testFindRoot_simpleFunction() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPred, double fxPred) {
        return ConvergenceChecker.NONE;
      }
    };

    boolean result = solver.findRoot(function, checker, 100, -2.0, -3.0, 2.0, 4.0);
    assertTrue(result);
    assertEquals(-1.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_convergedLow() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2.0;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPred, double fxPred) {
        return x < xPred ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
      }
    };

    boolean result = solver.findRoot(function, checker, 100, 1.0, 1.0, 2.0, 2.0);
    assertTrue(result);
    assertEquals(1.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_convergedHigh() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2.0;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPred, double fxPred) {
        return x > xPred ? ConvergenceChecker.HIGH : ConvergenceChecker.NONE;
      }
    };

    boolean result = solver.findRoot(function, checker, 100, -1.0, -1.0, 1.0, 1.0);
    assertTrue(result);
    assertEquals(-1.0, solver.getRoot(), 1e-6);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new ArithmeticException("Divide by zero");
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPred, double fxPred) {
        return ConvergenceChecker.NONE;
      }
    };

    solver.findRoot(function, checker, 100, -1.0, -1.0, 1.0, 1.0);
  }

}