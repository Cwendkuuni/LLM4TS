package org.spaceroots.mantissa.roots.test;

import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.functions.scalar.SineFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Unit tests for the {@link BrentSolver} class.
 */
public class BrentSolverTest {

  /**
   * Test finding a root within a small interval using a simple function.
   */
  @Test
  public void testFindRootWithinSmallInterval() throws Exception {
    double x0 = 1.0;
    double x1 = 1.5;
    double expectedRoot = 1.4142135625;

    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();
    SineFunction function = FunctionFactory.sineFunction();

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertEquals(expectedRoot, solver.getRoot(), 1e-6);
  }

  /**
   * Test finding a root outside of an interval using a simple function.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootOutsideInterval() throws Exception {
    double x0 = 1.0;
    double x1 = 0.5;
    double expectedRoot = 0.7853981633;

    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();
    SineFunction function = FunctionFactory.sineFunction();

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    } finally {
      Assert.fail();
    }
  }

  /**
   * Test finding a root using a function with multiple roots.
   */
  @Test
  public void testFindRootWithMultipleRoots() throws Exception {
    double x0 = -2.0;
    double x1 = 2.0;
    double expectedRoot1 = -1.13149538;
    double expectedRoot2 = 1.13149538;

    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();
    SineFunction function = FunctionFactory.sineFunction();

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    double root = solver.getRoot();
    if (Math.abs(root + 1.13149538) < 1e-6) {
      Assert.assertEquals(expectedRoot1, root, 1e-6);
    } else {
      solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x0, function.valueAt(x0));
      Assert.assertEquals(expectedRoot2, solver.getRoot(), 1e-6);
    }
  }

  /**
   * Test finding a root using a function with no roots.
   */
  @Test
  public void testFindRootWithNoRoots() throws Exception {
    double x0 = 0.0;
    double x1 = 1.0;
    double functionValue = Math.exp(Math.PI) / Math.sin(Math.PI);

    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();
    FunctionFactory functionFactory = new FunctionFactory() {
      @Override
      public ComputableFunction createFunction() {
        return () -> functionValue;
      }
    };
    ComputableFunction function = functionFactory.createFunction();

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.fail();
  }

}