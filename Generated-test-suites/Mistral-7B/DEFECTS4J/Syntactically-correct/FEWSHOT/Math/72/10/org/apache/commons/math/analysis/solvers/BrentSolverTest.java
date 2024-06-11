package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.scalar.ConstantFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * JUnit tests for {@link BrentSolver}.
 */
public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    ComputableFunction function = new ConstantFunction(2.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NEITHER;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    double x0 = 1.0, x1 = 3.0, root = 2.0;
    boolean found = brentSolver.findRoot(function, checker, 100, x0, 2.0, x1, 2.0);
    Assert.assertEquals(root, brentSolver.getRoot(), epsilon);
    Assert.assertTrue(found);
  }

  @Test
  public void testFindRootFailedConvergence() {
    ComputableFunction function = new ConstantFunction(0.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NEITHER;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    double x0 = 1.0, x1 = 3.0;
    boolean found = brentSolver.findRoot(function, checker, 100, x0, 0.0, x1, 0.0);
    Assert.assertFalse(found);
    Assert.assertEquals(Double.NaN, brentSolver.getRoot());
  }

  @Test
  public void testFindRootFailedFunctionException() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        if (x < 0.0) {
          throw new FunctionException("Function undefined for x <= 0.");
        }
        return x * x;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NEITHER;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    double x0 = -1.0, x1 = 1.0;
    try {
      boolean found = brentSolver.findRoot(function, checker, 100, x0, Double.NEGATIVE_INFINITY, x1, Double.POSITIVE_INFINITY);
      Assert.fail();
    } catch (FunctionException e) {
      // expected
    }
    Assert.assertEquals(Double.NaN, brentSolver.getRoot());
  }

  @Test
  public void testFindRootMaxIterations() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) * Math.cos(x);
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NEITHER;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    double x0 = -2.0, x1 = 2.0, root = 0.0;
    boolean found = brentSolver.findRoot(function, checker, 0, x0, Double.NEGATIVE_INFINITY, x1, Double.POSITIVE_INFINITY);
    Assert.assertFalse(found);
    Assert.assertEquals(Double.NaN, brentSolver.getRoot());
  }

}