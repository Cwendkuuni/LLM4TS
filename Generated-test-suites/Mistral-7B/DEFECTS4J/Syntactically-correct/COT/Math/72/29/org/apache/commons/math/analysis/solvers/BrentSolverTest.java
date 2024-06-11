package org.spaceroots.mantissa.roots.test;

import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar. ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for the BrentSolver class
 */
public class BrentSolverTest {

  /**
   * Test method for the findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)
   * method of the BrentSolver class.
   * @throws FunctionException
   */
  @Test
  public void testFindRoot() throws FunctionException {
    // Test case 1: F(x) = x^2 - 4
    ComputableFunction function1 = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker1 = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver brentSolver1 = new BrentSolver();
    double x0 = 2.0;
    double f0 = x0 * x0 - 4;
    double x1 = 3.0;
    double f1 = x1 * x1 - 4;
    Assert.assertTrue(brentSolver1.findRoot(function1, checker1, 10, x0, f0, x1, f1));
    Assert.assertEquals(2.0, brentSolver1.getRoot(), 0.001);

    // Test case 2: F(x) = exp(x) - 2
    ComputableFunction function2 = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.exp(x) - 2;
      }
    };
    ConvergenceChecker checker2 = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver brentSolver2 = new BrentSolver();
    double x2 = 1.0;
    double f2 = Math.exp(x2) - 2;
    double x3 = 2.0;
    double f3 = Math.exp(x3) - 2;
    Assert.assertTrue(brentSolver2.findRoot(function2, checker2, 10, x2, f2, x3, f3));
    Assert.assertTrue(Math.abs(Math.exp(brentSolver2.getRoot()) - 2) < 0.01);

    // Test case 3: F(x) = sin(x)
    ComputableFunction function3 = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };
    ConvergenceChecker checker3 = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver brentSolver3 = new BrentSolver();
    double x4 = 1.5708; // PI/2
    double f4 = Math.sin(x4);
    double x5 = 1.6;
    double f5 = Math.sin(x5);
    Assert.assertTrue(brentSolver3.findRoot(function3, checker3, 10, x4, f4, x5, f5));
    Assert.assertEquals(Math.PI/2, brentSolver3.getRoot(), 0.001);

    // Test case 4: F(x) = x^3 - 2
    ComputableFunction function4 = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x * x - 2;
      }
    };
    ConvergenceChecker checker4 = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver brentSolver4 = new BrentSolver();
    double x6 = 1.5;
    double f6 = x6 * x6 * x6 - 2;
    double x7 = 2.0;
    double f7 = x7 * x7 * x7 - 2;
    Assert.assertTrue(brentSolver4.findRoot(function4, checker4, 10, x6, f6, x7, f7));
    Assert.assertEquals(-1.0, brentSolver4.getRoot(), 0.001);

    // Test edge cases and exception handling
    ComputableFunction function5 = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("Function error");
      }
    };
    ConvergenceChecker checker5 = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver brentSolver5 = new BrentSolver();
    try {
      brentSolver5.findRoot(function5, checker5, 10, 0.0, 0.0, 1.0, 0.0);
      Assert.fail();
    } catch (FunctionException e) {
      // Expected
    }

    ComputableFunction function6 = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.log(x);
      }
    };
    ConvergenceChecker checker6 = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver brentSolver6 = new BrentSolver();
    try {
      brentSolver6.findRoot(function6, checker6, 10, 0.0, Double.NEGATIVE_INFINITY, 0.0, Double.NEGATIVE_INFINITY);
      Assert.fail();
    } catch (FunctionException e) {
      // Expected
    }

    ComputableFunction function7 = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 0.5);
      }
    };
    ConvergenceChecker checker7 = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver brentSolver7 = new BrentSolver();
    try {
      brentSolver7.findRoot(function7, checker7, 10, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
      Assert.fail();
    } catch (FunctionException e) {
      // Expected
    }

    ComputableFunction function8 = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 0.5);
      }
    };
    ConvergenceChecker checker8 = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver brentSolver8 = new BrentSolver();
    try {
      brentSolver8.findRoot(function8, checker8, 10, 0.0, 0.0, -1.0, 0.0);
      Assert.fail();
    } catch (FunctionException e) {
      // Expected
    }

    ComputableFunction function9 = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) / x;
      }
    };
    ConvergenceChecker checker9 = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver brentSolver9 = new BrentSolver();
    try {
      brentSolver9.findRoot(function9, checker9, 10, 0.0, 0.0, 0.0, 0.0);
      Assert.fail();
    } catch (FunctionException e) {
      // Expected
    }

  }

}