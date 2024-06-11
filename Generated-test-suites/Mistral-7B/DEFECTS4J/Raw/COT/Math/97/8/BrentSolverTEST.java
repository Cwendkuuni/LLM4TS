package org.spaceroots.mantissa.roots.test;

import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test cases for the BrentSolver class
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   */
  @Test
  public void testFindRoot() throws Exception {
    // Test with a simple function
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 2;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        if (Math.abs(f) < Double.MIN_VALUE) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;
    double root = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertNotNull(root);
    Assert.assertEquals(Math.sqrt(2.0), root, 0.001);

    // Test with a function with roots at 0
    ComputableFunction function2 = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x;
      }
    };

    ConvergenceChecker checker2 = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        if (Math.abs(f) < epsilon) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver2 = new BrentSolver();
    double x0_2 = -1.0;
    double x1_2 = 1.0;
    double root_2 = solver2.findRoot(function2, checker2, 10, x0_2, function2.valueAt(x0_2), x1_2, function2.valueAt(x1_2));

    Assert.assertNotNull(root_2);
    Assert.assertEquals(0.0, root_2, epsilon);

    // Test with a function with roots at infinity
    ComputableFunction function3 = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return 1 / x;
      }
    };

    ConvergenceChecker checker3 = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        if (Math.abs(x) > 1000.0) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver3 = new BrentSolver();
    double x0_3 = -100.0;
    double x1_3 = 100.0;
    double root_3 = solver3.findRoot(function3, checker3, 10, x0_3, function3.valueAt(x0_3), x1_3, function3.valueAt(x1_3));

    Assert.assertEquals(Double.POSITIVE_INFINITY, root_3);

    // Test with a function with no roots
    ComputableFunction function4 = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) + 1;
      }
    };

    ConvergenceChecker checker4 = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver4 = new BrentSolver();
    double x0_4 = 0.0;
    double x1_4 = 2.0;
    boolean rootFound = solver4.findRoot(function4, checker4, 10, x0_4, function4.valueAt(x0_4), x1_4, function4.valueAt(x1_4));

    Assert.assertFalse(rootFound);
  }

}