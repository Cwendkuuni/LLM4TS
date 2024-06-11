package org.spaceroots.mantissa.roots.test;

import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.TestFunctions;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  private static final int MAX_ITERATIONS = 10;

  @Test
  public void testFindRoot_SimpleFunction() throws Exception {
    ComputableFunction function = TestFunctions.SIMPLE_FUNCTION;
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.UNKNOWN;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double fx0 = function.valueAt(x0);
    double x1 = 2.0;
    double fx1 = function.valueAt(x1);

    Assert.assertTrue(solver.findRoot(function, checker, MAX_ITERATIONS, x0, fx0, x1, fx1));
    double root = solver.getRoot();
    double expectedRoot = TestFunctions.SIMPLE_FUNCTION.zero();
    Assert.assertEquals(expectedRoot, root, 1e-6);
  }

  @Test
  public void testFindRoot_FunctionWithRoot() throws Exception {
    ComputableFunction function = TestFunctions.FUNCTION_WITH_ROOT;
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.UNKNOWN;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double fx0 = function.valueAt(x0);
    double x1 = 3.0;
    double fx1 = function.valueAt(x1);

    Assert.assertTrue(solver.findRoot(function, checker, MAX_ITERATIONS, x0, fx0, x1, fx1));
    double root = solver.getRoot();
    double expectedRoot = TestFunctions.FUNCTION_WITH_ROOT.zero();
    Assert.assertEquals(expectedRoot, root, 1e-6);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_DivisionByZeroFunction() throws Exception {
    ComputableFunction function = TestFunctions.DIVISION_BY_ZERO_FUNCTION;
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.UNKNOWN;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double fx0 = function.valueAt(x0);
    double x1 = 2.0;
    double fx1 = function.valueAt(x1);

    Assert.assertFalse(solver.findRoot(function, checker, MAX_ITERATIONS, x0, fx0, x1, fx1));
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionWithNegativeValue() throws Exception {
    ComputableFunction function = TestFunctions.FUNCTION_WITH_NEGATIVE_VALUE;
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.UNKNOWN;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double fx0 = function.valueAt(x0);
    double x1 = 2.0;
    double fx1 = function.valueAt(x1);

    Assert.assertFalse(solver.findRoot(function, checker, MAX_ITERATIONS, x0, fx0, x1, fx1));
  }
}