package org.spaceroots.mantissa.roots.test;

import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for BrentSolver.
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} .
   */
  @Test
  public void testFindRoot() throws Exception {

    // Function for testing
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2;
      }
    };

    // Convergence checker for testing
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x0, double fx0) {
        return (Math.abs(fx) < 1e-12) ? ConvergenceChecker.LOW : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    // Create solver and test root finding
    BrentSolver solver = new BrentSolver();
    double[] x = new double[]{1.0, 2.0};
    double[] fx = new double[]{Math.pow(1.0, 3) - 2, Math.pow(2.0, 3) - 2};
    boolean result = solver.findRoot(function, checker, 100, x[0], fx[0], x[1], fx[1]);
    Assert.assertTrue(result);
    Assert.assertEquals(solver.getRoot(), x[1], 1e-12);

  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with exception.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootWithException() throws Exception {

    // Function for testing
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new ArithmeticException();
      }
    };

    // Convergence checker for testing
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x0, double fx0) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    // Create solver and test root finding with exception
    BrentSolver solver = new BrentSolver();
    double[] x = new double[]{1.0, 2.0};
    double[] fx = new double[]{Math.pow(1.0, 3) - 2, Math.pow(2.0, 3) - 2};
    solver.findRoot(function, checker, 100, x[0], fx[0], x[1], fx[1]);

  }

  /**
   * Test method for {@link BrentSolver#getRoot()}.
   */
  @Test
  public void testGetRoot() {

    // Create solver and set root
    BrentSolver solver = new BrentSolver();
    solver.root = 3.0;

    // Test getting root
    double result = solver.getRoot();
    Assert.assertEquals(result, 3.0, 1e-12);

  }

}