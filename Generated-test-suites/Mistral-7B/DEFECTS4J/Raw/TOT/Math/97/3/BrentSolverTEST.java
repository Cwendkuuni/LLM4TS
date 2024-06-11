package org.spaceroots.mantissa.roots;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.functions.scalar.SinFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Tests for the BrentSolver class.
 */
public class BrentSolverTest {

  /**
   * Test finding a root using the Brent solver with a simple function.
   */
  @Test
  public void testFindRootSinFunction() {
    ComputableFunction function = new SinFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 3.0;
    double root = Math.PI;

    try {
      assertTrue(solver.findRoot(function, checker, 100, x0, FunctionUtils.sin(x0), x1, FunctionUtils.sin(x1)));
      assertEquals(root, solver.getRoot(), 1e-6);
    } catch (FunctionException e) {
      fail("FunctionException should not be thrown");
    }
  }

  /**
   * Test finding a root using the Brent solver with a function having no real roots.
   */
  @Test
  public void testFindRootNoRootFunction() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

  /**
   * Test finding a root using the Brent solver with a function having multiple roots.
   */
  @Test
  public void testFindRootMultipleRootsFunction() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = -2.0;
    double x1 = 2.0;
    try {
      assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
      assertEquals(-2.0, solver.getRoot(), 1e-6);
      assertTrue(solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x0, function.valueAt(x0)));
      assertEquals(2.0, solver.getRoot(), 1e-6);
    } catch (FunctionException e) {
      fail("FunctionException should not be thrown");
    }
  }

  /**
   * Test handling an exception when the function cannot be evaluated.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootExceptionFunction() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new ArithmeticException("Divide by zero");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 3.0;
    try {
      solver.findRoot(function, checker, 100, x0, FunctionUtils.nan(), x1, FunctionUtils.nan());
      fail("FunctionException expected");
    } catch (FunctionException e) {
    }
  }

  /**
   * Test handling an exception when the checker throws an exception.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootCheckerExceptionFunction() {
    ComputableFunction function = new SinFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public ConvergenceChecker.ConvergenceResult converged(double x, double fx, double x1, double fx1) {
        throw new ArithmeticException("Divide by zero");
      }
    };
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 3.0;
    try {
      solver.findRoot(function, checker, 100, x0, FunctionUtils.sin(x0), x1, FunctionUtils.sin(x1));
      fail("FunctionException expected");
    } catch (FunctionException e) {
    }
  }

}