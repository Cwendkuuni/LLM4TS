package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.scalar.FunctionException;
import org.spaceroots.mantissa.functions.scalar.test.TestFunctions;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new TestFunctions.Sin();
    ConvergenceChecker checker = new TestConvergenceChecker();
    int maxIter = 10;
    double x0 = 1.0;
    double f0 = Math.sin(x0);
    double x1 = 2.0;
    double f1 = Math.sin(x1);

    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(result);
    double root = solver.getRoot();
    assertEquals(1.2732056652351127d, root, epsilon);

    x0 = 1.5;
    f0 = Math.sin(x0);
    x1 = 1.6;
    f1 = Math.sin(x1);

    result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(result);
    root = solver.getRoot();
    assertEquals(-1.2023492651062267d, root, epsilon);

    x0 = 0.5;
    f0 = Math.sin(x0);
    x1 = 1.0;
    f1 = Math.sin(x1);

    result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertFalse(result);

    x0 = 0.5;
    f0 = Math.sin(x0);
    x1 = 2.0;
    f1 = Math.sin(x1);

    result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(result);
    root = solver.getRoot();
    assertEquals(1.0471975511965978d, root, epsilon);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithException() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new TestFunctions.DivisionByZero();
    ConvergenceChecker checker = new TestConvergenceChecker();
    int maxIter = 10;
    double x0 = 1.0;
    double f0 = Double.NaN;
    double x1 = 2.0;
    double f1 = Double.NaN;

    solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRootWithNullFunction() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new TestConvergenceChecker();
    int maxIter = 10;
    double x0 = 1.0;
    double f0 = 0.0;
    double x1 = 2.0;
    double f1 = 0.0;
    ComputableFunction function = null;

    try {
      solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    } catch (FunctionException e) {
      fail("Unexpected FunctionException");
    }
    fail("Expected IllegalArgumentException");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRootWithNullChecker() {
    BrentSolver solver = new BrentSolver();
    int maxIter = 10;
    double x0 = 1.0;
    double f0 = 0.0;
    double x1 = 2.0;
    double f1 = 0.0;
    ConvergenceChecker checker = null;

    try {
      solver.findRoot(new TestFunctions.Sin(), checker, maxIter, x0, f0, x1, f1);
    } catch (FunctionException e) {
      fail("Unexpected FunctionException");
    }
    fail("Expected IllegalArgumentException");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRootWithNegativeMaxIter() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new TestFunctions.Sin();
    ConvergenceChecker checker = new TestConvergenceChecker();
    int maxIter = -1;
    double x0 = 1.0;
    double f0 = Math.sin(x0);
    double x1 = 2.0;
    double f1 = Math.sin(x1);

    try {
      solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    } catch (FunctionException e) {
      fail("Unexpected FunctionException");
    }
    fail("Expected IllegalArgumentException");
  }

  @Test
  public void testFindRootWithZeroInterval() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new TestFunctions.Sin();
    ConvergenceChecker checker = new TestConvergenceChecker();
    int maxIter = 10;
    double x0 = 1.0;
    double f0 = Math.sin(x0);
    double x1 = x0;
    double f1 = Math.sin(x1);

    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertFalse(result);
  }

  static class TestConvergenceChecker implements ConvergenceChecker {
    @Override
    public int converged(double x, double f, double xNext, double fNext) {
      if (fNext * f < 0) {
        return ConvergenceChecker.LOW;
      } else if (f < Double.MIN_VALUE) {
        return ConvergenceChecker.HIGH;
      } else {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    }
  }

}