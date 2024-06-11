package org.spaceroots.mantissa.roots.test;

import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.scalar.FunctionException;
import org.spaceroots.mantissa.functions.scalar.test.SineFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for {@link BrentSolver}.
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}.
   *
   * @throws FunctionException if an error occurs during the computation of the root
   */
  @Test
  public void testFindRoot() throws FunctionException {
    // given
    ComputableFunction function = new SineFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        if (Math.abs(fx) < 0.0001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fx - fxPrev) < 0.0001) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.ITERATION;
        }
      }
    };
    BrentSolver brentSolver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double root = Math.PI / 2.0;

    // when
    boolean result = brentSolver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    // then
    Assert.assertTrue(result);
    Assert.assertEquals(root, brentSolver.getRoot(), epsilon);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with a function that has no roots.
   *
   * @throws FunctionException if an error occurs during the computation of the root
   */
  @Test(expected = FunctionException.class)
  public void testFindRootNoRoots() throws FunctionException {
    // given
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 2;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.ITERATION;
      }
    };
    BrentSolver brentSolver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;

    // when
    brentSolver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    // then (an exception should be thrown)
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with a function that has multiple roots.
   *
   * @throws FunctionException if an error occurs during the computation of the root
   */
  @Test
  public void testFindRootMultipleRoots() throws FunctionException {
    // given
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        if (Math.abs(fx) < 0.0001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fx - fxPrev) < 0.0001) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.ITERATION;
        }
      }
    };
    BrentSolver brentSolver = new BrentSolver();
    double x0 = -2.0;
    double x1 = 2.0;

    // when
    boolean result = brentSolver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    // then
    Assert.assertTrue(result);
    double root = brentSolver.getRoot();
    Assert.assertTrue(Math.abs(root - 2.0) < epsilon);
    // Try again to find the other root
    boolean result2 = brentSolver.findRoot(function, checker, 10, x1, function.valueAt(x1), x0, function.valueAt(x0));
    Assert.assertTrue(result2);
    double root2 = brentSolver.getRoot();
    Assert.assertTrue(Math.abs(root2 + 2.0) < epsilon);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with a function that has roots close to the interval bounds.
   *
   * @throws FunctionException if an error occurs during the computation of the root
   */
  @Test
  public void testFindRootRootsCloseToBounds() throws FunctionException {
    // given
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        if (Math.abs(fx) < 0.0001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fx - fxPrev) < 0.0001) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.ITERATION;
        }
      }
    };
    BrentSolver brentSolver = new BrentSolver();
    double x0 = -1.1;
    double x1 = 1.1;

    // when
    boolean result = brentSolver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    // then
    Assert.assertTrue(result);
    double root = brentSolver.getRoot();
    Assert.assertTrue(Math.abs(root) < epsilon);
  }
}