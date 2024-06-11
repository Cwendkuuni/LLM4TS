package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) + 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x1, double f1) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    try {
      boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
      assertTrue(result);
      assertEquals(root, solver.getRoot(), epsilon);
    } catch (FunctionException e) {
      // FunctionException should not be thrown in this test case
      fail("Unexpected FunctionException: " + e.getMessage());
    }
  }

  @Test
  public void testFindRootDivergent() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.log(Math.abs(x - 0.5));
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x1, double f1) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;

    try {
      boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
      assertFalse(result);
      assertNull(solver.getRoot());
    } catch (FunctionException e) {
      // FunctionException is expected in this test case
    }
  }

  @Test
  public void testFindRootConverged() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 2.0, 2.0) - 4.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x1, double f1) {
        if (Math.abs(f) < 0.01) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;

    try {
      boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
      assertTrue(result);
      assertEquals(2.0, solver.getRoot(), epsilon);
    } catch (FunctionException e) {
      // FunctionException should not be thrown in this test case
      fail("Unexpected FunctionException: " + e.getMessage());
    }
  }

  @Test
  public void testFindRootConvergedHigh() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 2.0, 2.0) - 4.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x1, double f1) {
        if (Math.abs(f) > 0.01) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;

    try {
      boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
      assertTrue(result);
      assertEquals(2.0, solver.getRoot(), epsilon);
    } catch (FunctionException e) {
      // FunctionException should not be thrown in this test case
      fail("Unexpected FunctionException: " + e.getMessage());
    }
  }

  @Test
  public void testFindRootIterationLimit() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) + 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x1, double f1) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;

    try {
      boolean result = solver.findRoot(function, checker, 0, x0, function.valueAt(x0), x1, function.valueAt(x1));
      assertFalse(result);
      assertNull(solver.getRoot());
    } catch (FunctionException e) {
      // FunctionException should not be thrown in this test case
      fail("Unexpected FunctionException: " + e.getMessage());
    }
  }

}